package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.UserDTO;
import socialnetwork.domain.message.FriendshipRequest;
import socialnetwork.service.FriendshipRequestService;
import socialnetwork.service.FriendshipService;
import socialnetwork.utils.events.FriendshipChangeEvent;
import socialnetwork.utils.events.FriendshipRequestChangeEvent;
import socialnetwork.utils.observer.Observer;

import java.util.List;

public class FriendshipRequestViewController implements Observer<FriendshipRequestChangeEvent> {
    FriendshipRequestService friendshipRequestService;
    FriendshipService friendshipService;
    ObservableList<FriendshipRequest> model = FXCollections.observableArrayList();
    UserDTO selectedUserDTO;


    public void setFriendshipRequestService(FriendshipRequestService friendshipRequestService,UserDTO selectedUserDTO) {
        this.friendshipRequestService = friendshipRequestService;
        this.selectedUserDTO = selectedUserDTO;
        friendshipRequestService.addObserver(this);
        initModel();
    }

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @FXML
    TableView<FriendshipRequest> tableViewFriendshipRequest;

    @FXML
    TableColumn<FriendshipRequest,String> tableColumnFirstName;

    @FXML
    TableColumn<FriendshipRequest,String> tableColumnLastName;

    @FXML
    TableColumn<FriendshipRequest,String> tableColumnMessage;

    @FXML
    TableColumn<FriendshipRequest,String> tableColumnSentData;

    @FXML
    TableColumn<FriendshipRequest,String> tableColumnStatus;

    @FXML
    public void initialize(){
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        tableColumnMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
        tableColumnSentData.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableViewFriendshipRequest.setItems(model);

    }

    public void initModel(){
        List<FriendshipRequest> friendshipRequestList = friendshipRequestService.getAllRequest(selectedUserDTO.getId());

        if(friendshipRequestList.size() == 0){
            model.setAll(friendshipRequestList);
            tableViewFriendshipRequest.setPlaceholder(new Label("You have no friendship request"));
        }else{
            model.setAll(friendshipRequestList);
        }
    }



    public void approvedFriendshipRequest() {

        FriendshipRequest friendshipRequestSelected = tableViewFriendshipRequest.getSelectionModel().getSelectedItem();
        //give a friendship request when we clik on tabel on this friedship

        if(friendshipRequestSelected != null){
            if(friendshipRequestSelected.getStatus().equals("pending")){
                friendshipRequestService.updateFriendshipRequest(friendshipRequestSelected,"approved");
                Long idFrom = friendshipRequestSelected.getFrom().getId();
                Long idTo = friendshipRequestSelected.getTo().get(0).getId();
                Friendship friendship = new Friendship(new Tuple<>(idFrom,idTo));
                friendshipService.addFriendship(friendship);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR,"you must to select a pending friendship request");
                alert.show();
            }
        }
    }





    public void rejectedFriendshipRequest() {
        FriendshipRequest friendshipRequestSelected = tableViewFriendshipRequest.getSelectionModel().getSelectedItem();

        if(friendshipRequestSelected != null){
            if(friendshipRequestSelected.getStatus().equals("pending"))
            {
                friendshipRequestService.updateFriendshipRequest(friendshipRequestSelected,"rejected");

            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR,"You must to select a pending friendship request");
                alert.show();
            }
        }
    }

    @Override
    public void update(FriendshipRequestChangeEvent friendshipRequestChangeEvent) {
        initModel();
    }
}
