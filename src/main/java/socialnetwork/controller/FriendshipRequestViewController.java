package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
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

    Stage accountUserStage;
    Stage introductionStage;
    Stage frienshipRequestViewStage;




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
    Button buttonApproved;

    @FXML
    Button buttonRejected;

    @FXML
    Button buttonUnsend;

    @FXML
    TableColumn<FriendshipRequest,String> tableColumnFromTo;

    @FXML
    /**
     * intialize the friendship request view controller
     */
    public void initialize(){
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("FirstNameFrom"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("LastNameFrom"));
        tableColumnMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
        tableColumnSentData.setCellValueFactory(new PropertyValueFactory<>("dateString"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        buttonUnsend.setVisible(false);
        tableViewFriendshipRequest.setItems(model);

    }

    public void setFriendshipRequestService(FriendshipRequestService friendshipRequestService,UserDTO selectedUserDTO) {
        this.friendshipRequestService = friendshipRequestService;
        this.selectedUserDTO = selectedUserDTO;
        friendshipRequestService.addObserver(this);
        initModel();
    }

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    public void initModel(){

        if(tableColumnFromTo.getText().equals("From")){
            List<FriendshipRequest> friendshipRequestList = friendshipRequestService.getAllRequest(selectedUserDTO.getId());

            if(friendshipRequestList.size() == 0){
                model.setAll(friendshipRequestList);
                tableViewFriendshipRequest.setPlaceholder(new Label("You have no friendship request"));
            }else{
                model.setAll(friendshipRequestList);
            }
        }
        else
            if(tableColumnFromTo.getText().equals("To")){
                List<FriendshipRequest> friendshipRequestList = friendshipRequestService.getAllRequestTo(selectedUserDTO.getId());

                if(friendshipRequestList.size() == 0){
                    model.setAll(friendshipRequestList);
                    tableViewFriendshipRequest.setPlaceholder(new Label("You have no friendship request"));
                }else{
                    model.setAll(friendshipRequestList);
                }
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

    /**
     * interchange the interface
     */
    public void viewListOfFriendshipRequestFrom() {
        buttonUnsend.setVisible(false);
        buttonApproved.setVisible(true);
        buttonRejected.setVisible(true);

        tableColumnFromTo.setText("From");
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("FirstNameFrom"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("LastNameFrom"));
        initModel();

    }

    public void viewListOfFriendshipRequestTo() {
        buttonUnsend.setVisible(true);
        buttonApproved.setVisible(false);
        buttonRejected.setVisible(false);

        tableColumnFromTo.setText("To");
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("FirstNameTo"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("LastNameTo"));
        initModel();
    }

    public void deleteFriendshipRequest() {
        FriendshipRequest friendshipRequestSelected = tableViewFriendshipRequest.getSelectionModel().getSelectedItem();

        if(friendshipRequestSelected != null){
            friendshipRequestService.deleteRequest(friendshipRequestSelected.getId());
        }
        else{
            Alert alert =new Alert(Alert.AlertType.ERROR, "Nothing selected!");
            alert.show();
        }
    }

    public void exitButtonOnAction() {
        frienshipRequestViewStage.hide();
        accountUserStage.hide();
        introductionStage.show();
    }

    public void setStages(Stage accountUserStage, Stage introductionStage, Stage frienshipRequestViewStage) {
        this.accountUserStage = accountUserStage;
        this.introductionStage = introductionStage;
        this.frienshipRequestViewStage = frienshipRequestViewStage;
    }
}
