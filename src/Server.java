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

}
