package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import socialnetwork.domain.Credential;
import socialnetwork.domain.User;
import socialnetwork.domain.UserDTO;
import socialnetwork.service.*;
import socialnetwork.utils.Password;

import java.io.IOException;


public class IntroductionController {
    UserService userService;
    FriendshipService friendshipService;
    FriendshipRequestService friendshipRequestService;
    MessageService messageService;
    PhotoService photoService;
    EventService eventService;
    CredentialService credentialService;
    UserDTO selectedUserDTO;

    Stage introductionStage;

    @FXML
    TextField textFieldUsername;

    @FXML
    PasswordField passwordFiled;

    @FXML
    Pane paneLogin;

    @FXML
    Pane paneCreateAccount;

    @FXML
    TextField textFieldFirstName;

    @FXML
    TextField textFieldLastName;

    @FXML
    Button buttonSignUpCreateAccount;

    @FXML
    PasswordField textFieldPasswordCreateAccout;

    @FXML
    TextField textFieldUserNameCreateAccout;


    @FXML
    void initialize(){
        setPaneVisible();
    }

    /**
     * method that set Pane visibility
     */
    private void setPaneVisible() {
        paneCreateAccount.setVisible(false);
        paneLogin.setVisible(true);
    }

    public void setUserService(UserService userService, Stage introductionStage) {
        this.userService = userService;
        this.introductionStage = introductionStage;
//
    }

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }


    public void loginUser() throws IllegalAccessException {
        //functie case simuleaza logarea luan id in field username si returneaza un user
        String userName  = textFieldUsername.getText();

        Credential credential = credentialService.findOne(userName);
        if(credential != null){
            if(Password.checkPassword(passwordFiled.getText(),credential.getPassword())){
                 User user = userService.getUser(credential.getId());
                textFieldUsername.clear();
                passwordFiled.clear();
                UserDTO userDTO = new UserDTO(user.getFirstName(),user.getLastName());
                userDTO.setId(user.getId());
                showAccountUserStage(userDTO);
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR,"invalid password");
                alert.show();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR,"invalid Credential");
            alert.show();
        }




    }
    private void showAccountUserStage(UserDTO selectedUserDTO) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/accountUser.fxml"));
            AnchorPane root = loader.load();

            Stage accountUserStage = new Stage();
            accountUserStage.setResizable(false);
            accountUserStage.hide();
            accountUserStage.setTitle("Your account");
            accountUserStage.initModality(Modality.APPLICATION_MODAL);
            accountUserStage.setOnCloseRequest(event -> {
                introductionStage.show();
                textFieldUsername.clear();
            });
            textFieldUsername.clear();
            Scene scene = new Scene(root);
            accountUserStage.setScene(scene);

            AccountUserController accountUserController = loader.getController();
            accountUserController.setAttributes(friendshipService, userService, selectedUserDTO,friendshipRequestService,messageService,photoService,eventService);
            accountUserController.setStages(accountUserStage,introductionStage);
            accountUserController.setPhotoAccount(selectedUserDTO.getId() );

            accountUserController.setButtonInterested(selectedUserDTO.getId(),accountUserController.firstEvent().getId());

            introductionStage.hide();

            accountUserStage.show();

        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

    public void setFriendshipRequestService(FriendshipRequestService friendshipRequestService) {
        this.friendshipRequestService = friendshipRequestService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }


    public void setPhotoService(PhotoService photoService) {
        this.photoService = photoService;

    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public void showPaneCreateAccount() {
        paneLogin.setVisible(false);
        paneCreateAccount.setVisible(true);
    }


    public void signUp() {

        if(textFieldFirstName.getText().length() == 0 ||
                textFieldUserNameCreateAccout.getText().length() == 0 ||
                textFieldLastName.getText().length() == 0 ||
                textFieldPasswordCreateAccout.getText().length() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR,"you must fill all textField");
            alert.show();
            return;
        }



        String firstName = textFieldFirstName.getText();
        String lastName = textFieldLastName.getText();

        if(firstName.length() <= 3){
            Alert alert = new Alert(Alert.AlertType.ERROR,"first name must be longer than 3 characters!");
            alert.show();
            return;
        }
        if(lastName.length() <= 3){
            Alert alert = new Alert(Alert.AlertType.ERROR,"last name must be longer than 3 characters!");
            alert.show();
            return;
        }

        if(textFieldUserNameCreateAccout.getText().length() <= 3){
            Alert alert = new Alert(Alert.AlertType.ERROR,"Username must be longer than 3 characters!");
            alert.show();
            return;
        }


        User user = new User(firstName,lastName);
        User userAdded = userService.addUser(user);


        Credential credential = new Credential(textFieldUserNameCreateAccout.getText(), Password.hashPassword(textFieldPasswordCreateAccout.getText()));
        if(userAdded != null){
            credential.setId(userAdded.getId());
            Credential addedCredential = credentialService.addCredential(credential);

            if(addedCredential != null){
                paneCreateAccount.setVisible(false);
                paneLogin.setVisible(true);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR,"error finding up the Credential");
                alert.show();
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR,"error finding up the User");
            alert.show();
        }
        textFieldPasswordCreateAccout.clear();
        textFieldLastName.clear();
        textFieldFirstName.clear();
        textFieldUserNameCreateAccout.clear();



    }

    public void setCredentialService(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    public void switchPaneCreateAccountToLogin() {
        textFieldPasswordCreateAccout.clear();
        textFieldLastName.clear();
        textFieldFirstName.clear();
        textFieldUserNameCreateAccout.clear();
        paneLogin.setVisible(true);
        paneCreateAccount.setVisible(false);
    }
}