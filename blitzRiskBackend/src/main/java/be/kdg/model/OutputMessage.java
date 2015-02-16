package be.kdg.model;

import org.springframework.http.HttpOutputMessage;

import java.util.Date;

/**
 * Created by vman on 7/02/2015.
 */
public class OutputMessage extends Message { //is a message with a timestamp
    private Date time;

    public OutputMessage(Message original, Date time){
        super(original.getId(),original.getMessage());
        this.time=time;
    }

    public Date getTime(){
        return time;
    }

    public void setTime(Date time){
        this.time=time;
    }


}
