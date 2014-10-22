package pollinators.github.io.pollinatorresearchcollector.database.models;

import java.util.List;

/**
 * Created by ted on 10/22/14.
 */
public class OptionGroupModel extends AbstractModel {

    String optionGroupName;
    List<OptionChoiceModel> optionChoices;
    List<QuestionModel> questions;
}
