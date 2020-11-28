package socialnetwork.domain.validators;


import socialnetwork.domain.message.FriendshipRequest;

public class FriendshipRequestValidator implements Validator<FriendshipRequest> {
    @Override
    public void validate(FriendshipRequest entity) throws ValidationException {
        String err = "";

        if(!entity.getStatus().equals("rejected") && !entity.getStatus().equals("pending") && !entity.getStatus().equals("approved")){
            err += " status must be rejected or pending or approved!\n";
        }

        if(err.length() > 0){
            throw new ValidationException(err);
        }
    }
}
