package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class Songlist extends AppCompatActivity {
    ListView listView;
    File[] items;
   static MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songlist);
        listView = (ListView) findViewById(R.id.listview);
        File root = Environment.getExternalStorageDirectory();
        //File root1=Environment.getI
        final ArrayList<File> song = Audio.findSongs(root);
        items = new File[song.size()];
        Toast.makeText(getApplicationContext(), ""+song.size(), Toast.LENGTH_SHORT).show();
        for (int i = 0; i < items.length; i++) {
            items[i] = song.get(i);
        }
         adapter = new MyAdapter(this,  items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("position",i);
                intent.putExtra("song",song);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
}





