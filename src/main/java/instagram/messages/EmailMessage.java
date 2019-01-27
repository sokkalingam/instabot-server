package instagram.messages;

public enum EmailMessages {

    JOB_FINISHED ("Your Instabot job is finished!"),
    JOB_ABORTED ("Your Instabot job is aborted!"),
    JOB_ABORTED_FOR_MAINTENACE ("Sorry! Your Instabot job is aborted for maintenance");

    private String message;

    EmailMessages(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
