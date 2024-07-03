import java.util.*;

public class KVServicesTest {

    public static void main(String[] args) {
        KVServices KVServices = new KVServices();

        //Write test to create collection

        //Write test to add tags to existing collection

        // Test adding a key-value pair
        KVServices.setRecord("abc", 100);
        System.out.println(KVServices.getRecord("abc")); // Expected output: 100

        // Test retrieving the UUID for a given key
        UUID uuidForAbc = KVServices.getUUIDforKey("abc");
        System.out.println(uuidForAbc); // Expected output: UUID of the key "abc"

        // Test appending a key-value pair to existing list of values
        KVServices.updateRecord("abc",100, 200);
        System.out.println(KVServices.getRecord("abc")); // Expected output: 200

        //Test adding value to existing key-value
        KVServices.setRecord("abc",10);
        System.out.println(KVServices.getRecord("abc")); // Expected output: {200,10}

        //Write test for getAll
        System.out.println(KVServices.getAllRecords());

        // Test deleting a key-value pair
        if (KVServices.deleteKey("abc")) {
            System.out.println("Deleted");
        } else {
            System.out.println("Invalid delete");
        }

        // Attempt to retrieve a deleted key
        System.out.println(KVServices.getRecord("abc")); // Expected output: null

        // Test adding and deleting a non-existent key
        if (!KVServices.deleteKey("nonExistentKey")) {
            System.out.println("Invalid delete");
        }

        //Write test for closing connection

    }
}
