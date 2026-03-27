package Commands;
import Commands.ComandaBaza;
import package_1.Catalogul;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class commandSave implements ComandaBaza {
    private Catalogul catalog;
    private String path;

    public commandSave(Catalogul catalog, String path) { this.catalog = catalog; this.path = path;}

    @Override
    public void start() {
        try {
            ObjectOutputStream afara = new ObjectOutputStream(new FileOutputStream(this.path));
            afara.writeObject(catalog);
        }
        catch (Exception hopa) { System.err.println( "Nu s-a salvat/scris in fisier.");}


    }
}