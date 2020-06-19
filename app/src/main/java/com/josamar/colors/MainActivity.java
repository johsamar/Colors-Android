package com.josamar.colors;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView lblTargetColor = null;
    private TextView lblProposedColor = null;
    private SeekBar sbrRed = null;
    private SeekBar sbrGreen = null;
    private SeekBar sbrBlue = null;
    private TextView lblRedValue = null;
    private TextView lblGreenValue = null;
    private TextView lblBlueValue = null;
    private Button btnGetScore = null;
    private Button btnNewColor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intViews();
        initEvents();
    }
    private void intViews(){
        lblTargetColor = findViewById(R.id.lblTargetColor);
        lblProposedColor = findViewById(R.id.lblProposedColor);

        sbrRed = findViewById(R.id.sbrRed);
        sbrGreen = findViewById(R.id.sbrGreen);
        sbrBlue = findViewById(R.id.sbrBlue);

        lblRedValue = findViewById(R.id.lblRedValue);
        lblGreenValue = findViewById(R.id.lblGreenValue);
        lblBlueValue = findViewById(R.id.lblBlueValue);

        btnGetScore = findViewById(R.id.btnGetScore);
        btnNewColor = findViewById(R.id.btnNewColor);

    }

    private void initEvents(){
        SeekBar[] seekBars = {sbrRed,sbrGreen,sbrBlue};
        for (SeekBar seekBar : seekBars){
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        updateValues();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar)  {
                    }
                });
        }
        btnGetScore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showScore();
            }
        });
        btnNewColor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                restartGame();
            }
        });
    }

    public int randomColor(){
        Random rand = new Random();
        int redVal = rand.nextInt(256);
        int greenVal = rand.nextInt(256);
        int blueVal = rand.nextInt(256);
        int color = Color.rgb(redVal,greenVal,blueVal);
        return color;
    }
    public int getSuggestedTextColor(int backColor){
        int redVal = Color.red(backColor);
        int greenVal = Color.green(backColor);
        int blueVal = Color.blue(backColor);
        int grayVal = (int) (redVal*0.20 + greenVal*0.75+blueVal*0.05);
        int textColor = Color.BLACK;
        if (255 - grayVal > grayVal){
            textColor = Color.WHITE;
        }
        return textColor;
    }
    public void newTargetColor(){
        int newBackColor = randomColor();
        int newTextColor = getSuggestedTextColor(newBackColor);

        lblTargetColor.setBackgroundColor(newBackColor);
        lblTargetColor.setTextColor(newTextColor);
    }
    public void newProposedColor(){
        int newBackColor = randomColor();
        int newTextColor = getSuggestedTextColor(newBackColor);

        sbrRed.setProgress(Color.red(newBackColor));
        sbrGreen.setProgress(Color.green(newBackColor));
        sbrBlue.setProgress(Color.blue(newBackColor));

        lblProposedColor.setBackgroundColor(newBackColor);
        lblProposedColor.setTextColor(newTextColor);
    }
    public double distance(int color1, int color2){
        int redVal1 = Color.red(color1);
        int greenVal1 = Color.green(color1);
        int blueVal1 = Color.blue(color1);

        int redVal2 = Color.red(color2);
        int greenVal2 = Color.green(color2);
        int blueVal2 = Color.blue(color2);

        int resRedVal = (redVal1-redVal2)*(redVal1-redVal2);
        int resGreenVal = (greenVal1-greenVal2)*(greenVal1-greenVal2);
        int resBlueVal = (blueVal1-blueVal2)*(blueVal1-blueVal2);

        double distance = Math.sqrt(resRedVal+resGreenVal+resBlueVal);
        return distance;

    }
    public void updateValues(){
        int redVal = sbrRed.getProgress();
        int greenVal = sbrGreen.getProgress();
        int blueVal = sbrBlue.getProgress();
        int curBackColor = Color.rgb(redVal,greenVal,blueVal);
        int newTextColor = getSuggestedTextColor(curBackColor);

        lblRedValue.setText(String.valueOf(redVal));
        lblGreenValue.setText(String.valueOf(greenVal));
        lblBlueValue.setText(String.valueOf(blueVal));

        lblProposedColor.setBackgroundColor(curBackColor);
        lblProposedColor.setTextColor(newTextColor);
    }
    public int getTargetColor(){
        int targetColor = ((ColorDrawable) lblTargetColor.getBackground()).getColor();
        return targetColor;
    }
    public int getProposedColor(){
        int proposedColor = ((ColorDrawable) lblProposedColor.getBackground()).getColor();
        return proposedColor;
    }
    public int getScore(){
        int targetColor = getTargetColor();
        int proposedColor = getProposedColor();

        double distance = distance(targetColor,proposedColor);

        return (int) (100 - Math.min(distance,100));
    }
    public void showScore(){
        final String RED = getString(R.string.Red);
        final String GREEN = getString(R.string.Green);
        final String BLUE = getString(R.string.Blue);
        final String VERY_LOW = getString(R.string.Very_low);
        final String LOW = getString(R.string.Low);
        final String VERY_HIGH = getString(R.string.Very_high);
        final String HIGH = getString(R.string.High);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        StringBuilder text = new StringBuilder();
        StringBuilder tips = new StringBuilder();

        int redDiff = Color.red(getTargetColor())-Color.red(getProposedColor());
        int greenDiff = Color.green(getTargetColor())-Color.green(getProposedColor());
        int blueDiff = Color.blue(getTargetColor())-Color.blue(getProposedColor());

        text.append(getString(R.string.Your_score_is, String.valueOf(getScore())));

        if (redDiff>10){
            tips.append("\n");
            tips.append(getString(R.string.X_is_Y, RED.toLowerCase(),VERY_LOW.toLowerCase()));
        } else if (redDiff>0){
            tips.append("\n");
            tips.append(getString(R.string.X_is_Y, RED.toLowerCase(),LOW.toLowerCase()));
        } else if (redDiff<-10){
            tips.append("\n");
            tips.append(getString(R.string.X_is_Y, RED.toLowerCase(),VERY_HIGH.toLowerCase()));
        } else if (redDiff<0){
            tips.append("\n");
            tips.append(getString(R.string.X_is_Y, RED.toLowerCase(),HIGH.toLowerCase()));
        }
        if (greenDiff>10){
            tips.append("\n");
            tips.append(getString(R.string.X_is_Y, GREEN.toLowerCase(),VERY_LOW.toLowerCase()));
        } else if (greenDiff>0){
            tips.append("\n");
            tips.append(getString(R.string.X_is_Y, GREEN.toLowerCase(),LOW.toLowerCase()));
        } else if (greenDiff<-10){
            tips.append("\n");
            tips.append(getString(R.string.X_is_Y, GREEN.toLowerCase(),VERY_HIGH.toLowerCase()));
        } else if (greenDiff<0){
            tips.append("\n");
            tips.append(getString(R.string.X_is_Y, GREEN.toLowerCase(),HIGH.toLowerCase()));
        }
        if (blueDiff>10){
            tips.append("\n");
            tips.append(getString(R.string.X_is_Y, BLUE.toLowerCase(),VERY_LOW.toLowerCase()));
        } else if (blueDiff>0){
            tips.append("\n");
            tips.append(getString(R.string.X_is_Y, BLUE.toLowerCase(),LOW.toLowerCase()));
        } else if (blueDiff<-10){
            tips.append("\n");
            tips.append(getString(R.string.X_is_Y, BLUE.toLowerCase(),VERY_HIGH.toLowerCase()));
        } else if (blueDiff<0){
            tips.append("\n");
            tips.append(getString(R.string.X_is_Y, BLUE.toLowerCase(),HIGH.toLowerCase()));
        }
        if (tips.length()>0){
            text.append("\n\n");
            text.append(getString(R.string.Tips));
            text.append(tips);
        }
        alert.setMessage(text);
        alert.setPositiveButton("OK",null);

        alert.show();
    }
    public void restartGame(){

        newTargetColor();
        newProposedColor();

    }
}