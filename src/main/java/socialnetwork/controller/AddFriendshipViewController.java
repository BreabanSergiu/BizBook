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
import socialnetwork.domain.message.FriendshipRequest;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.FriendshipRequestService;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AddFriendshipViewController {

    ObservableList<UserDTO> modelUserDTO= FXCollections.observableArrayList();

    FriendshipService friendshipService;
    UserService userService;
    FriendshipRequestService friendshipRequestService;
    UserDTO selectedUserDTO;

    @FXML
    TableView<UserDTO> tableViewNonFriends;

    @FXML
    TableColumn<UserDTO,String> tableCollumnFirstName;

    @FXML
    TableColumn<UserDTO,String> tableCollumnLastName;
    @FXML
    TextField textFieldMessage;

    Stage accountUserStage;
    Stage introductionStage;
    Stage addFriendshipRequestStage;

    @FXML
    public void initialize(){
        tableCollumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableCollumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableViewNonFriends.setItems(modelUserDTO);
    }


    public void setFriendshipService(FriendshipService friendshipServicel) {
        this.friendshipService = friendshipServicel;
    }

    public void setUserService(UserService userService,UserDTO userDTO) {

        this.userService = userService;
        this.selectedUserDTO = userDTO;
        initModel();
    }

    public void setFriendshipRequestService(FriendshipRequestService friendshipRequestService) {
        this.friendshipRequestService = friendshipRequestService;
    }

    private void initModel(){
        Iterable<User> users = userService.getAll();

        List<UserDTO> nonFriends = new ArrayList<>();
        users.forEach(user -> {
            if(friendshipService.getOne(selectedUserDTO.getId(), user.getId())==null
                    && friendshipService.getOne(user.getId(),selectedUserDTO.getId())==null
                    && !user.getId().equals(selectedUserDTO.getId())){
                UserDTO userDTO = new UserDTO(user.getFirstName(),user.getLastName());
                userDTO.setId(user.getId());
                nonFriends.add(userDTO);

            }
        });
        if(nonFriends.size() == 0){
            modelUserDTO.setAll(nonFriends);
            tableViewNonFriends.setPlaceholder(new Label("you are a friend off all users!"));
        }
        else {
            modelUserDTO.setAll(nonFriends);
        }
    }


    public  void sendFriendshipRequest(){
        UserDTO userToDTO = tableViewNonFriends.getSelectionModel().getSelectedItem();
        if(userToDTO != null){
            User userFrom = userService.getUser(selectedUserDTO.getId());
            User userTo = userService.getUser(userToDTO.getId());

            String message = textFieldMessage.getText();

            if(message.matches("[ ]*")){
                message = userFrom.getFirstName() + " " + userFrom.getLastName() + " has sent your friendship request";
            }

            FriendshipRequest friendshipRequest = new FriendshipRequest(userFrom, Arrays.asList(userTo),message,
                    LocalDateTime.now(),"pending");

            try {
                friendshipRequestService.addRequest(friendshipRequest);
                tableViewNonFriends.getSelectionModel().clearSelection();
                textFieldMessage.clear();
                Alert alert =  new Alert(Alert.AlertType.CONFIRMATION,"the friendship request has been sent!");
                alert.show();
            }
            catch (ValidationException validationException){
                Alert alert = new Alert(Alert.AlertType.ERROR,"you have already sent a friendship request");
                alert.show();
            }

        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR,"you must to select an user");
            alert.show();
        }

    }

    public void exitButtonOnAction() {
        addFriendshipRequestStage.hide();
        accountUserStage.hide();
        introductionStage.show();
    }

    public void setStages(Stage accountUserStage, Stage introductionStage, Stage addFriendshipRequestStage) {
        this.accountUserStage = accountUserStage;
        this.introductionStage = introductionStage;
        this.addFriendshipRequestStage = addFriendshipRequestStage;
    }
}
