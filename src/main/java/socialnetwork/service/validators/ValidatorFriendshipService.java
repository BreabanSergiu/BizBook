package socialnetwork.service.validators;

import socialnetwork.domain.validators.ValidationException;

public class ValidatorFriendshipService<T> implements Validator<T> {


    @Override
    public void validateAdd(T entity) throws ValidationException {
        if(entity != null){
            throw new ValidationException("The friendship already exists!\n");
        }

    }

    @Override
    public void validateDelete(T entity) throws ValidationException {
            if(entity == null){
                throw new ValidationException("The friendship to be deleted doesn t exist!\n");
            }
    }


}
