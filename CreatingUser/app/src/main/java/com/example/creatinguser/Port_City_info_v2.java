package com.example.creatinguser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

import androidx.appcompat.app.AppCompatActivity;

public class Port_City_info_v2 extends AppCompatActivity {

    private Button button;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_port_city_info_v2);

        button = (Button) findViewById(R.id.portCityPhotosButton);
        button2 = (Button) findViewById(R.id.portCityMenuButton);

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
        Intent intent = new Intent(this,PortCityGallery.class);
        intent.putExtra("FirstPic", "PortCity1");
        intent.putExtra("SecondPic", "PortCity2");
        startActivity(intent);
    }

    public void openMenuActivity(){
        Intent intent = new Intent(this,PortCityMenu.class);
        intent.putExtra("NameOfPic", "PortCityMenu");
        intent.putExtra("PicID","interludeMenuPic");
        startActivity(intent);

    }
}