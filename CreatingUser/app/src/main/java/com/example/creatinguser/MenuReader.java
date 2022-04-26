package com.example.creatinguser;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MenuReader extends AppCompatActivity {

    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_reader);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            path = extras.getString("path");
            //The key argument here must match that used in the other activity
        }

        StringBuilder text = new StringBuilder();
        try {

            File file = new File(path.toString());

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close() ;
        }catch (IOException e) {
            e.printStackTrace();
        }

        TextView tv = (TextView)findViewById(R.id.menuTextView);
        tv.setText(text.toString()); ////Set the text to text view.
    }

    public MenuReader(String menuPath)
    {
        this.path=menuPath;
    }

   public void PrintMenu()
    {
        StringBuilder text = new StringBuilder();
        try {

            File file = new File(this.path);

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close() ;
        }catch (IOException e) {
            e.printStackTrace();
        }

        TextView tv = (TextView)findViewById(R.id.menuTextView);
        tv.setText(text.toString()); ////Set the text to text view.

    }
}