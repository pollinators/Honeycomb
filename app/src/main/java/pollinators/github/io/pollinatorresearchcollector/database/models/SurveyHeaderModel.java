package pollinators.github.io.pollinatorresearchcollector.database.models;

import java.util.List;

/**
 * Created by ted on 10/22/14.
 */
public class SurveyHeaderModel extends AbstractModel {

    long organizationId;
    String surveyName;
    String instructions;
    String otherHeaderInfo;
    List<SurveySectionModel> surveySections;
    List<SurveyCommentModel> surveyComments;
}
