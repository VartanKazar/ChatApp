<<<<<<< refs/remotes/origin/Dev_Kiyan
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
=======
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Server implements Runnable {

    private ServerSocket serverSocket;
    private int port;
    private ArrayList<ClientThread> clientList;
    protected Thread runningThread = null;

    public Server(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(port);
        clientList = new ArrayList<>();
    }

    public void start() throws IOException {

        Socket clientSocket;

        while(true){

            //Accept the incoming connection request from the client to the server.
            clientSocket = serverSocket.accept();
            System.out.println("New client has been accepted in the server:  " + clientSocket
                              +"\nYou can now type messages to everyone");
>>>>>>> local

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

<<<<<<< refs/remotes/origin/Dev_Kiyan
        keepGoing = true;
=======
            try{
               //Accept the incoming connection request from the client to the server.
                clientSocket = serverSocket.accept();
                System.out.println("New client has been accepted in the server: " + clientSocket);
>>>>>>> local

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

                //Confirmation message and announcement that a new user has joined the server.
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

<<<<<<< refs/remotes/origin/Dev_Kiyan
=======
                    //If the current clients name is equal to the currently iterated client in clientList array, then remove it from the array.
                    if (ct.clientName.equalsIgnoreCase(clientName)){
                        System.out.println(clientName + " has been removed from the server clientList array");
                        clientList.remove(index);
                        break;
>>>>>>> local
                    }
                }
            }//End of inner try-catch for stopping the server.
            catch(Exception e){
                System.out.print("\nException closing the server and its clients:  " + e);
            }


<<<<<<< refs/remotes/origin/Dev_Kiyan
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
=======
        //Method to handle any commands passed in from the user chat.  These commands are signaled with a / at the beginning of the message. (ex. /list, /help, /exit, etc.)
        private void handleCommand(String message){

            //Grab the first three words in the message, the rest are irrelevant for the command.
            String[] commands = new String[3];
            commands = message.split(" ", 3);

            //Check first character of the string to see if the message sent is a command.
            //Commands start with / character at beginning.
            if (commands[0].charAt(0) == '/'){
                if (commands[0].equalsIgnoreCase("/help")){
                    System.out.println("1. /myip \n" +
                                       "2. /myport \n" +
                                       "3. /list \n" +
                                       "4. /terminate <client name> \n" +
                                       "5. /send <client name> <message> \n" +
                                       "6. /exit \n");
                }

                else if (commands[0].equalsIgnoreCase("/myip")) {
                    try {
                        System.out.println("My Ip:  " + InetAddress.getLocalHost().getHostAddress());
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }

                else if (commands[0].equalsIgnoreCase("/myport"))
                    System.out.println("Listen Port:  " + port);
>>>>>>> local

    //Used to send the message to all clients connected on the server.
    private synchronized void broadcast(String userName, String message){

        String time = date.format(new Date());  //Add a timestamp to the beginning of the message;
        String stampedMsg = time + " - " + userName + ": " + message;  //New formatted message string which has the following layout:  <HH:mm:ss> - <user name>:  <message>

        System.out.print("\n" + stampedMsg);

        //Reverse loop to send this message to all connected clients.
        //Done in reverse order incase a user has to be removed before the message is broadcasted to all, or if something goes wrong and the current user has to be removed.
        for(int i = clientList.size(); --i >= 0;) {

            ClientThread currentThread = clientList.get(i);

<<<<<<< refs/remotes/origin/Dev_Kiyan
            // try to write to the Client if it fails remove it from the list
            if(!currentThread.writeMsg(stampedMsg)) {

                clientList.remove(i);
                System.out.print("\nDisconnected Client " + currentThread.userName + " removed from list.");
=======
                }

                else if (commands[0].equalsIgnoreCase("/exit")){
                    System.out.println("Logging out...");
                    keepGoing = false;
                }

                else{
                    System.out.print(commands[0] + " is not recognized as a command.  Type /help for a list of commands.");
                }
>>>>>>> local
            }
        }
    }//End broadcast function.

<<<<<<< refs/remotes/origin/Dev_Kiyan
    //For removing a connected client from the server when they type the terminate command
    synchronized void remove(int id){
        //Search the clientList array for the given id
        for(int i = 0; i < clientList.size(); ++i){

            ClientThread thread = clientList.get(i);
=======
            //Message sent is not a command order, so broadcast it to all other clients.
            else {
                //client name through looping
                //client message for specific name
                System.out.println(/*client name*/": " /* + client message */);
            }
>>>>>>> local

            if (thread.id == id){
                clientList.remove(i);
                return;
            }
        }
    }//End remove function.
}
