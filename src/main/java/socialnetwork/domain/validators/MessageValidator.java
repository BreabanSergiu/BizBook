

package socialnetwork.domain.validators;


import socialnetwork.domain.message.Message;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.MessageFile;

public class MessageValidator implements  Validator<Message> {
    @Override
    public void validate(Message entity) throws ValidationException {
        String err = "";
        if(entity.getMessage().equals("")){
            err += "Message is null!\n";
        }



        if(err.length() > 0){
            throw new ValidationException(err);
        }
    }
}