package com.example.creatinguser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Interlude_info_v2 extends AppCompatActivity {

    private Button button;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interlude_info_v2);

        button = (Button) findViewById(R.id.interludePhotosButton);
        button2 = (Button) findViewById(R.id.interludeMenuButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotoActivity();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuActivity();
            }
        });
    }
        public void openPhotoActivity(){
            Intent intent = new Intent(this,LocationGallery.class);
            intent.putExtra("FirstPic","Interlude1");
            intent.putExtra("SecondPic","Interlude2");
            startActivity(intent);
        }

        public void openMenuActivity(){
            Intent intent = new Intent(this,MenuDisplay.class);
            intent.putExtra("NameOfPic", "InterludeMenu");
            intent.putExtra("PicID","interludeMenuPic");
            startActivity(intent);

        }
}
