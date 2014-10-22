package pollinators.github.io.pollinatorresearchcollector.database.models;

import java.util.List;

/**
 * Created by ted on 10/22/14.
 */
public class OrganizationModel extends AbstractModel {

    String organizationName;
    List<SurveyHeaderModel> surveyHeaders;
    List<SurveyCommentModel> surveyComments;
}
