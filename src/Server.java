import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

//The server which runs on a designated ip and port.  Handles all incoming connections and their I/O.
public class Server {

    private static int uniqueId;                     //A unique id for each connection on the server.
    private ArrayList<ClientThread> clientList;     //A list of clients on the server
    private SimpleDateFormat date;                  //Used to display the times of connections and messages in the server.
    private int port;                               //The port number to listen to for the connection.  This is what the client types in to join the chat room.
    private boolean keepGoing;                      //Used to indicate whether the server should start or stop.

    Server(int port){
        this.port = port;
        date = new SimpleDateFormat("HH:mm:ss");
        clientList = new ArrayList<ClientThread>();
    }

    public void start(){

        keepGoing = true;

        //Create a new socket for the server and wait for connection requests.
        try{

            //The socket used by the server.
            ServerSocket serverSocket = new ServerSocket(port);

            //As long as keepGoing is true, the server will wait for connections.
            while(keepGoing){

                System.out.print("\nServer waiting for clients on port:  " + port);

                //New socket for incoming connections.
                Socket clientSocket = serverSocket.accept();

                //Check if server was asked to stop when switching keepGoing to false.  If asked to stop, break out of the while loop and stop waiting for connections.
                if (!keepGoing)
                    break;

                ClientThread clientThread = new ClientThread(clientSocket, uniqueId);  //Make a thread of the client socket.
                clientList.add(clientThread);  //Add it to the client list arrayList.
                clientThread.start();

            }//End of loop to wait for connections.

            //When keepGoing is detected false, the loop is broken and the server is asked to stop.
            try{

                serverSocket.close();

                //Boot out and close all of the socket threads in the clientThreads list
                for (int i = 0; i < clientList.size(); ++i){

                    ClientThread currentThread = clientList.get(i);

                    try{
                        currentThread.input.close();
                        currentThread.output.close();
                        currentThread.socket.close();
                    }//End inner-most try-catch for closing individual clients sockets in the clientThread list.
                    catch(IOException e){

                    }
                }
            }//End of inner try-catch for stopping the server.
            catch(Exception e){
                System.out.print("\nException closing the server and its clients:  " + e);
            }


        }//End of main try-catch for opening a new socket with a port.

        //Something went wrong, maybe port not specified or used.
        catch (IOException e){
            String errorMsg = "\n" +  date.format(new Date()) + " Exception on new server socket:  " + e + "\n";
            System.out.print(errorMsg);
        }
    }//End start method.

}
