package pollinators.github.io.pollinatorresearchcollector.database.models;

import java.util.List;

/**
 * Created by ted on 10/21/14.
 */
public class QuestionOptionModel {

    long id;
    long questionId;
    long optionChoiceId;
    List<AnswerModel> answers;
}
