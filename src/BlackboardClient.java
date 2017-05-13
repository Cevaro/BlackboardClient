/**
 * Created by D062299 on 10.05.2017.
 */

import java.io.IOException;

import org.springframework.web.client.RestTemplate;

public class BlackboardClient {

    public static void main(String[] args) throws IOException {

        int i = 0;
        String argument;
        String serverAddress = null;
        boolean exitFlag = false;
        String boardname = null;
        String message = null;
        String mode = null;
        boolean statusFlag = false;

        if (args.length == 0) {
            showHelp();
            System.exit(0);
        }

        while (i < args.length && args[i].startsWith("-")) {
            argument = args[i++];

            if (argument.equals("--server") || argument.equals("-s")) {
                if (i < args.length)
                    serverAddress = args[i++];
                else {
                    exitFlag = true;
                }
            } else if (argument.equals("--create") || argument.equals("-c")) {
                if ((i < args.length) && !args[i].startsWith("--")) {
                    boardname = args[i];
                    if (mode == null) {
                        mode = "create";
                    } else {
                        System.err.println("Only one action can be performed at a time.");
                        exitFlag = true;
                    }
                } else {
                    System.err.println("Board name not specified.");
                    exitFlag = true;
                }
            } else if (argument.equals("--display") || argument.equals("-d")) {
                if ((i < (args.length + 1)) && !args[i].startsWith("--") && !args[(i + 1)].startsWith("--")) {
                    boardname = args[i];
                    message = args[++i];
                    if (mode == null) {
                        mode = "display";
                    } else {
                        System.err.println("Only one action can be performed at a time.");
                        exitFlag = true;
                    }
                } else {
                    System.err.println("Board name not specified.");
                    exitFlag = true;
                }
            } else if (argument.equals("--read") || argument.equals("-r")) {
                if ((i < args.length) && !args[i].startsWith("--")) {
                    boardname = args[i];
                    if (mode == null) {
                        mode = "read";
                    } else {
                        System.err.println("Only one action can be performed at a time.");
                        exitFlag = true;
                    }
                } else {
                    System.err.println("Board name not specified.");
                    exitFlag = true;
                }
            } else if (argument.equals("--clear") || argument.equals("-cl")) {
                if ((i < args.length) && !args[i].startsWith("--")) {
                    boardname = args[i];
                    if (mode == null) {
                        mode = "clear";
                    } else {
                        System.err.println("Only one action can be performed at a time.");
                        exitFlag = true;
                    }
                } else {
                    System.err.println("Board name not specified.");
                    exitFlag = true;
                }
            } else if (argument.equals("--delete") || argument.equals("-de")) {
                if ((i < args.length) && !args[i].startsWith("--")) {
                    boardname = args[i];
                    if (mode == null) {
                        mode = "delete";
                    } else {
                        System.err.println("Only one action can be performed at a time.");
                        exitFlag = true;
                    }
                } else {
                    System.err.println("Board name not specified.");
                    exitFlag = true;
                }

            } else if (argument.equals("--show") || argument.equals("-sh")) {
                if (mode == null) {
                    mode = "show";
                } else {
                    System.err.println("Only one action can be performed at a time.");
                    exitFlag = true;
                }

            } else if (argument.equals("--status") || argument.equals("-st")) {
                statusFlag = true;

            } else if (argument.equals("--help") || argument.equals("--h") || argument.equals("-help") ||
                    argument.equals("-h")) {
                showHelp();
            }
        }

        if (serverAddress == null) {
            System.err.println("Server address not specified.");
            exitFlag = true;
        }

        if (mode == null) {
            System.err.println("No action specified.");
            exitFlag = true;
        } else if (!(mode.equals("create") || mode.equals("display") || mode.equals("read") || mode.equals("clear")
                || mode.equals("delete") || mode.equals("show"))) {
            System.out.println("Invalid action. --h for help.");
            exitFlag = true;
        }

        System.out.println("Server address: " + serverAddress);
        System.out.println("Mode: " + mode);
        System.out.println("Status: " + statusFlag);
        System.out.println("Boardname: " + boardname);
        System.out.println("Message: " + message);

        if (exitFlag == true) {
            System.out.println("Invalid input format. --h for help.");
            System.exit(0);
        } else {
            System.out.println("\nAccessing server...");
        }

        RestTemplate restTemplate = new RestTemplate();
        //Blackboard blackboard = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Blackboard.class);

    }

    public static void showHelp() {
        System.out.println("Usage:");
        System.out.println("BlackboardClient --server <address> --create <boardname> [--status]");
        System.out.println("BlackboardClient --server <address> --display <boardname> <message> [--status]");
        System.out.println("BlackboardClient --server <address> --read <boardname> [--status]");
        System.out.println("BlackboardClient --server <address> --clear <boardname> [--status]");
        System.out.println("BlackboardClient --server <address> --delete <boardname> [--status]");
        System.out.println("BlackboardClient --server <address> --show) [--status]");
        System.out.println("BlackboardClient --help \n");

        System.out.println("Options:");
        System.out.println("--help,-h: Display usage help");
        System.out.println("--server,-s <address>: Specify the target server with address <address>.");
        System.out.println("--create,-c <boardname>: Create a new blackboard with name <boardname>.");
        System.out.println("--display,-d <boardname> <message>: Assign message <message> to board <boradname>");
        System.out.println("--read,-r <boardname>: Read the message of board <boardname>");
        System.out.println("--clear,-cl <boardname>: Delete the message of board <boardname>");
        System.out.println("--delete,-de <boardname>: Delete the entirety of board <boardname>");
        System.out.println("--show,-sh: Display the list of all available blackboards");
        System.out.println("--status: Display the board's status after performing the action");

    }
}