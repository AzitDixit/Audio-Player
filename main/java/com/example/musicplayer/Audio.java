package com.example.musicplayer;

import java.io.File;
import java.util.ArrayList;

public class Audio {
    public  static ArrayList<File> findSongs(File root) {
        ArrayList<File>list=new ArrayList<>();
        File[] files=root.listFiles();
        for (File singleFile:files        ){
            if (singleFile.isDirectory()&& !singleFile.isHidden() && !singleFile.getName().equalsIgnoreCase("android")){
                list.addAll(findSongs(singleFile));
            }
            else
            {
                if (singleFile.getName().endsWith(".mp3")&& !singleFile.getName().contains("Call@"))

                {
                    list.add(singleFile);
                }
            }
        }

        return list;
    }
}
