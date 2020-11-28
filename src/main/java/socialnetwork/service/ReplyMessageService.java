package socialnetwork.service;

import socialnetwork.domain.message.ReplyMessage;
import socialnetwork.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class ReplyMessageService {

    private final Repository<Long, ReplyMessage> replyMessageServiceRepository;


    /**
     * Constructor that creates the ReplyMassege
     * @param replyMessageServiceRepository, Repository<T></T>
     */
    public ReplyMessageService(Repository<Long, ReplyMessage> replyMessageServiceRepository) {
        this.replyMessageServiceRepository = replyMessageServiceRepository;
    }


    /**
     *
     * @param replyMessage the ReplyMessage to be saved
     * @return null- if the ReplyMessage is saved
     *          otherwise return the ReplyMessage
     */
    public ReplyMessage addReplyMessage(ReplyMessage replyMessage){
        return replyMessageServiceRepository.save(replyMessage);
    }

    /**
     *
     * @param idReplyMessage Id of the ReplyMessage
     * @return  the ReplyMessage with the specified id
     */
    public ReplyMessage getReplyMessage(Long idReplyMessage){
        return replyMessageServiceRepository.findOne(idReplyMessage);
    }

    /**
     *
     * @param id1 Id of the ReplyMessage
     * @param id2 Id of the ReplyMessage
     * @return the conversation between the ReplyMessage with id1 and the ReplyMessage with id2
     */
    public Iterable<ReplyMessage> getConversation(Long id1,Long id2 ){
        Iterable<ReplyMessage> listReplyMessage = replyMessageServiceRepository.findAll();
        List<ReplyMessage> list = new ArrayList<ReplyMessage>();
        listReplyMessage.forEach(x->
        {
            if(x.getFrom().getId().equals(id1) && x.getTo().get(0).getId().equals(id2))
                list.add(x);

            if(x.getFrom().getId().equals(id2) && x.getTo().get(0).getId().equals(id1))
                list.add(x);
        });
        return list;
    }
}
