package socialnetwork.domain.message;

import socialnetwork.domain.User;
import socialnetwork.utils.Constants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class ReplyMessage extends Message{
    Message messageReply;

    public ReplyMessage(User from, List<User> to, String message, LocalDateTime date, Message messageReply) {
        super(from, to, message, date,true);
        this.messageReply = messageReply;
    }

    public Message getMessageReply() {
        return messageReply;
    }

    public void setMessageReply(Message messageReply) {
        this.messageReply = messageReply;
    }

    @Override
    public String toString() {
        String messageString = "";
        messageString += "ReplyMessage{" + " from = "+super.getFrom().getFirstName()+" "+ super.getFrom().getLastName()+
                " to  = " + super.getTo().get(0).getFirstName() +" "+super.getTo().get(0).getLastName()+
                " message= " + super.getMessage() +
                " data = " +super.getDate().format(Constants.DATE_TIME_FORMATTER) ;

        if(getMessageReply() != null)
            messageString += " reply to= "+ getMessageReply().getMessage()+" }";
        return messageString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReplyMessage that = (ReplyMessage) o;
        return Objects.equals(messageReply, that.messageReply);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageReply);
    }
}
