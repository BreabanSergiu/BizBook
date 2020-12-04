package socialnetwork.domain.message;

import socialnetwork.domain.User;
import socialnetwork.utils.Constants;

import java.time.LocalDateTime;
import java.util.List;

public class FriendshipRequest extends Message {

    private String status;


    public FriendshipRequest(User from, List<User> to, String message, LocalDateTime date,String status) {
        super(from, to, message, date," for request");
        this.status = status;
    }

    public String getFirstNameFrom(){
        return getFrom().getFirstName();
    }

    public String getLastNameFrom(){
        return getFrom().getLastName();
    }

    public String getLastNameTo(){return getTo().get(0).getLastName();}

    public String getFirstNameTo(){return getTo().get(0).getFirstName();}


    @Override
    public String toString() {

        String messageString = "";
        messageString += "FriendshipRequest{"+ "id FriendshipRequest = "+super.getId() + " from = "+super.getFrom().getFirstName()+" "+ super.getFrom().getLastName()+
                " to  = " + super.getTo().get(0).getFirstName() +" "+super.getTo().get(0).getLastName()+
                " message= " + super.getMessage() +
                " data = " +super.getDate().format(Constants.DATE_TIME_FORMATTER)+
                " status = "+ status +" }";

        return messageString;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
