package io.github.pollinators.honeycomb.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import io.github.pollinators.honeycomb.R;
import io.github.pollinators.honeycomb.data.models.Image;
import io.github.pollinators.honeycomb.util.Utils;
import io.github.pollinators.honeycomb.view.PollinatorImageView;

/**
 * Created by ted on 12/7/14.
 */
public class CameraReelAdapter extends RecyclerView.Adapter<ImageHolder> {

    public static final int TYPE_CAMERA = 1;
    public static final int TYPE_IMAGE = 2;
    Context context;
    List<Image> imageList;
    Picasso pollinatorPicasso;

    int height, width;

    public void setOnCameraSelectedListener(OnCameraSelectedListener onCameraSelectedListener) {
        this.onCameraSelectedListener = onCameraSelectedListener;
    }

    public OnCameraSelectedListener onCameraSelectedListener;
    public interface OnCameraSelectedListener {
        void onCameraSelected();
    }

    @Inject
    public CameraReelAdapter(Context context, Picasso pollinatorPicasso, List<Image> imageList) {
        this.context            = context;
        this.imageList          = imageList;
        this.pollinatorPicasso  = pollinatorPicasso;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_list_item, null);
        ImageHolder imageHolder = new ImageHolder(view);

        return imageHolder;
    }

    /**
     * Determine which Uri to pass to the view
     * @param imageHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(ImageHolder imageHolder, int i) {
        final Image image = imageList.get(i);
        imageHolder.imageId = image.getId();

        imageHolder.imageView.bindTo(image, pollinatorPicasso);

        if (imageHolder.getItemViewType() == TYPE_CAMERA) {
            imageHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCameraSelectedListener != null) {
                        onCameraSelectedListener.onCameraSelected();
                    }
                }
            });
        } else {
            imageHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(image.getUri(), "image/*");
                    context.startActivity(intent);

                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? TYPE_CAMERA : TYPE_IMAGE;
    }

    @Override
    public int getItemCount() {
        return (null != imageList ? imageList.size() : 0);
    }
}
