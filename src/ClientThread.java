import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

//A helper class for server.  One instance of this class (thread) will run for each client.
public class ClientThread extends Thread {

    public Socket socket;  //The socket for listening/talking
    public ObjectInputStream input;
    public ObjectOutputStream output;

    public int id;  //My clients unique id, used for making disconnecting easier.
    public String userName;  //My user name
    public String date;

    ClientThread(Socket socket, int uniqueId){

        id = ++uniqueId;
        this.socket = socket;

    }

    public boolean writeMsg(String chatMsg){


        return false;
    }

}
