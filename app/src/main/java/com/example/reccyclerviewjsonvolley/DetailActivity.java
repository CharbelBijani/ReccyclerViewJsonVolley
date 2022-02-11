package com.example.reccyclerviewjsonvolley;

import static com.example.reccyclerviewjsonvolley.MainActivity.EXTRA_CREATOR;
import static com.example.reccyclerviewjsonvolley.MainActivity.EXTRA_LIKES;
import static com.example.reccyclerviewjsonvolley.MainActivity.EXTRA_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /** #1 Récupération de l'intent **/
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra(EXTRA_URL);
        String creator = intent.getStringExtra(EXTRA_CREATOR);
        int likes = intent.getIntExtra(EXTRA_LIKES, 0 ); // 0 pour la valeur par défaut

        /** #2 On lie widgets et codes **/
        ImageView ivImageView = findViewById(R.id.ivImageView);
        TextView tvCreator = findViewById(R.id.tvCreator);
        TextView tvLikes = findViewById(R.id.tvLikes);

        /** # On associe les données aux conteneurs **/
        tvCreator.setText(creator);
        tvLikes.setText("Likes :" + likes);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher);

        Glide.with(this)
                .load(imageUrl)
                .apply(options)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivImageView);
    }
}