package instagram.messages;

public enum EmailSubject {

    JOB_FINISHED ("Your Instabot Job Is Finished!"),

    JOB_ABORTED ("Your Instabot Job Is Aborted!"),
    JOB_ABORTED_FOR_MAINTENANCE("Sorry! Your Instabot Job Is Aborted For Maintenance"),
    JOB_ABORTED_WILL_AUTO_RESUME ("Your Job Has Been Aborted But Will Auto Resume After Maintenance"),

    JOB_RESUMED ("Your Job Has Auto Resumed After Maintenance"),

    LIKE_IS_BLOCKED ("Liking Is Blocked By Instagram"),
    COMMENT_IS_BLOCKED ("Commenting Is Blocked By Instagram"),
    USER_IS_BLOCKED("Your Job Is Aborted Because It Is Blocked By Instagram");

    private String message;

    EmailSubject(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
