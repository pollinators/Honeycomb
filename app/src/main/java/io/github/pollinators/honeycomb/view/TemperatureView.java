package io.github.pollinators.honeycomb.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.simonvt.numberpicker.NumberPicker;

import io.github.pollinators.honeycomb.R;
import io.github.pollinators.honeycomb.data.api.DataView;
import timber.log.Timber;

/**
 * Created by ted on 11/2/14.
 */
public class TemperatureView extends LinearLayout implements DataView<Double> {

    private static final int PRECISION = 2;

    private NumberPicker integerNumberPicker;
    private NumberPicker decimalNumberPicker;

    public TemperatureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        integerNumberPicker = new NumberPicker(context, attrs);
        decimalNumberPicker = new NumberPicker(context, attrs);

        integerNumberPicker.setMinValue(0);
        integerNumberPicker.setMaxValue(130);
        integerNumberPicker.setWrapSelectorWheel(false);

        decimalNumberPicker.setMinValue(0);
        decimalNumberPicker.setMaxValue(99);
        decimalNumberPicker.setWrapSelectorWheel(false);
        decimalNumberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });


        TextView decimalPoint = new TextView(context);
        decimalPoint.setText(".");
        decimalPoint.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

        addView(integerNumberPicker);
        addView(decimalPoint);
        addView(decimalNumberPicker);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) decimalPoint.getLayoutParams();
        params.gravity = Gravity.CENTER_VERTICAL;
        decimalPoint.setLayoutParams(params);
    }

    @Override
    public void setData(Double data) {
        if (data == null) {
            data = null;
            return;
        }

        String format = "%." + PRECISION + "f";
        Timber.d("Value in decimal = %f", data);
        String value = String.format(format, data);

        String[] partsOfReal = value.split("\\.");
        integerNumberPicker.setValue(Integer.valueOf(partsOfReal[0]));

        // If the data given does not split at the decimal point
        // TODO: Check that this is necessary at all since it is a double
        if (partsOfReal.length > 1) {
            decimalNumberPicker.setValue(Integer.valueOf(partsOfReal[1]));
        } else {
            decimalNumberPicker.setValue(0);
        }
    }

    @Override
    public Double getData() {
        String value = String.format("%d.%d", integerNumberPicker.getValue(), decimalNumberPicker.getValue());
        return Double.parseDouble(value);
    }

    @Override
    public void setNullable(boolean isNullable) {

    }
}
