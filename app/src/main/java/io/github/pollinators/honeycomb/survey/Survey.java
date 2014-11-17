package io.github.pollinators.honeycomb.survey;

import android.content.res.Resources;

import java.util.List;

/**
 * Created by ted on 10/18/14.
 */
public abstract class Survey {

    protected Resources resources;
    List<SurveyQuestion<String>> questions;


    public Survey(Resources resources) {
        this.resources = resources;
    }

    public SurveyQuestion<String> getQuestion(int index) {
        return questions.get(index);
    }

    public abstract List<String> getQuestionStrings();
    public abstract int getQuestionCount();

    public static class SurveyQuestion<T> {
        public final String text;
        public final int type;
        public final T[] answerOptions;

        public SurveyQuestion(String questionText, int type, T[] answerOptions) {
            this.text = questionText;
            this.type = type;
            this.answerOptions = answerOptions;
        }
    }
}
