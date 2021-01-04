package socialnetwork.service;

import socialnetwork.domain.Event;
import socialnetwork.domain.Page;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.EventDbRepository;

public class EventService {
    private EventDbRepository eventRepository;

    public EventService(EventDbRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    /**
     *
     * @param id id of event
     * @return the event with specified id
     */
    public Event getEvent(Long id){
       return  eventRepository.findOne(id);
    }

    /**
     *
     * @return all Events
     */
    public Iterable<Event> getAllEvents(){
        return eventRepository.findAll();
    }


    /**
     *
     * @param currentPage , Page
     * @return all events from specified Page
     */
    public Iterable<Event> getAllEvents(Page currentPage){
        return eventRepository.findAll(currentPage);
    }

    /**
     *
     * @param event the event to be updated
     * @return null-> if the event is updated
     *         otherwise returns the entity
      */
    public Event updateEvent(Event event){
        return eventRepository.update(event);
    }

    /**
     *
     * @param event the event to be saved
     * @return null- if the given entity is saved
     *      *         otherwise returns the entity (id already exists)
     */
    public Event saveEvent(Event event){
        return eventRepository.save(event);
    }
}
