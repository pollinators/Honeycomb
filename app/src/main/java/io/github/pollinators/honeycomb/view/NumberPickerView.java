package io.github.pollinators.honeycomb.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Button;

import io.github.pollinators.honeycomb.data.api.DataView;

/**
 * Created by ted on 11/2/14.
 */

public class NumberPickerView extends Button implements DataView<Integer> {

    public NumberPickerView(Context context, @Nullable AttributeSet attrs, int min, int max) {
        super(context, attrs);
    }

    @Override
    public void setData(Integer data) {
        setText(String.valueOf(data));
    }

    @Override
    public Integer getData() {
        return Integer.parseInt(getText().toString());
    }

    @Override
    public void setNullable(boolean isNullable) {

    }
}
