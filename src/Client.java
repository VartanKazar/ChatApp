import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

//The main part of the project.  Allows those who run this class to connected to an already opened TCP channel, given an ip, port, and a user name.
public class Client {

    //Main communication variables + server socket.
    private ObjectInputStream input;    //Used to read from the socket.
    private ObjectOutputStream output;  //Used to write on the socket
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
            input  = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        }
<<<<<<< refs/remotes/origin/Dev_Kiyan
        catch (IOException eIO) {
            System.out.print("Exception creating new Input/output Streams: " + eIO);
            return false;
        }

        // creates the Thread to listen from the server
        //new ListenFromServer().start();
        try {
            msg = (String) input.readObject();
            System.out.println(msg);
            System.out.print("> ");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

=======
        catch (Exception e) {
            System.out.println("Exception with IO Streams or creating socket.");
            e.printStackTrace();
        }

        //Create new thread to scan system for messages by the user.
        Thread sendMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                //Continuously grab user input for the chat and send it to server socket.
                while (true){

                    String message = Chat.input.nextLine();

                    try {
                        outputStream.writeUTF(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }//End of main input loop
            }//End of run method
        });//End of sendMessage thread method

        //Create a new thread to read incoming messages to this client.
        Thread readMessage = new Thread(new Runnable() {
            @Override
            public void run() {
                //Continuously grab user input for the chat and send it to server socket.
                while (true){
                    try {
                        String message = inputStream.readUTF();
                        System.out.print("\n" + message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }//End of main reading loop
            }//End of run method
        });//End of readMessage thread method

        //Start the read/write threads for the client.  All commands will be taken care of server side.
        sendMessage.start();
        readMessage.start();
    }//End of start method
>>>>>>> local

        // Send our username to the server this is the only message that we
        // will send as a String. All other messages will be ChatMessage objects
        try
        {
            output.writeObject(userName);
        }
        catch (IOException eIO) {
            System.out.print("Exception doing login : " + eIO);
            disconnect();
            return false;
        }
        // success we inform the caller that it worked
        return true;
    }

    void sendMessage(ChatMessage msg) {
        try {
            output.writeObject(msg);
        }
        catch(IOException e) {
            System.out.print("Exception writing to server: " + e);
        }
    }

    public String getServerAddress() { return server; }
    public void setServerAddress(String address) { server = address; }

    public int getServerPort() { return port; }
    public void setServerPort(int port) { this.port = port; }

    public String getMyIp() throws UnknownHostException {return InetAddress.getLocalHost().getHostAddress();}

    private void disconnect() {
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

}
