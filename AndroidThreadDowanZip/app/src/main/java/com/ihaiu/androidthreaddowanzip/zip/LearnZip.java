package com.ihaiu.androidthreaddowanzip.zip;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ihaiu.androidthreaddowanzip.R;

import com.game.archiver.Archiver;
import com.game.archiver.IArchiverListener;

public class LearnZip
{

    public AppCompatActivity context;
    public String zipPath;
    public String outPath;


    public LearnZip setContext(AppCompatActivity context)
    {
        this.context = context;
        return  this;
    }
    public LearnZip setZipFilePath(String path)
    {
        this.zipPath = path;
        outPath = zipPath.replace(".zip", "");
        return  this;
    }


    public String TAG = "LearnZip";

    public Button zipStartButton;
    public ProgressBar zipProgressBar;
    public TextView zipProgressText;

    public LearnZip init()
    {
        zipStartButton = context.findViewById(R.id.zipStartButton);
        zipProgressBar = context.findViewById(R.id.zipProgressBar);
        zipProgressText = context.findViewById(R.id.zipProgressText);

        zipStartButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    Archiver.unArchiver(zipPath, outPath, "", new IArchiverListener() {
                        @Override
                        public void onArchiverPre()
                        {
                            setButtonState(false);
                        }

                        @Override
                        public void onArchiverStart()
                        {
                            zipProgressBar.setProgress(0);
                            zipProgressText.setText(String.format("%d%%", 0));
                        }

                        @Override
                        public void onArchiverProgress(int current, int total)
                        {
                            int percent = (int)((current * 1f / total) * 100);


                            zipProgressBar.setMax(total);
                            zipProgressBar.setProgress(current);
                            zipProgressText.setText(String.format("%d%%  (%d/%d)", percent, current, total));
                        }

                        @Override
                        public void onArchiverComplete()
                        {
//                            zipProgressBar.setProgress(100);
//                            zipProgressText.setText(String.format("%d%%", 100));
                            setButtonState(true);

                        }

                        @Override
                        public void onArchiverFail(String error)
                        {
                            setButtonState(true);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return this;
    }

    private void setButtonState(boolean startEnable) {
        zipStartButton.setEnabled(startEnable);
    }

}
