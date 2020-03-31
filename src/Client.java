import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

//The main part of the project.  Allows those who run this class to connected to an already opened TCP channel, given an ip, port, and a user name.
public class Client {

    //Main communication variables + server socket.
    private InputStream input;    //Used to read from the socket.
    private OutputStream output;  //Used to write on the socket
    private Socket socket;

    //Details for the client and the server.
    private String server, userName;
    private int port;

    /* Custom constructor which takes the following parameters to allow the user to connect
       to a custom server with a designated port. The address of the server for the user to connect to.
       The port holds the port number of the server
    */
    Client(String server, int port, String userName){
        this.server = server;
        this.port = port;
        this.userName = userName;
    }

    /*Default constructor
    server:  By default, if no ip address is supplied to the client on construction,
    then it is assumed the client will be hosting the server.  So server is set to hosts local ip.
    Can always change these details later.
     */
    Client() throws UnknownHostException {

        InetAddress myAddr = InetAddress.getLocalHost();
        System.out.println("My Ip Address:  " + myAddr.getHostAddress());
        System.out.println("My Host name:  " + myAddr.getHostName());

        server = myAddr.getHostAddress();
        port = 1337;
        userName = myAddr.getHostName();
        System.out.print("\nUser Name:  " + userName);

    }

    public boolean start() {
// try to connect to the server
        try {
            socket = new Socket(server, port);
        }
        catch(Exception ec) {
            System.out.print("Error connecting to server:" + ec);
            return false;
        }
        String msg = "Connection accepted " + socket.getInetAddress() + ":" + socket.getPort();
        System.out.print(msg);
        /* Creating both Data Stream */
        try
        {
            System.out.print("\nEntered Client Start!");

            input  = socket.getInputStream();
            output = socket.getOutputStream();
        }

        catch (IOException eIO) {
            System.out.print("Exception creating new Input/output Streams: " + eIO);
            return false;
        }

        // creates the Thread to listen from the server
        new ListenFromServer().start();

//        try {
//            msg = (String) input.readObject();
//            System.out.println(msg);
//            System.out.print("> ");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }


        // Send our username to the server this is the only message that we
        // will send as a String. All other messages will be ChatMessage objects
        try
        {

            byte[] uName = userName.getBytes();
            output.write(uName);
        }
        catch (IOException eIO) {
            System.out.print("Exception doing login : " + eIO);
            disconnect();
            return false;
        }
        // success we inform the caller that it worked
        return true;
    }

    void sendMessage(String msg) {
        try {
            output.write(Integer.parseInt(msg));
        }
        catch(IOException e) {
            System.out.print("Exception writing to server: " + e);
        }
    }

    public void disconnect() {
        try {
            if(input != null) input.close();
        }
        catch(Exception e) {}
        try {
            if(output != null) output.close();
        }
        catch(Exception e) {}
        try{
            if(socket != null) socket.close();
        }
        catch(Exception e) {}

    }

    //This class waits for the message from the server
    class ListenFromServer extends Thread{
        public void run(){
            try{
                int msg = input.read();
                System.out.print("\nMSG Listen from server:  " + msg);
            }
            catch(IOException e){
                System.out.print("\nServer has closed the connection to:  " + e);
            }
        }

    }//End of ListenFromServer class.

}
