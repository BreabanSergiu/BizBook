package socialnetwork.repository.database;

import org.postgresql.util.PSQLException;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDbRepository implements Repository<Long, User> {

    private String url;
    private String username;
    private String password;
    private Validator<User> validator;

    public UserDbRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public User findOne(Long aLong) {
        try(Connection connection = DriverManager.getConnection(url,username,password)){//aici creem conexiunea cu url
            String command = "SELECT *" +
                    "From users" +
                    " WHERE id = " +aLong;
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Long idUser = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                User user = new User(firstName,lastName);
                user.setId(idUser);
                return user;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                User user = new User(firstName, lastName);
                user.setId(id);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    @Override
    public User save(User entity) {
       try(Connection connection = DriverManager.getConnection(url,username,password);
       ){
           String command = "INSERT INTO users(\"first_name\" , \"last_name\")VALUES" +
                   "('"+entity.getFirstName()+"','"+entity.getLastName()+"')"+" RETURNING *";

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
    public User delete(Long aLong) {
        try(Connection connection = DriverManager.getConnection(url,username,password);
        ){
            String command = "DELETE FROM users WHERE id = "+aLong+" RETURNING *";
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            ResultSet resultSet =preparedStatement.executeQuery();
            if(resultSet.next()){
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                User user = new User(firstName,lastName);
                user.setId(aLong);
                return user;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public User update(User entity) {
        try(Connection connection = DriverManager.getConnection(url,username,password);
        ){
            String command = "UPDATE users SET" +
                    " \"first_name\" = '"+entity.getFirstName() + "' , \"last_name\" = '" +entity.getLastName()+"' WHERE id = '" +
                    entity.getId() + "' RETURNING *";

            PreparedStatement preparedStatement =  connection.prepareStatement(command);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                User user = new User(firstName,lastName);
                user.setId(entity.getId());

                return null;
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return entity;
    }
}
