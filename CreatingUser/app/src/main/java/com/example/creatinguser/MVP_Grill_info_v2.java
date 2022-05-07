package com.example.creatinguser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MVP_Grill_info_v2 extends AppCompatActivity {

    private Button button;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp_grill_info_v2);

        button = (Button) findViewById(R.id.mvpPhotosButton);
        button2 = (Button) findViewById(R.id.mvpMenuButton);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotoActivity();
            }
        });

        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenuActivity();
            }
        });
    }

    public void openPhotoActivity(){
        Intent intent = new Intent(this,MVPGallery.class);
        intent.putExtra("FirstPic", "MVP1");
        intent.putExtra("SecondPic", "MVP2");
        startActivity(intent);
    }

    public void openMenuActivity(){
        Intent intent = new Intent(this,MVPGrillMenu.class);
        intent.putExtra("NameOfPic", "mvpGrillMenu");
        //intent.putExtra("PicID","interludeMenuPic");
        startActivity(intent);

    }
}