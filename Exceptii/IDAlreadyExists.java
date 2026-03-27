package Exceptii;
import java.util.ArrayList;
import java.util.List;


public class IDAlreadyExists extends Exception {
    public IDAlreadyExists(int id) { System.err.println("Exista deja un element cu id-ul:" + id); }; }
