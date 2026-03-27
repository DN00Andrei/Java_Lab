package package_1;

import Exceptii.IDAlreadyExists;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


    public class ResursaSchita implements Serializable {
        public static List<Integer> lista_ids =new ArrayList<>();
        private int id;
        private String title;
        private String autor;
        private String location;
        private Map<String, String> properties = new HashMap<>();

        public ResursaSchita(int the_id, String the_title, String the_autor, String the_location) {
            try {
                if (lista_ids.contains(the_id))
                    throw new IDAlreadyExists(the_id);
                this.id = id;
                this.autor = the_autor;
                this.title = the_title;
                this.location = the_location;
                lista_ids.add(the_id);
            }
            catch (IDAlreadyExists error_type) { };
        }


        public ResursaSchita( int that_id) {
            try {
                if( lista_ids.contains(that_id) )
                    throw new IDAlreadyExists(that_id);
                this.id = that_id;
                this.autor = "Unnamed";
                this.title = "Untitled";
                this.location = "Who knows?";
                properties.put("Real?", "No");
                lista_ids.add(that_id);
            }
            catch (IDAlreadyExists error_type) { };
            };


        public void addProperty(String key, String value) { properties.put(key, value);}

        public int getId() { return this.id; }
        public String getAuthor() { return this.autor; }
         public String getLocation() { return this.location; }
        public String getCerainProperties(String the_cheie) { return properties.get(the_cheie); }
        public Map<String, String> getProperties() { return properties; }

        @Override
        public String toString() {
            return this.id + ": " + this.title + " BY "+ this.autor + "(" + location + ")";
        }
    }
