package io.github.pollinators.honeycomb.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import io.github.pollinators.honeycomb.data.models.Image;

/**
 * Created by ted on 12/7/14.
 */
public class PollinatorImageView extends ImageView {

    private RequestCreator request;

    public PollinatorImageView(Context context) {
        super(context);
    }
    public PollinatorImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PollinatorImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Override here to both make the view square, and allow Picasso to bind the image
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("layout_height must be match_parent");
        }

        int height = MeasureSpec.getSize(heightMeasureSpec);

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (request != null) {
            request.resize(height, height).centerCrop().into(this);
            request = null;
        }
    }


    public void bindTo(Image item, Picasso picasso) {
        request = picasso.load(item.getUri());
        requestLayout();
    }

}
