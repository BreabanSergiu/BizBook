package socialnetwork.utils.events;


import socialnetwork.domain.message.FriendshipRequest;

public class FriendshipRequestChangeEvent implements Event {

    private ChangeEventType eventType;
    private FriendshipRequest oldFriendshipRequest,newFriendshipRequest;

    public FriendshipRequestChangeEvent(ChangeEventType eventType, FriendshipRequest oldFriendshipRequest) {
        this.eventType = eventType;
        this.oldFriendshipRequest = oldFriendshipRequest;
    }

    public FriendshipRequestChangeEvent(ChangeEventType eventType, FriendshipRequest oldFriendshipRequest, FriendshipRequest newFriendshipRequest) {
        this.eventType = eventType;
        this.oldFriendshipRequest = oldFriendshipRequest;
        this.newFriendshipRequest = newFriendshipRequest;
    }

    public ChangeEventType getEventType() {
        return eventType;
    }

    public FriendshipRequest getOldFriendshipRequest() {
        return oldFriendshipRequest;
    }

    public FriendshipRequest getNewFriendshipRequest() {
        return newFriendshipRequest;
    }
}
