package socialnetwork.repository.database;

import org.postgresql.util.PSQLException;
import socialnetwork.domain.Event;
import socialnetwork.domain.Page;
import socialnetwork.domain.Photo;
import socialnetwork.domain.User;
import socialnetwork.domain.message.Message;
import socialnetwork.repository.Repository;
import socialnetwork.utils.Constants;

import javax.swing.text.html.ListView;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EventDbRepository implements Repository<Long, Event> {

    private String url;
    private String username;
    private String password;
    private Repository<Long, User> userDbRepository;

    public EventDbRepository(String url, String username, String password, Repository<Long, User> userDbRepository) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.userDbRepository = userDbRepository;
    }

    @Override
    public Event findOne(Long aLong) {
        try(Connection connection = DriverManager.getConnection(url,username,password)){//aici creem conexiunea cu url
            String command = "SELECT *" +
                    "From events " +
                    " WHERE id = " +aLong;
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){

                LocalDate date  = LocalDate.parse(resultSet.getString("date"));
                String name = resultSet.getString("name");
                Photo photo = new Photo(resultSet.getString("photoURL"));
                photo.setId(1l);




                String listParticipants = resultSet.getString("participants");
                String[] parts = listParticipants.split(",");
                List<User> listUsersParticipants = new ArrayList<>();
                for(String p : parts){
                    Long id = Long.parseLong(p);
                    User user = userDbRepository.findOne(id);
                    listUsersParticipants.add(user);
                }

                String description = resultSet.getString("description");
                String notification = resultSet.getString("notification");
                Event event = new Event(date,name,description,photo,listUsersParticipants,Boolean.parseBoolean(notification));
                event.setId(aLong);
                return event;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Event> findAll() {
        Set<Event> events = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url,username,password)){//aici creem conexiunea cu url
            String command = "SELECT *" +
                    "From events" ;
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){

                LocalDate date  = LocalDate.parse(resultSet.getString("date"));
                String name = resultSet.getString("name");
                Photo photo = new Photo(resultSet.getString("photoURL"));
                photo.setId(1l);




                String listParticipants = resultSet.getString("participants");
                String[] parts = listParticipants.split(",");
                List<User> listUsersParticipants = new ArrayList<>();
                for(String p : parts){
                    Long id = Long.parseLong(p);
                    User user = userDbRepository.findOne(id);
                    listUsersParticipants.add(user);
                }

                String description = resultSet.getString("description");
                String notification = resultSet.getString("notification");
                Event event = new Event(date,name,description,photo,listUsersParticipants,Boolean.parseBoolean(notification));
                Long idEvent = resultSet.getLong("id");
                event.setId(idEvent);
                events.add(event);

            }
            return events;


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Method that gets the list of all events on a specific Page
     * @param curentPage Page, representing the Page containing the events
     * @return Iterable<Event></>, representing the list of Events on that Page
     */
    public Iterable<Event> findAll(Page curentPage){
        List<Event> eventList = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url,username,password)) {
            String command = "SELECT * FROM events LIMIT "+ curentPage.getSizePage()+
                    " OFFSET " + (curentPage.getNumberPage()-1);

            PreparedStatement preparedStatement = connection.prepareStatement(command);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Long idEvent = resultSet.getLong("id");
                eventList.add(findOne(idEvent));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return eventList;
    }


    @Override
    public Event save(Event entity) {
        try(Connection connection = DriverManager.getConnection(url,username,password);
        ){
            List<User> participants = entity.getParticipants();
            String parts = "";
            for(User user :participants){
                parts = parts + user.getId() +",";
            }
            parts = parts.substring(0,parts.length()-1);


            String command = "INSERT INTO events(\"date\" , \"name\" , \"photoURL\", \"participants\",\"description\",\"notification\")VALUES" +
                    "('"+
                    entity.getDate().toString()+"','"+
                    entity.getName()+"','"+
                    entity.getPhoto().getURL()+"','"+
                    parts+"','"+
                    entity.getDescription()+"','"+
                    entity.isNotification()+
                    "')"+ " RETURNING *";

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
        return entity;    }

    @Override
    public Event delete(Long aLong) {
        return null;
    }

    @Override
    public Event update(Event entity) {
        try(Connection connection = DriverManager.getConnection(url,username,password);
        ){
            String participants = "";
            List<User > part = entity.getParticipants();
            for(User user:part){
                participants = participants + user.getId()+",";
            }
            participants = participants.substring(0,participants.length()-1);

            String command = "UPDATE events SET" +
                    " date = '"+entity.getDate() +
                    "' , name = '" +entity.getName()+
                    "' , \"photoURL\" = '" +entity.getPhoto().getURL()+
                    "' , participants = '"+participants+
                    "' , description = '"+entity.getDescription()+
                    "' , notification = '"+entity.isNotification()+
                    "' WHERE id = '" +
                    entity.getId() + "' RETURNING *";

            PreparedStatement preparedStatement =  connection.prepareStatement(command);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                LocalDate date  = LocalDate.parse(resultSet.getString("date"));
                String name = resultSet.getString("name");
                Photo photo = new Photo(resultSet.getString("photoURL"));
                photo.setId(1l);




                String listParticipants = resultSet.getString("participants");
                String[] parts = listParticipants.split(",");
                List<User> listUsersParticipants = new ArrayList<>();
                for(String p : parts){
                    Long id = Long.parseLong(p);
                    User user = userDbRepository.findOne(id);
                    listUsersParticipants.add(user);
                }

                String description = resultSet.getString("description");
                String notification = resultSet.getString("notification");
                Event event = new Event(date,name,description,photo,listUsersParticipants,Boolean.parseBoolean(notification));
                Long idEvent = resultSet.getLong("id");
                event.setId(idEvent);

                return null;
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return entity;

    }
}
