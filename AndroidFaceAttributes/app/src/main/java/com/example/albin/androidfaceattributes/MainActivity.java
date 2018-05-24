package com.example.albin.androidfaceattributes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.FaceServiceRestClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.rest.ClientException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private FaceServiceClient mFaceServiceClient = new FaceServiceRestClient("https://westcentralus.api.cognitive.microsoft.com/face/v1.0", "541d19e7930642e082c50761fee2c16e");
    //private FaceServiceClient mFaceServiceClient = new FaceServiceRestClient("541d19e7930642e082c50761fee2c16e");
    ImageView mImageView;
    Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.grupo);
        mImageView = (ImageView)findViewById(R.id.imageView);
        mImageView.setImageBitmap(mBitmap);


        Button btnProcess = (Button)findViewById(R.id.btnProcess);
        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectAndFrame(mBitmap);
            }
        });
    }

    private void detectAndFrame(Bitmap mBitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());


        AsyncTask<InputStream, String, Face[]> detectTask = new AsyncTask<InputStream, String, Face[]>() {

            private ProgressDialog pd = new ProgressDialog(MainActivity.this);

            @Override
            protected Face[] doInBackground(InputStream... inputStreams) {

                    publishProgress("Detecting...");
                    FaceServiceClient.FaceAttributeType[] faceAttr = new FaceServiceClient.FaceAttributeType[]{
                            FaceServiceClient.FaceAttributeType.HeadPose,
                            FaceServiceClient.FaceAttributeType.Age,
                            FaceServiceClient.FaceAttributeType.Gender,
                            FaceServiceClient.FaceAttributeType.Smile,
                            FaceServiceClient.FaceAttributeType.FacialHair,

                    };

                    try {
                        Face[] result = mFaceServiceClient.detect(inputStreams[0],
                                true ,
                                false ,
                                faceAttr );

                        if(result == null){
                            publishProgress("No se logro detectar nada");
                            return  null;
                        }

                        publishProgress("Deteccion exitosa");
                        return result;
                    } catch (Exception e) {
                        publishProgress("Error en la deteccion");
                        return  null;
                    }

            }

            @Override
            protected void onPreExecute(){
                pd.show();

            }

            @Override
            protected void onProgressUpdate(String... values){
                pd.setMessage(values[0]);
            }

            @Override
            protected void onPostExecute(Face[] faces){
                pd.dismiss();
                Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
                Gson gson = new Gson();
                String data = gson.toJson(faces);
                intent.putExtra("list_faces", data);
                startActivity(intent);
            }


        };

        detectTask.execute(inputStream);
    }
}
