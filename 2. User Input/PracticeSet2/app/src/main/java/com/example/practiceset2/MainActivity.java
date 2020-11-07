package com.example.practiceset2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int scoreTeamA = 0;
    private int scoreTeamB = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    /**
     * Displays the given score for Team A.
     */
    public void displayForTeamA(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_a_score);
        scoreView.setText(String.valueOf(score));
    }
    /**
     * This method increases points with 3
     */
    public void Points3_A(View view) {
        scoreTeamA += 3;
        displayForTeamA(scoreTeamA);
    }
    /**
     * This method increases points with 2
     */
    public void Points2_A(View view) {
        scoreTeamA += 2;
        displayForTeamA(scoreTeamA);
    }
    /**
     * This method increases points with 3
     */
    public void freeThrow_A(View view) {
        scoreTeamA += 1;
        displayForTeamA(scoreTeamA);
    }

    /**
     * Displays the given score for Team B.
     */
    public void displayForTeamB(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_b_score);
        scoreView.setText(String.valueOf(score));
    }
    /**
     * This method increases points with 3 of B
     */
    public void Points3_B(View view) {
        scoreTeamB += 3;
        displayForTeamB(scoreTeamB);
    }
    /**
     * This method increases points with 2 of B
     */
    public void Points2_B(View view) {
        scoreTeamB += 2;
        displayForTeamB(scoreTeamB);
    }
    /**
     * This method increases points with 3 of B
     */
    public void freeThrow_B(View view) {
        scoreTeamB += 1;
        displayForTeamB(scoreTeamB);
    }
    public void Reset(View view) {
        scoreTeamA = 0;
        scoreTeamB = 0;
        displayForTeamA(scoreTeamA);
        displayForTeamB(scoreTeamB);
    }
}