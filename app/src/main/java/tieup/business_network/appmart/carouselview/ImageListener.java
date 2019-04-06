package tieup.business_network.appmart.carouselview;

import android.widget.ImageView;

/**
 * Created by Sayyam on 3/15/16.
 * To set simple ImageView drawable in CarouselView
 */
public interface ImageListener extends com.synnapps.carouselview.ImageListener {

    void setImageForPosition(int position, ImageView imageView);

}
