package io.github.pollinators.honeycomb.view;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import io.github.pollinators.honeycomb.data.api.DataView;
import timber.log.Timber;

/**
 * Created by ted on 11/2/14.
 */

public class TextEntryView extends AutoCompleteTextView implements DataView<String> {

    public TextEntryView(Context context, String[] answerOptions) {
        super(context);

        if (answerOptions != null) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context,
                    android.R.layout.simple_expandable_list_item_1, answerOptions);

            setAdapter(arrayAdapter);
        }

        setHint("Enter your answer here");
    }

    @Override
    public void setData(String data) {
        setText(data);
    }

    @Override
    public String getData() {
        return getText().toString();
    }

    @Override
    public void setNullable(boolean isNullable) {

    }
}
