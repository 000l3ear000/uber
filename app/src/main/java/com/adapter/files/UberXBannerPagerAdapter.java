package com.adapter.files;

import android.content.Context;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.melevicarbrasil.usuario.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Admin on 02-03-2017.
 */
public class UberXBannerPagerAdapter extends PagerAdapter  {

    private ArrayList<String> IMAGES;
    private LayoutInflater inflater;
    private Context context;


    public UberXBannerPagerAdapter(Context context, ArrayList<String> IMAGES) {
        this.context = context;
        this.IMAGES = IMAGES;
        inflater = LayoutInflater.from(context);
        margin=context.getResources().getDimensionPixelSize(R.dimen.category_banner_left_right_margin);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }





    @Override
    public int getCount() {
//        return Character.MAX_VALUE;
        return IMAGES.size();
    }

    int bannerHeight;
    int bannerWidth;
    int margin;
    @Override
    public Object instantiateItem(ViewGroup view, int position) {

        View imageLayout = inflater.inflate(R.layout.item_uber_x_banner_design, view, false);
        assert imageLayout != null;

        final ImageView bannerImgView = (ImageView) imageLayout.findViewById(R.id.bannerImgView);
         CardView cardView = (CardView) imageLayout.findViewById(R.id.cardView);

        position = position % IMAGES.size();

        Picasso.get().load(IMAGES.get(position))
//                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .fit().into(bannerImgView);

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}