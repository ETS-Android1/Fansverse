package com.example.creatinguser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



import androidx.appcompat.app.AppCompatActivity;

public class CrookedDuck_info_v2 extends AppCompatActivity {

    private Button button;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crooked_duck_info_v2);

        button = (Button) findViewById(R.id.crookedDuckPhotosButton);
        button2 = (Button) findViewById(R.id.crookedDuckMenuButton);

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
        Intent intent = new Intent(this,CrookedDuckGallery.class);
        intent.putExtra("FirstPic","CrookedDuck1");
        intent.putExtra("SecondPic","CrookedDuck2");
        startActivity(intent);
    }

    public void openMenuActivity(){
        Intent intent = new Intent(this,crookedDuckMenu.class);
        intent.putExtra("NameOfPic", "CrookedDuckMenu");
        //intent.putExtra("PicID","interludeMenuPic");
        startActivity(intent);

    }
}