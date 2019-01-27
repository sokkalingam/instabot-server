package instagram.messages;

public enum EmailSubject {

    JOB_FINISHED ("Your Instabot Job Is Finished!"),
    JOB_ABORTED ("Your Instabot Job Is Aborted!"),
    JOB_ABORTED_FOR_MAINTENACE ("Sorry! Your Instabot Job Is Aborted For Maintenance."),
    JOB_ABORTED_WILL_AUTO_RESUME ("Your Job Has Been Aborted But Will Auto Resume After Maintenance"),
    JOB_RESUMED ("Your Job Has Auto Resumed After Maintenance!");

    private String message;

    EmailSubject(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
