

package socialnetwork.controller;

        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Scene;
        import javafx.scene.control.*;
        import javafx.scene.control.cell.PropertyValueFactory;
        import javafx.scene.image.Image;
        import javafx.scene.layout.AnchorPane;
        import javafx.stage.Modality;
        import javafx.stage.Stage;
        import socialnetwork.domain.Friendship;
        import socialnetwork.domain.Tuple;
        import socialnetwork.domain.UserDTO;
        import socialnetwork.service.FriendshipRequestService;
        import socialnetwork.service.FriendshipService;
        import socialnetwork.service.UserService;
        import socialnetwork.utils.events.FriendshipChangeEvent;
        import socialnetwork.utils.observer.Observer;

        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;

public class AccountUserController implements Observer<FriendshipChangeEvent> {
    ObservableList<UserDTO> model = FXCollections.observableArrayList();
    UserService userService;
    FriendshipService friendshipService;
    FriendshipRequestService friendshipRequestService;
    UserDTO selectedUserDTO;

    @FXML
    Button buttonAddFriendship;
    @FXML
    Button buttonDeleteFriendship;
    @FXML
    Label labelUserName;
    @FXML
    TableColumn<UserDTO, String> tableColumnFirstName;
    @FXML
    TableColumn<UserDTO, String> tableColumnLastName;
    @FXML
    TableView<UserDTO> tableViewAccountUser;

    @FXML
    void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("lastName"));
        tableViewAccountUser.setItems(model);
    }

    void setAttributes(FriendshipService friendshipService, UserService userService, UserDTO selectedUserDTO,
                       FriendshipRequestService friendshipRequestService) {
        this.friendshipRequestService = friendshipRequestService;
        this.friendshipService = friendshipService;
        this.userService = userService;
        this.selectedUserDTO = selectedUserDTO;
        friendshipService.addObserver(this);
        if (selectedUserDTO != null) {
            labelUserName.setText("Hello, " + selectedUserDTO.getFirstName()+" "+selectedUserDTO.getLastName());
            Iterable<Friendship> friendships = this.friendshipService.getAllFriendshipsUser(selectedUserDTO.getId());
            initModel();
        }
    }

    public void deleteFriendship(){
        UserDTO userDTO = tableViewAccountUser.getSelectionModel().getSelectedItem();
        if(userDTO != null){
            Long selectedUserID = selectedUserDTO.getId();
            Long userId = userDTO.getId();

            Friendship friendship1 = friendshipService.getOne(selectedUserID,userId);
            Friendship friendship2 = friendshipService.getOne(userId,selectedUserID);
            if(friendship1 != null){
                friendshipService.deleteFriendship(new Tuple<>(selectedUserID,userId));
            }
            if(friendship2 != null){
                friendshipService.deleteFriendship(new Tuple<>(userId,selectedUserID));
            }
            tableViewAccountUser.getSelectionModel().clearSelection();

        }
       else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Nothing selected");
            alert.show();
        }
    }

    public void addFriendshipRequest(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/addFriendshipView.fxml"));
            AnchorPane root = loader.load();

            Stage addFriendshipRequestStage = new Stage();
            addFriendshipRequestStage.setTitle("Send friendship request");
            addFriendshipRequestStage.setResizable(false);
            addFriendshipRequestStage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root);
            addFriendshipRequestStage.setScene(scene);

            AddFriendshipViewController addFriendshipViewController = loader.getController();

            addFriendshipViewController.setFriendshipService(friendshipService);
            addFriendshipViewController.setUserService(userService,selectedUserDTO);
            addFriendshipViewController.setFriendshipRequestService(friendshipRequestService);

            addFriendshipRequestStage.show();

        }catch (IOException e){
            e.printStackTrace();
        }


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
            model.setAll(listFriends);
            tableViewAccountUser.setPlaceholder(new Label("You have no added friends"));
        } else {
            model.setAll(listFriends);
        }
    }

    public void viewFriendshipRequest(){
            try{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/FriendshipRequestView.fxml"));
                AnchorPane root = loader.load();

                Stage frienshipRequestViewStage = new Stage();
                frienshipRequestViewStage.setScene(new Scene(root));
                frienshipRequestViewStage.setTitle("Friendship Request");
                frienshipRequestViewStage.getIcons().add(new Image(getClass().getResourceAsStream("/css/1.jpg")));
                frienshipRequestViewStage.show();

              FriendshipRequestViewController friendshipRequestViewController = loader.getController();
               friendshipRequestViewController.setFriendshipRequestService(friendshipRequestService,selectedUserDTO);
                friendshipRequestViewController.setFriendshipService(friendshipService);

            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void update(FriendshipChangeEvent friendshipChangeEvent) {
            initModel();
    }
}