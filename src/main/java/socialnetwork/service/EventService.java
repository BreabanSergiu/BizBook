package socialnetwork.service;

import socialnetwork.domain.Event;
import socialnetwork.repository.Repository;

public class EventService {
    private Repository<Long, Event> eventRepository;

    public EventService(Repository<Long, Event> eventRepository) {
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

    public Iterable<Event> getAllEvents(){
        return eventRepository.findAll();
    }
}
