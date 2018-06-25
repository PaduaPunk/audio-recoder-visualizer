/*
 * Copyright (C) 2017 Gautam Chibde
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.chemapadua.audiorecodervisualizer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.audiofx.Visualizer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.chemapadua.audiorecodervisualizer.visualizer.BarVisualizer;

import java.util.Random;

public abstract class BaseVisualizer extends View {

    public byte[] bytes;
    public byte[] values;
    public int size;
    protected  byte roundVal = 100;
    protected Paint paint;
    protected Visualizer visualizer;
    protected int color = Color.BLUE;
    protected AudioRecord audio;
    private Thread thread;
    private double lastLevel = 0;
    protected  int bufferSize;
    protected int sampleRate = 8000;
    private String state="";

    public BaseVisualizer(Context context) {

        super(context);
        init(null);
        init();

    }

    public BaseVisualizer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        init();

    }

    public BaseVisualizer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        init();
    }

    private void init(AttributeSet attributeSet) {
        paint = new Paint();

    }

    /**
     * Set color to visualizer with color resource id.
     *
     * @param color color resource id.
     */
    public void setColor(int color) {
        this.color = color;
        this.paint.setColor(this.color);
    }




    public void start(){
        if (audio==null) {
            state = "start";
            size = (int) BarVisualizer.getDensity();
            values = new byte[size];
            setPlayer();
            try {
                bufferSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT);
                audio = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT, bufferSize);
            } catch (Exception e) {
                Log.e("TrackingFlow", "Exception", e);
            }
            audio.startRecording();
            Thread();
        }
    }


    public  void stop(){
    release();
    state ="stop";
    }

    public void Thread() {

        thread = new Thread(new Runnable() {
            public void run() {


                while (state!="stop") {
                    short[] buffer = new short[bufferSize];

                    int bufferReadResult = 1;

                    if (audio != null) {

                        bufferReadResult = audio.read(buffer, 0, bufferSize);
                        double sumLevel = 0;
                        for (int i = 0; i < bufferReadResult; i++) {
                            sumLevel += buffer[i];
                        }
                        lastLevel = Math.abs((sumLevel / bufferReadResult));
                    }

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (lastLevel <= 1) {
                        lastLevel = 1;
                    }

                    int roundVal2 = (int) Math.round(lastLevel) * 5;

                    if (roundVal2 >= 127) {
                        roundVal2 = 127;
                    }
                    roundVal2 = 127 - roundVal2;
                    roundVal = (byte) roundVal2;
                    if (roundVal >= 127) {
                        roundVal = 127;
                    }


                }
            }
        });

        thread.start();
    }

    //Method from voice wave simulator
    public byte rangeSimulator(int s){
        byte bytes=0;
        Random randomGenerator = new Random();
        int randomNum = s+randomGenerator.nextInt((126+1) - s);
        bytes=(byte) randomNum;
        return bytes;
    }


    public void setPlayer() {

        visualizer = new Visualizer(1);
        try {
            visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        }catch (Exception e){
             setPlayer();
        }
       visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
                                              int samplingRate) {

                if(state !="stop") {
                    for (int i = 0; i < size; i++) {
                        values[i] = rangeSimulator(roundVal);
                    }
                }else{
                    for (int i = 0; i < size; i++) {
                        values[i] = 127;
                    }
                }

                BaseVisualizer.this.bytes = values;


                invalidate();
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
                                         int samplingRate) {
            }
        }, Visualizer.getMaxCaptureRate() / 2, true, false);

        visualizer.setEnabled(true);
    }

    public void release() {
        if (audio!=null) {
            audio.stop();
            audio.release();
            audio = null;
            visualizer.release();
        }
    }

    public Visualizer getVisualizer() {
        return visualizer;
    }

    protected abstract void init();
}
