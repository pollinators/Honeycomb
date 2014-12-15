package io.github.pollinators.honeycomb.fragment;

import android.content.ContentResolver;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.InjectView;
import io.github.pollinators.honeycomb.R;
import io.github.pollinators.honeycomb.adapter.CameraReelAdapter;
import io.github.pollinators.honeycomb.data.ImageDataSource;
import io.github.pollinators.honeycomb.data.api.DataView;
import io.github.pollinators.honeycomb.data.models.Image;
import io.github.pollinators.honeycomb.survey.Survey;
import io.github.pollinators.honeycomb.util.Events;
import io.github.pollinators.honeycomb.util.MediaUtils;
import io.github.pollinators.honeycomb.view.NumberPickerView;
import io.github.pollinators.honeycomb.view.RadioMultiChoiceView;
import io.github.pollinators.honeycomb.view.TemperatureView;
import io.github.pollinators.honeycomb.view.TextEntryView;
import io.github.pollinators.honeycomb.view.YesNoView;

/**
 * Created by ted on 11/1/14.
 */
public class QuestionFragment extends PollinatorsBaseFragment {

    @Arg int questionId;

    @Inject Survey survey;

    @Inject Bus bus;

    @Inject ImageDataSource imageDataSource;

    @Inject Picasso pollinatorPicasso;

    @InjectView(R.id.fl_container)
    RelativeLayout container;

    @InjectView(R.id.tv_question) TextView questionTextView;
    @InjectView(R.id.tv_progress) TextView progressTextView;


    @InjectView (R.id.recyclerView)
    RecyclerView recyclerView;
    GridLayoutManager layoutManager;

    DataView dataView;

    long responseId;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
            layoutManager.setSpanCount(3);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager.setOrientation(GridLayoutManager.VERTICAL);
            layoutManager.setSpanCount(2);
        }

        recyclerView.setLayoutManager(layoutManager);

    }

    public void setQuestion(int id) {
        this.questionId = id;
        Survey.SurveyQuestion<String> question = survey.getQuestion(id);

        questionTextView.setText(question.text);

        switch(question.getType()) {
            case Survey.TYPE_TEXT:
                dataView = new TextEntryView(getActivity(), question.answerOptions);
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
            case Survey.TYPE_TEMPERATURE_PICKER:
                dataView = new TemperatureView(getActivity(), null);
                break;
            default:
                dataView = new TextEntryView(getActivity(), question.answerOptions);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        setupCameraRoll(imageDataSource);
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

    public void setResponseId(long id) {
        this.responseId = id;
    }

    /**
     * Initializes and sets up the gallery view (a RecyclerView) which is defined in used in
     * this fragment.
     *
     * @param imageDataSource database access to images
     */
    public void setupCameraRoll(ImageDataSource imageDataSource) {
        Image cameraImage = new Image();
        cameraImage.setUri(MediaUtils.createUriFromResource(getResources(), android.R.drawable.ic_menu_camera));

        // Create the layout manager for the Recycler View
        List<Image> images = new ArrayList<Image>();
        images.add(cameraImage);
        images.addAll(imageDataSource.getAll());

        layoutManager = new GridLayoutManager(getActivity(), 1);
        layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);

        CameraReelAdapter cameraReelAdapter = new CameraReelAdapter(getActivity(), pollinatorPicasso, images);
        cameraReelAdapter.setOnCameraSelectedListener(new CameraReelAdapter.OnCameraSelectedListener() {
            @Override
            public void onCameraSelected() {
                bus.post(new Events.CameraRequestedEvent());
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cameraReelAdapter);
    }}
