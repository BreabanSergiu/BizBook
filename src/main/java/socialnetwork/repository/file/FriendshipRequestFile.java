package socialnetwork.repository.file;

import socialnetwork.domain.User;
import socialnetwork.domain.message.FriendshipRequest;
import socialnetwork.domain.message.ReplyMessage;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;
import socialnetwork.utils.Constants;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class FriendshipRequestFile extends AbstractFileRepository<Long,FriendshipRequest> {

    /**
     *  Constructor that creates a new friedship request
     * @param fileName String, representing the file name where data is loaded and written
     * @param validator Validator, validates the friendship request
     * @param repository Repositori<T></>
     */
    public FriendshipRequestFile(String fileName, Validator<FriendshipRequest> validator, Repository<Long, User> repository) {
        super(fileName, validator, repository);
    }

    @Override
    public FriendshipRequest extractEntity(List<String> attributes) {

        Long idSender = Long.parseLong(attributes.get(1));
        Long idReceiver = Long.parseLong(attributes.get(2));
        String textMessage = attributes.get(3);
        LocalDateTime data = LocalDateTime.parse(attributes.get(4) , Constants.DATE_TIME_FORMATTER);
        String status = attributes.get(5);

        return new FriendshipRequest(repository.findOne(idSender), Arrays.asList(repository.findOne(idReceiver)),
                textMessage,data,status);

    }
//id;from;to(list);mesagge;data;status;
    @Override
    protected String createEntityAsString(FriendshipRequest entity) {

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
                + entity.getDate().format(Constants.DATE_TIME_FORMATTER)+";"
                + entity.getStatus();


        return messageAtributes;

    }
}
