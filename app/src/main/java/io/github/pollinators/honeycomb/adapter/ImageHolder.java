package io.github.pollinators.honeycomb.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import butterknife.ButterKnife;
import io.github.pollinators.honeycomb.R;
import io.github.pollinators.honeycomb.view.PollinatorImageView;

/**
 * Created by ted on 12/7/14.
 */
public class ImageHolder extends RecyclerView.ViewHolder {

    protected PollinatorImageView imageView;
    protected long imageId;
    protected long responseId;
    protected Uri uri;

    public ImageHolder(View itemView) {
        super(itemView);

        this.imageView  = ButterKnife.findById(itemView, R.id.imageView);
        this.imageId    = 1;
        this.responseId = 1;
    }
}
