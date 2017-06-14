/*
 * Created by D062299 on 10.05.2017.
 */

import java.io.*;
import java.util.StringJoiner;

public class BlackboardClient {

    private static final String defaultServerAddress = "https://dhbw-blackboard.herokuapp.com/blackboard";

    private static String serverAddress = null;
    private static String boardname = "";
    private static String message = "";
    private static String mode = null;

    public static void main(String[] args) throws IOException {

        int argCounter = 0;
        String argument;
        boolean exitFlag = false;


        /*:***********************************************************************************************************
        *                             Argument determination                                                        *
        ***********************************************************************************************************:*/
        //if there are no arguments we can cancel early and print the help text
        if (args.length == 0) {
            printHelp();
            System.exit(0);
        }

        while (argCounter < args.length && args[argCounter].startsWith("-")) {
            argument = args[argCounter++];

            if (argument.equals("--server") || argument.equals("-s")) {
                if (argCounter < args.length)
                    serverAddress = args[argCounter++];
                else {
                    exitFlag = true;
                }
            } else if (argument.equals("--create") || argument.equals("-c")) {
                if ((argCounter < args.length) && !args[argCounter].startsWith("-")) {
                    StringJoiner joiner = new StringJoiner(" ");
                    while (argCounter < args.length && !args[argCounter].startsWith("-")) {
                        joiner.add(args[argCounter++]);
                    }
                    boardname = joiner.toString();
                    if (mode == null) {
                        mode = "CREATE";
                    } else {
                        System.err.println("Only one action can be performed at a time.");
                        exitFlag = true;
                    }
                } else {
                    System.err.println("Board name not specified.");
                    exitFlag = true;
                }
            } else if (argument.equals("--display") || argument.equals("-d")) {
                if ((argCounter < (args.length + 1)) && !args[argCounter].startsWith("-")) {
                    StringJoiner joiner = new StringJoiner(" ");
                    while (argCounter < args.length && !args[argCounter].startsWith("-")) {
                        joiner.add(args[argCounter++]);
                    }
                    boardname = joiner.toString();
                    if (mode == null) {
                        mode = "DISPLAY";
                    } else {
                        System.err.println("Only one action can be performed at a time.");
                        exitFlag = true;
                    }
                } else {
                    System.err.println("Board name not specified.");
                    exitFlag = true;
                }
            } else if (argument.equals("--message") || argument.equals("-m")) {
                if ((argCounter < (args.length + 1)) && !args[argCounter].startsWith("-")) {
                    StringJoiner joiner = new StringJoiner(" ");
                    while (argCounter < args.length && !args[argCounter].startsWith("-")) {
                        joiner.add(args[argCounter++]);
                    }
                    message = joiner.toString();
                } else {
                    System.err.println("Message not specified.");
                    exitFlag = true;
                }
            } else if (argument.equals("--read") || argument.equals("-r")) {
                if ((argCounter < args.length) && !args[argCounter].startsWith("-")) {
                    StringJoiner joiner = new StringJoiner(" ");
                    while (argCounter < args.length && !args[argCounter].startsWith("-")) {
                        joiner.add(args[argCounter++]);
                    }
                    boardname = joiner.toString();
                    if (mode == null) {
                        mode = "READ";
                    } else {
                        System.err.println("Only one action can be performed at a time.");
                        exitFlag = true;
                    }
                } else {
                    System.err.println("Board name not specified.");
                    exitFlag = true;
                }
            } else if (argument.equals("--clear") || argument.equals("-cl")) {
                if ((argCounter < args.length) && !args[argCounter].startsWith("-")) {
                    StringJoiner joiner = new StringJoiner(" ");
                    while (argCounter < args.length && !args[argCounter].startsWith("-")) {
                        joiner.add(args[argCounter++]);
                    }
                    boardname = joiner.toString();
                    if (mode == null) {
                        mode = "CLEAR";
                    } else {
                        System.err.println("Only one action can be performed at a time.");
                        exitFlag = true;
                    }
                } else {
                    System.err.println("Board name not specified.");
                    exitFlag = true;
                }
            } else if (argument.equals("--delete") || argument.equals("-de")) {
                if ((argCounter < args.length) && !args[argCounter].startsWith("-")) {
                    StringJoiner joiner = new StringJoiner(" ");
                    while (argCounter < args.length && !args[argCounter].startsWith("-")) {
                        joiner.add(args[argCounter++]);
                    }
                    boardname = joiner.toString();
                    if (mode == null) {
                        mode = "DELETE";
                    } else {
                        System.err.println("Only one action can be performed at a time.");
                        exitFlag = true;
                    }
                } else {
                    System.err.println("Board name not specified.");
                    exitFlag = true;
                }

            } else if (argument.equals("--status") || argument.equals("-st")) {
                if ((argCounter < args.length) && !args[argCounter].startsWith("-")) {
                    StringJoiner joiner = new StringJoiner(" ");
                    while (argCounter < args.length && !args[argCounter].startsWith("-")) {
                        joiner.add(args[argCounter++]);
                    }
                    boardname = joiner.toString();
                    if (mode == null) {
                        mode = "STATUS";
                    } else {
                        System.err.println("Only one action can be performed at a time.");
                        exitFlag = true;
                    }
                } else {
                    System.err.println("Board name not specified.");
                    exitFlag = true;
                }

            } else if (argument.equals("--show") || argument.equals("-sh")) {
                argCounter++;
                if (mode == null) {
                    mode = "SHOW";
                } else {
                    System.err.println("Only one action can be performed at a time.");
                    exitFlag = true;
                }

            } else if (argument.equals("--help") || argument.equals("--h") || argument.equals("-help") ||
                    argument.equals("-h")) {
                printHelp();
            }
        }

        /*:*********************************************************************************************************
        *                             Argument evaluation                                                          *
        **********************************************************************************************************:*/
        //if no server address is specified: choose the default server address
        if (serverAddress == null) {
            serverAddress = defaultServerAddress;
        }

        if (mode == null) {
            System.err.println("No action specified.");
            exitFlag = true;
            //check if given mode is valid
        } else if (!(mode.equals("CREATE") || mode.equals("DISPLAY") || mode.equals("READ") || mode.equals("CLEAR")
                || mode.equals("DELETE") || mode.equals("STATUS") || mode.equals("SHOW"))) {
            System.out.println("Invalid action. --h for help.");
            exitFlag = true;
        }

        //print results of argument evaluation
        System.out.println("Server address: " + serverAddress);
        System.out.println("Mode: " + mode);
        System.out.println("Boardname: " + boardname);
        System.out.println("Message: " + message);

        //check for invalid input
        if (exitFlag) {
            System.out.println("\nInvalid input format. --h for help.");
            System.exit(0);
        } else if (boardname.length() > 64) {
            System.out.println("\nThe given boardname exceeds the limit of 64 characters.");
            System.exit(0);
        } else if (message.length() > 255) {
            System.out.println("\nThe given message exceeds the limit of 255 characters.");
            System.exit(0);
        } else if (mode.equals("DISPLAY") && message.isEmpty()) {
            System.out.println("\nNo message to be displayed.");
            System.exit(0);
        } else {
            System.out.println("\nAccessing server...");

        }

        //HTTP request
        HTTPRequestHelper.startRequest(mode, serverAddress, boardname, message);

    }

