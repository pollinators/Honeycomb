package io.github.pollinators.honeycomb.survey;

import android.content.res.Resources;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.pollinators.honeycomb.R;

/**
 * Created by ted on 11/2/14.
 */
public class MedoraSurvey extends Survey {


    public final static int TYPE_TEXT = 0;
    public final static int TYPE_NUMERIC = 1;
    public final static int TYPE_YN = 2;
    public final static int TYPE_RADIO_MULTI_CHOICE = 3;
    public final static int TYPE_SPINNER_MULTI_CHOICE = 4;
    public final static int TYPE_NUMBER_PICKER = 5;

    // All of the questions in order of the string-arrays resource
    public final static int QUESTION_WHAT_KIND = 0;
    public final static int QUESTION_SKY = 1;
    public final static int QUESTION_WIND = 2;
    public final static int QUESTION_TEMP = 3;
    public final static int QUESTION_COLLECT_EVERY_TIME = 4;
    public final static int QUESTION_VISIT_MORE_THAN_ONCE = 5;
    public final static int QUESTION_SIZE_OF_FLOWER = 6;
    public final static int QUESTION_SIT_AROUND = 7;
    public final static int QUESTION_FLY_AROUND = 8;
    public final static int QUESTION_VISIT_IN_PATTERN = 9;

    private String[] questionStrings;

    public MedoraSurvey(Resources resources) {
        super(resources);

        questionStrings = resources.getStringArray(R.array.questions_bees);

        questions = new ArrayList<SurveyQuestion<String>>();
        addQuestion(QUESTION_WHAT_KIND, TYPE_RADIO_MULTI_CHOICE, R.array.question_options_what_kind);
        addQuestion(QUESTION_SKY, TYPE_RADIO_MULTI_CHOICE, R.array.question_options_is_the_sky);
        addQuestion(QUESTION_WIND, TYPE_RADIO_MULTI_CHOICE, R.array.question_options_is_the_wind);
        addQuestion(QUESTION_TEMP, TYPE_NUMBER_PICKER, null);
        addQuestion(QUESTION_COLLECT_EVERY_TIME, TYPE_YN, null);
        addQuestion(QUESTION_VISIT_MORE_THAN_ONCE, TYPE_YN, null);
        addQuestion(QUESTION_SIZE_OF_FLOWER, TYPE_RADIO_MULTI_CHOICE, R.array.question_options_what_size_is_the_flower);
        addQuestion(QUESTION_SIT_AROUND, TYPE_YN, null);
        addQuestion(QUESTION_FLY_AROUND, TYPE_YN, null);
        addQuestion(QUESTION_VISIT_IN_PATTERN, TYPE_YN, null);
    }

    private void addQuestion(int index, int type, @Nullable Integer answerOptionsId) {
        String[] choices = null;
        if (answerOptionsId != null) {
            choices = resources.getStringArray(answerOptionsId);
        }
        questions.add(index, new SurveyQuestion<String>(questionStrings[index], type, choices));
    }

    @Override
    public List<String> getQuestionStrings() {
        return Arrays.asList(questionStrings);
    }

    @Override
    public int getQuestionCount() {
        return questions.size();
    }
}
