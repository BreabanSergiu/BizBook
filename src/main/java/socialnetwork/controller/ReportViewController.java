package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.util.ResourceUtils;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.User;
import socialnetwork.domain.UserDTO;
import socialnetwork.domain.message.Message;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.MessageService;
import socialnetwork.service.UserService;
import socialnetwork.utils.observer.Observable;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class ReportViewController {

    MessageService messageService ;
    FriendshipService friendshipService;
    UserService userService;
    UserDTO selectedUserDTO;


    ObservableList<UserDTO> model = FXCollections.observableArrayList();
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    @FXML
    Button buttonGeneratePdfReport;

    @FXML
    PieChart piechart;

    @FXML
    TableView<UserDTO> tableViewFriends;

    @FXML
    TableColumn<UserDTO,String> tableColumnFirstName;

    @FXML
    TableColumn<UserDTO,String> tableColumnLastName;

    @FXML
    DatePicker datePickerStartDate;

    @FXML
    DatePicker datePickerEndDate;

    @FXML
    Label labelProcent;




    @FXML
    void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("lastName"));

        tableViewFriends.setItems(model);
        piechart.setData(pieChartData);

        labelProcent.setVisible(false);


    }
    public void populatePieChart(){
        Iterable<Message> messageIterable = messageService.getAllMessagesToUser(selectedUserDTO.getId());
        List<Message> messageList = new ArrayList<>();

        messageIterable.forEach(messageList::add);
        LocalDate dataStart = LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonth(),1);
        LocalDate dataEnd = LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonth(),31);
        List<Message> filtredMessageList = messageService.getMessagesBetweenDate(selectedUserDTO.getId(),
                dataStart,dataEnd);


        piechart.getData().clear();
        PieChart.Data data1 = new PieChart.Data("last month", filtredMessageList.size() );
        PieChart.Data data2 = new PieChart.Data("other months", messageList.size() - filtredMessageList.size() );
        pieChartData.add(data1);
        pieChartData.add(data2);

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        for (PieChart.Data data :pieChartData){

            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED,
                    new EventHandler<MouseEvent>() {
                        @Override public void handle(MouseEvent e) {
                            Double procent = (data.getPieValue()*100)/messageList.size();
                            labelProcent.setTranslateX(e.getSceneX()-15);
                            labelProcent.setTranslateY(e.getSceneY()-15);
                            labelProcent.setText(decimalFormat.format(procent) + "%");
                            labelProcent.setVisible(true);


                        }
                    });

        }
        piechart.setTitle("Messages");

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
            tableViewFriends.setPlaceholder(new Label("You have no added friends"));
        } else {
            model.setAll(listFriends);
        }
    }


    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void setSelectedUserDTO(UserDTO selectedUserDTO){
        this.selectedUserDTO = selectedUserDTO;

    }

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
        initModel();
    }


    public void generatePDF() {
        String pathToGenerate = "/Users/breabansergiugeorgica/desktop";

        try{
            File file = ResourceUtils.getFile("classpath:raport1.jrxml");
            JasperReport jasperReport  = JasperCompileManager.compileReport(file.getAbsolutePath());//compile de report

            UserDTO userDTO = tableViewFriends.getSelectionModel().getSelectedItem();

            if(datePickerStartDate.getValue() == null || datePickerEndDate.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.ERROR,"you must to introduce a date!");
                alert.show();
                return;
            }
            List<Message> messageList = null;
            int nrMessage = 0;
            int nrFriends = 0;
            if(userDTO != null){
                messageList = messageService.getAllMessageFromOneFriendBetweenDate(selectedUserDTO.getId(),userDTO.getId(),datePickerStartDate.getValue(),datePickerEndDate.getValue());
                nrMessage = messageService.getMessagesBetweenDate(selectedUserDTO.getId(),datePickerStartDate.getValue(),datePickerEndDate.getValue()).size();
                nrFriends = friendshipService.getFriendsBetweenDate(selectedUserDTO.getId(),datePickerStartDate.getValue(),datePickerEndDate.getValue()).size();

            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR,"you must to select one user!");
                alert.show();
                return;
            }


            if(messageList.size() == 0){
                User user = userService.getUser(selectedUserDTO.getId());
                messageList.add(new Message(user, Arrays.asList(user),"niciun mesaj gasit", LocalDateTime.now()));
            }



            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(messageList);///give collection from where we take the elements
            Map<String ,Object> parameters = new HashMap<>();
            String dateFormat = datePickerStartDate.getValue().getYear()+"/"+datePickerStartDate.getValue().getMonthValue()+"/"+datePickerStartDate.getValue().getDayOfMonth()+" - "
                    + datePickerEndDate.getValue().getYear()+"/"+datePickerEndDate.getValue().getMonthValue()+"/"+datePickerEndDate.getValue().getDayOfMonth();
            parameters.put("textFieldDescription",selectedUserDTO.getFirstName()+"_"+selectedUserDTO.getLastName()+"'s report");
            parameters.put("textFieldNrMessage","Between date "+dateFormat +" the number of messages is = "+nrMessage);
            parameters.put("textFieldNrPrieteni","Between date "+ dateFormat +" the number of newly created friends is = "+nrFriends);


            tableViewFriends.getSelectionModel().clearSelection();
            //create the report ,fill copiled report with data from datasource
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
            //export the report to dest
            String destinationFileName = pathToGenerate+"/"+selectedUserDTO.getFirstName()+"_"+selectedUserDTO.getLastName()+".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint,destinationFileName);


            //open pdf file
            if(Desktop.isDesktopSupported()){
                try{

                    File myFile = new File(destinationFileName);
                    Desktop.getDesktop().open(myFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void handleButtonMessagesReport(ActionEvent event) {

        labelProcent.setVisible(false);

        if(datePickerEndDate.getValue() == null || datePickerStartDate.getValue() ==null){
            Alert alert = new Alert(Alert.AlertType.ERROR,"please introduce the date!");
            alert.show();
            return;
        }

       Iterable<Message> messageIterable = messageService.getAllMessagesToUser(selectedUserDTO.getId());
       List<Message> messageList = new ArrayList<>();

       messageIterable.forEach(messageList::add);
       List<Message> filtredMessageList = messageService.getMessagesBetweenDate(selectedUserDTO.getId(),
               datePickerStartDate.getValue(),datePickerEndDate.getValue());


       piechart.getData().clear();
       PieChart.Data data1 = new PieChart.Data("picked date", filtredMessageList.size() );
       PieChart.Data data2 = new PieChart.Data("other date", messageList.size() - filtredMessageList.size() );
       pieChartData.add(data1);
       pieChartData.add(data2);

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        for (PieChart.Data data :pieChartData){

            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED,
                    new EventHandler<MouseEvent>() {
                        @Override public void handle(MouseEvent e) {
                            Double procent = (data.getPieValue()*100)/messageList.size();
                            labelProcent.setTranslateX(e.getSceneX()-15);
                            labelProcent.setTranslateY(e.getSceneY()-15);
                            labelProcent.setText(decimalFormat.format(procent) + "%");
                            labelProcent.setVisible(true);


                        }
                    });

        }

       piechart.setTitle("Messages");


    }

    @FXML
    private void handleButtonFriendsReport(ActionEvent event) {

        labelProcent.setVisible(false);


        if(datePickerEndDate.getValue() == null || datePickerStartDate.getValue() ==null){
            Alert alert = new Alert(Alert.AlertType.ERROR,"please introduce the date!");
            alert.show();
            return;
        }

        List<Friendship> filtredFriendshipList = friendshipService.getFriendsBetweenDate(selectedUserDTO.getId(),
                datePickerStartDate.getValue(),datePickerEndDate.getValue());

        Iterable<Friendship> friendshipIterable = friendshipService.getAllFriendshipsUser(selectedUserDTO.getId());
        List<Friendship> friendshipList = new ArrayList<>();
        friendshipIterable.forEach(friendshipList::add);


        piechart.getData().clear();

        PieChart.Data data1 = new PieChart.Data("picked date", filtredFriendshipList.size() );
        PieChart.Data data2 = new PieChart.Data("other date", friendshipList.size() - filtredFriendshipList.size() );
        pieChartData.add(data1);
        pieChartData.add(data2);

        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        for (PieChart.Data data :pieChartData){

            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED,
                    new EventHandler<MouseEvent>() {
                        @Override public void handle(MouseEvent e) {
                            Double procent = (data.getPieValue()*100)/friendshipList.size();
                            labelProcent.setTranslateX(e.getSceneX()-15);
                            labelProcent.setTranslateY(e.getSceneY()-15);
                            labelProcent.setText(decimalFormat.format(procent) + "%");
                            labelProcent.setVisible(true);


                        }
                    });

        }

        piechart.setTitle("Friends");


    }

    @FXML
    private void handleButtonClearAction(ActionEvent event) {
        labelProcent.setVisible(false);
        piechart.getData().clear();
        piechart.setTitle("");
        piechart.setData(pieChartData);
    }





}
