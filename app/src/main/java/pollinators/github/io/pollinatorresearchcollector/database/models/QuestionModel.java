package pollinators.github.io.pollinatorresearchcollector.database.models;

import java.util.List;

/**
 * Created by ted on 10/21/14.
 */
public class QuestionModel extends AbstractModel {

    long surveySectionId;
    long inputTypeId;
    String questionName;
    String questionSubtext;
    boolean isAnswerRequired;
    long optionGroupId;
    boolean allowMultipleAnswers;
    List<QuestionOptionModel> questionOptions;
}
