import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.net.InetAddress;
import java.lang.Exception;


public class Chat {

    private static int port = 8080;
    private static String serverAddress;

    //private static Scanner input;
    public static Scanner input = new Scanner(System.in);

    //Takes a string input for the serverIp from the user and validates it with regex comparison.
    private static boolean validateIp(final String ip) {
        if (ip.equalsIgnoreCase("localhost"))
            return true;

        //Regex pattern to compare string serveraddress.
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        return ip.matches(PATTERN);
    }

    private static Client createClient(){

        serverAddress = "localhost";

        String customServer = "";
        int customPort = 0;



        try{
            System.out.print("\nEnter server ip or leave blank for localhost default:  ");
            customServer = input.nextLine();
        }
        catch(InputMismatchException e){
            System.out.println("Input mismatch exception:  " + e);
        }

        try{
            System.out.print("Enter port or leave blank for default:  ");

            customPort = Integer.parseInt("0" + input.nextLine());
        }
        catch(InputMismatchException e){
            System.out.println("Input mismatch exception:  " + e);
        }

        //Check format for server ip string.  If format is incorrect, break out of current switch case to loop back around for input.
        if (!validateIp(serverAddress)){
            System.out.println(customServer + " is not a valid ip address!");
            return null;
        }

        //Check the format for port, if it is incorrect then break.
        if (customPort < 0 || customPort > 99999){
            System.out.println(customPort + " is not a valid port number (between 1 and 99999)!");
            return null;
        }

        //If user entered a custom server, set the address to be used to new ip.
        if (!customServer.isEmpty())
            serverAddress = customServer;

        //If user entered a custom port, set the port to be used to new port.
        if (customPort != 0)
            port = customPort;

        //input.close();

        return new Client(port, serverAddress);
    }

    public static void connect() throws InterruptedException {

        Client client = createClient();

        //Check if client has been correctly created and is not null.
        if (client == null) {
            System.out.println("Client has not been created yet.  Cannot attempt to connect to server when client is null!");
            return;
        }

        try {
            client.start();
            while(!client.isDisconnected);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) throws IOException, InterruptedException {

        Server server = null;

        //Look for command line args that supply a port.
        switch(args.length){

            //No command line port was given, so by default port will be set to 8080.
            case 0:
                System.out.println("No command line argument given on start, defaulting to port 1337...");
                port = 8080;
                break;

            //When 1 argument is passed in the command line parameters when running.
            case 1:
                try{
                    port = Integer.parseInt(args[1]);
                }
                catch (Exception e){
                    System.out.println("Invalid port number.");
                }

                break;
        }//End command line args switch case

        //Main splash screen logic for the program.
        String menuSelection = "";

        input = new Scanner(System.in);

        //Main program loop to wait for user input and act accordingly.
        while(!menuSelection.equalsIgnoreCase("/exit")){

            System.out.print("\n--------------  MAIN MENU  -----------------\n" +
                    "1.  /startserver: Create a new server on your machine using your local ip.\n" +
                    "2.  /connect: Prompts you for a server ip and port to connect to.\n" +
                    "3.  /exit: terminate this process.\n");

            System.out.print("\tMain Menu: ");
            menuSelection = input.nextLine();  //Get user input for a command.

            //An array of the menuSelection string split by any spaces found.  This is to check the inputs first word given for specific commands.
            String[] menuAry = menuSelection.split(" ");

            switch(menuAry[0]){

                case "/startserver":

                    System.out.println("Starting server on given port " + port + "....");

                    server = new Server(port);  //Instantiate new server with the given port
                    new Thread(server).start();

                    break;

                case "/connect":
                    connect();

                    break;

                case "/exit":
                    //input.close();
                    System.out.println("Shutting down the process...");
                    break;

                default:
                    System.out.println("\"" + menuSelection + "\" is not a valid command.");
                    break;

            }//End switch case for user input.
        }//End main while loop for user input.

    }
}
