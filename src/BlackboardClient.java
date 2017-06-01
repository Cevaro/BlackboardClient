/**
 * Created by D062299 on 10.05.2017.
 */

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BlackboardClient {

    private static String serverAddress = null;
    private static URL apiUrl = null;
    private static String boardname = null;
    private static String message = null;
    private static StringBuilder result;
    private static String mode = null;

    public static void main(String[] args) throws IOException {

        int i = 0;
        String argument;
        boolean exitFlag = false;


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
                if ((i < args.length) && !args[i].startsWith("-")) {
                    boardname = args[i];
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
                if ((i < (args.length + 1)) && !args[i].startsWith("-") && !args[(i + 1)].startsWith("-")) {
                    boardname = args[i];
                    message = args[++i];
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
            } else if (argument.equals("--read") || argument.equals("-r")) {
                if ((i < args.length) && !args[i].startsWith("-")) {
                    boardname = args[i];
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
                if ((i < args.length) && !args[i].startsWith("-")) {
                    boardname = args[i];
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
                if ((i < args.length) && !args[i].startsWith("-")) {
                    boardname = args[i];
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
                if ((i < args.length) && !args[i].startsWith("-")) {
                    boardname = args[i];
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
                if (mode == null) {
                    mode = "SHOW";
                } else {
                    System.err.println("Only one action can be performed at a time.");
                    exitFlag = true;
                }

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
        } else if (!(mode.equals("CREATE") || mode.equals("DISPLAY") || mode.equals("READ") || mode.equals("CLEAR")
                || mode.equals("DELETE") || mode.equals("STATUS") || mode.equals("SHOW"))) {
            System.out.println("Invalid action. --h for help.");
            exitFlag = true;
        }

        System.out.println("Server address: " + serverAddress);
        System.out.println("Mode: " + mode);
        System.out.println("Boardname: " + boardname);
        System.out.println("Message: " + message);

        if (exitFlag == true) {
            System.out.println("Invalid input format. --h for help.");
            System.exit(0);
        } else {
            System.out.println("\nAccessing server...");
        }

        //HTTP request
        sendRequest();

    }

    public static void sendRequest() {
        //get target URL
        try {
            switch (mode) {
                case "CREATE":
                    apiUrl = new URL(serverAddress + "/create_blackboard?name=" + boardname);
                    break;
                case "DISPLAY":
                    apiUrl = new URL(serverAddress + "/display_blackboard?name=" + boardname + "&message=" + message);
                    break;
                case "READ":
                    apiUrl = new URL(serverAddress + "/read_blackboard?name=" + boardname);
                    break;
                case "CLEAR":
                    apiUrl = new URL(serverAddress + "/clear_blackboard?name=" + boardname);
                    break;
                case "DELETE":
                    apiUrl = new URL(serverAddress + "/delete_blackboard?name=" + boardname);
                    break;
                case "STATUS":
                    apiUrl = new URL(serverAddress + "/get_blackboard_status?name=" + boardname);
                    break;
                case "SHOW":
                    apiUrl = new URL(serverAddress + "/show_blackboards");
                    break;
                default:
                    System.err.println("Invalid action.");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            //Create connection
            HttpURLConnection conn = (HttpURLConnection) apiUrl.openConnection();
            result = new StringBuilder();
            conn.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String singleLine;
            while ((singleLine = reader.readLine()) != null) {
                result.append(singleLine);
            }
            reader.close();
            printResult(result);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static void printResult(StringBuilder input) throws ParseException {
        switch (mode) {
            case "SHOW":
                System.out.println("The following blackboards were returned:");
                JSONParser parser = new JSONParser();
                JSONArray content = (JSONArray) parser.parse(input.toString());
                JSONObject jsonBlackboard = new JSONObject();
                for (int i = 0; i < content.size(); i++) {
                    jsonBlackboard = (JSONObject) content.get(i);
                    System.out.println(jsonBlackboard.get("name"));
                    System.out.println(jsonBlackboard.get("message")+"\n");
                }
                break;
            default:
                System.out.println(input.toString());
        }
    }

    public static void showHelp() {
        System.out.println("Usage:");
        System.out.println("BlackboardClient --server <address> --create <boardname>");
        System.out.println("BlackboardClient --server <address> --display <boardname> <message>");
        System.out.println("BlackboardClient --server <address> --read <boardname>");
        System.out.println("BlackboardClient --server <address> --clear <boardname>");
        System.out.println("BlackboardClient --server <address> --delete <boardname>");
        System.out.println("BlackboardClient --server <address> --status <boardname>");
        System.out.println("BlackboardClient --server <address> --show");
        System.out.println("BlackboardClient --help \n");

        System.out.println("Options:");
        System.out.println("--help,-h: Display usage help");
        System.out.println("--server,-s <address>: Specify the target server with address <address>.");
        System.out.println("--create,-c <boardname>: Create a new blackboard with name <boardname>.");
        System.out.println("--display,-d <boardname> <message>: Assign message <message> to board <boradname>");
        System.out.println("--read,-r <boardname>: Read the message of board <boardname>");
        System.out.println("--clear,-cl <boardname>: Delete the message of board <boardname>");
        System.out.println("--delete,-de <boardname>: Delete the entirety of board <boardname>");
        System.out.println("--status <boardname>: Display the status of the board <boardname>");
        System.out.println("--show,-sh: Display the list of all available blackboards");

    }
}