package socialnetwork.repository.database;

import org.postgresql.util.PSQLException;
import socialnetwork.domain.Photo;
import socialnetwork.domain.User;
import socialnetwork.repository.Repository;

import java.sql.*;

public class PhotoDbRepository implements Repository<Long, Photo> {

    private String url;
    private String username;
    private String password;

    public PhotoDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Photo findOne(Long aLong) {
        try(Connection connection = DriverManager.getConnection(url,username,password)){
            String command = "SELECT *" +
                    " FROM photos" +
                    " WHERE \"idUser\" = "+aLong;
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                String Url = resultSet.getString("URL");
                Photo photo = new Photo(Url);
                return photo;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Photo> findAll() {
        return null;
    }

    @Override
    public Photo save(Photo entity) {
        try(Connection connection = DriverManager.getConnection(url,username,password)){
            String command = "INSERT INTO photos(\"idUser\" , \"url\")VALUES" +
                    "('"+entity.getId()+"','"+entity.getURL()+"')"+" RETURNING *";

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
    public Photo delete(Long aLong) {
        return null;
    }

    @Override
    public Photo update(Photo entity) {
        try(Connection connection = DriverManager.getConnection(url,username,password)){
            String command = "UPDATE photos SET" +
                    " \"idUser\" = '"+entity.getId()+ "' , \"url\" = '" +entity.getURL()+"' WHERE \"idUser\" = '" +
                    entity.getId() + "' RETURNING *";

            PreparedStatement preparedStatement =  connection.prepareStatement(command);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                String url = resultSet.getString("url");

                Photo photo = new Photo(url);
                photo.setId(entity.getId());

                return null;
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return entity;
    }

}
