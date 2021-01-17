

package socialnetwork.controller;

        import javafx.beans.property.SimpleStringProperty;
        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Scene;
        import javafx.scene.control.*;
        import javafx.scene.control.cell.PropertyValueFactory;
        import javafx.scene.image.Image;
        import javafx.scene.image.ImageView;
        import javafx.scene.input.MouseEvent;
        import javafx.scene.layout.AnchorPane;
        import javafx.scene.layout.Pane;
        import javafx.scene.paint.ImagePattern;
        import javafx.scene.shape.Circle;
        import javafx.scene.shape.Rectangle;
        import javafx.stage.FileChooser;
        import javafx.stage.Modality;
        import javafx.stage.Stage;
        import javafx.util.Callback;
        import javafx.util.Duration;
        import org.graalvm.compiler.lir.LIRInstruction;
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
        import java.time.LocalDate;
        import java.time.temporal.ChronoUnit;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.concurrent.atomic.AtomicBoolean;


public class AccountUserController implements Observer<FriendshipChangeEvent>{
    ObservableList<UserDTO> modelFriends = FXCollections.observableArrayList();
    ObservableList<Message> modelMessages = FXCollections.observableArrayList();
    ObservableList<FriendshipRequest> modelRequests = FXCollections.observableArrayList();
    ObservableList<Event> modelNotification = FXCollections.observableArrayList();
    UserService userService;
    FriendshipService friendshipService;
    FriendshipRequestService friendshipRequestService;
    MessageService messageService;
    PhotoService photoService;
    EventService eventService;
    UserDTO selectedUserDTO;
    Stage accountUserStage;
    Stage introductionStage;
    Event currentEvent;
    private String pathPhotoCreateEvent;
    private final Page pageEvents = new Page(1,1);
    private final Page pageFriends = new Page(11,1);
    private final Page pageMessages = new Page(11,1);
    private final Page pageFriendshipRequests = new Page(11,1);

    @FXML
    Button buttonAddFriendship;
    @FXML
    Button buttonDeleteFriendship;
    @FXML
    Button buttonEvents;
    @FXML
    Button buttonInterestedNotInterested;
    @FXML
    Button buttonDisable;
    @FXML
    Button buttonCreateEvent;
    @FXML
    Button buttonPostsEvent;
    @FXML
    Button buttonDiscard;

    @FXML
    TableView<UserDTO> tableViewFriends;

    @FXML
    TableView<Message> tableViewMessages;

    @FXML
    TableView<FriendshipRequest> tableViewRequests;

    @FXML
    TableView<Event> tableViewNotification;

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
    TableColumn<Event,String> tableColumnNotification;


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
    Pane paneEventPhotoAndLabels;
    @FXML
    Pane paneEventCreateEvent;
    @FXML
    Pane paneNotification;


    @FXML
    Label labelNotification;
    @FXML
    Label labelBack;
    @FXML
    Label labelNextPage;
    @FXML
    Label labelPreviousPage;
    @FXML
    Label labelNameEvent;
    @FXML
    Label labelShowDescription;
    @FXML
    Label labelDescription;
    @FXML
    Label labelHideDescription;
    @FXML
    Label labelPreviousPageEvent;
    @FXML
    Label labelNextPageEvent;
    @FXML
    Label labelNrParticipants;
    @FXML
    Label labelDateEvent;
    @FXML
    Label labelUserName;

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
    Rectangle rectanglePhotoEvent;
    @FXML
    Rectangle rectanglePhotoCreateEvent;

    @FXML
    TextField textFieldNameEventCreateEvent;
    @FXML
    TextField textFieldDescriptionCreateEvent;

    @FXML
    DatePicker datePickerDateEvent;



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

        tableColumnNotification.setCellValueFactory(new PropertyValueFactory<Event,String>("NotificationString"));



        tableViewFriends.setItems(modelFriends);
        tableViewMessages.setItems(modelMessages);
        tableViewRequests.setItems(modelRequests);
        tableViewNotification.setItems(modelNotification);

