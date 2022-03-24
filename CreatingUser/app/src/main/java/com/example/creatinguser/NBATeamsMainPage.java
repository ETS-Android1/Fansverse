package com.example.creatinguser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class NBATeamsMainPage extends AppCompatActivity {
    private TextView Title;
    private Button sportsMainPage;
    //private Button showNBATeams;
    private TextView nbaTeamsText;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nbateams);

        Title = findViewById(R.id.nbaTeamsPageFansverse);
        sportsMainPage = findViewById(R.id.buttonSportsTeamMainPage);
        nbaTeamsText = findViewById(R.id.nbaTeamsText);
        //showNBATeams = findViewById(R.id.buttonShowTeams);

        sportsMainPage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SportsTeamsMainPage.class);
                startActivity(intent);
            }
        });

        //showNBATeams.setOnClickListener(new View.OnClickListener(){

            //public void onClick(View v) {
                CompletableFuture.runAsync(new Runnable() {
                    @Override
                    public void run() {
                        callAPINBATeams();
                    }
                });

            //}
        //});
    }

    private void callAPINBATeams(){
        StringBuilder result = new StringBuilder();
        URL url;
        HttpURLConnection connection;

        try {
            url = new URL("https://api-nba-v1.p.rapidapi.com/teams/league/standard");
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
            onResponseTeams(result.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void onResponseTeams(String result){
        try{
            JSONObject jsonResult = new JSONObject(result);
            JSONObject jAPI = jsonResult.getJSONObject("api");
            JSONArray jArrayTeams = jAPI.getJSONArray("teams");

            if(jArrayTeams.length() != 0){

                for(int n = 0; n < jArrayTeams.length(); n++) {
                    JSONObject teams = jArrayTeams.getJSONObject(n);
                    String nbaTeams = "";
                    String teamCheck = teams.getString("nbaFranchise");
                    if (teamCheck.equals("1")) {
                        if (teams.getString("fullName").equals("Home Team Stephen A")) {
                        } else {
                            nbaTeams += teams.getString("fullName") + " \n";
                            setText(this.nbaTeamsText, nbaTeams, teams);
                        }
                    }
                }
            }
            else{
                String nbaTeams = "There are no teams you messed up the code \n";
                setText(this.nbaTeamsText, nbaTeams, null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setText(TextView text, String value, JSONObject teams){
        text.setText(value);
        text.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), SpecificNBATeam.class);
                try {
                    intent.putExtra("teamID", teams.getString("teamId"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });
    }

}
