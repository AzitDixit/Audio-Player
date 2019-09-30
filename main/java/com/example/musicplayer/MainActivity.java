package com.example.musicplayer;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;


import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageButton play_pause,next,previous,forword,backword;
    static MediaPlayer mediaPlayer;
    SeekBar seekBar;
    TextView startTime,endTime;
    int flag=0;
    int flag2=0;
    ArrayList<File> mysongs;
    int position;
    Handler handler=new Handler();
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        play_pause=(ImageButton)findViewById(R.id.play);
        next=(ImageButton)findViewById(R.id.next);
        previous=(ImageButton)findViewById(R.id.privious);
        startTime=(TextView)findViewById(R.id.start);
        endTime=(TextView)findViewById(R.id.end);
        seekBar=(SeekBar)findViewById(R.id.bar);
        forword=(ImageButton)findViewById(R.id.forword);
        backword=(ImageButton)findViewById(R.id.backword);
        play_pause.setOnClickListener(this);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        forword.setOnClickListener(this);
        backword.setOnClickListener(this);



        mysongs=fetchAudio();
        setAudio();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                startTime.setText(getDuration(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                flag2=1;

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
                flag2=0;

            }
        });




    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.play:
                playAudio();
                break;
            case R.id.next:
                mediaPlayer.stop();
                mediaPlayer.release();
                setNext();
                break;
            case R.id.privious:
                setPrevious();
                break;
            case R.id.forword:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+10000);
                break;
            case R.id.backword:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-10000);
                break;

            default:
                break;
        }
    }

    private void setNext() {

        if(position<mysongs.size()-1)
        {
            position=position+1;
            uri=Uri.parse(mysongs.get(position).toString());
        }
        else
        {
            position=0;
            uri=Uri.parse(mysongs.get(position).toString());
        }
        mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
        playAudio();

    }


    private void setPrevious() {
        mediaPlayer.stop();
        mediaPlayer.release();
        if(position>0)
        {
            position=position-1;
            uri=Uri.parse(mysongs.get(position).toString());
        }
        else
        {
            position=0;
            uri=Uri.parse(mysongs.get(position).toString());
        }
        mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
        playAudio();

    }


    Runnable updateSeekBar=new Runnable() {
        @Override
        public void run() {
            if (flag2==0)
            {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                startTime.setText(getDuration(mediaPlayer.getCurrentPosition()));
            }

            handler.postDelayed(this,10);

        }
    };

    private String getDuration(long time) {
        String str="";
        long min=time/60000;
        long sec=(time%60000)/1000;
        str+=min+" m:"+sec+" s";
        return str;
    }
    public void setAudio()
    {
        if (mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();


            Intent intent=getIntent();
            Bundle b=intent.getExtras();
            // mysongs=(ArrayList)b.getParcelableArrayList("songs");
            position=b.getInt("position",0);
            uri=Uri.parse(mysongs.get(position).toString());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            playAudio();
        }
        else
        {
            uri=Uri.parse(mysongs.get(0).toString());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
        }
    }



    public void playList(View view) {
        Intent intent=new Intent(this,Songlist.class);
        startActivity(intent);

    }

    public void playAudio() {
        if(mediaPlayer.isPlaying())
        {
            play_pause.setBackgroundResource(R.drawable.play);
            mediaPlayer.pause();
            flag=0;
        }
        else
        {
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration());
            play_pause.setBackgroundResource(R.drawable.pause);
            endTime.setText(getDuration(mediaPlayer.getDuration()));
            flag=1;
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            handler.postDelayed(updateSeekBar,10);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {

                    if (position<mysongs.size()-1)
                    {
                        setNext();
                    }
                    else
                    {
                        mediaPlayer.pause();
                        play_pause.setBackgroundResource(R.drawable.play);
                        flag=0;
                    }
                }
            });


        }

    }

    @Override
    public void onBackPressed() {

        //minimize app on back pressed
        this.moveTaskToBack(true);

        // or minimizeApp();
    }
    public void minimizeApp() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }


    ArrayList<File> fetchAudio()
    {
        File root= Environment.getExternalStorageDirectory();
        return Audio.findSongs(root);
    }
}