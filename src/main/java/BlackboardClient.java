/**
 * Created by D062299 on 10.05.2017.
 */

import java.io.*;
import java.net.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class BlackboardClient {

    private static final String defaultServerAddress = "https://dhbw-blackboard.herokuapp.com/blackboard";

    private static String serverAddress = null;
    private static URL apiUrl = null;
    private static String boardname = null;
    private static String message = null;
    private static StringBuilder result;
    private static String mode = null;

    public static void main(String[] args) throws IOException {

        int argCounter = 0;
        String argument;
        boolean exitFlag = false;


/*************************************************************************************************************
 *                             Argument determination                                                        *
 *************************************************************************************************************/
        //if there are no arguments we can cancel early and print the help text
        if (args.length == 0) {
            showHelp();
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
                    boardname = args[argCounter];
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
                if ((argCounter < (args.length + 1)) && !args[argCounter].startsWith("-") && !args[(argCounter + 1)].startsWith("-")) {
                    boardname = args[argCounter];
                    message = args[++argCounter];
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
                if ((argCounter < args.length) && !args[argCounter].startsWith("-")) {
                    boardname = args[argCounter];
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
                    boardname = args[argCounter];
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
                    boardname = args[argCounter];
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
                    boardname = args[argCounter];
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

/*************************************************************************************************************
 *                             Argument evaluation                                                           *
 *************************************************************************************************************/
        //if no server address is specified: choose the default server address
        if (serverAddress == null) {
            serverAddress = defaultServerAddress;
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
        startRequest();

    }

    private static void startRequest() throws IOException {

/*************************************************************************************************************
 *                             Create connection                                                             *
 *************************************************************************************************************/
        //get target URL and create connection
        try {
            switch (mode) {
                case "CREATE":
                    apiUrl = new URL(serverAddress + "/create_blackboard");
                    sendPOSTRequest(apiUrl);
                    break;
                case "DISPLAY":
                    apiUrl = new URL(serverAddress + "/display_blackboard/" + boardname);
                    sendPUTRequest(apiUrl, message);
                    break;
                case "READ":
                    apiUrl = new URL(serverAddress + "/read_blackboard/" + boardname);
                    sendGETRequest(apiUrl);
                    break;
                case "CLEAR":
                    apiUrl = new URL(serverAddress + "/clear_blackboard/" + boardname);
                    sendPUTRequest(apiUrl);
                    break;
                case "DELETE":
                    apiUrl = new URL(serverAddress + "/delete_blackboard/" + boardname);
                    sendDELETERequest(apiUrl);
                    break;
                case "STATUS":
                    apiUrl = new URL(serverAddress + "/" + "blackboard_status/" + boardname);
                    sendGETRequest(apiUrl);
                    break;
                case "SHOW":
                    apiUrl = new URL(serverAddress + "/show_blackboards");
                    sendGETRequest(apiUrl);
                    break;
                default:
                    System.err.println("Invalid action.");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    private static void sendPOSTRequest(URL url) {
        try {
            String body = URLEncoder.encode(boardname, "UTF-8");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(body.length()));

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();


            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            for (String line; (line = reader.readLine()) != null; ) {
                System.out.println(line);
            }

            writer.close();
            reader.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendPUTRequest(URL url, String msg) {
        try {
            String body = URLEncoder.encode(msg, "UTF-8");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", String.valueOf(body.length()));

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();


            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            for (String line; (line = reader.readLine()) != null; ) {
                System.out.println(line);
            }

            writer.close();
            reader.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendPUTRequest(URL url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            result = new StringBuilder();
            conn.setRequestMethod("PUT");
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

    private static void sendGETRequest(URL url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

    private static void sendDELETERequest(URL url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            result = new StringBuilder();
            conn.setRequestMethod("DELETE");
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


    private static void printResult(StringBuilder input) throws ParseException {
        switch (mode) {
            case "SHOW":
                System.out.println("The following blackboards were returned:");
                JSONParser parser = new JSONParser();
                JSONArray content = (JSONArray) parser.parse(input.toString());
                JSONObject jsonBlackboard = new JSONObject();
                for (int i = 0; i < content.size(); i++) {
                    jsonBlackboard = (JSONObject) content.get(i);
                    System.out.println(jsonBlackboard.get("name"));
                    System.out.println(jsonBlackboard.get("message") + "\n");
                }
                break;
            default:
                System.out.println(input.toString());
        }
    }

    private static void showHelp() {
        System.out.println("Usage:");
        System.out.println("BlackboardClient [--server <address>] --create <boardname>");
        System.out.println("BlackboardClient [--server <address>] --display <boardname> <message>");
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
        System.out.println("--display,-d <boardname> <message>: Assign message <message> to board <boradname>");
        System.out.println("--read,-r <boardname>: Read the message of board <boardname>");
        System.out.println("--clear,-cl <boardname>: Delete the message of board <boardname>");
        System.out.println("--delete,-de <boardname>: Delete the entirety of board <boardname>");
        System.out.println("--status <boardname>: Display the status of the board <boardname>");
        System.out.println("--show,-sh: Display the list of all available blackboards");

    }
}