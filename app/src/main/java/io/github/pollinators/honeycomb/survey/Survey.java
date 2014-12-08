package io.github.pollinators.honeycomb.survey;

import android.content.res.Resources;

import java.util.List;

/**
 * Created by ted on 10/18/14.
 */
public abstract class Survey {

    // Entry Types
    public final static int BITS_TYPE                   = 0x0FFF;
    public final static int TYPE_TEXT                   = 0x0001;
    public final static int TYPE_NUMERIC                = 0x0002;
    public final static int TYPE_YN                     = 0x0004;
    public final static int TYPE_RADIO_MULTI_CHOICE     = 0x0008;
    public final static int TYPE_SPINNER_MULTI_CHOICE   = 0x0010;
    public final static int TYPE_NUMBER_PICKER          = 0x0020;
    public final static int TYPE_COLOR_PICKER           = 0x0040;
    public final static int TYPE_WEATHER_PICKER         = 0x0080;
    public final static int TYPE_TEMPERATURE_PICKER     = 0x0100;

    // Entry-type Flags
    public final static int BITS_FLAG                   = 0xF000;
    public final static int FLAG_NULLABLE               = 0x1000;
    public final static int FLAG_OTHER                  = 0x2000;
    public final static int FLAG_OPTIONAL               = 0x4000;

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
        private final int entryMode;
        public final T[] answerOptions;

        public SurveyQuestion(String questionText, int entryMode, T[] answerOptions) {
            this.text = questionText;
            this.entryMode = entryMode;
            this.answerOptions = answerOptions;
        }

        public int getType() {
            return entryMode & BITS_TYPE;
        }

        public int getFlag() {
            return entryMode & BITS_FLAG;
        }
    }

}
