package instagram.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Feedback {

    @NotNull
    @Size(min = 3, max = 20)
    private String name;

    private String email;

    @NotNull
    @Size(min = 3)
    private String subject;

    @NotNull
    private String body;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Feedback" +
                "\nName: " + name +
                "\nEmail: " + email +
                "\nSubject: " + subject +
                "\nBody: " + body;
    }
}
