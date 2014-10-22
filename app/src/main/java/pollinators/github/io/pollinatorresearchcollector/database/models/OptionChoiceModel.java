package pollinators.github.io.pollinatorresearchcollector.database.models;

import java.util.List;

/**
 * Created by ted on 10/22/14.
 */
public class OptionChoiceModel extends AbstractModel {

    long optionGroupId;
    String optionChoiceName;
    List<QuestionOptionModel> questionOptions;
}
