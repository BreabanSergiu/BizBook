package socialnetwork.service;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.UserDTO;
import socialnetwork.repository.Repository;
import socialnetwork.service.validators.Validator;
import socialnetwork.service.validators.ValidatorUserService;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    private Repository<Long, User> repoUser;
    private  Repository<Tuple<Long,Long>, Friendship> repoFriendship;
    private Validator<User> validatorUserService = new ValidatorUserService<>();

    public UserService(Repository<Long, User> repoUser, Repository<Tuple<Long, Long>, Friendship> repoFriendship) {
        this.repoUser = repoUser;
        this.repoFriendship = repoFriendship;
    }

    /**
     * add a user
     * @param user
     *          user must be not null
     * @return  null - if the user is added
     *          otherwise return the user
     */
    public User addUser(User user) {
        user.getId();
        User user1 = repoUser.save(user);
        validatorUserService.validateAdd(user1);
        return user1;
    }


    /**
     *
     *delete a user
     * @param id
     *          id must be not null
     * @return the removed user or null if there is not a user with specified id
     */
    public User deleteUtilizator(Long id){

        User user = repoUser.delete(id);
        validatorUserService.validateDelete(user);
        user.getFriends().forEach((x)->{
            if(repoFriendship.findOne(new Tuple<>(x.getId(),id)) != null){

                repoFriendship.delete(new Tuple<>(x.getId(),id));
                repoUser.findOne(x.getId()).getFriends().removeIf((elem)-> elem.getId().equals(id));
            }

            if(repoFriendship.findOne(new Tuple<>(id,x.getId())) != null){
                repoFriendship.delete(new Tuple<>(id,x.getId()));
                repoUser.findOne(x.getId()).getFriends().removeIf(elem-> elem.getId().equals(id));
            }
                                    });


        return user;
    }

    /**
     *
     * @return an iterable<T></> object
     */
    public Iterable<User> getAll(){
        return repoUser.findAll();
    }

    public User getUser(Long id){
        return repoUser.findOne(id);
    }

    public List<UserDTO> getAllUserDTO() {
        List<UserDTO> userDTOsList = new ArrayList<>();
        getAll().forEach(user -> {
            UserDTO userDTO = new UserDTO(user.getFirstName(), user.getLastName());
            userDTO.setId(user.getId());
            userDTOsList.add(userDTO);
        });
        return userDTOsList;
    }

    public UserDTO getUserDTO(Long userID) {
        User user = getUser(userID);
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO(user.getFirstName(), user.getLastName());
        userDTO.setId(userID);
        return userDTO;
    }

    ///TO DO: add other methods
}
