package io.github.pollinators.honeycomb.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.squareup.otto.Bus;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.InjectView;
import io.github.pollinators.honeycomb.R;
import io.github.pollinators.honeycomb.data.api.Saveable;
import io.github.pollinators.honeycomb.data.api.ViewData;
import io.github.pollinators.honeycomb.survey.MedoraSurvey;
import io.github.pollinators.honeycomb.survey.Survey;
import io.github.pollinators.honeycomb.util.Events;
import io.github.pollinators.honeycomb.view.NumberPickerView;
import io.github.pollinators.honeycomb.view.RadioMultiChoiceView;
import io.github.pollinators.honeycomb.view.YesNoView;

/**
 * Created by ted on 11/1/14.
 */
public class QuestionFragment extends PollinatorsBaseFragment implements Saveable {

    @Arg int questionId;

    @Inject Survey survey;

    @Inject Bus bus;

    @InjectView(R.id.fl_container)
    RelativeLayout container;

    @InjectView(R.id.tv_question) TextView questionTextView;
    @InjectView(R.id.tv_progress) TextView progressTextView;

    ViewData viewData;

    public void setQuestion(int id) {
        this.questionId = id;
        Survey.SurveyQuestion<String> question = survey.getQuestion(id);

        questionTextView.setText(question.text);

        switch(questionId) {
            case MedoraSurvey.QUESTION_WHAT_KIND:
                viewData = new RadioMultiChoiceView(getActivity(), null, Arrays.asList(question.answerOptions));
                break;
            case MedoraSurvey.QUESTION_SKY:
                viewData = new RadioMultiChoiceView(getActivity(), null, Arrays.asList(question.answerOptions));
                break;
            case MedoraSurvey.QUESTION_WIND:
                viewData = new RadioMultiChoiceView(getActivity(), null, Arrays.asList(question.answerOptions));
                break;
            case MedoraSurvey.QUESTION_TEMP:
                viewData = new NumberPickerView(getActivity(), null, 1, 3);
                break;
            case MedoraSurvey.QUESTION_COLLECT_EVERY_TIME:
                viewData = new YesNoView(getActivity(), null);
                break;
            case MedoraSurvey.QUESTION_VISIT_MORE_THAN_ONCE:
                viewData = new YesNoView(getActivity(), null);
                break;
            case MedoraSurvey.QUESTION_SIZE_OF_FLOWER:
                viewData = new RadioMultiChoiceView(getActivity(), null, Arrays.asList(question.answerOptions));
                break;
            case MedoraSurvey.QUESTION_SIT_AROUND:
                viewData = new YesNoView(getActivity(), null);
                break;
            case MedoraSurvey.QUESTION_FLY_AROUND:
                viewData = new YesNoView(getActivity(), null);
                break;
            case MedoraSurvey.QUESTION_VISIT_IN_PATTERN:
                viewData = new YesNoView(getActivity(), null);
                break;
            default:
                viewData = new YesNoView(getActivity(), null);
        }

        if (container.getChildCount() > 0) {
            container.removeAllViews();
        }

        container.addView((View) viewData);
    }

    @Override
    public int getContentViewId() {
        return R.layout.frag_question;
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.post(new Events.RetreiveQuestionDataEvent());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setQuestion(0);
    }

    @Override
    public Object getKey() {
        return null;
    }

    @Override
    public void get() {
    }

    @Override
    public void save() {
    }

    public Object getCurrentData() {
        return viewData.getData();
    }

    public void setCurrentData(Object data) {
        viewData.setData(data);
        progressTextView.setText((questionId + 1) + "/" + survey.getQuestionCount());
    }
}
