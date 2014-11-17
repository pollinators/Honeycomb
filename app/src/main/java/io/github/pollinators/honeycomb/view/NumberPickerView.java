package io.github.pollinators.honeycomb.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;

import io.github.pollinators.honeycomb.data.api.ViewData;

/**
 * Created by ted on 11/2/14.
 */

public class NumberPickerView extends Button implements ViewData<Integer> {


    @Override
    public void setData(Integer data) {
        setText(String.valueOf(data));
    }

    @Override
    public Integer getData() {
        return Integer.parseInt(getText().toString());
    }

    public NumberPickerView(Context context, @Nullable AttributeSet attrs, int min, int max) {
        super(context, attrs);


    }
}
