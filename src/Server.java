import java.net.ServerSocket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server implements Runnable {

    protected int port = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean isStopped = false;
    protected Thread runningThread = null;
    private final Lock mutex = new ReentrantLock(true);

    private static int uniqueId;    //A unique id for each connection on the server.
    private ArrayList<Server.ServerThread> clientList;     //A list of clients on the server

    public Server (int port){
        this.port = port;
        clientList = new ArrayList<Server.ServerThread>();
    }


    @Override
    public void run() {

        synchronized (this){
            this.runningThread = Thread.currentThread();
        }

        openServerSocket();

        while(!isStopped){
            Socket clientSocket = null;

            try {
                clientSocket = this.serverSocket.accept();
            }

            catch (IOException e) {

                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }

                throw new RuntimeException("Error accepting client connection", e);
            }

            ServerThread clientThread = new ServerThread(clientSocket);

            new Thread(clientThread).start();
            clientList.add(clientThread);  //Add it to the client list arrayList.
        }

        System.out.println("Server Stopped.") ;

    }

    private synchronized boolean isStopped(){ return isStopped; }

    public synchronized void stop(){

        this.isStopped = true;

        try {

            //Close all client socket connections and IOs frist before attempting to close the server down.
            for (int i = 0; i < clientList.size(); ++i){

                Server.ServerThread currentThread = clientList.get(i);

                try{
                    currentThread.input.close();
                    currentThread.output.close();
                    currentThread.clientSocket.close();
                }//End inner-most try-catch for closing individual clients sockets in the clientThread list.
                catch(IOException e){

                }
            }

            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    //Opens a server socket on the current machine with their local ip and port.
    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + port, e);
        }
    }

    public void writeString(ServerThread thread, String s)
    {
        mutex.lock();

        for(ServerThread th:clientList)
            if(th != null && th != thread) //different from the thread receiving the string
                th.writeString(s);  //send string to other threads

        mutex.unlock();
    }

    //A helper class for the server.  Rather than processing the incoming requests in the same thread that accepts the client connection, the connection is handed off to a worker thread that processes the request.
    public class ServerThread implements Runnable {

        protected Socket clientSocket = null;
        protected String userName = "";

        public ObjectInputStream input;
        public ObjectOutputStream output;

        public ServerThread (Socket clientSocket){
            this.clientSocket = clientSocket;

            //Instantiate both datastreams; input and output
            try{
                input = new ObjectInputStream(clientSocket.getInputStream());
                output = new ObjectOutputStream(clientSocket.getOutputStream());

                //Read the user name
                userName = (String) input.readObject();
                System.out.print("\n" + userName + " has connected.");
            }
            catch(IOException | ClassNotFoundException e){
                System.out.print("\nException creating new i/o streams:  " + e);
            }
        }

        @Override
        public void run() {

            try{
                output = new ObjectOutputStream(clientSocket.getOutputStream());
                input = new ObjectInputStream(clientSocket.getInputStream());


            }
            catch (IOException e){

            }

        }

        public void writeString(String s)
        {
//            mutex.lock();
//            output.println(s);
//            output.flush();
//            mutex.unlock();
        }

    }//End ServerThread class
}

