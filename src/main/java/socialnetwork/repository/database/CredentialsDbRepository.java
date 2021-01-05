package socialnetwork.repository.database;

import org.postgresql.util.PSQLException;
import socialnetwork.domain.Credential;
import socialnetwork.domain.Entity;
import socialnetwork.domain.User;
import socialnetwork.repository.Repository;

import java.sql.*;


public class CredentialsDbRepository implements Repository<Long, Credential> {

    private String url;
    private String username;
    private String password;

    public CredentialsDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }


    @Override
    public Credential findOne(Long aLong) {
        return null;
    }

    public Credential findOne(String userName){
        try(Connection connection = DriverManager.getConnection(url,username,password)){//aici creem conexiunea cu url
            String command = "SELECT *" +
                    " From credentials" +
                    " WHERE username = '" +userName+"'";
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Long id = resultSet.getLong("id");
                String password = resultSet.getString("password");
                Credential credential = new Credential(userName,password);
                credential.setId(id);
                return credential;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    @Override
    public Iterable<Credential> findAll() {
        return null;
    }

    @Override
    public Credential save(Credential entity) {
        try(Connection connection = DriverManager.getConnection(url,username,password);
        ){
            String command = "INSERT INTO credentials(\"id\" , \"username\",\"password\")VALUES" +
                    "('"+entity.getId()+"','"+entity.getUserName()+"','"+entity.getPassword()+"')"+" RETURNING *";

            PreparedStatement preparedStatement = connection.prepareStatement(command);
            try{
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    return entity;
                }

            }catch(PSQLException e){
                return null;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    @Override
    public Credential delete(Long aLong) {
        return null;
    }

    @Override
    public Credential update(Credential entity) {
        return null;
    }
}
