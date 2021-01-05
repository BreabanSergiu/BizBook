package socialnetwork.repository.database;

import org.postgresql.util.PSQLException;
import socialnetwork.domain.Page;
import socialnetwork.domain.User;
import socialnetwork.domain.message.FriendshipRequest;
import socialnetwork.domain.message.Message;
import socialnetwork.repository.Repository;
import socialnetwork.utils.Constants;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class FriendshipRequestDbRepository implements Repository<Long, FriendshipRequest> {

    private String url;
    private String username;
    private String password;
    private Repository<Long,User> userDbRepository;

    public FriendshipRequestDbRepository(String url, String username, String password, Repository<Long, User> userDbRepository) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.userDbRepository = userDbRepository;
    }

    @Override
    public FriendshipRequest findOne(Long aLong) {
        try(Connection connection = DriverManager.getConnection(url,username,password)){
            String command = "SELECT * " +
                    "From \"friendshipRequests\"" +
                    " WHERE id = " +aLong ;
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Long idFrom = resultSet.getLong("from");
                User userSender = userDbRepository.findOne(idFrom);

                Long idTo = resultSet.getLong("to");
                User userReceiver = userDbRepository.findOne(idTo);
                List<User> listUsersReceiver = Arrays.asList(userReceiver);

                String message = resultSet.getString("message");
                String data = resultSet.getString("date");
                String status = resultSet.getString("status");
                FriendshipRequest friendshipRequest = new FriendshipRequest(userSender,listUsersReceiver,message, LocalDateTime.parse(data, Constants.DATE_TIME_FORMATTER),status);
                friendshipRequest.setId(aLong);
                return friendshipRequest;
            }



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<FriendshipRequest> findAll() {
        Set<FriendshipRequest>friendshipRequests = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"friendshipRequests\" ");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Long idFrom = resultSet.getLong("from");
                User userSender = userDbRepository.findOne(idFrom);

                Long idTo = resultSet.getLong("to");
                User userReceiver = userDbRepository.findOne(idTo);
                List<User> listUsersReceiver = Arrays.asList(userReceiver);

                String message = resultSet.getString("message");
                String data = resultSet.getString("date");
                String status = resultSet.getString("status");
                FriendshipRequest friendshipRequest = new FriendshipRequest(userSender,listUsersReceiver,message, LocalDateTime.parse(data, Constants.DATE_TIME_FORMATTER),status);
                Long idFriendshipRequest = resultSet.getLong("id");
                friendshipRequest.setId(idFriendshipRequest);
                friendshipRequests.add(friendshipRequest);


            }
            return friendshipRequests;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendshipRequests;
    }

    /**
     *
     * @param curentPage Page
     * @param idUser Long, id of the user we want the friendship Request
     * @return return all FriendshipRequest to user with speciefied id
     */
    public Iterable<FriendshipRequest> findAll(Page curentPage, Long idUser){
        List<FriendshipRequest> friendshipRequestList = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url,username,password)) {

            String command = "SELECT * FROM \"friendshipRequests\" WHERE \"to\" = '"+idUser+
                    "' LIMIT "+curentPage.getSizePage() +" OFFSET "+(curentPage.getNumberPage()-1)*curentPage.getSizePage();

            PreparedStatement preparedStatement = connection.prepareStatement(command);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Long idFriendsfipRequest = resultSet.getLong("id");
                friendshipRequestList.add(findOne(idFriendsfipRequest));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return friendshipRequestList;
    }

    /**
     *
     *
     * @param status String, status of FriendshipRequest
     * @param curentPage Page
     * @param idUser Long, id of the user we want the friendship Request
     * @return return all FriendshipRequest to user with speciefied id
     */
    public Iterable<FriendshipRequest> findAll(Page curentPage, Long idUser,String status){
        List<FriendshipRequest> friendshipRequestList = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url,username,password)) {

            String command = "SELECT * FROM \"friendshipRequests\" WHERE \"to\" = '"+idUser +"' AND \"status\" = '"+status+
                    "' LIMIT "+curentPage.getSizePage() +" OFFSET "+(curentPage.getNumberPage()-1)*curentPage.getSizePage();

            PreparedStatement preparedStatement = connection.prepareStatement(command);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Long idFriendsfipRequest = resultSet.getLong("id");
                friendshipRequestList.add(findOne(idFriendsfipRequest));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return friendshipRequestList;
    }

    @Override
    public FriendshipRequest save(FriendshipRequest entity) {
        try(Connection connection = DriverManager.getConnection(url,username,password);
        ){

            String command = "INSERT INTO \"friendshipRequests\"(\"from\" , \"to\" , \"message\", \"date\" , \"status\")VALUES" +
                    "('"+entity.getFrom().getId()+"','"+ entity.getTo().get(0).getId()+ "','"+ entity.getMessage()+"','"
                            + entity.getDate().format(Constants.DATE_TIME_FORMATTER)+ "','"+entity.getStatus()+"')"+" RETURNING *";

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
    public FriendshipRequest delete(Long aLong) {
        try(Connection connection = DriverManager.getConnection(url,username,password);
        ){
            String command = "DELETE FROM \"friendshipRequests\" WHERE id = "+aLong+" RETURNING *";
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            ResultSet resultSet =preparedStatement.executeQuery();
            if(resultSet.next()){
                Long idFrom = resultSet.getLong("from");
                User userSender = userDbRepository.findOne(idFrom);

                Long idTo = resultSet.getLong("to");
                User userReceiver = userDbRepository.findOne(idTo);
                List<User> listUsersReceiver = Arrays.asList(userReceiver);

                String message = resultSet.getString("message");
                String data = resultSet.getString("date");
                String status = resultSet.getString("status");
                FriendshipRequest friendshipRequest = new FriendshipRequest(userSender,listUsersReceiver,message, LocalDateTime.parse(data, Constants.DATE_TIME_FORMATTER),status);
                friendshipRequest.setId(aLong);
                return friendshipRequest;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public FriendshipRequest update(FriendshipRequest entity) {
        return null;
    }
}
