package com.ihaiu.androidthreaddowanzip.learnthreads;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ihaiu.androidthreaddowanzip.R;

public class LearnTask
{

    public AppCompatActivity context;
    public MyTask task;


    public LearnTask setContext(AppCompatActivity context) {
        this.context = context;
        return  this;
    }

    public Button startButton;
    public Button cancelButton;
    public Button stopButton;
    public ProgressBar progressBar;
    public TextView progressText;
    public TextView resultText;

    public LearnTask init()
    {
        startButton = context.findViewById(R.id.startButton);
        cancelButton = context.findViewById(R.id.cancelButton);
        stopButton = context.findViewById(R.id.stopButton);
        progressBar = context.findViewById(R.id.progressBar);
        progressText = context.findViewById(R.id.progressText);
        resultText = context.findViewById(R.id.resutText);

        startButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                task.execute(300);
                if(task == null)
                {
                    task = new MyTask();
                    task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, 300);
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(task != null)
                {
                    task.cancel(true);
                }
            }
        });
        return this;
    }


    public class MyTask extends AsyncTask<Integer, Integer, Integer>
    {
        public int max = 100;
        public int value = 0;

        @Override
        protected void onPreExecute()
        {
            setButtonState(false);
            progressBar.setProgress(0);
            progressText.setText("");
        }

        @Override
        protected Integer doInBackground(Integer... integers)
        {
            int num = 0;
            max = integers[0];
            for(int i = 1; i <= max; i ++)
            {
                publishProgress(i);
                num += i;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return num;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            value = values[0];
            int progress = (int)((value * 1f / max) * 100);
            progressText.setText(String.format("%d/%d  (%d%%)", value, max, progress));
            progressBar.setProgress(value);
            progressBar.setMax(max);

        }

        @Override
        protected void onPostExecute(Integer integer)
        {
            resultText.setText(String.format("执行结果: %d", integer));
            task = null;
            setButtonState(true);
        }

        @Override
        protected void onCancelled(Integer integer)
        {
            resultText.setText(String.format("取消执行，当前结果: %d", integer));
            task = null;
            setButtonState(true);
        }

        @Override
        protected void onCancelled()
        {
            resultText.setText(String.format("取消执行"));
            task = null;
            setButtonState(true);
        }

        @Override
        protected void finalize() throws Throwable
        {
            super.finalize();
        }


        private void setButtonState(boolean startEnable) {
            startButton.setEnabled(startEnable);
            cancelButton.setEnabled(!startEnable);
            stopButton.setEnabled(!startEnable);
        }
    }

}
