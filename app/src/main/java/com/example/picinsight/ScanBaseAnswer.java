package com.example.picinsight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import java.io.File;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class ScanBaseAnswer extends AppCompatActivity implements View.OnClickListener {
    String picResult;
    Button captureBtn;
    PreviewView previewView;
    private ImageCapture imageCapture;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_base_answer);
        captureBtn = findViewById(R.id.imageCapture);
        previewView = findViewById(R.id.previewView);
        captureBtn.setOnClickListener(this);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(()->{
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                Log.d("CP", "Success");
                startCameraX(cameraProvider);

            }catch (ExecutionException e){
                e.printStackTrace();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        },getExecutor());
    }
    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }

    private void startCameraX(ProcessCameraProvider cameraProvider) {
        cameraProvider.unbindAll();
        // Camera Selection
        CameraSelector cameraSelector = new CameraSelector.Builder().
                requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        // Preview Case
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        // ImageCapture
        imageCapture = new ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
                .build();
        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview, imageCapture );
    }

    @Override
    public void onClick(View view) {
        int v = view.getId();
        if(v == R.id.imageCapture){
            capturePhoto();
        }
        else{
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }

    private void capturePhoto() {
        // Get a reference to the external files directory
        File photoDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Create the file name with a timestamp
        Date date =  new Date();
        String timestamp = String.valueOf(date.getTime());
        String photoFilePath = photoDir.getAbsolutePath() + "/" + timestamp + ".jpg";
        File photoFile = new File(photoFilePath);

        // Configure the image capture to save to the file
        ImageCapture.OutputFileOptions outputFileOptions =
                new ImageCapture.OutputFileOptions.Builder(photoFile)
                        .build();

        // Take the picture
        imageCapture.takePicture(outputFileOptions, getExecutor(), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Toast.makeText(ScanBaseAnswer.this, "Photo Taken", Toast.LENGTH_SHORT).show();
                Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getPath());
                extractTextFromBitmap(bitmap);
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Toast.makeText(ScanBaseAnswer.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void extractTextFromBitmap(Bitmap bitmap) {
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        Task<Text> result = recognizer.process(InputImage.fromBitmap(bitmap, 0))
                .addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text visionText) {
                        if (visionText != null && !visionText.getText().isEmpty()) {
                            picResult = visionText.getText();
                            Intent intent = new Intent(ScanBaseAnswer.this, TextBaseAnswer.class);
                            intent.putExtra("picResult", picResult);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ScanBaseAnswer.this, "No text recognized", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ML Kit", "Error: " + e.getMessage());
                        Intent intent = new Intent(ScanBaseAnswer.this, TextBaseAnswer.class);
                        intent.putExtra("picResult", e.getMessage());
                        startActivity(intent);
                    }
                });
    }

}