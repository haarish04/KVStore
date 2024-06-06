import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clienSocket;
    private KVStore KVStore;

    public ClientHandler(Socket clientSocket, KVStore KVStore){
        this.clienSocket= clientSocket;
        this.KVStore=KVStore;
    }
    
    @Override
    public void run(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(clienSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clienSocket.getOutputStream(), true);

            String requestLine = in.readLine();
            if(requestLine!= null){
                String[] req = requestLine.split(" ", 2);
                if(req.length >=1){
                    String command= req[0].toUpperCase();

                    switch(command){
                        case "SET":
                            if(req.length == 3){
                                String key= req[1];
                                String value= req[2];
                                KVStore.add(key,value);
                                out.println("Record added");

                            }
                            else{
                                out.println("Invalid SET operation");
                            }
                            break;
                        
                        case "GET":
                            String key= req[1];
                            Object value= KVStore.get(key);
                            if(value!= null){
                                out.println("Value: "+ value.toString());
                            }
                            else{
                                out.println("Not Found");
                            }
                            break;
                        
                        case "DELETE":
                            String key= req[1];
                            if(KVStore.delete(key)){
                                out.println("Record Deleted");
                            }
                            else{
                                out.println("Error in Delete");
                            }
                            break;
                        
                        case "UPDATE":
                            String key= req[1];
                            Object value= req[2];
                            if(KVStore.update(key,value)){
                                out.println("Update Success");
                            }
                            else{
                                out.println("Error in update");
                            }
                            break;
                        
                        default:
                            out.println("Invalid Operation");
                            break;
                    }
                    
                }
            }
        }
    }
}
