package com.example.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE = "com.example.android.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "com.example.android.geoquiz.answer_shown";
    private boolean mAnswerIsTrue;
    private TextView mAnswerTextView;
    private Button mShowAnswer;
    private static final String TAG = "QuizActivity";
    private static final String KEY_CHEATED = "cheated";

    //this itent is used to pass over the answer to the question from the parent to the child to be displayed in the cheat activity
    public static Intent newIntent(Context packageContext, boolean answerIsTrue){
        //a new intent created to and the extra added on and returned to caller
        Intent i = new Intent(packageContext, CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return i;
    }

    //decoding the result intent in a format that quizactivity can use
    //returns the boolean value from its extra
    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false); //getter to access the value of the key value pair extra
        //the second argument is the default value if nothinh returned from getter
        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        //WIRING UP THE SHOW ANSWER BUTTON
        //here we will register the user cheated
        mShowAnswer = (Button) findViewById(R.id.show_answer_button);
        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //reveals the answer to the question and also r
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                setAnswerShownResult(true); //set to true because the reveal button was pressed
            }
        });
    }
        //because the parent activity used startActivityForResult method the child needs to use setResult (&package up its info) to send back to the parent
        private void setAnswerShownResult(boolean isAnswerShown){ //creates new intent and adds an extra with value of true, then packages it up to send info back to parent
            Intent data = new Intent();
            data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
            setResult(RESULT_OK, data);
            //when user presses back button the activity manager will call the onActivity result on the parent
        }

        @Override
        public void onSaveInstanceState(Bundle savedInstanceState){
            super.onSaveInstanceState(savedInstanceState);
            Log.i(TAG, "onSaveInstanceStateCheat"); //only neccessary for tracking in Logcat
            //savedInstanceState.putInt(KEY_CHEATED, wasAnswerShown() );
        }
    }
