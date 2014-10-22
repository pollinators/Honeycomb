package pollinators.github.io.pollinatorresearchcollector.database.models;

/**
 * Created by ted on 10/21/14.
 */
public class AnswerModel {

    long id;
    long userId;
    long questionOptionId;
    int answerNumeric;
    String answerText;
    byte[] answerBlob;
}
