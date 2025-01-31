package com.example.math_kidz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class MultiplicationMedium extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference referenceProfile;
    private FirebaseAuth mAuth;
    private int CorrectAnswer;
    TextView scoreTextView;
    int Score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplication_medium);


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        //Extracts user reference from Database
        referenceProfile = firebaseDatabase.getInstance().getReference("Registered Users").child(mAuth.getUid());
        scoreTextView = (TextView) findViewById(R.id.ScoreTracker);

        getdata();

        ImageButton BackArrow = (ImageButton) findViewById(R.id.BackArrow);
        BackArrow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SubjectDifficultyMultiplication.class);
                startActivity(i);


            }
        });

        Random rand = new Random(); //instance of random class


        //generate random values from 0-99
        int upperbound = 100;
        //generate random values from 0-9
        int upperbound2=10;
        int AddRand1 = rand.nextInt(upperbound);
        int AddRand2 = rand.nextInt(upperbound2);


        //generating answer for user to pick from
        CorrectAnswer = AddRand1 * AddRand2;



        TextView questionTextView = (TextView) findViewById(R.id.MultiplicationMediumTextView);
        questionTextView.setText(AddRand1 + " * " + AddRand2);

        EditText UserInput = (EditText) findViewById(R.id.MultMediumInput);

        Button AnswerChecker = (Button) findViewById(R.id.CheckButton);



        AnswerChecker.setOnClickListener(new View.OnClickListener() {



            public void onClick(View view) {

//Checking user input vs answer
                if (UserInput.getText().toString().equals(String.valueOf(CorrectAnswer))) {
                    boolean correct = true;
                    ScoreDisplay(Score, correct);
                    CorrectAnswer=QuestionGenerator();
                    UserInput.setText("");

                }
                else{
                    boolean incorrect = false;
                    ScoreDisplay(Score, incorrect);

                }

            }
            //Incrementing and decrementing score based on answer validity
            private void ScoreDisplay(int num, boolean ans) {
                if(ans == true)
                    Score = Score + 1;
                else
                    Score = Score - 1;
                referenceProfile.child("totalScore0").setValue(Score);
                getdata();


            }
            //method to generate new question
            public int QuestionGenerator() {
                //generate random values from 0-99
                int upperbound = 100;
                //generate random values from 0-9
                int upperbound2 = 10;
                int AddRand1 = rand.nextInt(upperbound);
                int AddRand2 = rand.nextInt(upperbound2);
                //generating answer for user to pick from
                int NewCorrectAnswer = AddRand1 * AddRand2;
                questionTextView.setText(AddRand1 + " * " + AddRand2);
                return NewCorrectAnswer;


            }


        });





    }


    private void getdata() {

        // calling add value event listener method
        // for getting the values from database.
        referenceProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String score = snapshot.child("totalScore0").getValue().toString();
                scoreTextView.setText(score);
                Score = Integer.parseInt(score);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(MultiplicationMedium.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}