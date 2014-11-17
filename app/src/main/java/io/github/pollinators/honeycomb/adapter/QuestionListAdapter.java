package io.github.pollinators.honeycomb.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by ted on 11/2/14.
 */
public class QuestionListAdapter extends ArrayAdapter<String> {

    public QuestionListAdapter(Context context, List<String> objects) {
        super(context, android.R.layout.simple_list_item_1, android.R.id.text1, objects);
    }
}
