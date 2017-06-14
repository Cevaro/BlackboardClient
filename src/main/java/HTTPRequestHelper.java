import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.*;

/*
 * Created by D062299 on 03.06.2017.
 */
class HTTPRequestHelper {

    private static StringBuilder result;
    private static String usageMode;

    static void startRequest(String mode, String serverAddress, String boardname, String message) throws IOException {
        usageMode = mode;
        /*:**********************************************************************************************************
        *                             Create connection                                                             *
        ***********************************************************************************************************:*/
        //get target URL and create connection
        try {
            boardname = boardname.replaceAll(" ", "%20");
            switch (usageMode) {
                case "CREATE":
                    URL apiUrl = new URL(serverAddress + "/create_blackboard");
                    sendPOSTRequest(apiUrl, boardname);
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

    private static void sendPOSTRequest(URL url, String body) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            writeOutput(connection, body);
            readResponse(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendPUTRequest(URL url, String body) {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            writeOutput(connection, body);
            readResponse(connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendPUTRequest(URL url) {
        try {
            BufferedReader reader;
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            result = new StringBuilder();

            try {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } catch (IOException e) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String singleLine;
            while ((singleLine = reader.readLine()) != null) {
                result.append(singleLine);
            }
            reader.close();
            printResult(result, usageMode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendGETRequest(URL url) {
        try {
            BufferedReader reader;
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            result = new StringBuilder();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            try {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } catch (IOException e) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String singleLine;
            while ((singleLine = reader.readLine()) != null) {
                result.append(singleLine);
            }
            reader.close();
            printResult(result, usageMode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendDELETERequest(URL url) {
        try {
            BufferedReader reader;
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            result = new StringBuilder();
            connection.setRequestMethod("DELETE");
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setUseCaches(false);


            try {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } catch (IOException e) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            String singleLine;
            while ((singleLine = reader.readLine()) != null) {
                result.append(singleLine);
            }
            reader.close();
            printResult(result, usageMode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeOutput(HttpURLConnection conn, String body) throws IOException {
        OutputStreamWriter writer = null;

        conn.setRequestProperty("Content-Type", "application/raw");
        conn.setRequestProperty("Content-Length", String.valueOf(body.length()));

        try {
            writer = new OutputStreamWriter(conn.getOutputStream());
        } catch (UnknownHostException e) {
            System.out.println("Cannot connect to server. Check your internet connection.");
            System.exit(1);
        }
        writer.write(body);
        writer.flush();
        writer.close();
    }

    private static void readResponse(HttpURLConnection conn) throws IOException {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } catch (IOException e) {
            reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        for (String line; (line = reader.readLine()) != null; ) {
            System.out.println(line);
        }
        reader.close();
    }


    private static void printResult(StringBuilder input, String mode) throws ParseException {
        switch (mode) {
            case "SHOW":
                System.out.println("The following blackboards were returned:");
                JSONParser parser = new JSONParser();
                JSONArray content = (JSONArray) parser.parse(input.toString());
                JSONObject jsonBlackboard = new JSONObject();
                for (Object aContent : content) {
                    jsonBlackboard = (JSONObject) aContent;
                    System.out.println(jsonBlackboard.get("name"));
                    System.out.println(jsonBlackboard.get("message") + "\n");
                }
                break;
            default:
                System.out.println(input.toString());
        }
    }
}