        setTooltipPhotos();
        setPaneVisible();



    }


    /**
     * set visibility for Pane
     */
    private void setPaneVisible() {
        paneAccount.setVisible(true);
        paneEvents.setVisible(false);
        paneNameShowDetails.setVisible(true);
        paneDescription.setVisible(false);
        paneEventCreateEvent.setVisible(false);
        paneEventPhotoAndLabels.setVisible(true);
        paneNotification.setVisible(false);
        paneEventCreateEvent.setVisible(false);


    }

    private void setTooltipPhotos() {
        setTooltipPhotoAccount();
        setTooltipPhotoNotification();
        setTooltipPhotoBack();
        setTooltipPhotoNextPage();
        setTooltipPhotoPreviousPage();
        setTooltipPhotoCreateEvent();

    }

    private void setTooltipPhotoCreateEvent() {
        Tooltip tooltip =new Tooltip("press to add\n " +
                "  new photo");;
        tooltip.setShowDelay(new Duration(10));
        tooltip.setHideDelay(new Duration(10));
        Tooltip.install(rectanglePhotoCreateEvent,tooltip);
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
            labelUserName.setText(selectedUserDTO.getFirstName()+" "+selectedUserDTO.getLastName());
            initModelFriends();
            initModelMessages();
            initModelRequests();
        }
    }

    /**
     * init model FriendshipRequest
     */
    private void initModelRequests() {
        Iterable<FriendshipRequest> friendshipRequests = this.friendshipRequestService.getAllPendingRequest(selectedUserDTO.getId(),pageFriendshipRequests);
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

    /**
     * init model Messages
     */
    private void initModelMessages() {
        Iterable<Message> messages = this.messageService.getAllMessagesToUser(selectedUserDTO.getId(),pageMessages);
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

    /**
     * init model Friends
     */
    private void initModelFriends(){
        Iterable<Friendship> friendships = this.friendshipService.getAllFriendshipsUser(selectedUserDTO.getId(),pageFriends);
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


    /**
     * method that delete a freindship
     */
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

    /**
     * show view add FriendShipRequest
     */
    public void addFriendshipRequest(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/addFriendshipView.fxml"));
            AnchorPane root = loader.load();

            Stage addFriendshipRequestStage = new Stage();
            addFriendshipRequestStage.setTitle("Send friendship request");
            addFriendshipRequestStage.initModality(Modality.APPLICATION_MODAL);

            Scene scene = new Scene(root);
            addFriendshipRequestStage.setScene(scene);

            AddFriendshipViewController addFriendshipViewController = loader.getController();

//            addFriendshipRequestStage.setOnCloseRequest(event -> {
//                accountUserStage.show();
//            });
//            accountUserStage.show();


            addFriendshipViewController.setFriendshipService(friendshipService);
            addFriendshipViewController.setUserService(userService,selectedUserDTO);
            addFriendshipViewController.setFriendshipRequestService(friendshipRequestService);
            addFriendshipViewController.setStages(accountUserStage, introductionStage, addFriendshipRequestStage);
            addFriendshipRequestStage.setX(595);
            addFriendshipRequestStage.setY(129);
            addFriendshipRequestStage.show();

        }catch (IOException e){
            e.printStackTrace();
        }


    }

    /**
     * show view FriendshipRequest
     */
    public void viewFriendshipRequest(){
            try{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/FriendshipRequestView.fxml"));
                AnchorPane root = loader.load();

                Stage frienshipRequestViewStage = new Stage();
                frienshipRequestViewStage.setScene(new Scene(root));
                frienshipRequestViewStage.setTitle("Friendship Request");
                frienshipRequestViewStage.initModality(Modality.APPLICATION_MODAL);
                frienshipRequestViewStage.setX(595);
                frienshipRequestViewStage.setY(129);
                frienshipRequestViewStage.getIcons().add(new Image(getClass().getResourceAsStream("/css/1.jpg")));

                frienshipRequestViewStage.show();

//                frienshipRequestViewStage.setOnCloseRequest(event -> {
//                    accountUserStage.show();
//                });
//                accountUserStage.hide();

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

    /**
     * show view Messages
     */
    public void viewMessages() {

        try{
            FXMLLoader loader  = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/messageView.fxml"));

            AnchorPane root = loader.load();

            Stage messageViewStage = new Stage();
            messageViewStage.setScene(new Scene(root));
            messageViewStage.setTitle("Message");
            messageViewStage.initModality(Modality.APPLICATION_MODAL);//set the priority stage
            messageViewStage.setX(595);
            messageViewStage.setY(129);
            messageViewStage.show();

            MessageViewController messageViewController = loader.getController();

//            messageViewStage.setOnCloseRequest(event -> {
//                accountUserStage.show();
//            });
//
//
//            accountUserStage.hide();

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
            reportViewStage.initModality(Modality.APPLICATION_MODAL);//set the priority of this stage
            reportViewStage.setX(595);
            reportViewStage.setY(129);
            reportViewStage.show();

            ReportViewController reportViewController = loader.getController();
            reportViewController.setMessageService(messageService);
            reportViewController.setFriendshipService(friendshipService);
            reportViewController.setSelectedUserDTO(selectedUserDTO);
            reportViewController.setUserService(userService);
            reportViewController.setStages(accountUserStage,reportViewStage);
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
        Iterable<Event> events = eventService.getAllEvents(pageEvents);
        List<Event> eventList = new ArrayList<>();
        events.forEach(eventList::add);
        Event event = eventList.get(0);
        currentEvent = event;

        setPhotoEvent(event.getPhoto(),rectanglePhotoEvent);

        labelNameEvent.setText("Name Event: "+event.getName());
        String description = "Description: "+event.getDescription();
        labelDescription.setText(description);
        setTooltipLabelDescription(description);
        labelDateEvent.setText("Date: "+event.getDate().toString());
        labelNrParticipants.setText("Number Of Participants: "+ event.getParticipants().size());


    }

    /**
     *Method that initialize one event
     * @param event Event that will initialized
     */
    private void initEvent(Event event){

        setPhotoEvent(event.getPhoto(),rectanglePhotoEvent);
        labelNameEvent.setText("Name Event: "+event.getName());
        String description = "Description: "+event.getDescription();
        labelDescription.setText(description);
        setTooltipLabelDescription(description);
        labelDateEvent.setText("Date: "+event.getDate().toString());
        labelNrParticipants.setText("Number Of Participants: "+ event.getParticipants().size());
    }

    /**
     * Method that set the event photo
     * @param photo photo that will be set on event photo
     */
    private void setPhotoEvent(Photo photo,Rectangle rectangle) {

        if(photo != null){
            String photoPath = photo.getURL();
            FileInputStream fileInputStream =null;
            if(photoPath != null){
                try {
                    fileInputStream = new FileInputStream(photoPath);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                rectangle.setFill(new ImagePattern(new Image( fileInputStream )));
            }
        }

        else {
            rectangle.setFill(new ImagePattern(new Image(getClass().getClassLoader().getResourceAsStream("photos/profile.png"))));
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


    /**
     * method that registers a user at the event
     */
    public void joinOrLeaveEvent() {

        AtomicBoolean ok = new AtomicBoolean(false);
        List<User> listPart = currentEvent.getParticipants();
        listPart.forEach(user1 -> {
            if(user1.getId().equals(selectedUserDTO.getId())){
                ok.set(true);
            }
        });
        if(ok.get()){
            buttonInterestedNotInterested.setText("interested");
            listPart.removeIf(user -> (user.getId().equals(selectedUserDTO.getId())));
            eventService.updateEvent(currentEvent);
        }
        else {
            buttonInterestedNotInterested.setText("I am not interested");
            listPart.add(userService.getUser(selectedUserDTO.getId()));
            eventService.updateEvent(currentEvent);

        }
        labelNrParticipants.setText("Number Of Participants: "+ currentEvent.getParticipants().size()); //observer for labelNrParticipants

    }

    /**
     * set button -> not interested, if the user is in list of participants
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


    /**
     * go to the next Event page
     */
    public void goToTheNextPageEvent() {
        pageEvents.nextPage();
        Iterable<Event> eventIterable = eventService.getAllEvents(pageEvents);
        List<Event> eventList = new ArrayList<>();
        eventIterable.forEach(eventList::add);
        if(eventList.size() != 0){
            Event event = eventList.get(0);
            currentEvent  = event;
            initEvent(event);
            setButtonInterested(selectedUserDTO.getId(),event.getId());

        }
        else {
            pageEvents.previousPage();
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"no other events");
            alert.show();

        }

    }

    /**
     * go to the previous Event page
     */
    public void goToThePreviousPage() {
        if(pageEvents.getNumberPage() > 1){//it must not be negative offset
            pageEvents.previousPage();
            Iterable<Event> eventIterable = eventService.getAllEvents(pageEvents);
            List<Event> eventList = new ArrayList<>();
            eventIterable.forEach(eventList::add);
            Event event = eventList.get(0);
            currentEvent = event;
            initEvent(event);
            setButtonInterested(selectedUserDTO.getId(),event.getId());

        }
        else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION,"no other events");
                alert.show();
            }

    }


    /**
     * show the notifications
     */
    public void showNotification() {

        paneNotification.setVisible(!paneNotification.isVisible());
        initModelNotification();

    }

    private void initModelNotification() {
        Iterable<Event> eventIterable = eventService.getAllEvents();
        List<Event> eventList = new ArrayList<>();
        eventIterable.forEach(event -> {
            if( ChronoUnit.DAYS.between( LocalDate.now(),event.getDate()) <= 30 && event.isNotification() ){
                List<User> parts = event.getParticipants();
                parts.forEach(user->{
                    if(user.getId().equals(selectedUserDTO.getId())){
                        eventList.add(event);
                    }
                });

            }
        });

        if(eventList.size() == 0){

            modelNotification.setAll(eventList);
            tableViewNotification.setPlaceholder(new Label("you have no notification!"));
        }
        else
        {
            modelNotification.setAll(eventList);
        }

    }

    /**
     * disable notification for selected user
     */
    public void disableNotification() {
        Event event = tableViewNotification.getSelectionModel().getSelectedItem();
        event.setNotification(false);
        eventService.updateEvent(event);
        initModelNotification();
    }

    /**
     * method that show the Pane when you can create a event
     */
    public void showPaneCreateEvent() {
        paneEventPhotoAndLabels.setVisible(false);

        rectanglePhotoCreateEvent.setFill(new ImagePattern(new Image(getClass().getClassLoader().getResourceAsStream("photos/photoStandardEvent.jpg") )));
        pathPhotoCreateEvent = "";


        paneEventCreateEvent.setVisible(true);
    }

    /**
     * method that create a new event and post it
     */
    public void createEventAndPosts() {

        List<User> participants = new ArrayList<>();
        participants.add(userService.getUser(selectedUserDTO.getId()));


        Photo photo;
        if(!pathPhotoCreateEvent.equals("")){
            photo = new Photo(pathPhotoCreateEvent);
            setPhotoEvent(photo,rectanglePhotoCreateEvent);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR,"you must to select one photo");
            alert.show();
            return;
        }
        if(textFieldNameEventCreateEvent.getText().length() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR,"you must to introduce a name event");
            alert.show();
            return;
        }
        if(textFieldDescriptionCreateEvent.getText().length() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR,"you must to introduce a description");
            alert.show();
            return;
        }
        if(datePickerDateEvent.getEditor().getText().length() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR,"you must to introduce a date event");
            alert.show();
            return;
        }

        Event event = new Event(datePickerDateEvent.getValue(),textFieldNameEventCreateEvent.getText(),textFieldDescriptionCreateEvent.getText(),
                photo,participants,true);

        eventService.saveEvent(event);
        Alert alert = new Alert(Alert.AlertType.INFORMATION,"post made successfully");
        alert.show();

        paneEventCreateEvent.setVisible(false);
        paneEventPhotoAndLabels.setVisible(true);
    }


    /**
     * method that cancel the event creation
     */
    public void discardCreateEvent() {
        datePickerDateEvent.getEditor().clear();
        textFieldDescriptionCreateEvent.clear();
        textFieldNameEventCreateEvent.clear();
        paneEventCreateEvent.setVisible(false);
        paneEventPhotoAndLabels.setVisible(true);
    }

    /**
     * add new photo to the event
     */
    public void addPhotoCreateEvent() {
        FileInputStream fileInputStream = null;
        String photoPath = getPhotoURL();
        if(photoPath != null){
            try {
                fileInputStream = new FileInputStream(photoPath);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            rectanglePhotoCreateEvent.setFill(new ImagePattern(new Image( fileInputStream )));
            pathPhotoCreateEvent = photoPath;

        }
        else{
            pathPhotoCreateEvent = "";
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"you don t selected any photos ");
            alert.show();
        }


    }


    /**
     * method that set previous Page for Messages, Friends and FriendshipRequests
     */
    public void previousPageFriendsMessagesRequests() {
        if(tabPaneFriendsMessagesRequests.getSelectionModel().getSelectedItem().getId().equals(tabFriends.getId())){
            previousPageFriends();
        }
        else
            if(tabPaneFriendsMessagesRequests.getSelectionModel().getSelectedItem().getId().equals(tabMessages.getId())){
                previousPageMessages();
            }
            else
                if (tabPaneFriendsMessagesRequests.getSelectionModel().getSelectedItem().getId().equals(tabFriendshipRequests.getId())){
                    previousPageFriendshipRequests();
                }
    }

    /**
     * method that set previous Page for FriendshipRequests
     */
    private void previousPageFriendshipRequests() {

        if(pageFriendshipRequests.getNumberPage() > 1){
            pageFriendshipRequests.previousPage();
            initModelRequests();
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"no other FriendshipRequest");
            alert.show();
        }
    }

    /**
     * method that set previous Page for Messages
     */
    private void previousPageMessages() {
        if(pageMessages.getNumberPage() > 1){
            pageMessages.previousPage();
            initModelMessages();

        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"no other Messages");
           alert.show();
        }
    }

    /**
     * method that set previous Page for Friends
     */
    private void previousPageFriends() {
        if(pageFriends.getNumberPage() > 1){
            pageFriends.previousPage();
            initModelFriends();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"no other Friendship");
            alert.show();
        }
    }

    /**
     * method that set the next Page for Friends, Messages and Friendship requests
     */
    public void nextPageFriendsMessagesRequests() {
        if(tabPaneFriendsMessagesRequests.getSelectionModel().getSelectedItem().getId().equals(tabFriends.getId())){
            nextPageFriends();
        }
        else
            if(tabPaneFriendsMessagesRequests.getSelectionModel().getSelectedItem().getId().equals(tabMessages.getId())){
                nextPageMessages();
            }
            else
                if(tabPaneFriendsMessagesRequests.getSelectionModel().getSelectedItem().getId().equals(tabFriendshipRequests.getId())){
                    nextPageFriendshipRequests();
                }
    }

    /**
     * method that set the next Page for Friendship Requests
     */
    private void nextPageFriendshipRequests() {
        pageFriendshipRequests.nextPage();
        Iterable<FriendshipRequest> friendshipRequestIterable = friendshipRequestService.getAllPendingRequest(selectedUserDTO.getId(),pageFriendshipRequests);
        List<Message> messageList = new ArrayList<>();
        friendshipRequestIterable.forEach(messageList::add);
        if(messageList.size() != 0){
            initModelRequests();
        }
        else
        {
            pageFriendshipRequests.previousPage();
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"no other FriendshipRequest");
            alert.show();
        }
    }

    /**
     * method that set the next Page for Messages
     */
    private void nextPageMessages() {
        pageMessages.nextPage();
        Iterable<Message> messageIterable = messageService.getAllMessagesToUser(selectedUserDTO.getId(),pageMessages);
        List<Message> messageList = new ArrayList<>();
        messageIterable.forEach(messageList::add);
        if(messageList.size() != 0){
            initModelMessages();
        }
        else{
            pageMessages.previousPage();
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"no other Messages");
            alert.show();
        }

    }

    /**
     * method that set the next Page for Friends
     */
    private void nextPageFriends() {
        pageFriends.nextPage();
        Iterable<Friendship> friendshipIterable = friendshipService.getAllFriendshipsUser(selectedUserDTO.getId(),pageFriends);
        List<Friendship> friendshipList = new ArrayList<>();
        friendshipIterable.forEach(friendshipList::add);
        if(friendshipList.size() != 0 ){
            initModelFriends();
        }else
        {
            pageFriends.previousPage();
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"no other Friendship");
            alert.show();

        }
    }

    /**
     * method that logout a user
     */
    public void Logout() {
        accountUserStage.hide();
        introductionStage.show();
    }
}