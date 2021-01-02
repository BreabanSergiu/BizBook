

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
        import javafx.scene.layout.Pane;
        import javafx.scene.paint.ImagePattern;
        import javafx.scene.shape.Circle;
        import javafx.scene.shape.Rectangle;
        import javafx.stage.FileChooser;
        import javafx.stage.Modality;
        import javafx.stage.Stage;
        import javafx.util.Duration;
        import socialnetwork.domain.*;
        import socialnetwork.domain.message.FriendshipRequest;
        import socialnetwork.domain.message.Message;
        import socialnetwork.service.*;
        import socialnetwork.utils.events.FriendshipChangeEvent;
        import socialnetwork.utils.observer.Observer;

        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.concurrent.atomic.AtomicBoolean;

public class AccountUserController implements Observer<FriendshipChangeEvent>{
    ObservableList<UserDTO> modelFriends = FXCollections.observableArrayList();
    ObservableList<Message> modelMessages = FXCollections.observableArrayList();
    ObservableList<FriendshipRequest> modelRequests = FXCollections.observableArrayList();
    UserService userService;
    FriendshipService friendshipService;
    FriendshipRequestService friendshipRequestService;
    MessageService messageService;
    PhotoService photoService;
    EventService eventService;
    UserDTO selectedUserDTO;
    Stage accountUserStage;
    Stage introductionStage;
    private final Page pageEvents = new Page(1,1);


    @FXML
    Button buttonAddFriendship;
    @FXML
    Button buttonDeleteFriendship;
    @FXML
    Label labelUserName;
    @FXML
    Button buttonEvents;

    @FXML
    TableView<UserDTO> tableViewFriends;

    @FXML
    TableView<Message> tableViewMessages;

    @FXML
    TableView<FriendshipRequest> tableViewRequests;

    @FXML
    TableColumn<UserDTO, String> tableColumnFirstNameFriends;
    @FXML
    TableColumn<UserDTO, String> tableColumnLastNameFriends;

    @FXML
    TableColumn<Message,String> tableColumnFromMessages;
    @FXML
    TableColumn<Message,String> tableColumnMessageMessages;
    @FXML
    TableColumn<Message,String> tableColumnDataMessages;

    @FXML
    TableColumn<FriendshipRequest,String> tableColumnFromRequests;
    @FXML
    TableColumn<FriendshipRequest,String> tableColumnMessageRequests;
    @FXML
    TableColumn<FriendshipRequest,String> tableColumnDataRequests;

    @FXML
    Circle myCircle;

    @FXML
    Pane paneAccount;
    @FXML
    Pane paneEvents;
    @FXML
    Pane paneDescription;
    @FXML
    Pane paneNameShowDetails;

    @FXML
    Label labelNotification;
    @FXML
    Label labelBack;

    @FXML
    Label labelNextPage;

    @FXML
    Label labelPreviousPage;

    @FXML
    Rectangle rectanglePhotoEvent;

    @FXML
    Label labelNameEvent;

    @FXML
    Label labelShowDescription;

    @FXML
    Label labelDescription;

    @FXML
    Label labelHideDescription;


    @FXML
    Label labelNrParticipants;

    @FXML
    Label labelDateEvent;

    @FXML
    TabPane tabPaneFriendsMessagesRequests;

    @FXML
    Tab tabFriends;

    @FXML
    Tab tabMessages;

    @FXML
    Tab tabFriendshipRequests;

    @FXML
    AnchorPane anchorPaneMessages;

    @FXML
    Button buttonInterestedNotInterested;




