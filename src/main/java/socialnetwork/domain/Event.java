package socialnetwork.domain;


import java.time.LocalDate;
import java.util.List;

public class Event extends Entity<Long> {

    private LocalDate date;
    private String name;
    private String description;
    private Photo photo;
    private List<User> participants;

    public Event(LocalDate date, String nume, String descriere, Photo photo, List<User> participants) {
        this.date = date;
        this.name = nume;
        this.description = descriere;
        this.photo = photo;
        this.participants = participants;
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
