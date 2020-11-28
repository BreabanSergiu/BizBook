package socialnetwork.repository.file;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;

import java.time.LocalDate;
import java.util.List;

public class FriendshipFile extends AbstractFileRepository<Tuple<Long,Long>, Friendship>{

    private Repository<Long, User> userRepository;

    /**
     *  Constructor that creates a new friendship
     * @param fileName String, representing the file name where data is loaded and written
     * @param validator Validator, validates the friedship request
     * @param userRepository Repositori<T></>
     */
    public FriendshipFile(String fileName, Validator<Friendship> validator, Repository<Long, User> userRepository) {
        super(fileName, validator);
        this.userRepository = userRepository;
        createListsFriends();
    }


    @Override
    public Friendship extractEntity(List<String> attributes) {
        Friendship friendship = new Friendship(LocalDate.parse(attributes.get(0)));
        Long leftId = Long.parseLong(attributes.get(1));
        Long rightId = Long.parseLong(attributes.get(2));
        friendship.setId(new Tuple<Long, Long>(leftId,rightId));
        return friendship;

    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getDate() + ";"+entity.getId().getLeft() + ";" + entity.getId().getRight();
    }

    public void createListsFriends(){
        entities.forEach((id,elem)->
        {
            User userLeft = userRepository.findOne(id.getLeft());
            User userRight = userRepository.findOne(id.getRight());
            userLeft.getFriends().add(userRight);
            userRight.getFriends().add(userLeft);

        });
    }
}
