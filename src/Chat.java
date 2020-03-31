import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.net.InetAddress;
import java.lang.Exception;


public class Chat {

    private static int port = 8080;
    private static String serverAddress;
    private static String userName;

    private static Scanner input;

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
        userName = "Gman";

        String customServer = "";
        int customPort = 0;

        input = new Scanner(System.in);

        try{
            System.out.print("\n\t" + "Enter server ip or leave blank for localhost default:  ");
            customServer = input.nextLine();
        }
        catch(InputMismatchException e){
            System.out.print("\nInput mismatch exception:  " + e);
        }

        try{
            System.out.print("\n\t" + "Enter port or leave blank for 1337 default:  ");

            customPort = Integer.parseInt("0" + input.nextLine());
        }
        catch(InputMismatchException e){
            System.out.print("\nInput mismatch exception:  " + e);
        }

        //Check format for server ip string.  If format is incorrect, break out of current switch case to loop back around for input.
        if (!validateIp(serverAddress)){
            System.out.print("\n" + customServer + " is not a valid ip address!");
            return null;
        }

        //Check the format for port, if it is incorrect then break.
        if (customPort < 0 || customPort > 99999){
            System.out.print("\n" + customPort + " is not a valid port number (between 1 and 99999)!");
            return null;
        }

        //If user entered a custom server, set the address to be used to new ip.
        if (!customServer.isEmpty())
            serverAddress = customServer;

        //If user entered a custom port, set the port to be used to new port.
        if (customPort != 0)
            port = customPort;

        input.close();

        return new Client(serverAddress, port, userName);
    }

    public static void connect() throws UnknownHostException, InterruptedException {

        Client client = createClient();

        //Check if client has been correctly created and is not null.
        if (client == null) {
            System.out.print("\n\nClient has not been created yet.  Cannot attempt to connect to server when client is null!");
            return;
        }

        //Test if we can start the connection to the server with the new client object.
        if (!client.start()) {
            System.out.print("\n\nClient has failed to start!");
            return;
        }
//
//        input = new Scanner(System.in);
//
//        while(true){
//
//            System.out.print("\n\tinput:  ");
//
//            String msg = input.nextLine();
//
//            System.out.print("\n\tYou typed:  " + msg);
//
//            //Log out of server if user typed the logout message.
//            if (msg.equalsIgnoreCase("exit")){
//
//                break;
//            }
//
//            //Client requests a list of connected users with the list command.
//            else if (msg.equalsIgnoreCase("list")){
//
//            }
//
//
//            //Client asks for help with the help command.  No other users are required to see this so it isn't transmitted.
//            else if (msg.equalsIgnoreCase("help")){
//                System.out.print("\n----------------------------------------------------------------------------------------\n" +
//                        "\t\t\tAvailable Commands" +
//                        "\nhelp:  Display information about the available user interface options or command manual." +
//                        "\nmyip:  Display the IP address of this process." +
//                        "\nmyport:  Display the port on which this process is listening for incoming connections." +
//                        "\nlist:  Display a numbered list of all the connections this process is part of." +
//                        "\nterminate <connection id.>:  This command will terminate the connection listed under the specified number when LIST is used to display all connections." +
//                        "\nsend <connection id.> <chatMsg>:  Sends a chatMsg to the specified connected user." +
//                        "\nexit:  exits out of the program and the server." +
//                        "\n----------------------------------------------------------------------------------------\n");
//            }
//
//            //Client asks for their own ip using the myip command.
//            else if (msg.equalsIgnoreCase("myip")){
//                System.out.print("\n\t" + userName + " ip address:  " + InetAddress.getLocalHost().getHostAddress());
//            }
//
//            //client asks for the port of the connection using the myport command.
//            else if (msg.equalsIgnoreCase("myport")){
//                System.out.print("\n\t" + userName + " port:  " + port);
//            }
//
//            //Client boots out another user from the server using the terminate command with the given user number from the list command.
//            else if (msg.contains("terminate")){
//
//            }
//
//            //Default case where the user inputs a generic message that is not a command.
//            else {
//
//            }
//        }
//
//        input.close();
        client.disconnect();
    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {

        Server server = null;

        //Look for command line args that supply a port.
        switch(args.length){

            //No command line port was given, so by default port will be set to 8080.
            case 0:
                System.out.print("\nNo command line argument given on start, defaulting to port 1337...");
                port = 8080;
                break;

            //When 1 argument is passed in the command line parameters when running.
            case 1:
                try{
                    port = Integer.parseInt(args[1]);
                }
                catch (Exception e){
                    System.out.print("\nInvalid port number.");
                }

                break;
        }//End command line args switch case

        //Main splash screen logic for the program.
        String menuSelection = "";

        input = new Scanner(System.in);

        //Main program loop to wait for user input and act accordingly.
        while(!menuSelection.equalsIgnoreCase("exit")){

            System.out.print("\n--------------  MAIN MENU  -----------------\n" +
                            "1.  startserver:  Create a new server on your machine using your local ip.\n" +
                            "2.  connect:  Prompts you for a server ip and port to connect to.\n" +
                            "3.  stopserver:  If a server exists on this machine, this command will close all connections to it, then stop the server." +
                            "3.  exit:  terminate this process.\n");

            System.out.print("\n\tcmd:  ");
            menuSelection = input.nextLine();  //Get user input for a command.

            //An array of the menuSelection string split by any spaces found.  This is to check the inputs first word given for specific commands.
            String[] menuAry = menuSelection.split(" ");

            switch(menuAry[0]){

                case "startserver":

                    System.out.print("\n\tStarting server on given port " + port + "....");

                    server = new Server(port);  //Instantiate new server with the given port
                    new Thread(server).start();

                    break;

                case "connect":
                    connect();
                    break;

                case "stopserver":
                    System.out.print("\n\nStopping server...");

                    if (server != null)
                        server.stop();
                    break;

                case "exit":
                    input.close();
                    System.out.print("\n\n\tShutting down the process...");
                    break;

                default:
                    System.out.println("\"" + menuSelection + "\" is not a valid command.  Type \"help\" for a list of accepted commands");
                    break;

            }//End switch case for user input.
        }//End main while loop for user input.

    }
}
