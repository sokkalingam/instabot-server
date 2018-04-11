package instagram.model;

public class Action {

    public boolean like;
    public boolean comment;
    public boolean follow;
    public boolean spamLike;

    public int counter;

    public Action() {

    }

    public Action(boolean like, boolean comment, boolean follow, boolean spamLike) {
        counter = 0;
        this.like = like;
        this.comment = comment;
        this.follow = follow;
        this.spamLike = spamLike;
    }

    public static Action getLikeAction() {
        return new Action(true, false, false, false);
    }

    public static Action getCommentAction() {
        return new Action(false, true, false, false);
    }

    public static Action getLikeCommentAction() {
        return new Action(true, true, false, false);
    }
}
