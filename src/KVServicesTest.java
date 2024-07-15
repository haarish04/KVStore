import java.util.*;

public class KVServicesTest {

    public static void main(String[] args) {
        KVServices KVServices = new KVServices();

        //Test to create collection
        System.out.println(KVServices.createCollection("testColl", "firstCollection"));

        //Test to get collection by name
        System.out.println(KVServices.getCollection("testColl"));

        //Test to print tags of a collection
        System.out.println(KVServices.getCollectionTag("testColl"));

        //Test to add tags to existing collection
        if(KVServices.addCollectionTag("testColl", "addTags")){
            System.out.println("Tags added successfully: ");
            System.out.println(KVServices.getCollectionTag("testColl"));
        }
        else
            System.out.println("Invalid add Tag");

        //Write test to rename collection
        if(KVServices.renameCollection("testColl", "newTestColl"))
            System.out.println("Rename collection successfull");
        else
            System.out.println("Unsuccessfull collection Rename");

        //Write test to delete tags from collection
        if(KVServices.deleteCollectionTag("newTestColl")){
            System.out.println("Tags deleted successfully");
            System.out.println(KVServices.getCollectionTag("newTestColl"));
        }
        else
            System.out.println("Invalid delete tags");


        // Test adding a key-value pair
        KVServices.setRecord("newTestColl","abc", 100);
        System.out.println(KVServices.getRecord("newTestColl","abc")); // Expected output: 100

        // Test retrieving the UUID for a given key
        UUID uuidForAbc = KVServices.getUUIDforKey("newTestColl","abc");
        System.out.println(uuidForAbc); // Expected output: UUID of the key "abc"

        // Test appending a key-value pair to existing list of values
        KVServices.updateRecord("newTestColl","abc",100, 200);
        System.out.println(KVServices.getRecord("newTestColl","abc")); // Expected output: 200

        //Test adding value to existing key-value
        KVServices.setRecord("newTestColl","abc",10);
        System.out.println(KVServices.getRecord("newTestColl","abc")); // Expected output: {200,10}

        //Write test for getAll
        System.out.println(KVServices.getAllRecords("newTestColl"));

        // Test deleting a key-value pair
        if (KVServices.deleteKey("newTestColl","abc")) {
            System.out.println("Deleted");
        } else {
            System.out.println("Invalid delete");
        }

        //Write test to delete a value from list of values associated with a key
        KVServices.setRecord("newTestColl","xyz",10);
        KVServices.setRecord("newTestColl","xyz",20);
        System.out.println(KVServices.getRecord("newTestColl","xyz"));
        if(KVServices.deleteValue("newTestColl","xyz",10))
            System.out.println("Delete successfull");
        else
            System.out.println("Delete unsuccessfull");
        System.out.println(KVServices.getRecord("newTestColl","xyz"));


        // Attempt to retrieve a deleted key
        System.out.println(KVServices.getRecord("newTestColl","abc")); // Expected output: null

        // Test deleting a non-existent key
        if (!KVServices.deleteKey("newTestColl","nonExistentKey")) {
            System.out.println("Invalid delete");
        }

        //Write test for closing connection

    }
}
