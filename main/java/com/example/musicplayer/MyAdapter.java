package com.example.musicplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

public class MyAdapter extends BaseAdapter {
    Context context;
    File[] song;
    private ImageView imageView;
    Filter filter;

    public MyAdapter(Context context, File[] song) {
        this.context = context;
        this.song = song;
    }

    @Override
    public int getCount() {
        return song.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = View.inflate(context, R.layout.list_song, null);
        TextView songname = (TextView) view1.findViewById(R.id.text1);
        TextView songpath = (TextView) view1.findViewById(R.id.text2);
        ImageView imageView=(ImageView)view1.findViewById(R.id.image);
        songname.setText(song[i].getName());
        songpath.setText(song[i].getParent());
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(song[i].getPath());
        byte[] artBytes =  mmr.getEmbeddedPicture();
        if(artBytes != null)
        {
            InputStream is = new ByteArrayInputStream(mmr.getEmbeddedPicture());
            Bitmap bm = BitmapFactory.decodeStream(is);
            imageView.setImageBitmap(bm);
        }
        else
        {
            imageView.setBackgroundResource(R.drawable.headphone);
        }

        return view1;
    }

    public void getFilter() {
        Object a = null;

        filter.convertResultToString(a);

    }
}