package instagram.messages;

public enum EmailMessage {

    JOB_FINISHED ("Your Instabot job is finished!"),
    JOB_ABORTED ("Your Instabot job is aborted!"),
    JOB_ABORTED_FOR_MAINTENACE ("Sorry! Your Instabot job is aborted for maintenance"),
    JOB_ABORTED_WILL_AUTO_RESUME ("Your job has been aborted but will auto resume after maintenace"),
    JOB_RESUMED ("Your job has auto resumed after maintenace!");

    private String message;

    EmailMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
