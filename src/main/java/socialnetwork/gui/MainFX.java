package socialnetwork.gui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jdk.javadoc.internal.doclets.toolkit.MethodWriter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.util.ResourceUtils;
import socialnetwork.config.ApplicationContext;
import socialnetwork.controller.IntroductionController;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Photo;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.message.FriendshipRequest;
import socialnetwork.domain.message.Message;
import socialnetwork.domain.message.ReplyMessage;
import socialnetwork.domain.validators.*;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.*;
import socialnetwork.repository.file.*;
import socialnetwork.service.*;
import socialnetwork.utils.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class MainFX extends Application {

    private static UserService userService ;
    private static FriendshipService friendshipService ;
    private static MessageService messageService ;
    private static ReplyMessageService replyMessageService;
    private static FriendshipRequestService friendshipRequestService ;
    private static PhotoService photoService;



    @Override
    public void start(Stage primaryStage) throws Exception {

        initView(primaryStage);
        primaryStage.setTitle("Window");
        primaryStage.setResizable(false);

        primaryStage.show();

    }

    private void initView (Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/introduction.fxml"));
        AnchorPane layout = loader.load();
        primaryStage.setScene(new Scene((layout)));
        IntroductionController introductionController = loader.getController();
        introductionController.setUserService(userService,primaryStage);
        introductionController.setFriendshipService(friendshipService);
        introductionController.setFriendshipRequestService(friendshipRequestService);
        introductionController.setMessageService(messageService);
        introductionController.setPhotoService(photoService);

    }

    public static void main(String[] args) {

        //db
        final String url = ApplicationContext.getPROPERTIES().getProperty("database.socialnetwork.url");
        final String username= ApplicationContext.getPROPERTIES().getProperty("databse.socialnetwork.username");
        final String pasword= ApplicationContext.getPROPERTIES().getProperty("database.socialnetwork.pasword");
        Repository<Long,User> userDbRepository = new UserDbRepository(url,username,pasword, new UserValidator());
        Repository<Tuple<Long,Long>,Friendship> friendshipDbRepository = new FriendshipDbRepository(url,username,pasword);
        Repository<Long,Message> messageDbRepository = new MessageDbRepository(url,username,pasword,  userDbRepository);
        Repository<Long,FriendshipRequest> friendshipRequestDbRepository = new FriendshipRequestDbRepository(url,username,pasword,userDbRepository);
        Repository<Long, Photo> photoDbRepository = new PhotoDbRepository(url,username,pasword);
        //file
        String fileName= ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
        Repository<Long, User> userFileRepository = new UserFile(fileName
                , new UserValidator());


        String fileNamePrieteni = ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.friendship");
        Repository<Tuple<Long,Long>, Friendship> friendshipFileRepository = new FriendshipFile(fileNamePrieteni,
                new FriendshipValidator(userFileRepository.findAll()),userFileRepository);


        String fileNameMessage = ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.message");
        Repository<Long, Message> messageFileRepository = new MessageFile(fileNameMessage,new MessageValidator(),userFileRepository);


        String fileNameReplyMessage = ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.conversation");
        Repository<Long, ReplyMessage> replyMessageFileRepository = new ReplyMessageFile(fileNameReplyMessage,
                new ReplyMessageValidator(),userFileRepository);

        String fileNameFriednshipRequest = ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.request");
        Repository<Long, FriendshipRequest> friendshipRequestFileRepository = new FriendshipRequestFile(fileNameFriednshipRequest
                ,new FriendshipRequestValidator(),userFileRepository);



        userService = new UserService(userDbRepository,friendshipDbRepository);
        friendshipService = new FriendshipService(friendshipDbRepository,userDbRepository);
        messageService = new MessageService(messageDbRepository);
        replyMessageService = new ReplyMessageService(replyMessageFileRepository);
        friendshipRequestService = new FriendshipRequestService(friendshipRequestDbRepository,friendshipDbRepository);
        photoService = new PhotoService(photoDbRepository);

       launch(args);




///--------------------reading data from databasex

//        System.out.println(userDbRepository.findOne(1l));
//        System.out.println("findAll");
//        System.out.println(userDbRepository.findAll());
//        User user = new User("breaban","Simona");
//        user.setId(5L);
        //userDbRepository.save(user);
        //userDbRepository.delete(3l);
        //userDbRepository.update(user);

//        System.out.println(friendshipDbRepository.findOne(new Tuple<>(1L, 2L)));
//        System.out.println(friendshipDbRepository.findAll());
//        Friendship friendship = new Friendship(LocalDate.now());
//        friendship.setId(new Tuple<>(1L,5L));
// //       friendshipDbRepository.save(friendship);
//        System.out.println(friendshipDbRepository.delete(new Tuple<>(1L,5L)));

        //System.out.println(messageDbRepository.findOne(3L));
        //System.out.println(messageDbRepository.findAll());
//        User user1 = userDbRepository.findOne(4L);
//        User user2 = userDbRepository.findOne(1L);
//        User user3 = userDbRepository.findOne(2L);


//        Message message = new Message(user2, Arrays.asList(user1,user3),"hello", LocalDateTime.now());
//        messageDbRepository.save(message);
//        messageDbRepository.delete(5L);

//        Repository<Long,FriendshipRequest> friendshipRequestDbRepository = new FriendshipRequestDbRepository(url,username,pasword,userDbRepository);
//        System.out.println( friendshipRequestDbRepository.findOne(1L));
//        System.out.println(friendshipRequestDbRepository.findAll());
//        FriendshipRequest friendshipRequest = new FriendshipRequest(user1,Arrays.asList(user3),"hey",LocalDateTime.now(),"pending");
//
//        friendshipRequestDbRepository.save(friendshipRequest);
//      friendshipRequestDbRepository.delete(8L);
//


    }
}
