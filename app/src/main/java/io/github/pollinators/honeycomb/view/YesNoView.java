package io.github.pollinators.honeycomb.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import io.github.pollinators.honeycomb.data.api.DataView;
import timber.log.Timber;

/**
 * Created by ted on 11/2/14.
 */

public class YesNoView extends RadioGroup implements DataView<Boolean> {

    RadioButton yesButton;
    RadioButton noButton;
    RadioButton clearButton;

    @Override
    public void setData(Boolean data) {
        if (data == null) {
            if (clearButton != null) {
                clearButton.setChecked(true);
            } else {
                clearCheck();
                Timber.d("Data was null and cleared checks");
            }
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
        if ((clearButton != null) && (clearButton.isChecked())) {
            Timber.d("Returning null");
            return null;
        }

        return yesButton.isChecked();
    }

    public YesNoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

//        ButterKnife.inject(this, View.inflate(getContext(), R.layout.view_yes_no, null));
        yesButton = new RadioButton(context);
        noButton = new RadioButton(context);

        yesButton.setText("Yes");
        noButton.setText("No");

        addView(yesButton);
        addView(noButton);
    }

    /**
     * Add an extra radio button to clear the data (presented to the user as "I don't know")
     * @param isNullable
     */
    public void setNullable(boolean isNullable) {
        if (isNullable && clearButton == null) {
            clearButton = new RadioButton(getContext());
            clearButton.setText("I don't know");
            addView(clearButton);
            clearButton.setChecked(true);
        } else if (!isNullable && clearButton != null) {
            removeView(clearButton);
            clearButton = null;
        }
    }
}
