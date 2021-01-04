package socialnetwork.domain;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Event extends Entity<Long> {

    private LocalDate date;
    private String name;
    private String description;
    private Photo photo;
    private List<User> participants;
    private boolean notification;

    public Event(LocalDate date, String name, String description, Photo photo, List<User> participants, boolean notification) {
        this.date = date;
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.participants = participants;
        this.notification = notification;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public String getNotificationString(){
        return getName() +" event take place in "+ ChronoUnit.DAYS.between( LocalDate.now(),getDate()) +" days";
    }
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Photo getPhoto() {
        return photo;
    }


    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }



    @Override
    public String toString() {
        return "Event{" +
                "date=" + date +
                ", nume='" + name + '\'' +
                ", descriere='" + description + '\'' +
                ", photo=" + photo +
                ", participants=" + participants +
                '}';
    }
}
