package pollinators.github.io.pollinatorresearchcollector;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.twotoasters.android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * Created by ted on 10/16/14.
 */
public class SurveyRecycleAdapter extends RecyclerView.Adapter<SurveyRecycleAdapter.ViewHolder>
    implements View.OnClickListener {

    private List<SurveyQuestionModel> items;
    private int itemLayoutId;
    private OnRecyclerViewItemClickListener<SurveyQuestionModel> itemClickListener;
    private View.OnClickListener mOnSubmitButtonClickListener;

    @Override
    public void onClick(View view) {
        if (itemClickListener != null) {
            SurveyQuestionModel model = (SurveyQuestionModel) view.getTag();
            itemClickListener.onItemClick(view, model);
        }
    }

    public static class SurveyQuestionModel {
        public String questionText;
        public String entryValue;
        public int entryType;

        public SurveyQuestionModel(String questionText, String entryValue, int entryType) {
            this.questionText = questionText;
            this.entryValue = entryValue;
            this.entryType = entryType;
        }
    }

    public SurveyRecycleAdapter(List<SurveyQuestionModel> items, int itemLayoutId) {
        this.items = items;
        this.itemLayoutId = itemLayoutId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        SurveyQuestionModel item = items.get(position);
        viewHolder.labelTextView.setText(item.questionText);
        viewHolder.entryTextBox.setText(item.entryValue);
        viewHolder.itemView.setTag(item);
        viewHolder.submitButton.setOnClickListener(mOnSubmitButtonClickListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void add(SurveyQuestionModel item, int position) {
        items.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(SurveyQuestionModel item) {
        int position = items.indexOf(item);
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener<SurveyQuestionModel> listener) {
        this.itemClickListener = listener;
    }

    public void setOnSubmitClickListener(View.OnClickListener onSubmitClickListener) {
        this.mOnSubmitButtonClickListener = onSubmitClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView labelTextView;
        public EditText entryTextBox;
        public Button submitButton;

        public ViewHolder(View itemView) {
            super(itemView);
            labelTextView = (TextView) itemView.findViewById(R.id.tv_label);
            entryTextBox = (EditText) itemView.findViewById(R.id.et_entry);
            submitButton = (Button) itemView.findViewById(R.id.btn_submit);
        }

    }
}
