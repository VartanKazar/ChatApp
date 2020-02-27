import java.net.*;
import java.io.*;
import java.util.*;

//The main part of the project.  Allows those who run this class to connected to an already opened TCP channel, given an ip, port, and a user name.
public class Client {

    //Main communication variables + server socket.
    private ObjectInputStream input;    //Used to read from the socket.
    private ObjectOutputStream output;  //Used to write on the socket
    private Socket socket;

    //Details for the client and the server.
    private String server, userName;
    private int port;

    /*Custom constructor which takes the following parameters to allow the user to connect to a custom server with a designated port.
        server:  The address of the server for the user to connect to.
        port:  The port number of the server (should be
     */
    Client(String server, int port, String userName){
        this.server = server;
        this.port = port;
        this.userName = userName;
    }

    /*Default constructor

     */
    Client(){


    }
}
