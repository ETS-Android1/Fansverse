package com.example.creatinguser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Interlude_info_v2 extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interlude_info_v2);

        button = (Button) findViewById(R.id.interludePhotosButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotoActivity();
            }
        });
    }
        public void openPhotoActivity(){
            Intent intent = new Intent(this,LocationGallery.class);
            startActivity(intent);
        }
}
