package pollinators.github.io.pollinatorresearchcollector.survey;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pollinators.github.io.pollinatorresearchcollector.R;

/**
 * Created by ted on 10/18/14.
 */
public class PollinatorSurvey implements Survey {

    // All of the questions in order of the string-arrays resource
    private final static int QUESTION_WHERE = 0;
    private final static int QUESTION_WHAT_TYPE = 1;
    private final static int QUESTION_HOW_MANY = 2;
    private final static int QUESTION_FLOWER_TYPE = 3;

    List<SurveyQuestion> fields;

    public PollinatorSurvey(Resources resources) {
        String[] questions = resources.getStringArray(R.array.questions_bees);

        fields = new ArrayList<SurveyQuestion>(questions.length);
        fields.add(QUESTION_WHERE, new SurveyQuestion(String.class, questions[QUESTION_WHERE]));
        fields.add(QUESTION_WHAT_TYPE, new SurveyQuestion(String.class, questions[QUESTION_WHAT_TYPE]));
        fields.add(QUESTION_HOW_MANY, new SurveyQuestion(Integer.class, questions[QUESTION_HOW_MANY]));
        fields.add(QUESTION_FLOWER_TYPE, new SurveyQuestion(String.class, questions[QUESTION_FLOWER_TYPE]));
    }

    public String getEntryType(int questionIndex) {
        return fields.get(questionIndex).getSqlType();
    }

    @Override
    public String getTitle() {
        return "Bees";
    }

    /**
     * Creates an array of all of the column definitions for each field, in order
     * @return the array of column definitions
     */
    @Override
    public String[] getColumnDefs() {
        String[] columnDef = new String[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            SurveyQuestion field = fields.get(i);
            columnDef[i] = "answer_" + (i+1) + " " + field.getSqlType();
        }

        return columnDef;
    }

    public class SurveyQuestion {

        public String getSqlType() {
            return sqlType;
        }

        public String sqlType;
        public String question;

        public SurveyQuestion(Class type, String question) {
            this.question = question;

            if (type == Integer.class) {
                sqlType = "INTEGER";
            } else if ((type == Double.class) || (type == Float.class)) {
                sqlType = "REAL";
            } else if (type == Object.class) {
                sqlType = "BLOB";
            } else
                sqlType = "TEXT";
        }
    }
}
