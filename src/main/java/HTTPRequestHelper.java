import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by D062299 on 03.06.2017.
 */
public class HTTPRequestHelper {

    private static StringBuilder result;
    private static String usageMode;

    public static void startRequest(String mode, String serverAddress, String boardname, String message) throws IOException {
        usageMode = mode;
/*:***********************************************************************************************************
 *                             Create connection                                                             *
 ***********************************************************************************************************:*/
        //get target URL and create connection
        try {
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
            connection.setRequestProperty("Content-Type", "application/raw");
            connection.setRequestProperty("Content-Length", String.valueOf(body.length()));

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            for (String line; (line = reader.readLine()) != null; ) {
                System.out.println(line);
            }
            writer.close();
            reader.close();
        } catch (ProtocolException e) {
            e.printStackTrace();
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
            connection.setRequestProperty("Content-Type", "application/raw");
            connection.setRequestProperty("Content-Length", String.valueOf(body.length()));

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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
            printResult(result, usageMode);
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
            printResult(result, usageMode);
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
            printResult(result, usageMode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printResult(StringBuilder input, String mode) throws ParseException {
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

}
