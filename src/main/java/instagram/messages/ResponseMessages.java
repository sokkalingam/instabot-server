package instagram.messages;

public enum ResponseMessages {

    REQUEST_ACCEPTED ("Request accepted and will be processed immediately"),
    REQUEST_REJECTED ("Your request cannot be accepted at this time, please try again later"),
    REQUEST_QUEUED ("Your request is queued. Will be processed as soon we have availability"),
    REQUEST_EXISTS ("Request cannot be accepted since your previous request is already in place"),

    SESSION_DOES_NOT_EXIST ("Your session does not exist"),
    SESSION_ABORTED ("Your session is aborted successfully"),

    NO_JOB_TO_ABORT ("No job found to abort"),

    EMAIL_SENT ("Email sent successfully!"),
    EMAIL_FAILED ("Email could not be sent");

    private String message;

    ResponseMessages(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
