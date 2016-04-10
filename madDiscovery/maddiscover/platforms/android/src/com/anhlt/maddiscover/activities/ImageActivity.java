package com.anhlt.maddiscover.activities;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.anhlt.maddiscover.R;
import com.anhlt.maddiscover.fragments.event.EventDetails;
import com.squareup.picasso.Picasso;

import org.apache.cordova.CordovaActivity;

/**
 * Created by anhlt on 4/3/16.
 */
public class ImageActivity extends CordovaActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);
    }

    @Nullable
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {

        ImageView imageView = (ImageView)findViewById(R.id.image_view);

        int position = getIntent().getIntExtra("position", -1);
        if(position != -1){
            Picasso.with(ImageActivity.this).load(EventDetails.eatFoodyImages[position])
                    .noFade().resize(500,500).centerCrop().into(imageView);
        }else{

        }

        return super.onCreateView(name, context, attrs);
    }
}
