package socialnetwork.repository.database;

import org.postgresql.util.PSQLException;
import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class FriendshipDbRepository implements Repository<Tuple<Long,Long>, Friendship> {
    private String url;
    private String username;
    private String password;


    public FriendshipDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;

    }

    @Override
    public Friendship findOne(Tuple<Long, Long> longLongTuple) {
        try(Connection connection = DriverManager.getConnection(url,username,password)){//aici creem conexiunea cu url
            String command = "SELECT * " +
                    "From friendships" +
                    " WHERE \"idUser1\"= '" +longLongTuple.getLeft() +"' AND " +"\"idUser2\" = '"  + longLongTuple.getRight()+"'" ;
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                    Long id1 = Long.parseLong(resultSet.getString("idUser1"));
                    Long id2 = Long.parseLong(resultSet.getString("idUser2"));
                    String data = resultSet.getString("data");
                    Friendship friendship = new Friendship(LocalDate.parse(data));
                    friendship.setId(new Tuple<>(id1,id2));
                    return friendship;
            }



        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                LocalDate date = LocalDate.parse(resultSet.getString("data"));
                Long id1 = Long.parseLong(resultSet.getString("idUser1"));
                Long id2 = Long.parseLong(resultSet.getString("idUser2"));
                Friendship friendship = new Friendship(date);
                friendship.setId(new Tuple<>(id1,id2));
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Friendship save(Friendship entity) {
        try(Connection connection = DriverManager.getConnection(url,username,password);
        ){
            String command = "INSERT INTO friendships(\"data\" , \"idUser1\" , \"idUser2\")VALUES" +
                    "('"+entity.getDate()+"','"+entity.getId().getLeft()+ "','"+ entity.getId().getRight()+"')"+" RETURNING *";

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
    public Friendship delete(Tuple<Long, Long> longLongTuple) {
        try(Connection connection = DriverManager.getConnection(url,username,password);
        ){
            String command = "DELETE FROM friendships WHERE \"idUser1\" = "+longLongTuple.getLeft()+
                    " AND \"idUser2\" = " +longLongTuple.getRight()+" RETURNING *";
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            ResultSet resultSet =preparedStatement.executeQuery();
            if(resultSet.next()){
                LocalDate date = LocalDate.parse(resultSet.getString("data"));
                Long id1 = Long.parseLong(resultSet.getString("idUser1"));
                Long id2 = Long.parseLong(resultSet.getString("idUser2"));
                Friendship friendship = new Friendship(date);
                friendship.setId(new Tuple<>(id1,id2));
                return friendship;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship update(Friendship entity) {
        return null;
    }
}
