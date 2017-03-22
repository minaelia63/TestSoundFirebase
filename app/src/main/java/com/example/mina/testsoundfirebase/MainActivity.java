package com.example.mina.testsoundfirebase;
//firebasestorage
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button record;
    private TextView tv;
    private ProgressDialog mprogress;
    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFileName = null;

    private StorageReference mstorage;
    private MediaRecorder mRecorder = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mstorage= FirebaseStorage.getInstance().getReference();
        record= (Button) findViewById(R.id.recordbutton);
        tv= (TextView) findViewById(R.id.Textlabel);
        mprogress=new ProgressDialog(this);

        mFileName= Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName+="/recorder_audio.3gp";
record.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN)
        {
            startRecording();
            tv.setText("Recording start");
        }
        else if(event.getAction()==MotionEvent.ACTION_UP)
        {
            stopRecording();
            tv.setText("Recording stopped");
        }
        return false;
    }
});
    }


    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        UploadAudio();
    }

    private void UploadAudio() {

        mprogress.setMessage("Uploaging audio");
        mprogress.show();
StorageReference filepath=mstorage.child("audio").child("newaudio"+new Random()+".3gp");
        Uri uri= Uri.fromFile(new File(mFileName));
filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

        mprogress.dismiss();

        tv.setText("Uploading finished");


    }
});
    }


}
