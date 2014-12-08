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

    // All of the questions in order of the string-arrays resource
    public final static int QUESTION_WHAT_KIND = 0;
    public final static int QUESTION_ANOTHER_NAME = 1;
    public final static int QUESTION_PLANT_TYPE = 2;
    public final static int QUESTION_SKY = 3;
    public final static int QUESTION_WIND = 4;
    public final static int QUESTION_TEMP = 5;
    public final static int QUESTION_FLOWER_COLOR = 6;
    public final static int QUESTION_FLOWER_SIZE = 7;
    public final static int QUESTION_COLLECT_EVERY_TIME = 8;
    public final static int QUESTION_VISIT_MORE_THAN_ONCE = 9;
    public final static int QUESTION_SIT_AROUND = 10;
    public final static int QUESTION_VISIT_IN_PATTERN = 11;

    private String[] questionStrings;

    public MedoraSurvey(Resources resources) {
        super(resources);

        questionStrings = resources.getStringArray(R.array.questions_bees);

        questions = new ArrayList<SurveyQuestion<String>>();

        addQuestion(QUESTION_WHAT_KIND,
                TYPE_RADIO_MULTI_CHOICE,
                R.array.question_options_what_kind_of_pollinator);

        addQuestion(QUESTION_ANOTHER_NAME,
                TYPE_TEXT | FLAG_OPTIONAL,
                null);

        addQuestion(QUESTION_PLANT_TYPE,
                TYPE_RADIO_MULTI_CHOICE,
                R.array.question_options_what_kind_of_plant);

        addQuestion(QUESTION_SKY,
                TYPE_RADIO_MULTI_CHOICE,
                R.array.question_options_is_the_sky);

        addQuestion(QUESTION_WIND,
                TYPE_RADIO_MULTI_CHOICE,
                R.array.question_options_is_the_wind);

        addQuestion(QUESTION_TEMP,
                TYPE_TEMPERATURE_PICKER,
                null);

        addQuestion(QUESTION_FLOWER_COLOR,
                TYPE_COLOR_PICKER | FLAG_OTHER,
                R.array.question_options_flower_color);

        addQuestion(QUESTION_FLOWER_SIZE,
                TYPE_RADIO_MULTI_CHOICE | FLAG_NULLABLE,
                R.array.question_options_what_size_is_the_flower);

        addQuestion(QUESTION_COLLECT_EVERY_TIME,
                TYPE_YN | FLAG_NULLABLE,
                null);

        addQuestion(QUESTION_VISIT_MORE_THAN_ONCE,
                TYPE_YN | FLAG_NULLABLE,
                null);

        addQuestion(QUESTION_SIT_AROUND,
                TYPE_YN | FLAG_NULLABLE,
                null);

        addQuestion(QUESTION_VISIT_IN_PATTERN,
                TYPE_YN | FLAG_NULLABLE,
                null);
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
