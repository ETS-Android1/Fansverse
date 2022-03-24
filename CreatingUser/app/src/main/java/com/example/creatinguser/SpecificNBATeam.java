package com.example.creatinguser;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

public class SpecificNBATeam extends AppCompatActivity {
    private TextView Title;
    private TextView teamPlayers;
    private Button nbaMainPage;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nbateam);

        Title = findViewById(R.id.nbaTeamPageFansverse);
        nbaMainPage = findViewById(R.id.buttonNBATeamMainPage);
        teamPlayers = findViewById(R.id.nbaTeamText);


        CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                callAPINBATeam();
            }
        });
    }

    private void callAPINBATeam(){
        StringBuilder result = new StringBuilder();
        URL url;
        HttpURLConnection connection;
        Bundle extras = getIntent().getExtras();

        try {
            url = new URL("https", "api-nba-v1.p.rapidapi.com/players/teamId", "/" + extras.getString("teamId"));
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
            onResponseTeam(result.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onResponseTeam(String result){
        try{
            JSONObject jsonResult = new JSONObject(result);
            JSONObject jAPI = jsonResult.getJSONObject("api");
            JSONArray jArrayTeams = jAPI.getJSONArray("players");

            if(jArrayTeams.length() != 0){
                String nbaTeam = "";

                for(int n = 0; n < jArrayTeams.length(); n++) {
                    JSONObject teams = jArrayTeams.getJSONObject(n);
                    nbaTeam += teams.getString("firstName") + " " + teams.getString("lastName");
                }
                setText(this.teamPlayers, nbaTeam);
            }
            else{
                String nbaTeams = "There are no teams you messed up the code \n";
                setText(this.teamPlayers, nbaTeams);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setText(TextView text, String value){
        text.setText(value);
    }
}
