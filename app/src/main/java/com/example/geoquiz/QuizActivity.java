package com.example.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    //MEMBER VARIABLES
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private TextView mQuestionDetailTextView;
    private TextView mYourScore;
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_oceans, true, R.string.Q1_detail),
            new Question(R.string.question_mideast, false, R.string.Q2_detail),
            new Question(R.string.question_africa, false, R.string.Q3_detail),
            new Question(R.string.question_americas, true, R.string.Q4_detail),
            new Question(R.string.question_asia, true, R.string.Q5_detail),
    };
    private int mCurrentIndex; //default is 0
    private int counter; //keeps tally of correct answers
    private static final String TAG = "QuizActivity";
    private static final int REQUEST_CODE_CHEAT = 0; //request code is like an identifier code it sends to the child/children activity so when it receives data back from any of the children it knows which one is reporting back
    private boolean mIsCheater;// a variable to hold the boolean value that it wil receive from its child
    private static final String KEY_INDEX = "index"; //holds the key for the key value pair that will be stored in the bundle


    //METHODS
    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();//eg. currentIndex=0, for the question at that index return the resource Id and store in variable
        mQuestionTextView.setText(question); //takes integer and finds associated string to update the view
    }

    private void checkAnswer(boolean userPressedTrue){
        //takes in boolean variable which will identify whether the user pressed True or False.
        //Then will check user’s answer against answer in the current Question object
        //after determining if user answered correctly, will make a Toast that displays the appropriate message
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;

        if(mIsCheater){
            messageResId = R.string.judgment_toast;//if cheated then display the judgment string in the toast
        }else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                counter = counter + 1;

            } else {
                messageResId = R.string.incorrect_toast;
                int questionDetail = mQuestionBank[mCurrentIndex].getDetailResID();
                mQuestionDetailTextView.setText(questionDetail);
            }
            //when user selects and answer then display question details
        }
       mYourScore.setText(counter + "");
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }


    //PROGRAM
    @Override
    //from layout XML to objects
    protected void onCreate(Bundle savedInstanceState) {//when this instance of activity is created it is given an user interface
        super.onCreate(savedInstanceState); //constructor of superclass
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz); //method called from activity class to instantiate each widget in the layout as defined by its id attributes(in XML)

        //WIRING UP THE QUESTION TEXT VIEW TO MOVE TO NEXT QUESTION
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);//assign text view to variable
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;//set as default so user innocent til proven guilty
                updateQuestion();
            }
        });

        //INFLATING THE SCORE COUNTER AND THE QUESTION DETAIL VIEWS
        mQuestionDetailTextView = (TextView) findViewById(R.id.answer_details);
        mYourScore = (TextView) findViewById(R.id.your_score);

        //WIRING UP THE NEXT & PREV BUTTONS
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here we want to increment the index and update the text view
                mQuestionDetailTextView.setText(""); //clears detail text view
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });

        mPrevButton = (ImageButton) findViewById(R.id.prev_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestionDetailTextView.setText("");
                //here we want to increment the index and update the text view
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        //WIRING UP THE TRUE FALSE BUTTONS
        //access to inflated views by assigning them to m variables
        //setting listeners (for event driven applications - app waits/listens for for an user to click, etc
        mTrueButton = (Button) findViewById(R.id.buttonTrue);
        mTrueButton.setOnClickListener(new View.OnClickListener() {//creates an listener object and passes it in as an arguement for setonclicklistener method
            //object passed is a nameless class because not assigned to a variable name. no need as class will be used in one place only

            @Override
            public void onClick(View v) {//because the view class that was passed implements the onclicklistener method the onclick method must be implemented here

                //now that we will know when a button will be pressed, this is what we want to happen
                //toasts are pop ups that alert the user of something but does not require an action from them
                //here we call the static method from toast class: make text and pass in:
                //context = an instance of activity(subclass of context) bascially telling it which view to show it
                //resId = the reference of the string that we want displayed
                //and duration = how long we want to display it
                //then call show method from toast class to show it
                //Toast.makeText(QuizActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT).show();
                //REPLACED BY (encapsulation)
                checkAnswer(true);
            }
        });
        mFalseButton = (Button) findViewById(R.id.buttonFalse);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  Toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
                //replaced by - encapsulation
                checkAnswer(false);
            }
        });

        //WIRING UP THE CHEAT BUTTON
        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  boolean isAnswerTrue = mQuestionBank[mCurrentIndex].isAnswerTrue(); //saves asnwer to current question in variable
                Intent i = CheatActivity.newIntent(QuizActivity.this, isAnswerTrue); //calls intent method in destination activity and pass in the arguments to create a new intexnt with an extra
                //the reason why the extra is not added here is to adhere to encapusulation and keep the extra with the class thats going to need and use it
                startActivityForResult(i,REQUEST_CODE_CHEAT); //the method called above returns an intent and the intent will be started here to start the new activity
            }
        });

        //checks for value of key value pair in bundle if there is one
        if(savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        updateQuestion();
    }

    //override on SaveInstance method (which is usually called before stop, pause or destroy), to write tha value of mCurrentIndex to bundle constant
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState"); //only neccessary for tracking in Logcat
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex); //add a key value pair to the bundle
    }

    //this method is called by the activity manager but here we override it to handle the result sent back
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){ // the activity manager calls this method on the parent when the user presses back button
        if(resultCode != Activity.RESULT_OK){ //if the user did not reveal asnwer then jump out of block
            return;
        }
        if(requestCode ==  REQUEST_CODE_CHEAT){ //if user revealed asn do this...
            if(data == null){ //first check the intent has an extra with a key value pair. it it doesnt then jump out of block
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data); //call the method from cheatactivty that makes the data more useable to quiz activity by extracting the boolean value from the extra
        }
    }

    //OVERRIDE METHODS FOR ACTIVITY LIFETIME
    //for the purpose of the exercise of checking log output to observe and understand the activity lifecycle
    @Override
        public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() called");
        }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
