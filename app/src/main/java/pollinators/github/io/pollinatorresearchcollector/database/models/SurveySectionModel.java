package pollinators.github.io.pollinatorresearchcollector.database.models;

import java.util.List;

/**
 * Created by ted on 10/22/14.
 */
public class SurveySectionModel extends AbstractModel {

    long surveyHeaderId;
    String sectionName;
    String sectionTitle;
    String sectionSubHeading;
    boolean isSectionRequired;
    List<UserSurveySectionModel> userSurveySections;
    List<QuestionModel> questions;
}
