package com.global.test.testglobal.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.global.test.testglobal.Activitys.DetailItemActivity;
import com.global.test.testglobal.Model.Item;
import com.global.test.testglobal.R;
import com.global.test.testglobal.Services.PicassoTrustAll;
import com.squareup.picasso.Transformation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Andrea on 25/07/2018.
 */

public class RecyclerItemAdapter extends RecyclerView.Adapter<RecyclerItemHolders> {


    private List<Item> itemList;
    private Context context;
    private SharedPreferences prefs;
    private String autorization;
    private String ids = "";
    public static final String MESSAGE_PROGRESS = "message_progress";
    private static final int PERMISSION_REQUEST_CODE = 1;
    Activity activity;


    public RecyclerItemAdapter(Context context, List<Item> itemList, Activity activity) {
        this.itemList = itemList;
        this.context = context;
        this.activity = activity;


    }

    @Override
    public RecyclerItemHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, null);
        RecyclerItemHolders rcv = new RecyclerItemHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final RecyclerItemHolders holder, final int position) {
        holder.title.setText(itemList.get(position).getTitle());
        holder.descripcion.setText(itemList.get(position).getDescription());
        Transformation transformation = new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {
                int targetWidth = 500;

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
        if (itemList.get(position).getImage()!=null) {

            String url = itemList.get(position).getImage();
            PicassoTrustAll.getInstance(context)
                    .load(url)
                    .error(R.drawable.ic_dummy_ime_grande)
                    .transform(transformation)
                    .fit()
                    .into(holder.image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Log.e("Cargar imagen", "Success: ");
                            holder.progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            Log.e("Cargar imagen", "Error: ");
                           holder.progress.setVisibility(View.GONE);
                        }
                    });

        } else {
            holder.image.setImageResource(R.drawable.ic_dummy_ime_grande);
        }



        holder.viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetailItemActivity.class);
                i.putExtra("title",itemList.get(position).getTitle());
                i.putExtra("descripcion",itemList.get(position).getDescription());
                i.putExtra("url",itemList.get(position).getImage());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

}
