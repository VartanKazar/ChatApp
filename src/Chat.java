public class Chat {

    public static void main(String[] args) {

        /*Main splash screen logic for the program.  Detailed description of the options can be found in the project requirements pdf under section 3.3.
        1:  help
        2:  myip
        3:  myport
        4:  startserver
        5:  connect <destination> <port #>
        6:  list
        7:  terminate <connection id>
        8:  send <connection id> <message>
        9:  exit

         */
        String menuSelection = "";

        Client client = new Client();

        //Main program loop to wait for user input and act accordingly.
        while(!menuSelection.equalsIgnoreCase("exit")){
            switch(menuSelection){

                case "help":
                    System.out.println("------------------------------------------\n" +
                                        "\t\tAvailable Commands" +
                                        "\nhelp:  " +
                                        "\nmyip:  " +
                                        "");
                    break;

                default:
                    System.out.println("\"" + menuSelection + "\" is not a valid command.  Type \"help\" for a list of accepted commands");
                    break;

            }//End switch case for user input.
        }//End main while loop for user input.

    }
}
