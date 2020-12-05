package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import socialnetwork.domain.UserDTO;
import socialnetwork.service.FriendshipRequestService;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.MessageService;
import socialnetwork.service.UserService;

import java.io.IOException;


public class IntroductionController {
    UserService userService;
    FriendshipService friendshipService;
    FriendshipRequestService friendshipRequestService;
    ObservableList<UserDTO> modelUserDTO = FXCollections.observableArrayList();
    MessageService messageService;

    @FXML
    TableColumn<UserDTO, String> tableColumnFirstName;
    @FXML
    TableColumn<UserDTO, String> tableColumnLastName;
    @FXML
    TableView<UserDTO> tableViewIntroduction;
    Stage introductionStage;

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableViewIntroduction.setItems(modelUserDTO);
    }

    public void setUserService(UserService userService, Stage introductionStage) {
        this.userService = userService;
        this.introductionStage = introductionStage;
        modelUserDTO.setAll(this.userService.getAllUserDTO());
        if (modelUserDTO.size() == 0) {
            tableViewIntroduction.setPlaceholder(new Label("There are no users in the social network"));
        }
    }

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    public void selectFriendsUser() {
        // TODO: 25/11/2020 aici trebe sa iau argumentele din campuri 
        UserDTO selectedUserDTO = tableViewIntroduction.getSelectionModel().getSelectedItem();
        if (selectedUserDTO != null) {
            showAccountUserStage(selectedUserDTO);
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
                tableViewIntroduction.getSelectionModel().clearSelection();
            });
            Scene scene = new Scene(root);
            accountUserStage.setScene(scene);
            AccountUserController accountUserController = loader.getController();
            accountUserController.setAttributes(friendshipService, userService, selectedUserDTO,friendshipRequestService,messageService);
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
}