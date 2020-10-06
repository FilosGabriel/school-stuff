package com.example.morsecode.MorseCode;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MorseFlashTask extends AsyncTask<String, String, String> {

    public MorseFlashTask(CameraManager cameraManager, TextView preview, Button stop, int unit) {
        this.cameraManager = cameraManager;
        this.preview = preview;
        this.stopButton = stop;
        this.morseCode = new MorseCode();
        this.letters = "";
        this.unit = unit;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        stopButton.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... strings) {
        String codes = morseCode.encodeToMorse(strings[0]);
        Log.d("encodeMorse", "doInBackground: " + codes);
        this.letters = strings[0].replaceAll("\\s+", "");
        boolean wainting = false;
        int pos = 0;
        for (String code : codes.split("")) {
            publishProgress(String.valueOf(letters.charAt(pos)));
            if (isCancelled())
                return "";
            switch (code) {
                case "*":
                    wainting = true;
                    flash(1, unit);
                    break;
                case "-":
                    wainting = true;
                    flash(3, unit);
                    break;
                case " ":
                    pos++;
                    publishProgress(" ");
                    waitC(7 * unit);
                    break;
                case "_":
                    pos++;
                    publishProgress(" ");
                    waitC(3 * unit);
                    break;
                default:
                    break;
            }
            if (wainting) {
                waitC(unit);
                wainting = false;
            }
        }
        return "";
    }

    @Override
    protected void onProgressUpdate(String... values) {
        Log.d("Morse task", "onProgressUpdate: " + values[0]);
        if (!values[0].equals(" "))
            preview.setText(values[0] + "\n" + morseCode.getEncoding(values[0].toLowerCase()));
        else
            preview.setText(" ");

    }

    @Override
    protected void onPostExecute(String bitmap) {
        super.onPostExecute("");
        preview.setText("");
        stopButton.setVisibility(View.GONE);


    }

    private void waitC(int unit) {
        try {
            Thread.sleep(1 * unit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void flash(int time, int unit) {
        flashLightOn();
        try {
            Thread.sleep(unit * time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        flashLightOff();
    }

    private void flashLightOn() {

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
        } catch (CameraAccessException e) {
        }
    }

    private void flashLightOff() {

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
        } catch (CameraAccessException e) {
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        preview.setText("");
//        stopButton.setVisibility(View.GONE);

    }

    private final Button stopButton;
    private CameraManager cameraManager;
    private TextView preview;
    private MorseCode morseCode;
    private String letters;
    private int unit;

}
