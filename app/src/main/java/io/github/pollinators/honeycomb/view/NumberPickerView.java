package io.github.pollinators.honeycomb.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;


import net.simonvt.numberpicker.NumberPicker;

import io.github.pollinators.honeycomb.data.api.DataView;
import timber.log.Timber;

/**
 * Created by ted on 11/2/14.
 */
public class NumberPickerView extends NumberPicker implements DataView<Integer> {

    public NumberPickerView(Context context, @Nullable AttributeSet attrs, int min, int max) {
        super(context);
//        setMinValue(min);
//        setMaxValue(max);
        setMaxValue(0);
        setMaxValue(5);
        setDisplayedValues(new String[]{"1.3", "4.5", "10.1", "11", "12.4", "15.3"});
        setWrapSelectorWheel(true);
    }

    @Override
    public void setData(Integer data) {
        if (data instanceof Integer) {
            this.setValue(data);
        } else {
            Timber.d("Data was not an Integer");
        }
    }

    @Override
    public Integer getData() {
        return getValue();
    }

    @Override
    public void setNullable(boolean isNullable) {

    }
}
