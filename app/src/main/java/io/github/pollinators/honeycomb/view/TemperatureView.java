package io.github.pollinators.honeycomb.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;

import io.github.pollinators.honeycomb.data.api.ViewData;

/**
 * Created by ted on 11/2/14.
 */
public class TemperatureView extends EditText implements ViewData<Double> {

    public TemperatureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setData(Double data) {
        setText(String.valueOf(data));
    }

    @Override
    public Double getData() {
        return Double.parseDouble(getText().toString());
    }
}
