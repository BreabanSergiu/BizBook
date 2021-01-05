package socialnetwork.service;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Page;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.message.Message;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.FriendshipDbRepository;
import socialnetwork.service.validators.Validator;
import socialnetwork.service.validators.ValidatorFriendshipService;
import socialnetwork.utils.events.ChangeEventType;
import socialnetwork.utils.events.FriendshipChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FriendshipService implements Observable<FriendshipChangeEvent> {

    private FriendshipDbRepository repositoryFriendship;
    private Repository<Long, User> repositoryUsers;
    private Validator<Friendship> validatorFriendshipService = new ValidatorFriendshipService<>();
    private  List<Observer<FriendshipChangeEvent>> observers = new ArrayList<>();


    public FriendshipService(FriendshipDbRepository repositoryFriendship, Repository<Long, User> repositoryUsers) {
        this.repositoryFriendship = repositoryFriendship;
        this.repositoryUsers = repositoryUsers;
    }

    /**
     * add a friendship
     * @param friendship
     *          friendship must be not null
     * @return  null - if the given friendship is saved
     *          otherwise return the friendship
     * @throws ValidationException
     */
    public Friendship addFriendship(Friendship friendship)throws ValidationException{
        Friendship friendship1 = repositoryFriendship.save(friendship);
        validatorFriendshipService.validateAdd(friendship1);
        User userLeft = repositoryUsers.findOne(friendship.getId().getLeft()); //check the user with given id
        User userRight = repositoryUsers.findOne(friendship.getId().getRight());
        userLeft.getFriends().add(userRight);//add userRight in list of UserLeft
        userRight.getFriends().add(userLeft);

            if(friendship1 == null){
                notifyObserver(new FriendshipChangeEvent(ChangeEventType.ADD,friendship));
            }
        return friendship1;
    }

    /**
     *  removes the friendship with the specified id
     * @param id
     *        id must be not null
     * @return the removed entity with specified id or null if there is no entity with the given id
     * @throws ValidationException
     */
    public Friendship deleteFriendship(Tuple<Long,Long> id)throws ValidationException {

        Friendship friendship = repositoryFriendship.delete(id);
        validatorFriendshipService.validateDelete(friendship);
        if(friendship != null){
            notifyObserver(new FriendshipChangeEvent(ChangeEventType.DELETE,friendship));
        }
        return friendship;
    }

    /**
     *
     * @return an interable<T></> object
     */

    public Iterable<Friendship>  getAll(){
        return repositoryFriendship.findAll();
    }


    /**
     * method that return all friends of the user with specified id
     * @param userID
     * @return all friends of the user with specified id
     */
    public Iterable<Friendship> getAllFriendshipsUser(Long userID) {
        Iterable<Friendship> allFriendships = this.getAll();
        List<Friendship> listFriendshipsUser = new ArrayList<>();
        allFriendships.forEach(friendship -> {
            if (friendship.getId().getLeft().equals(userID) || friendship.getId().getRight().equals(userID))
                listFriendshipsUser.add(friendship);
        });
        return listFriendshipsUser;
    }

    /**
     * method that return all friends of the user with specified id from Page
     * @param idUser Long
     * @param currentPage Page
     * @return all friends of the user with specified id from Page
     */
    public Iterable<Friendship> getAllFriendshipsUser(Long idUser, Page currentPage){
        return  repositoryFriendship.findAll(currentPage,idUser);
    }

    /**
     *
     * @param idLeft
     * @param idRight
     * @return an friendship
     */
    public Friendship getOne(Long idLeft, Long idRight){
        return repositoryFriendship.findOne(new Tuple<>(idLeft,idRight));
    }

    @Override
    public void addObserver(Observer<FriendshipChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<FriendshipChangeEvent> e) {
       // observers.remove(e);
    }

    @Override
    public void notifyObserver(FriendshipChangeEvent friendshipChangeEvent) {
        observers.stream().forEach(observer->observer.update(friendshipChangeEvent));
    }


    /**
     * method that return all user friendships created between given date
     * @param id  Long, id of user
     * @param startDate LocalDate
     * @param endDate  LocalDate
     * @return all usernfriendships created between given date
     */
    public List<Friendship> getFriendsBetweenDate(Long id, LocalDate startDate,LocalDate endDate){

        Iterable<Friendship> friendships = getAllFriendshipsUser(id);
        List<Friendship> friendshipList = new ArrayList<>();
        friendships.forEach(friendship -> {
            if(friendship.getDate().compareTo(startDate)>=0 && friendship.getDate().compareTo(endDate)<=0){
                        friendshipList.add(friendship);
            }

        });
        return friendshipList;
    }


}
