package socialnetwork;

import socialnetwork.config.ApplicationContext;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.message.FriendshipRequest;
import socialnetwork.domain.message.Message;
import socialnetwork.domain.message.ReplyMessage;
import socialnetwork.domain.validators.*;
import socialnetwork.gui.MainFX;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.*;
import socialnetwork.service.*;
import socialnetwork.ui.Ui;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        String fileName=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
//        Repository<Long, User> userFileRepository = new UserFile(fileName
//                , new UserValidator());
//
//
//        String fileNamePrieteni = ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.friendship");
//        Repository<Tuple<Long,Long>, Friendship> friendshipFileRepository = new FriendshipFile(fileNamePrieteni,
//                new FriendshipValidator(userFileRepository.findAll()),userFileRepository);
//
//
//        String fileNameMessage = ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.message");
//        Repository<Long, Message> messageFileRepository = new MessageFile(fileNameMessage,new MessageValidator(),userFileRepository);
//
//
//        String fileNameReplyMessage = ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.conversation");
//        Repository<Long, ReplyMessage> replyMessageFileRepository = new ReplyMessageFile(fileNameReplyMessage,
//                                                                new ReplyMessageValidator(),userFileRepository);
//
//        String fileNameFriednshipRequest = ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.request");
//        Repository<Long, FriendshipRequest> friendshipRequestFileRepository = new FriendshipRequestFile(fileNameFriednshipRequest
//                                                        ,new FriendshipRequestValidator(),userFileRepository);
//
//        UserService userService = new UserService(userFileRepository,friendshipFileRepository);
//        FriendshipService friendshipService = new FriendshipService(friendshipFileRepository,userFileRepository);
//        MessageService messageService = new MessageService(messageFileRepository);
//        ReplyMessageService replyMessageService = new ReplyMessageService(replyMessageFileRepository);
//        FriendshipRequestService friendshipRequestService = new FriendshipRequestService(friendshipRequestFileRepository);
//


        MainFX.main(args);
//        Ui ui = new Ui(userService, friendshipService,messageService,replyMessageService,friendshipRequestService);
//
//
//
//
//        ui.run();




//        //print output
//        userFileRepository.findAll().forEach(System.out::println);
//        friendshipFileRepository.findAll().forEach(System.out::println);
//        replyMessageFileRepository.findAll().forEach(System.out::println);
//        friendshipRequestFileRepository.findAll().forEach(System.out::println);

    }
}


