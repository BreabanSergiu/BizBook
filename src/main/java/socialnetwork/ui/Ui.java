package socialnetwork.ui;

import org.graalvm.compiler.lir.LIRInstruction;
import socialnetwork.comunitate.Community;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.message.FriendshipRequest;
import socialnetwork.domain.message.Message;
import socialnetwork.domain.message.ReplyMessage;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.service.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ui {
    private UserService userService;
    private FriendshipService friendshipService;
    private MessageService messageService;
    private ReplyMessageService replyMessageService;
    private FriendshipRequestService friendshipRequestService;

    public Ui(UserService userService, FriendshipService friendshipService, MessageService messageService, ReplyMessageService replyMessageService, FriendshipRequestService friendshipRequestService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        this.replyMessageService = replyMessageService;
        this.friendshipRequestService = friendshipRequestService;
    }

    public void  run() throws IOException {
        int command = 0 ;


        while(true){
            try{
                System.out.println("\n\nMENIU :\n " +
                        "0 - EXIT\n " +
                        "1 - addUser \n" +
                        "2 - deleteUser\n" +
                        "3 - addFriend\n" +
                        "4 - deleteFriend\n" +
                        "5 - number of comunities\n " +
                        "6 - most sociable community \n" +
                        "7 - all user friendships \n" +
                        "8 - all user freindships from one month \n" +
                        "9 - send message to many\n" +
                        "10 - add conversation \n" +
                        "11 - print conversation\n" +
                        "12- send FriendshipRequest\n" +
                        "13- answer FriendshipRequest\n" +
                        "14 - answer message\n");

            System.out.print("\nintroduce a command: ");
            BufferedReader bufferedReader  = new BufferedReader(new InputStreamReader(System.in));
            command = Integer.parseInt(bufferedReader.readLine());
            switch (command) {
                case 0:
                    return;
                case 1:
                    this.userService.addUser(readUser());
                    System.out.println("Successful add!\n");
                    break;
                case 2:
                   this.userService.deleteUtilizator(Long.parseLong(readNumber("id user:")));
                    System.out.println("Successful deleted!\n");
                   break;
                case 3:
                    this.friendshipService.addFriendship(readPrietenie());
                    System.out.println("Successful add!\n");
                    break;
                case 4:
                    this.friendshipService.deleteFriendship(readPrietenie().getId());
                    System.out.println("Successful deleted!\n");
                    break;
                case 5:
                    Community community = new Community(friendshipService.getAll());
                    community.printNumberOfCommunities();
                    break;
                case 6:
                    Community community1 = new Community(friendshipService.getAll());
                    community1.printMostSociableCommunities();
                    break;
                case 7:
                    FriendshipOfUser();
                    break;
                case 8:
                    AllFriendshipOfUser();
                    break;
                case 9:
                    sendMessageToMany();
                    break;
                case 10:
                    addConversatione();
                    break;
                case 11:
                    printConversation();
                    break;
                case 12:
                    sendFriendshipRequest();
                    break;
                case 13:
                    answerFriendshipRequest();
                    break;
                case 14:
                    respondMessage();
                    break;


            }
            }catch (ValidationException e){
                System.out.println(e.getMessage());
            }
        }
    }

    protected User readUser() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String idUser;
        System.out.print("Introduce a id user: ");
        while(true){
            try{

                idUser = bufferedReader.readLine();
                 Long.parseLong(idUser);
                 break;
            }catch (NumberFormatException e){
                System.err.print("ID invalid! Introduce a new Id User: ");

            }
        }


        String firstNameUser;
        System.out.print("introduce the first name of the user: ");
        firstNameUser = bufferedReader.readLine();
        String lastNameUser;
        System.out.print("introduce the last name of the user: ");
        lastNameUser = bufferedReader.readLine();
        User user = new User(firstNameUser,lastNameUser);
        user.setId(Long.parseLong(idUser));
        return user;

    }

    private String readNumber(String string)
        { BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String idUser;
        System.out.print(string);
        while(true){
            try{

                idUser = bufferedReader.readLine();
                Long.parseLong(idUser);
                break;
            }catch (IOException e){
                System.err.print("ID invalid! Introduce a new Id User: ");

            }
        }
        return idUser;
    }

    private Friendship readPrietenie()throws  IOException{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((System.in)));

        String idPrietenie1 , idPrietenie2;
        System.out.print("Introduce the first id Friendship: ");
        while(true){
            try{
               idPrietenie1 = bufferedReader.readLine();
               Long.parseLong(idPrietenie1);
               break;
            }catch (NumberFormatException e){
                System.err.print("id invalid! Introduce a new iD friendship: ");
            }
        }
        System.out.print("introduce the second id Friendship: ");
        while(true){
            try{
                idPrietenie2 = bufferedReader.readLine();
                Long.parseLong(idPrietenie2);
                break;
            }catch (NumberFormatException e){
                System.err.print("id invalid! Introduce a new iD friendship: ");
            }
        }
        Friendship friendship = new Friendship(new Tuple<Long, Long>(Long.parseLong(idPrietenie1), Long.parseLong(idPrietenie2)));
        return friendship;
    }

    private String readString(String string) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(string);
        String stringReturned = bufferedReader.readLine();
        return stringReturned;
    }

    private void FriendshipOfUser(){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String idUser;
        System.out.print("Introduce a id user: ");
        while(true){
            try{

                idUser = bufferedReader.readLine();
                Long.parseLong(idUser);
                break;
            }catch (NumberFormatException | IOException e){
                System.err.print("ID invalid! Introduce a new Id User: ");

            }
        }
        Long idUserLong = Long.parseLong(idUser);

        List<Friendship> list = new ArrayList<Friendship>();
        friendshipService.getAll().forEach(list::add);

         list.stream()
                .filter(x->{
                    return x.getId().getLeft().equals(idUserLong) || x.getId().getRight().equals(idUserLong);
                })
                .forEach(x->{
                    User user=null;
                    if(x.getId().getRight().equals(idUserLong)){
                        user = userService.getUser(x.getId().getLeft());
                    }
                    else {
                        user = userService.getUser(x.getId().getRight());
                    }
                    System.out.println(user.getFirstName() + " | " + user.getLastName() +" | " + x.getDate());
                });
    }

    private void AllFriendshipOfUser(){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String idUser;
        System.out.print("Introduce a id user: ");
        while(true){
            try{

                idUser = bufferedReader.readLine();
                Long.parseLong(idUser);
                break;
            }catch (NumberFormatException | IOException e){
                System.err.print("ID invalid! Introduce a new Id User: ");

            }
        }
        Long idUserLong = Long.parseLong(idUser);

        String month;
        System.out.print("Introduce the month: ");
        while(true){
            try{

                month = bufferedReader.readLine();
                Long.parseLong(month);
                break;
            }catch (NumberFormatException | IOException e){
                System.err.print("Invalid month! Introduce a new month between 01 and 12: ");

            }
        }

        System.out.print("introduce year= ");
        String year;
        while(true){
            try{

                year = bufferedReader.readLine();
                Long.parseLong(year);
                break;
            }catch (NumberFormatException | IOException e){
                System.err.print("Invalid year! Introduce an Int ");

            }
        }
        List<Friendship> list = new ArrayList<Friendship>();

        friendshipService.getAll().forEach(list::add);

        String finalMonth = month;
        String finalYear = year;
        list.stream()
                .filter(x->{
                    return x.getId().getRight().equals(idUserLong) || x.getId().getLeft().equals(idUserLong);
                })
                .filter(x->{
                    String date = x.getDate().toString();
                    return date.substring(5,7).equals(finalMonth) && date.substring(0,4).equals(finalYear);
                })
                .forEach(x->{
                User user=null;
                if(x.getId().getRight().equals(idUserLong)){
                user = userService.getUser(x.getId().getLeft());
                }
                else {
                user = userService.getUser(x.getId().getRight());
                }
                System.out.println(user.getFirstName() + " | " + user.getLastName() +" | " + x.getDate());
        });
        ;
    }

    private void sendMessageToMany() throws IOException {
        //citire id-ul userului care trimite mesajul
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String idString;
        System.out.print("Introduce the id of the user from: ");
        while(true){
            try{
                idString = bufferedReader.readLine();
                Long.parseLong(idString);
                break;
            }catch (NumberFormatException | IOException e){
                System.err.println("ID invalid! Introduce a valid id - a number! ");
            }
        }
        Long idUserFrom = Long.parseLong(idString);

        //citire mesaj trimis
        String mesaj;
        System.out.print("Introduce the message: ");
        mesaj = bufferedReader.readLine(); ///read message

        LocalDateTime data = LocalDateTime.now(); //init data

        //read list of user whom the message is sent
        List<User> listTo =new ArrayList<>();
        System.out.print("Introduce the ids of users to whom the messages is sent,to end press - 0: ");
        String idUserString = "";
        while(true) {
            while (true) {
                try {
                    idUserString = bufferedReader.readLine();
                    Long.parseLong(idUserString);
                    break;
                } catch (NumberFormatException | IOException e) {
                    System.err.println("ID invalid! Introduce a valid id - a number! ");
                }
            }

            Long idd = Long.parseLong(idUserString);
            if(idd == 0)
                break;
            listTo.add(userService.getUser(idd));
        }




        User userFrom = userService.getUser(idUserFrom);
        Message messageDone = new Message(userFrom,listTo,mesaj,data);
        messageService.addMessage(messageDone);
        System.out.println("Successful send!\n");

    }

    private void addConversatione() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String idSender = readNumber("Id from= ");
        String idReceiver = readNumber("Id to = ");

        String message = readString("message= ");
        User user1 = userService.getUser(Long.parseLong(idSender));
        User user2 = userService.getUser(Long.parseLong(idReceiver));

        ReplyMessage replyMessage = new ReplyMessage(user1, Arrays.asList(user2),message,LocalDateTime.now(),
                replyMessageService.getReplyMessage(0l));
        replyMessageService.addReplyMessage(replyMessage);


        while(true){
            System.out.println("do you want to continue this conversation? [y / n]");
            System.out.print("the response= ");
            String response = bufferedReader.readLine();
            if(response.equals("y")  || response.equals("Y")){
                User auxiliarUser = user1;
                user1 = user2;
                user2 = auxiliarUser;
                message = readString("new message = ");
                replyMessage = new ReplyMessage(user1, Arrays.asList(user2),message,LocalDateTime.now(),
                        replyMessageService.getReplyMessage(replyMessage.getId()));
                replyMessageService.addReplyMessage(replyMessage);
            }
            else
                if(response.equals("N") || response.equals("n")){
                    System.out.println("the conversation ended!\n");
                    break;
                }
        }
    }


    private void printConversation(){
       String idFist = readNumber("first id= ");
       String idSecond = readNumber("second id= ");
       Iterable<ReplyMessage> conversation = replyMessageService.getConversation(Long.parseLong(idFist),Long.parseLong(idSecond));
        conversation.forEach(x-> System.out.println(x));
        if(!conversation.iterator().hasNext()){
            System.out.println("the conversation doesn t exist!\n");
        }
    }

    private void sendFriendshipRequest() throws IOException {
        String idSender = readNumber("User from= ");
        String idReceiver = readNumber("User to= ");

        String message = readString("message = ");

        User userSender = userService.getUser(Long.parseLong(idSender));
        User userReceiver = userService.getUser(Long.parseLong(idReceiver));

        FriendshipRequest friendshipRequest = new FriendshipRequest(userSender,Arrays.asList(userReceiver),message,
                LocalDateTime.now(),"pending");


        friendshipRequestService.addRequest(friendshipRequest);

    }

    private void answerFriendshipRequest() throws IOException {
        String id = readNumber("id User to whom want to find out the friendhip request= ");

        Iterable<FriendshipRequest> friendshipRequestIterable = friendshipRequestService.getAllPendingRequest(Long.parseLong(id));

        if(!friendshipRequestIterable.iterator().hasNext())
        {
            System.out.println("there are not pending friendship request!\n");
            return;
        }
        friendshipRequestIterable.forEach(System.out::println);

        System.out.println("which friendship request do you choose?!\n");

        String idFriendshipRequest = readNumber("id friendshipRequest= ");

        System.out.println("approved(A)/rejected(R)?\n ");
        String action = readString("action = ");

        FriendshipRequest friendshipRequest = friendshipRequestService.getFriendshipRequest(Long.parseLong(idFriendshipRequest));

        if(action.equals("A") || action.equals("a")){

            friendshipRequestService.deleteRequest(Long.parseLong(idFriendshipRequest));
            friendshipRequest.setStatus("approved");
            friendshipRequestService.addRequest(friendshipRequest);
            System.out.println("approved succesfull!\n");
            Friendship friendship = new Friendship(new Tuple<>(friendshipRequest.getFrom().getId(),Long.parseLong(id)));
            friendshipService.addFriendship(friendship);
        }
        else
            if(action.equals("r") || action.equals("R")){
                friendshipRequestService.deleteRequest(Long.parseLong(idFriendshipRequest));
                friendshipRequest.setStatus("rejected");
                System.out.println("rejected succesfull!\n");
                friendshipRequestService.addRequest(friendshipRequest);
            }
            else{
                System.out.println("no action!\n");
            }
    }


    private  void respondMessage() throws IOException {
        Long id = Long.parseLong(readNumber("introduce the id of the user who want to check the message= "));

        Iterable<Message> filterList = messageService.getReceiveMessageUser(id);
        filterList.forEach(x-> System.out.println("Id message= "+ x.getId() +" | from= " + x.getFrom().getId() + "," + x.getFrom().getFirstName() + " " + x.getFrom().getLastName() + " |  message = " + x.getMessage()));


        Long idMessage = Long.parseLong(readNumber(" introduce the id message who want to respond = "));
        String message =  readString("introduce the message reply = ");

        Message messageFrom = messageService.getMessage(idMessage);

        User userSender = userService.getUser(id);

        Message messageResponse = new Message(userSender, Arrays.asList(messageFrom.getFrom()),message,LocalDateTime.now());

        messageService.addMessage(messageResponse);

    }
}
