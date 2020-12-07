package socialnetwork.service;

import socialnetwork.domain.User;
import socialnetwork.domain.message.Message;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageService {

    private Repository<Long, Message> repositoryMessage;

    /**
     *  Constructor that creates the Message
     * @param repositoryMessage Repository<T></>
     */
    public MessageService(Repository<Long, Message> repositoryMessage) {
        this.repositoryMessage = repositoryMessage;
    }

    /**
     *
     * @param message Message to be saved
     * @return null- if the message is save
     *              otherwise return the message
     */
    public Message addMessage(Message message){
        List<User> list = message.getTo();
        list.forEach(user -> {
            if(user == null)    //put null if doesn t exist user with specified id
            {
                throw new ValidationException("there is no User with specified id!\n");
            }
        });
        return repositoryMessage.save(message);
    }

    /**
     * return all receive messages of user with long id
     * @param id
     * @return
     */
    public Iterable<Message> getReceiveMessageUser(Long id){

        Iterable<Message> listAllMessage = repositoryMessage.findAll();

        List<Message> filterList  = new ArrayList<>();
        listAllMessage.forEach(x->{
                x.getTo().forEach(user->{
                    if(user.getId().equals(id)){
                        filterList.add(x);
                    }
            });

        });

        return filterList;
    }

    public Message getMessage (Long id){
        return repositoryMessage.findOne(id);
    }
}
