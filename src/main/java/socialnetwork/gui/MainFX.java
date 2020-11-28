package socialnetwork.gui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import socialnetwork.config.ApplicationContext;
import socialnetwork.controller.IntroductionController;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.message.FriendshipRequest;
import socialnetwork.domain.message.Message;
import socialnetwork.domain.message.ReplyMessage;
import socialnetwork.domain.validators.*;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.*;
import socialnetwork.service.*;

import java.io.IOException;

public class MainFX extends Application {

    private static UserService userService ;
    private static FriendshipService friendshipService ;
    private static MessageService messageService ;
    private static ReplyMessageService replyMessageService;
    private static FriendshipRequestService friendshipRequestService ;



    @Override
    public void start(Stage primaryStage) throws Exception {

        initView(primaryStage);
        primaryStage.setTitle("Window");
        primaryStage.setResizable(false);
        primaryStage.setWidth(521);
        primaryStage.setHeight(420);
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

    }

    public static void main(String[] args) {

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

        userService = new UserService(userFileRepository,friendshipFileRepository);
        friendshipService = new FriendshipService(friendshipFileRepository,userFileRepository);
        messageService = new MessageService(messageFileRepository);
        replyMessageService = new ReplyMessageService(replyMessageFileRepository);
        friendshipRequestService = new FriendshipRequestService(friendshipRequestFileRepository,friendshipFileRepository);



        launch(args);

    }
}
