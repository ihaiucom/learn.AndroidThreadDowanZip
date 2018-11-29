package com.ihaiu.androidthreaddowanzip.zip;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ihaiu.androidthreaddowanzip.R;

import com.game.archiver.Archiver;
import com.game.archiver.IArchiverListener;

public class LearnAssetCopy
{

    public AppCompatActivity context;


    public LearnAssetCopy setContext(AppCompatActivity context)
    {
        this.context = context;
        return  this;
    }


    public String TAG = "LearnAssetCopy";

    public Button assetsCopyStartButton;
    public ProgressBar assetsCopyProgressBar;
    public TextView assetsCopyProgressText;

    public LearnAssetCopy init()
    {
        assetsCopyStartButton = context.findViewById(R.id.assetsCopyStartButton);
        assetsCopyProgressBar = context.findViewById(R.id.assetsCopyProgressBar);
        assetsCopyProgressText = context.findViewById(R.id.assetsCopyProgressText);

        final String assetsPath = "www";
        final String savePath =  context.getFilesDir().getAbsolutePath() + "/www";

        assetsCopyStartButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    Archiver.assetsCopyTo(context, assetsPath, savePath,133537339, new IArchiverListener() {
                        @Override
                        public void onArchiverPre()
                        {
                            setButtonState(false);
                        }

                        @Override
                        public void onArchiverStart()
                        {
                            assetsCopyProgressBar.setProgress(0);
                            assetsCopyProgressText.setText(String.format("%d%%", 0));
                        }

                        @Override
                        public void onArchiverProgress(int current, int total)
                        {
                            int percent = (int)((current * 1f / total) * 100);


                            assetsCopyProgressBar.setMax(total);
                            assetsCopyProgressBar.setProgress(current);
                            assetsCopyProgressText.setText(String.format("%d%%  (%d/%d)", percent, current, total));
                        }

                        @Override
                        public void onArchiverComplete()
                        {
//                            assetsCopyProgressBar.setProgress(100);
//                            assetsCopyProgressText.setText(String.format("%d%%", 100));
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
        assetsCopyStartButton.setEnabled(startEnable);
    }

}
