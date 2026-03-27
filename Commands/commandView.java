package Commands;

import Commands.ComandaBaza;
import package_1.ResursaSchita;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;

public class commandView implements ComandaBaza {
    private ResursaSchita resursa;

    public commandView(ResursaSchita resursa_nou) { this.resursa = resursa_nou;
    }

    @Override
    public void start() {
        try{
            Desktop ecran = Desktop.getDesktop();

            String unde = resursa.getLocation();

            if (unde.startsWith("http")) {
                ecran.browse(new URI(unde));
              } else {
                ecran.open(new File(unde));
            }

            }
            catch (Exception hopa) { System.err.println( "Nu s-a salvat/scris in fisier.");}

    }
}