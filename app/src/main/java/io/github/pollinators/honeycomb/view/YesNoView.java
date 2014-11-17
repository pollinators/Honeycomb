package io.github.pollinators.honeycomb.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import javax.inject.Inject;

import io.github.pollinators.honeycomb.data.api.ViewData;

/**
 * Created by ted on 11/2/14.
 */

public class YesNoView extends RadioGroup implements ViewData<Boolean> {

    RadioButton yesButton;
    RadioButton noButton;

    @Override
    public void setData(Boolean data) {
        if (data == null) {
            clearCheck();
            return;
        }

        if (data) {
            yesButton.setChecked(true);
        } else {
            noButton.setChecked(true);
        }
    }

    @Override
    public Boolean getData() {
        return yesButton.isChecked();
    }


    @Inject public YesNoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

//        ButterKnife.inject(this, View.inflate(getContext(), R.layout.view_yes_no, null));
        yesButton = new RadioButton(context);
        noButton = new RadioButton(context);

        yesButton.setText("Yes");
        noButton.setText("No");

        addView(yesButton);
        addView(noButton);
    }
}
