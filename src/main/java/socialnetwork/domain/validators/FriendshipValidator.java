package socialnetwork.domain.validators;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.User;


import java.util.concurrent.atomic.AtomicBoolean;

public class FriendshipValidator implements Validator<Friendship>{
    private Iterable<User> listUsers;
    public FriendshipValidator(Iterable<User> utilizators) {
        listUsers = utilizators;
    }


    @Override
    public void validate(Friendship entity) throws ValidationException {
        String err = "";
        if(entity.getId().getLeft() == entity.getId().getRight())
            err += "Id are the same \n";

        AtomicBoolean leftIdExist = new AtomicBoolean(false);
        AtomicBoolean rightIdExist = new AtomicBoolean(false);

        listUsers.forEach(utilizator -> {
            if(utilizator.getId() == entity.getId().getLeft())
                leftIdExist.set(true);
            if(utilizator.getId() == entity.getId().getRight())
                rightIdExist.set(true);

        });

        if(!leftIdExist.get()){
            err += "id Left doesn t exist! \n";
        }
        if(!rightIdExist.get())
        {
            err += "id right doesn t exist! \n";
        }

        if(err.length() > 0)
            throw new ValidationException(err);
    }
}
