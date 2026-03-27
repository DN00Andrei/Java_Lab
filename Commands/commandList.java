package Commands;

import Commands.ComandaBaza;
import package_1.ResursaSchita;
import package_1.Catalogul;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.util.List;

public class commandList implements ComandaBaza {
    private ResursaSchita resursa;
    private List<ResursaSchita> toate_res;

    public commandList(Catalogul resursa_nou) { this.toate_res = resursa_nou.getListaRes(); }

    @Override
    public void start()  {

            for (int i = 0; i < this.toate_res.size(); i++)
            {
                System.out.println(toate_res.get(i).toString());
                System.out.println( this.toate_res.get(i).getProperties() );
            }

    }
}