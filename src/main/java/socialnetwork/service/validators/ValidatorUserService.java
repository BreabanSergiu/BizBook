package socialnetwork.service.validators;

import socialnetwork.domain.validators.ValidationException;

public class ValidatorUserService<T> implements Validator<T>{

    @Override
    public void validateAdd(T entity) throws ValidationException {
        if(entity != null){
            throw new ValidationException("The user already exists!\n");
        }
    }

    @Override
    public void validateDelete(T entity) throws ValidationException {
        if(entity == null){
            throw new ValidationException("The user to be deleted doesn t exist!\n");
        }
    }
}
