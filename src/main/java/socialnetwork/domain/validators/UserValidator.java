package socialnetwork.domain.validators;

import socialnetwork.domain.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        //TODO: implement method validate
        String errors = "";
        if(entity.getFirstName().matches(".*\\d.*") ){
            errors += "First name contains numbers! \n";
        }
        if(entity.getLastName().matches(".*\\d.*")){
            errors += "Last name contains numbers! \n";
        }
        if(entity.getLastName().equals("") ){
            errors += "First name is null!\n";
        }
        if(entity.getFirstName().equals("")){
            errors += "Last name is null!\n";
        }
        if(entity.getFirstName().length() < 3){
            errors += "First name contains less than 3 letters\n";
        }
        if(entity.getLastName().length() < 3){
            errors += "Last name contains less than 3 letters\n";
        }
            if(errors.length() > 0)
                throw new ValidationException(errors);

    }
}
