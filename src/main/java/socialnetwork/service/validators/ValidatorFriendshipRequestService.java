package socialnetwork.service.validators;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.message.FriendshipRequest;
import socialnetwork.domain.validators.ValidationException;

public class ValidatorFriendshipRequestService implements Validator<FriendshipRequest> {
    @Override
    public void validateAdd(FriendshipRequest entity) throws ValidationException {
        if(entity != null){
            throw new ValidationException("The friendship request already exists!\n");
        }

    }

    @Override
    public void validateDelete(FriendshipRequest entity) throws ValidationException {
        if(entity == null){
            throw new ValidationException("The friendship request doesn t exist!\n");
        }
    }

    /**
     * Method that checks if a Friendship Request already exists
     * @param friendshipRequestToBeChecked FriendshipRequest, representing the Friendship request to be checked
     * @param friendshipRequestList Iterable<FriendshipRequest>, representing the list of friendship requests
     * @throws ValidationException, if the Friendship Request already exists (the sender and the receiver ar the same)
     */
    private void validateFriendshipRequestAlreadyExists(FriendshipRequest friendshipRequestToBeChecked,
                                                        Iterable<socialnetwork.domain.message.FriendshipRequest> friendshipRequestList) throws ValidationException{
        friendshipRequestList.forEach(friendshipRequest -> {
            if (friendshipRequest.getFrom().getId().equals(friendshipRequestToBeChecked.getFrom().getId()) &&
                    friendshipRequest.getTo().get(0).getId().equals(friendshipRequestToBeChecked.getTo().get(0).getId())) {
                throw new ValidationException("The friendship request already exists!");
            }
        });
    }

    /**
     * Method that checks if a Friendship Request can't be sent because that Friendship already exists
     * @param friendshipRequestToBeChecked FriendshipRequest, representing the Friendship quest to be checked
     * @param friendshipList Iterable<Friendship>, representing the list of friendships
     * @throws ValidationException, if the Friendship already exists
     */
    private void validateFriendshipAlreadyExists(FriendshipRequest friendshipRequestToBeChecked,
                                                 Iterable<Friendship> friendshipList) throws ValidationException {
        if (friendshipRequestToBeChecked.getStatus().equals("pending")) {
            friendshipList.forEach(friendship -> {
                if (friendship.getId().getLeft().equals(friendshipRequestToBeChecked.getFrom().getId()) &&
                        friendship.getId().getRight().equals(friendshipRequestToBeChecked.getTo().get(0).getId()))
                    throw new ValidationException("The friendship already exists!");
                if (friendship.getId().getLeft().equals(friendshipRequestToBeChecked.getTo().get(0).getId()) &&
                        friendship.getId().getRight().equals(friendshipRequestToBeChecked.getFrom().getId()))
                    throw new ValidationException("The friendship already exists!");
            });
        };
    }

    /**
     * Method that checks if a Friendship Request is valid before it is added
     * @param friendshipRequestToBeChecked FriendshipRequest, representing the Friendship quest to be checked
     * @param friendshipRequestsList Iterable<FriendshipRequest>, representing the list of friendship requests
     * @param friendshipsList Iterable<Friendship>, representing the list of friendships
     * @throws ValidationException
     */
    public void validateBeforeAdding(FriendshipRequest friendshipRequestToBeChecked,
                                     Iterable<socialnetwork.domain.message.FriendshipRequest> friendshipRequestsList, Iterable<Friendship> friendshipsList) throws ValidationException{
        validateFriendshipRequestAlreadyExists(friendshipRequestToBeChecked, friendshipRequestsList);
        validateFriendshipAlreadyExists(friendshipRequestToBeChecked, friendshipsList);
    }
}
