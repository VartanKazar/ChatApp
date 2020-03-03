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

    public void setPort(int port){ this.port = port; }

    public int getPort(){ return port; }

    public String getServerIp() throws UnknownHostException { return InetAddress.getLocalHost().getHostAddress(); }

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

                //Confirmation chatMsg and announcement that a new user has joined the server.
                System.out.print("\n" + clientThread.userName + " has joined the chat!");

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

    protected void stop(){
        keepGoing = false;
    }

    //Used to send the chatMsg to all clients connected on the server.
    private synchronized void broadcast(String userName, String chatMsg){

        String time = date.format(new Date());  //Add a timestamp to the beginning of the chatMsg;
        String stampedMsg = time + " - " + userName + ": " + chatMsg;  //New formatted chatMsg string which has the following layout:  <HH:mm:ss> - <user name>:  <chatMsg>

        System.out.print("\n" + stampedMsg);

        //Reverse loop to send this chatMsg to all connected clients.
        //Done in reverse order incase a user has to be removed before the chatMsg is broadcasted to all, or if something goes wrong and the current user has to be removed.
        for(int i = clientList.size(); --i >= 0;) {

            ClientThread currentThread = clientList.get(i);

            // try to write to the Client if it fails remove it from the list
            if(!currentThread.writeMsg(stampedMsg)) {

                clientList.remove(i);
                System.out.print("\nDisconnected Client " + currentThread.userName + " removed from list.");
            }
        }
    }//End broadcast function.

    //For removing a connected client from the server when they type the terminate command
    synchronized void remove(int id){
        //Search the clientList array for the given id
        for(int i = 0; i < clientList.size(); ++i){

            ClientThread thread = clientList.get(i);

            if (thread.id == id){
                clientList.remove(i);
                return;
            }
        }
    }//End remove function.
}
