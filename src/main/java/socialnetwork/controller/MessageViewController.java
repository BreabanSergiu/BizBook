package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.User;
import socialnetwork.domain.UserDTO;
import socialnetwork.domain.message.Message;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.MessageService;
import socialnetwork.service.UserService;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageViewController {

    ObservableList<UserDTO> modelUnselected = FXCollections.observableArrayList();
    ObservableList<UserDTO> modelSelected = FXCollections.observableArrayList();
    ObservableList<Message> modelInbox = FXCollections.observableArrayList();

    UserService userService;
    MessageService messageService;
    FriendshipService friendshipService;
    UserDTO selectedUserDTO;
    List<UserDTO> listUsersSelected = new ArrayList<>();
    List<UserDTO> listUsersUnselected  = new ArrayList<>();

    Stage accountUserStage;
    Stage introductionStage;
    Stage messageViewStage;


    @FXML
    TableView<UserDTO> tableViewUnselected;

    @FXML
    TableView<UserDTO> tableViewSelected;

    @FXML
    TextField textFieldMessage;

    @FXML
    TableColumn<UserDTO,String> tableColumnFirstNameUnselected;

    @FXML
    TableColumn<UserDTO,String> tableColumnFirstNameSelected;

    @FXML
    TableColumn<UserDTO,String> tableColumnLastNameUnselected;

    @FXML
    TableColumn<UserDTO,String> tableColumnLastNameSelected;

    @FXML
    TableView<Message> tableViewInbox;

    @FXML
    TableColumn<Message,String> tableColumnFirstName;

    @FXML
    TableColumn<Message,String > tableColumnLastName;

    @FXML
    TableColumn<Message,String> tableColumnDate;

    @FXML
    TableColumn<Message,String> tableColumnMessage;

    @FXML
    TextField textFieldMessageInbox;

    @FXML
    public void initialize(){

        tableColumnFirstNameUnselected.setCellValueFactory(new PropertyValueFactory<UserDTO,String>("FirstName"));
        tableColumnFirstNameSelected.setCellValueFactory(new PropertyValueFactory<UserDTO,String>("FirstName"));
        tableColumnLastNameSelected.setCellValueFactory(new PropertyValueFactory<UserDTO,String>("LastName"));
        tableColumnLastNameUnselected.setCellValueFactory(new PropertyValueFactory<UserDTO,String>("LastName"));

        //for inbox
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<Message,String>("FromFirstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<Message,String>("FromLastName"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<Message,String>("DateString"));
        tableColumnMessage.setCellValueFactory(new PropertyValueFactory<Message,String>("Message"));




        tableViewUnselected.setItems(modelUnselected);
        tableViewSelected.setItems(modelSelected);
        tableViewInbox.setItems(modelInbox);

    }


    public void setUserService(UserService userService) {
        this.userService = userService;
        initModel();


    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
        initModelInbox();
    }

    private void initModel(){
        Iterable<Friendship> friendships = this.friendshipService.getAllFriendshipsUser(selectedUserDTO.getId());
        List<UserDTO> listFriends = new ArrayList();
        friendships.forEach(friendship -> {
            if(friendship.getId().getLeft().equals(selectedUserDTO.getId()))
            {
                listFriends.add(userService.getUserDTO(friendship.getId().getRight()));
            }
            else
            {
                listFriends.add(userService.getUserDTO(friendship.getId().getLeft()));

            }
        });
        if (!friendships.iterator().hasNext()) {
            modelUnselected.setAll(listFriends);
            tableViewUnselected.setPlaceholder(new Label("You have no added friends"));
        } else {
            modelUnselected.setAll(listFriends);
            refreshTables(listFriends);


        }
    }
    
    private void initModelInbox(){
        Iterable<Message> messages = this.messageService.getReceiveMessageUser(selectedUserDTO.getId());
        List<Message> listMessages = new ArrayList<>();

        messages.forEach(listMessages::add);

        if(! messages.iterator().hasNext()){
            modelInbox.setAll(listMessages);
            tableViewInbox.setPlaceholder(new Label("you have no receive message"));
        }else {
            modelInbox.setAll(listMessages);
        }
    }

    public void refreshTables(List<UserDTO> listFriends){

        listUsersUnselected.clear();
        listUsersSelected.clear();
        listUsersUnselected.addAll(listFriends);
        modelSelected.setAll(listUsersSelected);

    }
    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    public void setSelectedUserDTO(UserDTO selectedUserDTO) {
        this.selectedUserDTO = selectedUserDTO;
    }

    public void addUserForMessage() {
        UserDTO userDTO = tableViewUnselected.getSelectionModel().getSelectedItem();
        if(userDTO != null){
            listUsersSelected.add(userDTO);
            modelSelected.setAll(listUsersSelected);
            tableViewSelected.setItems(modelSelected);
            listUsersUnselected.remove(userDTO);
            modelUnselected.setAll(listUsersUnselected);
            tableViewUnselected.setItems(modelUnselected);

        }
        else {
            Alert alert= new Alert(Alert.AlertType.ERROR,"Nothing selected");
            alert.show();
        }

    }

    public void removeUserFromMessageTable() {
        UserDTO userDTO = tableViewSelected.getSelectionModel().getSelectedItem();
        if(userDTO != null){
            listUsersUnselected.add(userDTO);
            modelUnselected.setAll(listUsersUnselected);
            tableViewUnselected.setItems(modelUnselected);
            listUsersSelected.remove((userDTO));
            modelSelected.setAll(listUsersSelected);
            tableViewSelected.setItems(modelSelected);
        }
        else{
            Alert alert= new Alert(Alert.AlertType.ERROR,"Nothing selected");
            alert.show();
        }
    }

    public void sendTheMessage() {
        String textMessage = textFieldMessage.getText();


        if(!textMessage.matches("[ ]*")){
            if(listUsersSelected.size() != 0)
            {
                List<User> listUserTo = new ArrayList<>();
                listUsersSelected.forEach(userDTO -> {
                    listUserTo.add(userService.getUser(userDTO.getId()));
                            });

                Message message = new Message(userService.getUser(selectedUserDTO.getId()),listUserTo,textMessage, LocalDateTime.now());
                messageService.addMessage(message);
                textFieldMessage.clear();
                initModel();
            }else {
                Alert alert= new Alert(Alert.AlertType.ERROR,"Please select users for massage");
                alert.show();
            }

        }
        else{
            Alert alert= new Alert(Alert.AlertType.ERROR,"Please insert a text message");
            alert.show();
        }
    }

    public void replyMessage() {
        String replyMessage = textFieldMessageInbox.getText();
        Message selectedMessage = tableViewInbox.getSelectionModel().getSelectedItem();
        User user = userService.getUser(selectedUserDTO.getId());

        if(selectedMessage != null){
            if(!replyMessage.matches("[ ]* ")){
                Message messageResponse = new Message(user, Arrays.asList(selectedMessage.getFrom()),replyMessage,LocalDateTime.now());
                messageService.addMessage(messageResponse);
                textFieldMessageInbox.clear();
                tableViewInbox.getSelectionModel().clearSelection();
                Alert alert= new Alert(Alert.AlertType.CONFIRMATION,"Reply message sent");
                alert.show();
            }else {
                Alert alert= new Alert(Alert.AlertType.ERROR,"Please insert a text message");
                alert.show();
            }
        }
        else {
            Alert alert= new Alert(Alert.AlertType.ERROR,"Nothing selected");
            alert.show();
        }

    }




    public void setStages(Stage accountUserStage, Stage introductionStage,Stage messageViewStage) {
        this.accountUserStage=accountUserStage;
        this.introductionStage = introductionStage;
        this.messageViewStage = messageViewStage;
    }




    public void exitButtonOnAction() {

        messageViewStage.hide();
        accountUserStage.hide();
        introductionStage.show();

    }



}