    private static void printHelp() {
        System.out.println("Usage:");
        System.out.println("BlackboardClient [--server <address>] --create <boardname>");
        System.out.println("BlackboardClient [--server <address>] --display <boardname> --message <message>");
        System.out.println("BlackboardClient [--server <address>] --read <boardname>");
        System.out.println("BlackboardClient [--server <address>] --clear <boardname>");
        System.out.println("BlackboardClient [--server <address>] --delete <boardname>");
        System.out.println("BlackboardClient [--server <address>] --status <boardname>");
        System.out.println("BlackboardClient [--server <address>] --show");
        System.out.println("BlackboardClient --help \n");

        System.out.println("Options:");
        System.out.println("--help,-h: Display usage help");
        System.out.println("--server,-s <address>: Specify the target server with address <address>. If no server address is specified, the project's Heroku server will be used by default.");
        System.out.println("--create,-c <boardname>: Create a new blackboard with name <boardname>.");
        System.out.println("--display,-d <boardname>: Specify a board <boradname> where a new message should be assigned");
        System.out.println("--message,-m <message>: Assign message <message> to the specified board.");
        System.out.println("--read,-r <boardname>: Read the message of board <boardname>");
        System.out.println("--clear,-cl <boardname>: Delete the message of board <boardname>");
        System.out.println("--delete,-de <boardname>: Delete the entirety of board <boardname>");
        System.out.println("--status <boardname>: Display the status of the board <boardname>");
        System.out.println("--show,-sh: Display the list of all available blackboards");
        System.exit(0);
    }
}