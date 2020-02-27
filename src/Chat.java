import java.net.UnknownHostException;
import java.util.Scanner;

public class Chat {

    public static void main(String[] args) throws UnknownHostException {

        /*Main splash screen logic for the program.  Detailed description of the options can be found in the project requirements pdf under section 3.3.
        1:  help
        2:  myip
        3:  myport
        4:  startserver <OPTIONAL Port>
        5:  connect <destination> <port #>
        6:  list
        7:  terminate <connection id>
        8:  send <connection id> <message>
        9:  exit
         */
        String menuSelection = "";

        Scanner input = new Scanner(System.in);
        Client client = new Client();

        //Main program loop to wait for user input and act accordingly.
        while(!menuSelection.equalsIgnoreCase("exit")){

            System.out.print("\ncmd:  ");
            menuSelection = input.nextLine();  //Get user input for a command.

            //An array of the menuSelection string split by any spaces found.  This is to check the inputs first word given for specific commands.
            String[] menuAry = menuSelection.split(" ");

            switch(menuAry[0]){

                case "help":
                    System.out.println("----------------------------------------------------------------------------------------\n" +
                                        "\t\tAvailable Commands" +
                                        "\nhelp:  Display information about the available user interface options or command manual." +
                                        "\nmyip:  Display the IP address of this process." +
                                        "\nmyport:  Display the port on which this process is listening for incoming connections." +
                                        "\nstartserver <OPTIONAL Port>:  Starts a new listen server either with the supplied port, or with default port." +
                                        "\nconnect <destination> <port no>:  This command establishes a new TCP connection to the specified <destination> at the specified <port no>." +
                                        "\nlist:  Display a numbered list of all the connections this process is part of." +
                                        "\nterminate <connection id.>:  his command will terminate the connection listed under the specified number when LIST is used to display all connections." +
                                        "\nsend <connection id.> <message>:  Sends a message to the specified connected user." +
                                        "\nexit:  exits out of the program and the server." +
                                        "\n----------------------------------------------------------------------------------------\n");
                    break;

                case "myip":
                    System.out.println("Ipv4 Address: ");
                    break;

                case "myport":
                    System.out.println("Listen Port: ");
                    break;

                case "startserver":
                    break;

                case "connect":
                    break;

                case "list":
                    break;

                case "terminate":
                    break;

                case "send":
                    break;

                case "exit":
                    break;

                default:
                    System.out.println("\"" + menuSelection + "\" is not a valid command.  Type \"help\" for a list of accepted commands");
                    break;

            }//End switch case for user input.
        }//End main while loop for user input.

    }
}
