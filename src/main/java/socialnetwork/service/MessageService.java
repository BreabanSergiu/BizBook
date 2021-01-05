package socialnetwork.service;

import socialnetwork.domain.Page;
import socialnetwork.domain.User;
import socialnetwork.domain.message.Message;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.MessageDbRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageService {

    private MessageDbRepository repositoryMessage;

    /**
     *  Constructor that creates the Message
     * @param repositoryMessage Repository<T></>
     */
    public MessageService(MessageDbRepository repositoryMessage) {
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
     * @param id Long , id of the user
     * @return all receive messages of user with long id
     */
    public Iterable<Message> getAllMessagesToUser(Long id){

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

    /**
     * method that return one message
     * @param id id of selected user
     * @return one message
     */
    public Message getMessage (Long id){
        return repositoryMessage.findOne(id);
    }


    /**
     * method that returns all messages between one date
     * @param id id of selected user
     * @param startDate start Date for received messages
     * @param endDate end Date for received messages
     * @return all messages between one date
     */
   public List<Message> getMessagesBetweenDate(Long id, LocalDate startDate,LocalDate endDate){
       List<Message> messageList = new ArrayList<>();

       Iterable<Message> messageIterable = getAllMessagesToUser(id);
       messageIterable.forEach(message -> {
           if(message.getDate().toLocalDate().compareTo(startDate) >= 0  && message.getDate().toLocalDate().compareTo(endDate)<=0){
                messageList.add(message);
           }
       });
       return messageList;

   }

    /**
     * method that returns all messages between one date and received from one friend
     * @param idUser id of selected user
     * @param idFriend id of the selected user's friend
     * @param startDate start Date for received messages
     * @param endDate end Date for received messages
     * @return all messages between one date and received from one friend
     */
   public List<Message> getAllMessageFromOneFriendBetweenDate(Long idUser, Long idFriend,LocalDate startDate,LocalDate endDate){
       List<Message> messageList = new ArrayList<>();
       Iterable<Message> messages = getAllMessagesToUser(idUser);

       for(Message message: messages){

           if(message.getDate().toLocalDate().compareTo(startDate) >= 0  && message.getDate().toLocalDate().compareTo(endDate)<=0){
               if(message.getFrom().getId().equals(idFriend)){
                   messageList.add(message);
               }
           }

       }
       return messageList;
   }


    /**
     * method that return all messages from currentPage
     * @param currentPage Page, the page where we want to get the messages from
     * @return all messages from currentPage
     */
   public Iterable<Message> getAllMessages(Page currentPage){
       return repositoryMessage.findAll(currentPage);
   }

    /**
     * method that return all receive messages of user with id idUser
     * @param idUser Long, id of the user
     * @param currentPage Page , the page where we want to get the messages from
     * @return all receive messages of user with id idUser
     */
   public Iterable<Message> getAllMessagesToUser(Long idUser,Page currentPage){
      return repositoryMessage.findAll(currentPage,idUser);
   }




}
