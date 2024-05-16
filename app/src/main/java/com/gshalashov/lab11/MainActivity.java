package com.gshalashov.lab11;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {
    public ImageView imgView;
    Dialog answerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgView = findViewById(R.id.image_view);
        answerDialog = new Dialog(this);
        answerDialog.setContentView(R.layout.answer_dialog);

    }

    private Bitmap loadImageFromNetwork(String url) {
        try {
            Bitmap bitmap;
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void loadImage(View view) {
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute("https://sun89-1.userapi.com/s/v1/if1/OXYKwzqOwhqGRtdzfv1liTqxPWj_xQQegOv-To3Q-25--XjzPRqn_dBkjALDWf1Qmk6vPgol.jpg?quality=96&crop=0,0,674,674&as=50x50,100x100,200x200,400x400&ava=1&u=onajHo7qRkfy1JfxOz3yoxzP4gcCdcC_raokISg7aGY&cs=200x200");
    }

    public void onClick(View view) {
        answerDialog.show();
    }

    public void calculate(View view) {
        ;
        TextView answerText = answerDialog.findViewById(R.id.answer_text);
        EditText editText = answerDialog.findViewById(R.id.array_edt_text);
        String inputString = editText.getText().toString();
        String[] stringNumbers = inputString.split(" ");

        double[] numbers = new double[stringNumbers.length];
        for (int i = 0; i < stringNumbers.length; i++) {
            numbers[i] = Integer.parseInt(stringNumbers[i]);
        }

        double maxAbsoluteValue = findMaxAbsoluteValue(numbers);
        double sumBetweenPositives = calculateSumBetweenPositives(numbers);
        answerText.setText("Максимальный элемент по модулю: " + maxAbsoluteValue + "\n\nСумма элементов между первым и вторым положительными элементами: " + sumBetweenPositives);

    }

    public class DownloadTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            return loadImageFromNetwork(strings[0]);
        }

        @Override
        public void onPostExecute(Bitmap bitmap) {
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
            imgView.setImageBitmap(bitmap);
        }
    }


    private static double findMaxAbsoluteValue(double[] numbers) {
        return Arrays.stream(numbers)
                .map(Math::abs)
                .max()
                .orElse(0.0);
    }

    private static double calculateSumBetweenPositives(double[] numbers) {
        double sum = 0;
        int firstPositiveIndex = -1;
        int secondPositiveIndex = -1;

        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] > 0) {
                if (firstPositiveIndex == -1) {
                    firstPositiveIndex = i;
                } else {
                    secondPositiveIndex = i;
                    break;
                }
            }
        }

        if (firstPositiveIndex != -1 && secondPositiveIndex != -1) {
            for (int i = firstPositiveIndex + 1; i < secondPositiveIndex; i++) {
                sum += numbers[i];
            }

            return sum;
        }
        return sum;
    }
}