    @FXML
    void initialize() {

        tableColumnFirstNameFriends.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("firstName"));
        tableColumnLastNameFriends.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("lastName"));

        tableColumnFromMessages.setCellValueFactory(new PropertyValueFactory<Message,String>("NameFrom"));
        tableColumnMessageMessages.setCellValueFactory(new PropertyValueFactory<Message,String>("Message"));
        tableColumnDataMessages.setCellValueFactory(new PropertyValueFactory<Message,String>("LocalDateString"));

        tableColumnFromRequests.setCellValueFactory(new PropertyValueFactory<FriendshipRequest,String>("NameFrom"));
        tableColumnMessageRequests.setCellValueFactory(new PropertyValueFactory<FriendshipRequest,String>("Message"));
        tableColumnDataRequests.setCellValueFactory(new PropertyValueFactory<FriendshipRequest,String>("LocalDateString"));

        tableViewFriends.setItems(modelFriends);
        tableViewMessages.setItems(modelMessages);
        tableViewRequests.setItems(modelRequests);

        setTooltipPhotos();
        setPaneVisible();


    }

    private void setPaneVisible() {
        paneAccount.setVisible(true);
        paneEvents.setVisible(false);
        paneNameShowDetails.setVisible(true);
        paneDescription.setVisible(false);

    }

    private void setTooltipPhotos() {
        setTooltipPhotoAccount();
        setTooltipPhotoNotification();
        setTooltipPhotoBack();
        setTooltipPhotoNextPage();
        setTooltipPhotoPreviousPage();

    }

    private void setTooltipLabelDescription(String string) {
        Tooltip tooltip =new Tooltip(string);
        tooltip.setShowDelay(new Duration(10));
        tooltip.setHideDelay(new Duration(10));
        labelDescription.setTooltip(tooltip);
    }

    private void setTooltipPhotoPreviousPage() {
        Tooltip tooltip =new Tooltip("PreviousPage");
        tooltip.setShowDelay(new Duration(10));
        tooltip.setHideDelay(new Duration(10));
        labelPreviousPage.setTooltip(tooltip);
    }

    private void setTooltipPhotoNextPage() {
        Tooltip tooltip =new Tooltip("NextPage");
        tooltip.setShowDelay(new Duration(10));
        tooltip.setHideDelay(new Duration(10));
        labelNextPage.setTooltip(tooltip);
    }

    private void setTooltipPhotoBack() {
        Tooltip tooltip =new Tooltip("Back");
        tooltip.setShowDelay(new Duration(10));
        tooltip.setHideDelay(new Duration(10));
        labelBack.setTooltip(tooltip);
    }

    private void setTooltipPhotoNotification() {
        Tooltip tooltip =new Tooltip("Notifications");
        tooltip.setShowDelay(new Duration(10));
        tooltip.setHideDelay(new Duration(10));
        labelNotification.setTooltip(tooltip);
    }

    private void setTooltipPhotoAccount() {
        Tooltip tooltip =new Tooltip("press to change\n " +
                "        photo");
        tooltip.setShowDelay(new Duration(10));
        tooltip.setHideDelay(new Duration(10));
        Tooltip.install(myCircle,tooltip);
    }

    void setAttributes(FriendshipService friendshipService, UserService userService, UserDTO selectedUserDTO,
                       FriendshipRequestService friendshipRequestService, MessageService messageService, PhotoService photoService, EventService eventService) {

        this.friendshipRequestService = friendshipRequestService;
        this.friendshipService = friendshipService;
        this.userService = userService;
        this.selectedUserDTO = selectedUserDTO;
        this.messageService = messageService;
        this.photoService = photoService;
        this.eventService = eventService;
        System.out.println(selectedUserDTO);

        friendshipService.addObserver(this);

        if (selectedUserDTO != null) {
            labelUserName.setText("Hello, " + selectedUserDTO.getFirstName()+" "+selectedUserDTO.getLastName());
            initModelFriends();
            initModelMessages();
            initModelRequests();
        }
    }

    private void initModelRequests() {
        Iterable<FriendshipRequest> friendshipRequests = this.friendshipRequestService.getAllPendingRequest(selectedUserDTO.getId());
        List<FriendshipRequest> friendshipRequestList = new ArrayList<>();
        friendshipRequests.forEach(friendshipRequestList::add);

        if(!friendshipRequests.iterator().hasNext()){
            modelRequests.setAll(friendshipRequestList);
            tableViewRequests.setPlaceholder(new Label("you have no pending friendship request!"));
        }
        else{
            modelRequests.setAll(friendshipRequestList);
        }
    }

    private void initModelMessages() {
        Iterable<Message> messages = this.messageService.getAllMessagesToUser(selectedUserDTO.getId());
        List<Message> messageList = new ArrayList<>();
        messages.forEach(messageList::add);
        if(!messages.iterator().hasNext()){
            modelMessages.setAll(messageList);
            tableViewMessages.setPlaceholder(new Label("You have no messages received!"));
        }
        else{
            modelMessages.setAll(messageList);
        }
    }

    private void initModelFriends(){
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
            modelFriends.setAll(listFriends);
            tableViewFriends.setPlaceholder(new Label("You have no added friends!"));
        } else {
            modelFriends.setAll(listFriends);
        }
    }


    public void deleteFriendship(){
        UserDTO userDTO = tableViewFriends.getSelectionModel().getSelectedItem();
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
            tableViewFriends.getSelectionModel().clearSelection();


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

            addFriendshipRequestStage.setOnCloseRequest(event -> {
                accountUserStage.show();
            });
            accountUserStage.hide();


            addFriendshipViewController.setFriendshipService(friendshipService);
            addFriendshipViewController.setUserService(userService,selectedUserDTO);
            addFriendshipViewController.setFriendshipRequestService(friendshipRequestService);
            addFriendshipViewController.setStages(accountUserStage, introductionStage, addFriendshipRequestStage);
            addFriendshipRequestStage.show();

        }catch (IOException e){
            e.printStackTrace();
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

                frienshipRequestViewStage.setOnCloseRequest(event -> {
                    accountUserStage.show();
                });
                accountUserStage.hide();

              FriendshipRequestViewController friendshipRequestViewController = loader.getController();
               friendshipRequestViewController.setFriendshipRequestService(friendshipRequestService,selectedUserDTO);
                friendshipRequestViewController.setFriendshipService(friendshipService);
                friendshipRequestViewController.setStages(accountUserStage, introductionStage, frienshipRequestViewStage);

            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void update(FriendshipChangeEvent friendshipChangeEvent) {
            initModelFriends();
            initModelRequests();
    }

    public void viewMessages() {

        try{
            FXMLLoader loader  = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/messageView.fxml"));

            AnchorPane root = loader.load();

            Stage messageViewStage = new Stage();
            messageViewStage.setScene(new Scene(root));
            messageViewStage.setTitle("Message");
            messageViewStage.show();

            MessageViewController messageViewController = loader.getController();

            messageViewStage.setOnCloseRequest(event -> {
                accountUserStage.show();
            });


            accountUserStage.hide();

            messageViewController.setFriendshipService(friendshipService);
            messageViewController.setSelectedUserDTO(selectedUserDTO);
            messageViewController.setUserService(userService);
            messageViewController.setMessageService(messageService);
            messageViewController.setStages(accountUserStage, introductionStage, messageViewStage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStages(Stage accountUserStage, Stage introductionStage) {
        this.introductionStage = introductionStage;
        this.accountUserStage = accountUserStage;
    }

    public void showReports() {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/report.fxml"));

            AnchorPane root = loader.load();

            Stage reportViewStage = new Stage();
            Scene scene = new Scene(root);
            reportViewStage.setScene(scene);
            reportViewStage.setTitle("Reports");;
            reportViewStage.show();

            ReportViewController reportViewController = loader.getController();
            reportViewController.setMessageService(messageService);
            reportViewController.setFriendshipService(friendshipService);
            reportViewController.setSelectedUserDTO(selectedUserDTO);
            reportViewController.setUserService(userService);
            reportViewController.populatePieChart();




        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    mothod that change the user's photo profile
     */
    public void changePhoto() {
        FileInputStream fileInputStream = null;
        String photoPath = getPhotoURL();
        if(photoPath != null){
            try {
                fileInputStream = new FileInputStream(photoPath);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            myCircle.setFill(new ImagePattern(new Image( fileInputStream )));
            Photo photo = new Photo(photoPath);
            photo.setId(selectedUserDTO.getId());
             photoService.changePhoto(photo);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"you don t selected any photos ");
            alert.show();
        }




    }

    /*
    method that opens up a FileChooser Dialog in order to select a photo
     */
    public String getPhotoURL(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("/Users/breabansergiugeorgica/desktop"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG Files","*.PNG"),
                new FileChooser.ExtensionFilter("JPG FILE","*.JPG"),new FileChooser.ExtensionFilter("JPEG FILE","*.JPEG"));
        fileChooser.setTitle("Choose Photo");

        File file = fileChooser.showOpenDialog(accountUserStage);
        if(file != null){
            return file.toString();
        }
        return null;
    }


    public void setPhotoAccount(Long id) {
        Photo photo = photoService.getPhoto(id);
        if(photo != null){
            String photoPath = photo.getURL();
            FileInputStream fileInputStream =null;
            if(photoPath != null){
                try {
                    fileInputStream = new FileInputStream(photoPath);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                myCircle.setFill(new ImagePattern(new Image( fileInputStream )));
            }
        }

        else {
          myCircle.setFill(new ImagePattern(new Image(getClass().getClassLoader().getResourceAsStream("photos/profile.png"))));
        }

    }

    public void openPageEvents() {
        initFirstEvent();
        paneEvents.setVisible(true);
        paneAccount.setVisible(false);

    }

    /**
     * initialize first event
     */
    private void initFirstEvent() {
        Iterable<Event> events = eventService.getAllEvents();
        List<Event> eventList = new ArrayList<>();
        events.forEach(eventList::add);
        Event event = eventList.get(0);

        setPhotoEvent(event.getId());

        labelNameEvent.setText("Name Event: "+event.getName());
        String description = "Description: "+event.getDescription();
        labelDescription.setText(description);
        setTooltipLabelDescription(description);
        labelDateEvent.setText("Date: "+event.getDate().toString());
        labelNrParticipants.setText("Number Of Participants: "+ event.getParticipants().size());


    }

    /**
     * set event photo
     * @param id id of event
     */
    private void setPhotoEvent(Long id) {
        Photo photo = photoService.getPhoto(1l);
        if(photo != null){
            String photoPath = photo.getURL();
            FileInputStream fileInputStream =null;
            if(photoPath != null){
                try {
                    fileInputStream = new FileInputStream(photoPath);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                rectanglePhotoEvent.setFill(new ImagePattern(new Image( fileInputStream )));
            }
        }

        else {
            rectanglePhotoEvent.setFill(new ImagePattern(new Image(getClass().getClassLoader().getResourceAsStream("photos/profile.png"))));
        }

    }


    public void backToAccountFromEvents() {
        paneEvents.setVisible(false);
        paneAccount.setVisible(true);
    }

    public void openDescriptionPane() {
        paneDescription.setVisible(true);
        paneNameShowDetails.setVisible(false);
    }

    public void closeDescriptionPane() {
        paneDescription.setVisible(false);
        paneNameShowDetails.setVisible(true);
    }

    public void joinOrLeaveEvent() {
        Event event  = firstEvent();
        AtomicBoolean ok = new AtomicBoolean(false);
        List<User> listPart = event.getParticipants();
        listPart.forEach(user1 -> {
            if(user1.getId().equals(selectedUserDTO.getId())){
                ok.set(true);
            }
        });
        if(ok.get()){
            buttonInterestedNotInterested.setText("interested");
            listPart.remove(0);
        }
        else {
            buttonInterestedNotInterested.setText("I am not interested");
            listPart.add(userService.getUser(selectedUserDTO.getId()));
        }
        // TODO: 30/12/2020 de aici trebuie sa continui maine cu paginarea 
    }

    /**
     * set button -> not interested if the user is in list of participants
     *      *              otherwise set interested
     * @param idSelectedUser id of the user selected
     * @param idEvent id of the event
     */
    public void setButtonInterested(Long idSelectedUser ,Long idEvent) {
        Iterable<Event> events = eventService.getAllEvents();
        AtomicBoolean exist = new AtomicBoolean(false);
        events.forEach(event -> {
            if(event.getId().equals(idEvent)){
                event.getParticipants().forEach(participant->{
                        if(participant.getId().equals(idSelectedUser)){
                            exist.set(true);
                        }
                });
            }

        });
        if(exist.get()){
            buttonInterestedNotInterested.setText("i am not interested");
        }
        else
        {
            buttonInterestedNotInterested.setText("interested");
        }

    }

    /**
     *
     * @return first event
     */
    public Event firstEvent() {
        Iterable<Event> events = eventService.getAllEvents();
        List<Event> eventList = new ArrayList<>();
        events.forEach(eventList::add);
        return eventList.get(0);
    }


}