package socialnetwork.domain.validators;

import socialnetwork.domain.message.ReplyMessage;

public class ReplyMessageValidator implements Validator<ReplyMessage>{
    @Override
    public void validate(ReplyMessage entity) throws ValidationException {
        String err = "";
        if(entity.getMessage().matches("[ ]*")){
            err += "the message can t cantains only black spaces";
        }

        if(err.length() > 0 )
        {
            throw new ValidationException(err);
        }
    }
}
