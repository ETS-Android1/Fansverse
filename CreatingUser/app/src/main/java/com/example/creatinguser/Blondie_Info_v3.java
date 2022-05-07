package com.example.creatinguser;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

import androidx.appcompat.app.AppCompatActivity;

public class Blondie_Info_v3 extends AppCompatActivity {

    private Button button;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blondie_info_v3);

        button = (Button) findViewById(R.id.blondiePhotosButton);
        button2 = (Button) findViewById(R.id.blondieMenuButton);

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
        Intent intent = new Intent(this,BlondieGallery.class);
        intent.putExtra("FirstPic", "Blondie1");
        intent.putExtra("SecondPic", "Blondie2");
        startActivity(intent);
    }

    public void openMenuActivity(){
        Intent intent = new Intent(this,BlondieMenu.class);
        intent.putExtra("NameOfPic", "BlondieMenu");
        //intent.putExtra("PicID","interludeMenuPic");
        startActivity(intent);

    }
}