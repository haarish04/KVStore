import java.io.*;
import java.net.Socket;

public class KVClientHandler implements Runnable {
    private Socket clienSocket;
    private KVServices KVServices;
    private boolean connectionStatus = true;

    public KVClientHandler(Socket clientSocket, KVServices KVServices){
        this.clienSocket= clientSocket;
        this.KVServices=KVServices;
    }
    
    @Override
    public void run(){
        try{
            //Handle input from the socket
            BufferedReader in = new BufferedReader(new InputStreamReader(clienSocket.getInputStream()));

            //Write the output 
            PrintWriter out = new PrintWriter(clienSocket.getOutputStream(), true);

            String collName= "testCollection";

            //Read the input and store as string
            String requestLine = in.readLine();
            while(requestLine!= null && connectionStatus){
                String[] req = requestLine.split(" ", 2);
                if(req.length >=1){
                    String command = req[0].toUpperCase();
                    switch(command){

                        //Create new collection with some tags
                        case "CREATE":
                            if(req.length >= 2){
                                String name= req[1];
                                StringBuilder tags= new StringBuilder();

                                for(int i=2;i<req.length;i++)
                                    tags.append(req[i]).append(" ");
                                out.println(KVServices.createCollection(name, tags.toString()));   
                            }
                            else{
                                out.println("Invalid request");
                            }
                            break;
                        
                        //Get collection using name
                        case "GET":
                            if(req.length >= 2){
                                String name = req[1];
                                Object getCollObject= KVServices.getCollection(name);
                                if(getCollObject == null){
                                    out.println("Collection not found");
                                }
                                else
                                    out.println(getCollObject.toString());
                            }
                        
                        //Append new tags to the collection
                        case "ADDTAG":
                            if(req.length>=2){
                                String name= req[1];
                                StringBuilder tags = new StringBuilder();

                                for(int i=2;i<req.length;i++)
                                    tags.append(req[i]).append(" ");
                                KVServices.addCollectionTag(name, tags.toString());
                            }
                            else{
                                out.println("Invalid Add Tag");
                            }
                            break;
                        
                        //Delete all the tags associate with the collection 
                        case "DELTAG":
                            if(req.length==2){
                                String name= req[1];
                                KVServices.deleteCollectionTag(name);
                            }
                            else{
                                out.println("Invalid Delete Tag");
                            }
                            break;
                        
                        //Rename collection name
                        case "RENAME":
                            if(req.length == 3){
                                String oldCollName= req[1];
                                String newCollName= req[2];
                                KVServices.renameCollection(oldCollName, newCollName);
                            }
                            else{
                                out.println("Invalid Rename");
                            }
                            break;
                    }

                    //The query sent starts with the command followed by other details about key and value
                    switch(req[0]){

                        //Enter new data
                        case "SET":
                            if(req.length == 3){
                                String setKey= req[1];
                                Object setValue= req[2];
                                if(KVServices.setRecord(collName,setKey,setValue)==0)
                                    out.println("New Record created");
                                else
                                    out.println("Key already exists, added to list of values ");

                            }
                            else{
                                out.println("Invalid SET operation");
                            }
                            break;

                        //Retrieve data
                        case "GET":
                            if(req.length == 1){
                                KVServices.getAllRecords(collName);
                            }
                            else{
                                String getKey= req[1];
                                Object getValue= KVServices.getRecord(collName,getKey);
                                if(getValue!= null){
                                    out.println("Value: "+ getValue.toString());
                                }
                                else{
                                    out.println("Not Found");
                                }
                            }
                            break;

                        //Delete entire key value record
                        case "DELETEKEY":
                            String deleteKey= req[1];
                            if(KVServices.deleteKey(collName,deleteKey))
                                out.println("Record Deleted");
                            
                            else
                                out.println("Error in Delete");
                            
                            break;
                        
                        //Delete value from existing key value list
                        case "DELETEVALUE":
                            String deletekey= req[1];
                            String deleteValue= req[2];
                            if(KVServices.deleteValue(collName,deletekey,deleteValue))
                                out.println("Value Deleted");
                            else
                                out.println("Delete error !! Value not found");
                            
                            break;
                            
                        
                        //Update existing
                        case "UPDATE":
                            String updateKey= req[1];
                            Object oldValue= req[2];
                            Object newValue= req[3];
                            if(KVServices.updateRecord(collName,updateKey,oldValue, newValue)){
                                out.println("Update Success");
                            }
                            else{
                                out.println("Error in update");
                            }
                            break;
                        
                        //Terminate connection
                        case "BYE":
                            out.println("Closing connection......");
                            connectionStatus = false;
                            break;
                        
                        default:
                            out.println("Invalid Operation");
                            break;
                    }
                    
                }
                else{
                    out.println("Invalid Request");
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally{
            try{
                clienSocket.close();
                System.out.println("Connection terminated");

            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}
