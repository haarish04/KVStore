import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clienSocket;
    private KVStore KVStore;
    private boolean connectionStatus = true;

    public ClientHandler(Socket clientSocket, KVStore KVStore){
        this.clienSocket= clientSocket;
        this.KVStore=KVStore;
    }
    
    @Override
    public void run(){
        try{
            //Handle input from the socket
            BufferedReader in = new BufferedReader(new InputStreamReader(clienSocket.getInputStream()));

            //Write the output 
            PrintWriter out = new PrintWriter(clienSocket.getOutputStream(), true);

            //Read the input and store as string
            String requestLine = in.readLine();
            while(requestLine!= null && connectionStatus){
                String[] req = requestLine.split(" ", 2);
                if(req.length >=1){
                    String command= req[0].toUpperCase();

                    //The query sent starts with the command followed by other details about key and value
                    switch(command){
                        //Enter new data
                        case "SET":
                            if(req.length == 3){
                                String setKey= req[1];
                                Object setValue= req[2];
                                KVStore.set(setKey,setValue);
                                out.println("Record added");

                            }
                            else{
                                out.println("Invalid SET operation");
                            }
                            break;
                        //Retrieve data
                        case "GET":
                            String getKey= req[1];
                            Object getValue= KVStore.get(getKey);
                            if(getValue!= null){
                                out.println("Value: "+ getValue.toString());
                            }
                            else{
                                out.println("Not Found");
                            }
                            break;

                        //Delete record
                        case "DELETE":
                            String deleteKey= req[1];
                            if(KVStore.delete(deleteKey)){
                                out.println("Record Deleted");
                            }
                            else{
                                out.println("Error in Delete");
                            }
                            break;
                        
                        //Update existing
                        case "UPDATE":
                            String updateKey= req[1];
                            Object updateValue= req[2];
                            if(KVStore.update(updateKey,updateValue)){
                                out.println("Update Success");
                            }
                            else{
                                out.println("Error in update");
                            }
                            break;
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
