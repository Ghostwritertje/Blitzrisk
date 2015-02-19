package be.kdg.model;

/**
 * Created by vman on 7/02/2015.
 */
public class Message { //the chat message itself
    private String message;
    private int id;

    public Message(){

    }

    public Message(int id, String message){
        this.id=id;
        this.message=message;
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
}
