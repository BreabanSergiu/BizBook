package socialnetwork.repository.database;

import org.graalvm.compiler.lir.LIR;
import org.postgresql.util.PSQLException;
import socialnetwork.domain.*;
import socialnetwork.domain.message.Message;
import socialnetwork.repository.Repository;
import socialnetwork.utils.Constants;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessageDbRepository implements Repository<Long, Message> {

    private String url;
    private String username;
    private String password;
    private Repository<Long,User> userDbRepository;

    public MessageDbRepository(String url, String username, String password,Repository<Long,User> userDbRepository) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.userDbRepository = userDbRepository;
    }

    @Override
    public Message findOne(Long aLong) {
        try(Connection connection = DriverManager.getConnection(url,username,password)){//aici creem conexiunea cu url
            String command = "SELECT * " +
                    "From messages" +
                    " WHERE id = " +aLong ;
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Long idFrom = resultSet.getLong("from");
                User userSender = userDbRepository.findOne(idFrom);

                String listIdTo = resultSet.getString("to");
                String[] parts = listIdTo.split(",");
                List<User> listUsersReceiver = new ArrayList<>();
                for(String p : parts){
                    Long id = Long.parseLong(p);
                    User user = userDbRepository.findOne(id);
                    listUsersReceiver.add(user);
                }

                String message = resultSet.getString("message");
                String data = resultSet.getString("date");
                Message resultingMessage = new Message(userSender,listUsersReceiver,message, LocalDateTime.parse(data, Constants.DATE_TIME_FORMATTER));
                resultingMessage.setId(aLong);
                return resultingMessage;
            }



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Long idFrom = resultSet.getLong("from");
                User userSender = userDbRepository.findOne(idFrom);

                String listIdTo = resultSet.getString("to");
                String[] parts = listIdTo.split(",");
                List<User> listUsersReceiver = new ArrayList<>();
                for(String p : parts){
                    Long id = Long.parseLong(p);
                    User user = userDbRepository.findOne(id);
                    listUsersReceiver.add(user);
                }

                String message = resultSet.getString("message");
                String data = resultSet.getString("date");
                Message resultingMessage = new Message(userSender,listUsersReceiver,message, LocalDateTime.parse(data, Constants.DATE_TIME_FORMATTER));
                Long idMessage  = resultSet.getLong("id");
                resultingMessage.setId(idMessage);
                messages.add(resultingMessage);

            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Message save(Message entity) {
        try(Connection connection = DriverManager.getConnection(url,username,password);
        ){
            List<User> listUsersTo = entity.getTo();
            String idsTo = "";
            for(User user :listUsersTo){
                idsTo = idsTo + user.getId() +",";
            }
            idsTo = idsTo.substring(0,idsTo.length()-1);


            String command = "INSERT INTO messages(\"from\" , \"to\" , \"message\", \"date\")VALUES" +
                    "('"+entity.getFrom().getId()+"','"+ idsTo+ "','"+ entity.getMessage()+"','"+entity.getDate().format(Constants.DATE_TIME_FORMATTER)+"')"+" RETURNING *";

            PreparedStatement preparedStatement = connection.prepareStatement(command);
            try{
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    return null;
                }

            }catch(PSQLException e){
                return entity;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return entity;
    }

    @Override
    public Message delete(Long aLong) {
        try(Connection connection = DriverManager.getConnection(url,username,password);
        ){
            String command = "DELETE FROM messages WHERE id = "+aLong+" RETURNING *";
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            ResultSet resultSet =preparedStatement.executeQuery();
            if(resultSet.next()){
                Long idFrom = resultSet.getLong("from");
                User userSender = userDbRepository.findOne(idFrom);

                String listIdTo = resultSet.getString("to");
                String[] parts = listIdTo.split(",");
                List<User> listUsersReceiver = new ArrayList<>();
                for(String p : parts){
                    Long id = Long.parseLong(p);
                    User user = userDbRepository.findOne(id);
                    listUsersReceiver.add(user);
                }

                String message = resultSet.getString("message");
                String data = resultSet.getString("date");
                Message resultingMessage = new Message(userSender,listUsersReceiver,message, LocalDateTime.parse(data, Constants.DATE_TIME_FORMATTER));
                resultingMessage.setId(aLong);
                return  resultingMessage;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }



    public Iterable<Message> findAll(Page curentPage){
        List<Message> messageList = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url,username,password)) {


            String command = "SELECT * FROM messages LIMIT "+ curentPage.getSizePage()+
                    " OFFSET " + (curentPage.getNumberPage()-1)*curentPage.getSizePage();

            PreparedStatement preparedStatement = connection.prepareStatement(command);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Long idMessages = resultSet.getLong("id");
                messageList.add(findOne(idMessages));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return messageList;
    }

    public Iterable<Message> findAll(Page curentPage,Long idUser){
        List<Message> messageList = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url,username,password)) {

            String command = "SELECT * FROM messages WHERE \"to\" ~* '"+idUser+",' or \"to\" ~* '," +
                    idUser+"' or \"to\" ~* '^"+idUser+"$'" +
                    " LIMIT "+curentPage.getSizePage() +" OFFSET "+(curentPage.getNumberPage()-1)*curentPage.getSizePage();

            PreparedStatement preparedStatement = connection.prepareStatement(command);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Long idMessages = resultSet.getLong("id");
                messageList.add(findOne(idMessages));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return messageList;
    }

    @Override
    public Message update(Message entity) {
        return null;
    }
}
