package com.example.creatinguser;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.HttpResponse;
import com.google.logging.type.HttpRequest;

public class LiveScoresPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView Title;
    private Button homePage;
    private Button currScores;
    private TextView currScoresText;
    private Button stats;
    private Button myProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livescores);

        Title = findViewById(R.id.liveScoresPageFansverse);
        homePage = findViewById(R.id.buttonHomePage);
        currScores = findViewById(R.id.buttoncurrScores);
        currScoresText = findViewById(R.id.currScoresText);
        //stats = findViewById(R.id.buttonStats);
        //myProfile = findViewById(R.id.buttonProfile);

        homePage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
            }
        });

        currScores.setOnClickListener(new View.OnClickListener(){

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(View v) {
                CompletableFuture.runAsync(new Runnable() {
                    @Override
                    public void run() {
                        callAPINBA();
                    }
                });

            }
        });
    }

    private void callAPINBA(){
        StringBuilder result = new StringBuilder();
        URL url;
        HttpURLConnection connection;

        try {
            url = new URL("https://api-nba-v1.p.rapidapi.com/games/live/");
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("x-rapidapi-host", "api-nba-v1.p.rapidapi.com");

            connection.setRequestProperty("x-rapidapi-key", "ed43b1ab22msh4caee7f6fe2a8c8p145bfdjsn07ec91a1affb");
            connection.setRequestProperty("content-type", "application/json");

            connection.setRequestMethod("GET");

            InputStream in = connection.getInputStream();
            Reader reader = new BufferedReader(new InputStreamReader(in));

            int data = reader.read();
            while (data != - 1) {
                char current = (char) data;
                result.append(current);
                data = reader.read();
            }
            onResponse(result.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void onResponse(String result){
        try{
            JSONObject jsonResult = new JSONObject(result);
            JSONObject jAPI = jsonResult.getJSONObject("api");
            JSONArray jArrayGames = jAPI.getJSONArray("games");

            if(jArrayGames.length() != 0){
                String currentScores = "There currently is a game in \n";

                for(int n = 0; n < jArrayGames.length(); n++){
                    JSONObject games = jArrayGames.getJSONObject(n);
                    currentScores += games.getString("city") + " in period " + games.getString("currentPeriod") + " \n";
                }
                setText(this.currScoresText, currentScores);
            }
            else{
                String currentScores = "There is no game live right now \n";
                setText(this.currScoresText, currentScores);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setText(TextView text, String value){
        text.setText(value);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
