package com.example.geoquiz;

/**
 * Created by Ashley on 09/11/2017.
 */

public class Question {
    private int mTextResId;
    private boolean mAnswerTrue;
    private int mDetailResID;
    private boolean mUserCheated;

    public Question(int textResId, boolean answerTrue, int detailResID){
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        mDetailResID = detailResID;
        mUserCheated = false; //default as false
    }

    public int getTextResId() {
        return mTextResId;
    }

    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }

    public int getDetailResID() {
        return mDetailResID;
    }

    public void setDetailResID(int detailResID) {
        mDetailResID = detailResID;
    }
}
