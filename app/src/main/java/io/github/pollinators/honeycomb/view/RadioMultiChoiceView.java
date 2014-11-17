package io.github.pollinators.honeycomb.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

import butterknife.ButterKnife;
import io.github.pollinators.honeycomb.data.api.ViewData;

/**
 * Created by ted on 11/2/14.
 */

public class RadioMultiChoiceView extends RadioGroup implements ViewData<String> {

    List<String> choices;

    @Override
    public void setData(String data) {
        if (data == null) {
            clearCheck();
            return;
        }

        int choiceIndex = choices.indexOf(data);
        if (choiceIndex < 0) {
            clearCheck();
            return;
        }

        RadioButton button = (RadioButton) getChildAt(choiceIndex);
        button.setChecked(true);
    }

    @Override
    public String getData() {
        int checkedId = getCheckedRadioButtonId();

        if (checkedId >= 0) {
            RadioButton button = ButterKnife.findById(this, checkedId);
            return button.getText().toString();
        }
        return null;
    }


    public RadioMultiChoiceView(Context context, @Nullable AttributeSet attrs, List<String> choices) {
        super(context, attrs);

        this.choices = choices;
        for (String choice : choices) {
            RadioButton button = new RadioButton(context);
            button.setText(choice);
            addView(button);
        }
    }
}
