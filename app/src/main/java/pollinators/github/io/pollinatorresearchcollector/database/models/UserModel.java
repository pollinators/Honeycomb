package pollinators.github.io.pollinatorresearchcollector.database.models;

import java.util.List;

/**
 * Created by ted on 10/21/14.
 */
public class UserModel extends AbstractModel {

    String username;
    String passwordHashed;
    String email;
    List<AnswerModel> answers;
    List<UserSurveySectionModel> userSurveySections;
    List<SurveyCommentModel> comments;

    public String getUsername() {
        return username;
    }

    public String getPasswordHashed() {
        return passwordHashed;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHashed(String passwordHashed) {
        // TODO: Call out to a hash setter
        this.passwordHashed = passwordHashed;
    }

    /**
     * Sets the hashed password given the original password
     * @param password the password that the user entered
     */
    public void setPassword(String password) {
//        setPasswordHashed(PasswordUtil.hash(password));
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
