import java.util.*;
public class KVStore {

    private static final Map<String, Object> store = new HashMap<>();

    public void test(String []args){

        //Test case while initializing
        set("abc", 100);
        System.out.println(get("abc"));
        if(delete("abc"))
            System.out.println("Deleted");
        else
            System.out.println("Invalid delete");

    }

    public void set(String key, Object value ){
        store.put(key, value);
    }

    public Object get(String key){
        Object value= store.get(key);
        return value;
    }

    public boolean delete(String key){
        Object value= store.remove(key);
        if(value!= null)
            return true;
        else
            return false;

    }

    
}
