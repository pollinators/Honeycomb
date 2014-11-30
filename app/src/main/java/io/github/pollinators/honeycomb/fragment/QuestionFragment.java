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
import io.github.pollinators.honeycomb.data.api.DataView;
import io.github.pollinators.honeycomb.survey.Survey;
import io.github.pollinators.honeycomb.util.Events;
import io.github.pollinators.honeycomb.view.NumberPickerView;
import io.github.pollinators.honeycomb.view.RadioMultiChoiceView;
import io.github.pollinators.honeycomb.view.YesNoView;

/**
 * Created by ted on 11/1/14.
 */
public class QuestionFragment extends PollinatorsBaseFragment {

    @Arg int questionId;

    @Inject Survey survey;

    @Inject Bus bus;

    @InjectView(R.id.fl_container)
    RelativeLayout container;

    @InjectView(R.id.tv_question) TextView questionTextView;
    @InjectView(R.id.tv_progress) TextView progressTextView;

    DataView dataView;

    public void setQuestion(int id) {
        this.questionId = id;
        Survey.SurveyQuestion<String> question = survey.getQuestion(id);

        questionTextView.setText(question.text);

        switch(question.getType()) {
            case Survey.TYPE_TEXT:
                dataView = new YesNoView(getActivity(), null);
                break;
            case Survey.TYPE_RADIO_MULTI_CHOICE:
                dataView = new RadioMultiChoiceView(getActivity(), null, Arrays.asList(question.answerOptions));
                break;
            case Survey.TYPE_NUMBER_PICKER:
                dataView = new NumberPickerView(getActivity(), null, 0, 100);
                break;
            case Survey.TYPE_YN:
                dataView = new YesNoView(getActivity(), null);
                break;
            default:
                dataView = new YesNoView(getActivity(), null);
                break;
        }

//        dataView.setNullable(question.isNullable());
//        dataView.setOptional(question.isOptional());
//        dataView.setAllowTextInput(question.isAllowTextInput());

        switch(question.getFlag()) {
            case Survey.FLAG_NULLABLE:
                dataView.setNullable(true);
                break;
            case Survey.FLAG_OPTIONAL:
//                dataView.setOptional(true);
                break;
            case Survey.FLAG_OTHER:
//                dataView.allowTextInput(true);
                break;
            default:
                break;
        }

        if (container.getChildCount() > 0) {
            container.removeAllViews();
        }

        container.addView((View) dataView);
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

    public Object getCurrentData() {
        return dataView.getData();
    }

    public void setCurrentData(Object data) {
        dataView.setData(data);
        progressTextView.setText((questionId + 1) + "/" + survey.getQuestionCount());
    }
}
