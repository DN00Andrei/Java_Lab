package app;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class Main {

    static List<Class<?>> adnotariGasite = new ArrayList<>(); // <?> = clasa de orice tip

    public static void main(String[] args) throws Exception {


        Scanner sc = new Scanner(System.in);
        System.out.print("Introdu calea catre fisier sau folder: ");
        String cale = sc.nextLine();
        cale = cale.trim();

        File intrare = new File(cale);

        boolean exista = intrare.exists();
        if (exista == false) {
            System.out.println("Nu exista asa ceva la calea aia...");
            sc.close();
            return;
        }

        //functii din java.io
        boolean eFolder = intrare.isDirectory();
        boolean eFisier = intrare.isFile();

        if (eFisier == true) {
            System.out.println("Ok e fisier, merg mai departe");
            proceseazaFisierSingur(intrare);
        } else if (eFolder == true) {
            System.out.println("Ok e folder, merg mai departe");
            proceseazaFolder(intrare);
        } else {
            System.out.println("Nu stiu ce e asta");  }


        sc.close();
    }

    static void proceseazaFisierSingur(File fisier) throws Exception {
        File fisierClass = fisier;

        if (fisier.getName().endsWith(".java")) {
            fisierClass = compileaza(fisier, fisier.getParentFile());
            if (fisierClass == null) return;
     }

          if (!fisierClass.getName().endsWith(".class")) {  System.out.println("Tip de fisier necunoscut."); return; }

        File radacina = fisierClass.getParentFile();
        URLClassLoader incarcator = new URLClassLoader(new URL[]{ radacina.toURI().toURL() });

        //pt trunchiaza numele fisierului
          String numeClasa = fisierClass.getName().replace(".class", "");
        Class<?> clasa = incarcator.loadClass(numeClasa);

        try {
            Method metoda = clasa.getMethod("run");
            Object instanta = clasa.getDeclaredConstructor().newInstance();
            System.out.println("[Rulez metoda 'run' din " + numeClasa + ")");
            metoda.invoke(instanta);
        } catch (NoSuchMethodException e) {
            System.out.println("Nu exista metoda 'run' fara argumente in " + numeClasa);
        }
    }

    static void proceseazaFolder(File folder) throws Exception {
        List<File> toateFisierele = new ArrayList<>();
        colecteazaRecursiv(folder, toateFisierele);

        File dirOut = new File(folder, "_out"); //pentru a fi sigur ca fisierul nu exista deja din cine stie ce motiv
        dirOut.mkdirs();

        List<File> claseDinDisk = new ArrayList<>();

        for (File f : toateFisierele) {
            if (f.getName().endsWith(".java")) {
                compileaza(f, dirOut);
            }

            else if (f.getName().endsWith(".class") && !f.getAbsolutePath().contains("_out")) { claseDinDisk.add(f);} }

        List<File> claseDinOut = new ArrayList<>();
        colecteazaRecursiv(dirOut, claseDinOut);

        claseDinOut.removeIf(f -> !f.getName().endsWith(".class"));

        File radacinaDisk = claseDinDisk.isEmpty() ? folder : gasesteRadacina(folder, claseDinDisk);

        URL[] urluri = { radacinaDisk.toURI().toURL(), dirOut.toURI().toURL() };
        URLClassLoader incarcator = new URLClassLoader(urluri);

        List<Class<?>> claseIncarcate = new ArrayList<>();

        for (File fc :claseDinDisk) { //pargurgerea claselor deja existente pe disk
            String numeClasa = numeComplet(fc, radacinaDisk);
            incarcaClasa(numeClasa, incarcator, claseIncarcate);
          }

        for (File fc : claseDinOut) {// parcurgerea claselor ce se alfa deja salvate in repository-ul "_out"
            String numeClasa = numeComplet(fc, dirOut); // pct de referinta diferit
            incarcaClasa(numeClasa, incarcator, claseIncarcate);
        }

        proceseazaClase(claseIncarcate);
    }

    static void proceseazaClase(List<Class<?>> claseIncarcate) {
        for (Class<?> clasa : claseIncarcate) {
              if (!clasa.isAnnotation() && Modifier.isPublic(clasa.getModifiers())) {
                afiseazaPrototip(clasa);
                invocaMetodeAdnotate(clasa);
            }
          }
    }

    static void incarcaClasa(String numeClasa, URLClassLoader incarcator, List<Class<?>> lista) {
        try {
            Class<?> clasa = incarcator.loadClass(numeClasa);
            lista.add(clasa);
            if (clasa.isAnnotation()) {
                adnotariGasite.add(clasa);
                System.out.println("[Adnotare gasita] " + clasa.getName());
            }
        } catch (Throwable t) {
            System.out.println("Nu am putut incarca: " + numeClasa + " (" + t.getMessage() + ")");
        }
    }

    static void afiseazaPrototip(Class<?> clasa) {
        System.out.println("\n--- " + Modifier.toString(clasa.getModifiers()) + " class " + clasa.getSimpleName() + " --");// traisca java.lang pt Modifier si getModifiers
        for (Constructor<?> c : clasa.getDeclaredConstructors()) {
            System.out.println("  constructor: " + clasa.getSimpleName()   + "(" + parametriSir(c.getParameterTypes()) + ")");
            //getParameterTypes xtrage un vector cu tipurile de date pe care constructorul le cere ca argumente
            // rezultate tip" constructor: User(int, String)""
        }

         //analog dat pentru metode
        for (Method m : clasa.getDeclaredMethods()) {
            System.out.println("  metoda: " + Modifier.toString(m.getModifiers()) + " "  + m.getReturnType().getSimpleName() + " "
             +   m.getName() + "(" + parametriSir(m.getParameterTypes())  + ")");
        } }

    static String parametriSir(Class<?>[] tipuri) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tipuri.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(tipuri[i].getSimpleName());
          } return sb.toString();
    }

    @SuppressWarnings("unchecked")
    static void invocaMetodeAdnotate(Class<?> clasa) {
        if (adnotariGasite.isEmpty()) return;

        Object instanta = null;
        try {
            instanta = clasa.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            System.out.println("  Nu pot crea instanta: " + e.getMessage());
            return;
        }

        for (Method m : clasa.getDeclaredMethods()) {
            for (Class<?> tipAnn : adnotariGasite) {
                Annotation ann = m.getAnnotation((Class<? extends Annotation>) tipAnn);
                if (ann == null) continue;

                Class<?>[] params = m.getParameterTypes();
                m.setAccessible(true);

                try {
                    if (params.length == 0) {
                        System.out.println("  [LOG] Inainte de executie: " + clasa.getSimpleName() + "." + m.getName());
                        //am verificat deja ca metoda sa nu aiba parametrii deci putem forta invoke
                        m.invoke(instanta);
                    } else if (params.length == 1 && params[0] == int.class) {
                        System.out.println("  [LOG] Inainte de eecutie: " + clasa.getSimpleName() + "." + m.getName() + "(42)"  );
                        m.invoke(instanta, 42);
                    }
                } catch (Exception e) {
          System.out.println("  Eroare la invocare: " + e.getCause( ));
                }
            }
        }
    }

          static File compileaza(File sursa, File dirIesire) {
        JavaCompiler compilator = ToolProvider.getSystemJavaCompiler();
        if (compilator == null) {
            System.out.println("JDK nu arecompilator disponibil.");
            return null;
        }
           int cod = compilator.run(null, null, null, "-d", dirIesire.getAbsolutePath(), sursa.getAbsolutePath());


         
        if (cod != 0) {
            System.out.println("Eroare la compilare: " + sursa.getName());
            return null;
        }
        return new File(dirIesire, sursa.getName().replace(".java", ".class"));
    }

    static String numeComplet(File fisierClass, File radacina) {
        String caleRelativa = radacina.toURI().relativize(fisierClass.toURI()).getPath();
        return caleRelativa.replace("/", ".").replace("\\", ".").replace(".class", "");
    }

    static File gasesteRadacina(File folder, List<File> claseFisiere) {
        for (File fc : claseFisiere) {
            try {
                DataInputStream dis = new DataInputStream(new FileInputStream(fc));
                dis.readInt(); dis.readShort(); dis.readShort();
                int cpCount = dis.readUnsignedShort();
                List<String> utfEntries = new ArrayList<>();
                for (int i = 1; i < cpCount; i++) {
                    int tag = dis.readUnsignedByte();
                    if (tag == 1) {
                        int len = dis.readUnsignedShort();
                        byte[] bytes = new byte[len];
                        dis.readFully(bytes);
                        utfEntries.add(new String(bytes));
                        // identificam din ce pachet face parte clasa codului prin "Contant Pool"-ul fisierului de unde extragem stringuri
                        // sursa pentru explicatii tags: https://medium.com/@AlexanderObregon/why-the-jvm-needs-a-constant-pool-and-what-it-stores-873d89f19771
                        //https://www.youtube.com/watch?v=FqSrO05OwOs
                    } else if (tag == 7 || tag == 8 || tag == 16 || tag == 19 || tag == 20) {
                        dis.readUnsignedShort();
                    } else if (tag == 9 || tag == 10 || tag == 11 || tag == 12 || tag == 17 || tag  == 18) {
                        dis.readUnsignedShort(); dis.readUnsignedShort();
                    } else if (tag == 3 || tag == 4) {
                        dis.readInt();
                    } else if (tag == 5 || tag == 6) {
                        dis.readLong(); i++;
                    } else if (tag == 15 || tag == 16) {

                        dis.readUnsignedByte(); dis.readUnsignedShort(); // nu am inteles sigur de ce functioneaza asta, ceva legat de octeti idk

                    }
                }
                dis.close();
                for (String s : utfEntries) {
                    if (s.contains("/") && !s.contains("(") && !s.startsWith("java")) {
                        String pachet = s.replace("/", File.separator); // inlocuieste slash-ul sa se potriveasca cu sistemul de operare, ce smecher
                        // sursa: https://www.baeldung.com/java-file-vs-file-path-separator
                        String caleAbs = fc.getAbsolutePath();
                          int idx = caleAbs.lastIndexOf(pachet); 
                        if (idx != -1) { return new File(caleAbs.substring(0, idx)); //daca pachet-ul se afla in interuiorul path-ulu, taiem si aruncam partea din dreapta
                        }
                    }
                }
            } catch (Exception e) {   System.out.println("[[[gasesteRadacina eroare]]] " + e.getMessage());
            }
        }
        return folder;
    }

    static void colecteazaRecursiv(File dir, List<File> lista) {
        File[] continut = dir.listFiles();
        if (continut ==null) return;
        for (File f : continut) { if (f.isDirectory()) colecteazaRecursiv(f, lista); else lista.add(f);  }
    }
}