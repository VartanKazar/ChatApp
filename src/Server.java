import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
            System.out.print("\n\nNew client has been accepted in the server:  " + clientSocket);

            //Get IO streams for the client to send to the thread.
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

            //Default user name which is just the word client concatenated with their position in the server array list.
            String userName = "Client" + clientList.size();

            ClientThread client = new ClientThread(clientSocket, userName, input, output);

            Thread clientThread = new Thread(client);
            clientList.add(client);
            clientThread.start();
        }

    }

    @Override
    public void run() {

        synchronized (this){
            this.runningThread = Thread.currentThread();
        }

        Socket clientSocket;

        while(true){

            try{
               //Accept the incoming connection request from the client to the server.
                clientSocket = serverSocket.accept();
                System.out.print("\n\nNew client has been accepted in the server:  " + clientSocket);

                //Get IO streams for the client to send to the thread.
                DataInputStream input = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

                //Default user name which is just the word client concatenated with their position in the server array list.
                String userName = "Client" + clientList.size();

                ClientThread client = new ClientThread(clientSocket, userName, input, output);

                Thread clientThread = new Thread(client);
                clientList.add(client);
                clientThread.start();
            }
            catch(IOException e){

            }
        }
    }

    //Helper class for server which starts a new thread for every client that connects to the server.  This handles the clients IO for the server.
    class ClientThread implements Runnable{

        private String clientName;
        private Socket clientSocket;
        private final DataInputStream input;
        private final DataOutputStream output;
        private boolean keepGoing;

        public ClientThread (Socket socket, String name, DataInputStream input, DataOutputStream output){
            this.input = input;
            this.output = output;
            this.clientName = name;
            this.clientSocket = socket;
            keepGoing = true;
        }

        @Override
        public void run() {

            String messageReceived;

            //Main IO loop for the client
            while (keepGoing){

                try {
                    messageReceived = input.readUTF();

                    handleCommand(messageReceived);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }//End of main IO loop

            try{

                int index = 0;

                //Find this client thread inside the clientList array in the server.
                for (ClientThread ct : clientList){

                    //If the current clients name is equal to the currently iterated client in clientList array, then remove it from the array.
                    if (ct.clientName.equalsIgnoreCase(clientName)){
                        System.out.print("\n" + clientName + " has been removed from the server clientList array");
                        clientList.remove(index);
                        break;
                    }

                    index++;
                }

                input.close();
                output.close();
                clientSocket.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }

        }

        //Method to handle any commands passed in from the user chat.  These commands are signaled with a / at the beginning of the message. (ex. /list, /help, /exit, etc.)
        private void handleCommand(String message){

            //Grab the first three words in the message, the rest are irrelevant for the command.
            String[] commands = new String[3];
            commands = message.split(" ", 3);

            //Check first character of the string to see if the message sent is a command.
            //Commands start with / character at beginning.
            if (commands[0].charAt(0) == '/'){
                if (commands[0].equalsIgnoreCase("/help")){
                    System.out.print("\n\n\t" + "1.  /myip\n" +
                                    "2.  /myport\n" +
                                    "3.  /list\n" +
                                    "4.  /myip\n" +
                                    "5.  /terminate <client name>\n" +
                                    "6.  /send <client name> <message>\n" +
                                    "7.  /exit");
                }

                else if (commands[0].equalsIgnoreCase("/myip"))
                    System.out.print("\n\n\tMy Ip:  " + clientSocket.getInetAddress().getHostAddress());

                else if (commands[0].equalsIgnoreCase("/myport"))
                    System.out.print("\n\n\tListen Port:  " + port);

                else if (commands[0].equalsIgnoreCase("/list")){

                }

                else if (commands[0].equalsIgnoreCase("/terminate")){

                }

                else if (commands[0].equalsIgnoreCase("/send")){

                }

                else if (commands[0].equalsIgnoreCase("/exit")){
                    System.out.print("\n\nLogging out...");
                    keepGoing = false;
                }

                else{
                    System.out.print("\n\n\t" + commands[0] + " is not recognized as a command.  Type /help for a list of commands.");
                }
            }

            //Message sent is not a command order, so broadcast it to all other clients.
            else {

            }

        }

    }

}

