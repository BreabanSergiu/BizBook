package socialnetwork.repository.database;

import socialnetwork.domain.Event;
import socialnetwork.domain.Page;
import socialnetwork.domain.Photo;
import socialnetwork.domain.User;
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

                Event event = new Event(date,name,description,photo,listUsersParticipants);
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

                Event event = new Event(date,name,description,photo,listUsersParticipants);
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
        return null;
    }


    @Override
    public Event save(Event entity) {
        return null;
    }

    @Override
    public Event delete(Long aLong) {
        return null;
    }

    @Override
    public Event update(Event entity) {
        return null;
    }
}
