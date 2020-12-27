package socialnetwork.domain.message;

import socialnetwork.domain.Entity;
import socialnetwork.domain.User;
import socialnetwork.utils.Constants;

import java.time.LocalDateTime;
import java.util.List;

public class Message extends Entity<Long> {
    private static long idMaxMessage = 0;
    private static long idMaxReplyMessage = 0;
    private static long idMaxFriendshipRequest = 0;
    private User from;
    private List<User> to;
    private String message;
    private LocalDateTime date;


    public Message(User from, List<User> to, String message, LocalDateTime date, boolean x) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
        idMaxReplyMessage++;
        setId(idMaxReplyMessage);
    }
    public Message(User from, List<User> to, String message, LocalDateTime date) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
//        idMaxMessage++;
//        setId(idMaxMessage);
    }

    public Message(User from, List<User> to, String message, LocalDateTime date,String trash) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
//        idMaxFriendshipRequest++;
//        setId(idMaxFriendshipRequest);
    }

    public User getFrom() {
        return from;
    }
    public String getNameFrom(){
        return getFromFirstName() + " " + getFrom().getLastName();
    }

    public String getFromFirstName(){return from.getFirstName();}
    public String getFromLastName(){return from.getLastName();}


    public void setFrom(User from) {
        this.from = from;
    }

    public List<User> getTo() {
        return to;
    }

    public void setTo(List<User> to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getDateString(){ return date.format(Constants.DATE_TIME_FORMATTER);}

    public String getLocalDateString(){ return date.toLocalDate().toString();}

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from=" + from +
                ", to=" + to +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }

    
}
