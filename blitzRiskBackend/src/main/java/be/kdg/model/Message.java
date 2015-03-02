package be.kdg.model;

/**
 * Chatmessage used to send over websockets from client to server and back.
 */
public class Message { //the chat message itself
    private String message;
    private int id;
    private String username;
    private int color;


    public Message(){

    }

    public Message(int id, String message, String username, int color){
        this.id=id;
        this.message=message;
        this.username = username;
        this.color = color;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message=message;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
