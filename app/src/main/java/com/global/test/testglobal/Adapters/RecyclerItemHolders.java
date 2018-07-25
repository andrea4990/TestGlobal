package com.global.test.testglobal.Adapters;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.global.test.testglobal.R;

/**
 * Created by Andrea on 25/07/2018.
 */

public class RecyclerItemHolders extends RecyclerView.ViewHolder  {

    public TextView title;
    public TextView descripcion;
    public ImageView image;
    public ProgressBar progress;


    View viewItem;

    public RecyclerItemHolders(View itemView) {
        super(itemView);
        viewItem=itemView;
        title = (TextView)itemView.findViewById(R.id.title);
        descripcion = (TextView)itemView.findViewById(R.id.descripcion);
        image = (ImageView) itemView.findViewById(R.id.image);
        progress=(ProgressBar) itemView.findViewById(R.id.progressBar);


    }

}
