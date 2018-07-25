package com.global.test.testglobal.Activitys;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.global.test.testglobal.R;
import com.global.test.testglobal.Services.PicassoTrustAll;
import com.squareup.picasso.Transformation;

public class DetailItemActivity extends AppCompatActivity {

    ImageView imageView;
    TextView title;
    TextView desc;
    String url;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);
        title = (TextView) findViewById(R.id.titleTxt);
        desc = (TextView) findViewById(R.id.descTxt);
        imageView = (ImageView) findViewById(R.id.imageView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        getSupportActionBar().setTitle("Detail Item");

        if (getIntent().getExtras() != null) {
            title.setText(getIntent().getStringExtra("title"));
            desc.setText(getIntent().getStringExtra("descripcion"));
            url = getIntent().getStringExtra("url");
        }

        Transformation transformation = new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {
                int targetWidth = 500;//imagen.getWidth();

                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                    // Same bitmap is returned if sizes are the same
                    source.recycle();
                }
                return result;
            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };


        PicassoTrustAll.getInstance(this)
                .load(url)
                .error(R.drawable.ic_dummy_ime_grande)
                .transform(transformation)
                .fit()
                .into(imageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.GONE);
                        Log.e("Cargar imagen", "Error");
                    }
                });


    }
}
