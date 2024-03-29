package pdb.cm.fc.ul.pt.pdb.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

import pdb.cm.fc.ul.pt.pdb.R;

public class PitchView extends View {

    private static final int BALL_DIAMETER = 100, GOAL_DIAMETER = 150;
    private static final float FRAME_TIME = 0.266f;
    private static final double GOAL_DISTANCE = 80;
    private PitchViewListener mListener;
    private Context mContext;
    private Bitmap mBallSrc;
    private Bitmap mGoalSrc;

    private float mPosX, mVelX = 0.0f;
    private float mPosY, mVelY = 0.0f;
    private float xMax, yMax;
    private float x, y;

    public PitchView(Context context) {
        super(context);
        init(context);
    }

    public PitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PitchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setBackground(getResources().getDrawable(R.drawable.grass));
        Bitmap ballBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.golf);
        mBallSrc = Bitmap.createScaledBitmap(ballBitMap, BALL_DIAMETER, BALL_DIAMETER, true);
        Bitmap goalBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.hole);
        mGoalSrc = Bitmap.createScaledBitmap(goalBitMap, GOAL_DIAMETER, GOAL_DIAMETER, true);
        mContext = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        xMax = w - 100;
        yMax = h - 100;
        placeGoal();
    }

    public void setOnPitchViewListener(PitchViewListener listener) {
        mListener = listener;
    }

    public void updateBall(float xAccel, float yAccel) {
        mVelX += (xAccel * FRAME_TIME);
        mVelY += (yAccel * FRAME_TIME);
        mPosX -= mVelX;
        mPosY -= mVelY;

        if (mPosX > xMax) {
            mPosX = xMax;
            mVelX = 0;
        } else if (mPosX < 0) {
            mPosX = 0;
            mVelX = 0;
        }

        if (mPosY > yMax) {
            mPosY = yMax;
            mVelY = 0;
        } else if (mPosY < 0) {
            mPosY = 0;
            mVelY = 0;
        }

        invalidate();
        checkWin();

    }

    private void checkWin() {
        if(mPosX != 0 && mPosY != 0) {
            if(euclideanDistance() <= GOAL_DISTANCE) {
                Toast.makeText(mContext, "GOAL", Toast.LENGTH_LONG).show();
                mListener.onGoal();
                placeGoal();
            }
        }
    }

    private double euclideanDistance() {
        return Math.sqrt(Math.pow((x - mPosX), 2) + Math.pow((y - mPosY), 2));
    }

    private void placeGoal() {
        Random randomGenerator = new Random();
        x = randomGenerator.nextInt((int) xMax);
        y = randomGenerator.nextInt((int) yMax);
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBallSrc, mPosX, mPosY, null);
        canvas.drawBitmap(mGoalSrc, x, y, null);
        invalidate();
    }

}