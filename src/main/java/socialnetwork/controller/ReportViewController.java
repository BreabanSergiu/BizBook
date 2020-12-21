package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

public class ReportViewController {

    MessageService messageService ;
    FriendshipService friendshipService;
    UserService userService;
    UserDTO selectedUserDTO;

    ObservableList<UserDTO> model = FXCollections.observableArrayList();

    @FXML
    Button buttonGeneratePdfReport;
    @FXML
    PieChart piechart;

    @FXML
    TextField textFieldMonth;

    @FXML
    TextField textFieldYear;

    @FXML
    TableView<UserDTO> tableViewFriends;

    @FXML
    TableColumn<UserDTO,String> tableColumnFirstName;

    @FXML
    TableColumn<UserDTO,String> tableColumnLastName;

    @FXML
    void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<UserDTO, String>("lastName"));
        tableViewFriends.setItems(model);
        ObservableList<PieChart.Data>pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("January", 2 ),
                        new PieChart.Data("February", 2),
                        new PieChart.Data("March", 2),
                        new PieChart.Data("April",2),
                        new PieChart.Data("May",2),
                        new PieChart.Data("June", 2),
                        new PieChart.Data("July", 2),
                        new PieChart.Data("August", 2),
                        new PieChart.Data("September",2),
                        new PieChart.Data("October", 2),
                        new PieChart.Data("November", 2),
                        new PieChart.Data("December", 2)
                );
        piechart.setData(pieChartData);
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
            List<Message> messageList = null;
            int nrMessage = 0;
            int nrFriends = 0;
           if(userDTO!=null){
               String month  = textFieldMonth.getText();
               String year = textFieldYear.getText();
               if(month.matches("[0-9]+" ) && Integer.parseInt(month) > 0 && Integer.parseInt(month) <=12){
                   if(year.length() > 3 && year.matches("[0-9]+")){
                       nrMessage = messageService.getNumberOfMessagesFromOneMonth(selectedUserDTO.getId(),Integer.parseInt(month),Integer.parseInt(year));
                       nrFriends= friendshipService.getNumberOfFriendsFromOneMounthAndYear(selectedUserDTO.getId(),Integer.parseInt(month),Integer.parseInt(year));
                       messageList = messageService.getAllMessageFromOneFriendInOneMonthAndYear(selectedUserDTO.getId(),userDTO.getId(),Integer.parseInt(textFieldMonth.getText()),Integer.parseInt(textFieldYear.getText()));


                   }else
                   {
                       Alert alert = new Alert(Alert.AlertType.ERROR,"introduce a year between 1000 and 3000 ");
                       alert.show();
                       return;
                   }
               }
               else
               {
                   Alert alert = new Alert(Alert.AlertType.ERROR,"introduce a valid month 1-12 ");
                   alert.show();
                   return;

               }

           }
           else
           {
               Alert alert = new Alert(Alert.AlertType.ERROR,"Nothing selected");
               alert.show();
               return;
           }

            if(messageList.size() == 0){
                User user = userService.getUser(selectedUserDTO.getId());
                messageList.add(new Message(user, Arrays.asList(user),"niciun mesaj gasit", LocalDateTime.now()));
            }



            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(messageList);///give collection from where we take the elements
            Map<String ,Object> parameters = new HashMap<>();
            parameters.put("textFieldDescription",selectedUserDTO.getFirstName()+"_"+selectedUserDTO.getLastName()+"'s report");
            parameters.put("textFieldNrMessage","Number of messages = "+nrMessage );
            parameters.put("textFieldNrPrieteni","Number of new friends = "+nrFriends);

            textFieldMonth.clear();
            textFieldYear.clear();
            tableViewFriends.getSelectionModel().clearSelection();
            //create the report ,fill copiled report with data from datasource
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
            //export the report to dest
            JasperExportManager.exportReportToPdfFile(jasperPrint,pathToGenerate+"/"+selectedUserDTO.getFirstName()+"_"+selectedUserDTO.getLastName()+".pdf");


        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void handleButtonMessagesReport(ActionEvent event) {

        String year = textFieldYear.getText();
        ObservableList<PieChart.Data> pieChartData ;
            if(year.length() > 3 && year.matches("[0-9]+")){

                pieChartData =
                        FXCollections.observableArrayList(
                                new PieChart.Data("January", messageService.getNumberOfMessagesFromOneMonth(selectedUserDTO.getId(),1,Integer.parseInt(year) )  ),
                                new PieChart.Data("February", messageService.getNumberOfMessagesFromOneMonth(selectedUserDTO.getId(),2,Integer.parseInt(year) )),
                                new PieChart.Data("March", messageService.getNumberOfMessagesFromOneMonth(selectedUserDTO.getId(),3,Integer.parseInt(year) )),
                                new PieChart.Data("April", messageService.getNumberOfMessagesFromOneMonth(selectedUserDTO.getId(),4,Integer.parseInt(year) )),
                                new PieChart.Data("May", messageService.getNumberOfMessagesFromOneMonth(selectedUserDTO.getId(),5,Integer.parseInt(year) )),
                                new PieChart.Data("June", messageService.getNumberOfMessagesFromOneMonth(selectedUserDTO.getId(),6,Integer.parseInt(year) )),
                                new PieChart.Data("July", messageService.getNumberOfMessagesFromOneMonth(selectedUserDTO.getId(),7,Integer.parseInt(year) )),
                                new PieChart.Data("August", messageService.getNumberOfMessagesFromOneMonth(selectedUserDTO.getId(),8,Integer.parseInt(year) )),
                                new PieChart.Data("September", messageService.getNumberOfMessagesFromOneMonth(selectedUserDTO.getId(),9,Integer.parseInt(year) )),
                                new PieChart.Data("October", messageService.getNumberOfMessagesFromOneMonth(selectedUserDTO.getId(),10,Integer.parseInt(year) )),
                                new PieChart.Data("November", messageService.getNumberOfMessagesFromOneMonth(selectedUserDTO.getId(),11,Integer.parseInt(year) )),
                                new PieChart.Data("December", messageService.getNumberOfMessagesFromOneMonth(selectedUserDTO.getId(),12,Integer.parseInt(year) ))
                        );

            }else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR,"introduce a year between 1000 and 3000 ");
                alert.show();
                return;
            }


        piechart.setTitle("Messages");
        piechart.setData(pieChartData);

        textFieldYear.clear();
        textFieldMonth.clear();
        tableViewFriends.getSelectionModel().clearSelection();

    }

    @FXML
    private void handleButtonFriendsReport(ActionEvent event) {
        String year = textFieldYear.getText();
        ObservableList<PieChart.Data> pieChartData ;
        if(year.length() > 3 && year.matches("[0-9]+")){

            pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("January", friendshipService.getNumberOfFriendsFromOneMounthAndYear(selectedUserDTO.getId(),1,Integer.parseInt(year) )  ),
                            new PieChart.Data("February", friendshipService.getNumberOfFriendsFromOneMounthAndYear(selectedUserDTO.getId(),2,Integer.parseInt(year) )),
                            new PieChart.Data("March", friendshipService.getNumberOfFriendsFromOneMounthAndYear(selectedUserDTO.getId(),3,Integer.parseInt(year) )),
                            new PieChart.Data("April", friendshipService.getNumberOfFriendsFromOneMounthAndYear(selectedUserDTO.getId(),4,Integer.parseInt(year) )),
                            new PieChart.Data("May",friendshipService.getNumberOfFriendsFromOneMounthAndYear(selectedUserDTO.getId(),5,Integer.parseInt(year) )),
                            new PieChart.Data("June", friendshipService.getNumberOfFriendsFromOneMounthAndYear(selectedUserDTO.getId(),6,Integer.parseInt(year) )),
                            new PieChart.Data("July", friendshipService.getNumberOfFriendsFromOneMounthAndYear(selectedUserDTO.getId(),7,Integer.parseInt(year) )),
                            new PieChart.Data("August", friendshipService.getNumberOfFriendsFromOneMounthAndYear(selectedUserDTO.getId(),8,Integer.parseInt(year) )),
                            new PieChart.Data("September", friendshipService.getNumberOfFriendsFromOneMounthAndYear(selectedUserDTO.getId(),9,Integer.parseInt(year) )),
                            new PieChart.Data("October", friendshipService.getNumberOfFriendsFromOneMounthAndYear(selectedUserDTO.getId(),10,Integer.parseInt(year) )),
                            new PieChart.Data("November", friendshipService.getNumberOfFriendsFromOneMounthAndYear(selectedUserDTO.getId(),11,Integer.parseInt(year) )),
                            new PieChart.Data("December",friendshipService.getNumberOfFriendsFromOneMounthAndYear(selectedUserDTO.getId(),12,Integer.parseInt(year) ))
                    );

        }else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR,"introduce a year between 1000 and 3000 ");
            alert.show();
            return;
        }


        piechart.setTitle("Friends");
        piechart.setData(pieChartData);

        textFieldYear.clear();
        textFieldMonth.clear();
        tableViewFriends.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleButtonClearAction(ActionEvent event) {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList();
        piechart.setTitle("");
        piechart.setData(pieChartData);
    }


//    public void hadlepp(MouseEvent mouseEvent) {
//
//        for (PieChart.Data data :piechart.getData()){
//            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    JOptionPane.showMessageDialog(null,data.getPieValue());
//                }
//            });
//        }
//    }
}
