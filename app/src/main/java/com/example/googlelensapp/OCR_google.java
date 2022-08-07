package com.example.googlelensapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.ArrayList;

public class OCR_google extends AppCompatActivity {

    private ImageView img;
    private Button snap, logout;
    private Bitmap imageBitmap;
    private TextView text123;
    private final int Capture_code = 1000;
    private String title, link, displayed_link, snippet;
    TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    LoginActivity lo = new LoginActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_login);
        // initializing all our variables for views
        img = (ImageView) findViewById(R.id.image);
        snap = (Button) findViewById(R.id.snapbtn);
        logout = findViewById(R.id.logout);
        text123 = findViewById(R.id.textView2);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
        snap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent img_capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(img_capture,Capture_code);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gsc.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                        Toast.makeText(OCR_google.this,"Signed Out",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OCR_google.this,MainActivity.class));
                    }
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // inside on activity result method we are
        // setting our image to our image view from bitmap.
        if (requestCode == Capture_code && resultCode == RESULT_OK) {
            imageBitmap = (Bitmap) (data.getExtras().get("data"));
            // on below line we are setting our
            // bitmap to our image view.
            img.setImageBitmap(imageBitmap);
            getTextfromimage();
        }
    }
    protected void getTextfromimage()
    {
        InputImage image = InputImage.fromBitmap(imageBitmap, 0);
        Task<Text> result = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {
                processTextBlock(text);
                text123.setText(text.getText().toString());
            }
        });
    }
    private void processTextBlock(Text result) {
        String resultText = result.getText();
        for (Text.TextBlock block : result.getTextBlocks()) {
            String blockText = block.getText();
            Point[] blockCornerPoints = block.getCornerPoints();
            Rect blockFrame = block.getBoundingBox();
            for (Text.Line line : block.getLines()) {
                String lineText = line.getText();
                Point[] lineCornerPoints = line.getCornerPoints();
                Rect lineFrame = line.getBoundingBox();
                for (Text.Element element : line.getElements()) {
                    String elementText = element.getText();
                    Point[] elementCornerPoints = element.getCornerPoints();
                    Rect elementFrame = element.getBoundingBox();
                }
            }
        }
    }
}