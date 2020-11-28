package socialnetwork.repository.file;

import socialnetwork.domain.User;
import socialnetwork.domain.message.Message;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;
import socialnetwork.utils.Constants;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageFile extends AbstractFileRepository <Long, Message>{

    /**
     *  Constructor that creates a new Message
     * @param fileName String, representing the file name where data is loaded or written
     * @param validator Validator, validates the message
     */
    public MessageFile(String fileName, Validator<Message> validator, Repository<Long , User> userRepository)  {
        super(fileName, validator,userRepository);
    }

    @Override
    public Message extractEntity(List<String> attributes) {
//        Message message = new Message(new User("Test","test"),new ArrayList<>(),"TEST", LocalDateTime.now());
//        return message;

        //8;1;2,3;salut;2020-11-11 15:21:20

        Long idUserSender = Long.parseLong(attributes.get(1));
        String list = attributes.get(2);

        String[] parts = list.split(",");
        List<User> idUsersReceiver = new ArrayList<>();

        for(String p : parts){
            Long id = Long.parseLong(p);
            User user = repository.findOne(id);
            idUsersReceiver.add(user);
        }

        String message = attributes.get(3);
        LocalDateTime dateTime = LocalDateTime.parse(attributes.get(4),Constants.DATE_TIME_FORMATTER);

        return new Message(repository.findOne(idUserSender),idUsersReceiver,message,dateTime);

    }

    @Override
    protected String createEntityAsString(Message entity) {
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
                + entity.getDate().format(Constants.DATE_TIME_FORMATTER);

        return messageAtributes;
    }
}
