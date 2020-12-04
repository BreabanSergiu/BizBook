package socialnetwork.service;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.message.FriendshipRequest;
import socialnetwork.repository.Repository;
import socialnetwork.service.validators.Validator;
import socialnetwork.service.validators.ValidatorFriendshipRequestService;
import socialnetwork.service.validators.ValidatorUserService;
import socialnetwork.utils.events.ChangeEventType;
import socialnetwork.utils.events.FriendshipChangeEvent;
import socialnetwork.utils.events.FriendshipRequestChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.LongFunction;

public class FriendshipRequestService implements Observable<FriendshipRequestChangeEvent> {

    private Repository<Long, FriendshipRequest> requestRepository ;
    private ValidatorFriendshipRequestService friendshipRequestValidator = new ValidatorFriendshipRequestService();
    private Repository<Tuple<Long,Long>,Friendship> friendshipRepository;
    List<Observer<FriendshipRequestChangeEvent>> observers = new ArrayList<>();

    /**
     *
     * @param requestRepository Repository<T></>
     */
    public FriendshipRequestService(Repository<Long, FriendshipRequest> requestRepository,
                                    Repository<Tuple<Long,Long>,Friendship> friendshipRepository) {
        this.requestRepository = requestRepository;
        this.friendshipRequestValidator = friendshipRequestValidator;
        this.friendshipRepository = friendshipRepository;
    }

    /**
     *
     * @param friendshipRequest , a friendship to be saved
     * @return null, if the given friendship is saved
     *          otherwise return the friendship
     */
    public FriendshipRequest addRequest(FriendshipRequest friendshipRequest){

        friendshipRequestValidator.validateBeforeAdding(friendshipRequest,getAll(),friendshipRepository.findAll());
        FriendshipRequest friendshipRequest1 =  requestRepository.save(friendshipRequest);

        friendshipRequestValidator.validateAdd(friendshipRequest1);


        return friendshipRequest1;

    }

    /**
     *
     * @param id representing id of the friendship request to be deleted
     * @return the removed friendship request or null if there is no the friendship request with the given id
     */
    public FriendshipRequest deleteRequest(Long id){
        FriendshipRequest friendshipRequest =  requestRepository.delete(id);
        if(friendshipRequest != null){
            notifyObserver(new FriendshipRequestChangeEvent(ChangeEventType.DELETE,friendshipRequest));
        }
       // friendshipRequestValidator.validateDelete(friendshipRequest);
        return friendshipRequest;

    }

    /**
     *
     *
     * @param id Id of the friendship request
     * @return list of friendship requests that are pending
     */
    public Iterable<FriendshipRequest> getAllPendingRequest(Long id){
        Iterable<FriendshipRequest> listRequest = requestRepository.findAll();

        List<FriendshipRequest> listPendingRequest = new ArrayList<>();
        listRequest.forEach(request->{
            if(request.getStatus().equals("pending") && request.getTo().get(0).getId().equals(id)){
                listPendingRequest.add(request);
            }
        });

        return listPendingRequest;
    }

    /**
     *
     * @param id -id of user
     * @return list of friendship requests
     */
    public List<FriendshipRequest> getAllRequest(Long id){
        Iterable<FriendshipRequest> listRequest = requestRepository.findAll();

        List<FriendshipRequest> listPendingRequest = new ArrayList<>();
        listRequest.forEach(request->{
            if(request.getTo().get(0).getId().equals(id)){
                listPendingRequest.add(request);
            }
        });

        return listPendingRequest;
    }
    public FriendshipRequest getFriendshipRequest(Long id){
        return requestRepository.findOne(id);
    }

    /**
     *
     * @return all friendship
     */
    public Iterable<FriendshipRequest> getAll(){
        return requestRepository.findAll();
    }

    public void updateFriendshipRequest(FriendshipRequest friendshipRequest, String status){

        FriendshipRequest friendshipRequestDeleted = deleteRequest(friendshipRequest.getId());
        friendshipRequest.setStatus(status);
        friendshipRequest.setDate(LocalDateTime.now());
        FriendshipRequest friendshipRequest1 = addRequest(friendshipRequestDeleted);

        if(friendshipRequest1 == null){
            notifyObserver(new FriendshipRequestChangeEvent(ChangeEventType.UPDATE,friendshipRequest));
        }


    }

    @Override
    public void addObserver(Observer<FriendshipRequestChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<FriendshipRequestChangeEvent> e) {
        //observers.remove(e);
    }

    @Override
    public void notifyObserver(FriendshipRequestChangeEvent friendshipChangeEvent) {
        observers.stream().forEach(observer -> observer.update(friendshipChangeEvent));
    }

    /**
     *
     * @param id id of user
     * @return list of friendship send from user with id specified, in panding status
     */
    public List<FriendshipRequest> getAllRequestTo(Long id) {

        Iterable<FriendshipRequest> listRequest = requestRepository.findAll();

        List<FriendshipRequest> listPendingRequest = new ArrayList<>();
        listRequest.forEach(request->{
            if(request.getFrom().getId().equals(id) && request.getStatus().equals("pending")){
                listPendingRequest.add(request);
            }
        });

        return listPendingRequest;

    }
}

