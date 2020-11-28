package socialnetwork.repository.file;

import socialnetwork.domain.User;
import socialnetwork.domain.message.ReplyMessage;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;
import socialnetwork.utils.Constants;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ReplyMessageFile extends AbstractFileRepository<Long , ReplyMessage> {



    /**
     * Constructor that creates a new Reply Message
     * @param fileName String, representing the file name where the data is loaded or written
     * @param validator Validator, validates the reply message
     *
     */
    public ReplyMessageFile(String fileName, Validator<ReplyMessage> validator,
                            Repository<Long,User> userRepository) {
        super(fileName, validator,userRepository);
    }

    @Override
    public ReplyMessage extractEntity(List<String> attributes) {
        Long idSender = Long.parseLong(attributes.get(1));
        Long idReceiver = Long.parseLong(attributes.get(2));
        String textMessage = attributes.get(3);
        LocalDateTime data = LocalDateTime.parse(attributes.get(4) , Constants.DATE_TIME_FORMATTER);
        Long idReply = Long.parseLong(attributes.get(5));
        return new ReplyMessage(repository.findOne(idSender),Arrays.asList(repository.findOne(idReceiver)),
                textMessage,data,findOne(idReply));
    }

    @Override
    protected String createEntityAsString(ReplyMessage entity) {
        String listTo = "";
        List<User> list = entity.getTo();
        for(User user:list){
            listTo += user.getId() + ",";
        }
        listTo = listTo.substring(0,listTo.length()-1);

        String messageAtributes = "";
        messageAtributes += entity.getId() + ";"
                + entity.getFrom().getId() + ";"
                + listTo + ";"
                + entity.getMessage() + ";"
                + entity.getDate().format(Constants.DATE_TIME_FORMATTER)+";";
        if(entity.getMessageReply() == null) {
            messageAtributes += "0";
        }
        else{
            messageAtributes += entity.getMessageReply().getId();
        }

        return messageAtributes;
    }
}
