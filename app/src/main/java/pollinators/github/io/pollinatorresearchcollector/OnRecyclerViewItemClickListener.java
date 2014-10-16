package pollinators.github.io.pollinatorresearchcollector;

import android.view.View;

public interface OnRecyclerViewItemClickListener<Model> {
    public void onItemClick(View view, Model model);
}