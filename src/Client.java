import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


//The main part of the project.  Allows those who run this class to connected to an already opened TCP channel, given an ip, port, and a user name.
public class Client {

    private Socket socket;  //The socket for this client which will connect to a server ip using a port number.
    private int port;  //The port number for the desired server to connect to.
    private String server;  //The ip of the desired server to connect to.  Can either be localhost or a valid local ip (192.168.x.x etc).  Cant be an external ip as that requires port forwarding.
    public boolean isDisconnected;

    //The input and output streams for the socket and the user.
    DataInputStream inputStream;
    DataOutputStream outputStream;

    public Client(int port, String server){
        this.port = port;
        this.server = server;
        isDisconnected = true;
    }

    //Starts up the clients connection to the desired server with a given port.
    public void start() throws IOException {

        //Initialize IO streams and socket.
        try {
            socket = new Socket(server, port);  //Establish a connection from the client to the server using a given port.
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            isDisconnected = false;
        }
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


}
