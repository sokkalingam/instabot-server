package instagram.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserData {

    private String username;
    private int numberOfPosts;
    private List<String> hashtags;

    private int postCounter;
    private int rightArrowNotFoundCounter;
    private int newPostNotFoundCounter;
    private int likedBlockedCounter;
    private int commentBlockedCounter;
    private boolean isLikingBlocked;
    private boolean isCommentingBlocked;
    private Set<String> alreadyVisitedSet;

    public UserData() {
        hashtags = new ArrayList<>();
        alreadyVisitedSet = new HashSet<>();
    }

    public UserData(int numberOfPosts, List<String> hashtags) {
        this();
        this.numberOfPosts = numberOfPosts;
        this.hashtags = hashtags;
    }

    public UserData(Data data) {
        this();
        this.username = data.username;
        this.numberOfPosts = data.noOfPhotos;
        this.hashtags = data.hashtags;
    }

    public int getNumberOfPosts() {
        return numberOfPosts;
    }

    public void setNumberOfPosts(int numberOfPosts) {
        this.numberOfPosts = numberOfPosts;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public int getPostCounter() {
        return postCounter;
    }

    public void setPostCounter(int postCounter) {
        this.postCounter = postCounter;
    }

    public void incrementPostCounter() {
        this.postCounter++;
    }

    public int getRightArrowNotFoundCounter() {
        return rightArrowNotFoundCounter;
    }

    public void setRightArrowNotFoundCounter(int rightArrowNotFoundCounter) {
        this.rightArrowNotFoundCounter = rightArrowNotFoundCounter;
    }

    public int getNewPostNotFoundCounter() {
        return newPostNotFoundCounter;
    }

    public void setNewPostNotFoundCounter(int newPostNotFoundCounter) {
        this.newPostNotFoundCounter = newPostNotFoundCounter;
    }

    public int getLikedBlockedCounter() {
        return likedBlockedCounter;
    }

    public void setLikedBlockedCounter(int likedBlockedCounter) {
        this.likedBlockedCounter = likedBlockedCounter;
    }

    public int getCommentBlockedCounter() {
        return commentBlockedCounter;
    }

    public void setCommentBlockedCounter(int commentBlockedCounter) {
        this.commentBlockedCounter = commentBlockedCounter;
    }

    public boolean isLikingBlocked() {
        return isLikingBlocked;
    }

    public void setLikingBlocked(boolean likingBlocked) {
        isLikingBlocked = likingBlocked;
    }

    public boolean isCommentingBlocked() {
        return isCommentingBlocked;
    }

    public void setCommentingBlocked(boolean commentingBlocked) {
        isCommentingBlocked = commentingBlocked;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<String> getAlreadyVisitedSet() {
        return alreadyVisitedSet;
    }

    public void setAlreadyVisitedSet(Set<String> alreadyVisitedSet) {
        this.alreadyVisitedSet = alreadyVisitedSet;
    }
}
