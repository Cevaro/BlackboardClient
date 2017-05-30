/**
 * Created by D062299 on 10.05.2017.
 */

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class BlackboardClient {

    private static RestTemplate restTemplate;
    private static String apiUrl = null;
    private static Blackboard board;
    private static BlackboardList list;

    public static void main(String[] args) throws IOException {

        int i = 0;
        String argument;
        String serverAddress = null;
        boolean exitFlag = false;
        String boardname = null;
        String message = null;
        String mode = null;

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
                if ((i < (args.length + 1)) && !args[i].startsWith("-") && !args[(i + 1)].startsWith("-")) {
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
                if ((i < args.length) && !args[i].startsWith("-")) {
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
                if ((i < args.length) && !args[i].startsWith("-")) {
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
                if ((i < args.length) && !args[i].startsWith("-")) {
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

            } else if (argument.equals("--status") || argument.equals("-st")) {
                if ((i < args.length) && !args[i].startsWith("-")) {
                    boardname = args[i];
                    if (mode == null) {
                        mode = "status";
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
                || mode.equals("delete") || mode.equals("status") || mode.equals("show"))) {
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

        if (mode.equals("create")) {
            createBoard(boardname);
        } else if (mode.equals("display")) {
            displayMessage(boardname, message);
        } else if (mode.equals("read")) {
            readMessage(boardname);
        } else if (mode.equals("clear")) {
            clearMessage(boardname);
        } else if (mode.equals("delete")) {
            deleteBoard(boardname);
        } else if (mode.equals("status")) {
            displayBoardStatus(boardname);
        } else if (mode.equals("show")) {
            showBoards();
        }
        restTemplate = new RestTemplate();
    }



    public static void createBoard(String boardname){
        apiUrl = "https://dhbw-blackboard.herokuapp.com/blackboard/create_blackboard?name=" + boardname;
        board = restTemplate.getForObject(apiUrl, Blackboard.class);
    }

    public static void displayMessage(String boardname, String message){
        apiUrl = "https://dhbw-blackboard.herokuapp.com/blackboard/display_blackboard?name=" + boardname + "&message=" + message;
        board = restTemplate.getForObject(apiUrl, Blackboard.class);
    }

    public static void readMessage(String boardname){
        apiUrl = "https://dhbw-blackboard.herokuapp.com/blackboard/read_blackboard?name=" + boardname;
        board = restTemplate.getForObject(apiUrl, Blackboard.class);
    }

    public static void clearMessage(String boardname){
        apiUrl = "https://dhbw-blackboard.herokuapp.com/blackboard/clear_blackboard?name=" + boardname;
        board = restTemplate.getForObject(apiUrl, Blackboard.class);
    }

    public static void deleteBoard(String boardname){
        apiUrl = "https://dhbw-blackboard.herokuapp.com/blackboard/delete_blackboard?name=" + boardname;
        board = restTemplate.getForObject(apiUrl, Blackboard.class);
    }

    public static void displayBoardStatus(String boardname){
        apiUrl = "https://dhbw-blackboard.herokuapp.com/blackboard/get_blackboard_status?name=" + boardname;
        board = restTemplate.getForObject(apiUrl, Blackboard.class);
    }

    public static void showBoards(){
        apiUrl = "https://dhbw-blackboard.herokuapp.com/blackboard/show_blackboards";
        String result = restTemplate.getForObject(apiUrl, String.class);

        System.out.println(result);
        /*ResponseEntity<List<Blackboard>> blackboardResponse = restTemplate.exchange(apiUrl,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Blackboard>>() {
                });
        List<Blackboard> blackboardList = blackboardResponse.getBody();

        Iterator<Blackboard> blackboardIterator = blackboardList.iterator();
        while (blackboardIterator.hasNext()) {
            System.out.println(blackboardIterator.next().toString());
        }
                /*restTemplate.getForEntity(apiUrl, Blackboard.class);
        List<Blackboard> boardList = blackboardResponse.getBody();
        MediaType contentType = responseEntity.getHeaders().getContentType();
        HttpStatus statusCode = responseEntity.getStatusCode();*/


        //list = restTemplate.getForObject(apiUrl, BlackboardList.class);
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