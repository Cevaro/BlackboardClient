/**
 * Created by D062299 on 11.05.2017.
 */
public class Blackboard {
    private String name;
    private String message;

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nmessage: " + message;
    }
}
