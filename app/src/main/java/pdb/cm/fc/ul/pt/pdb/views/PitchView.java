package pdb.cm.fc.ul.pt.pdb.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

import pdb.cm.fc.ul.pt.pdb.R;

public class PitchView extends View {

    private static final float FRAME_TIME = 0.266f;
    private static final double GOAL_DISTANCE = 70;
    private static final int BALL_DIAMETER = 100;
    private Context mContext;
    private Bitmap mBallSrc;
    private Bitmap mGoalSrc;

    private float mPosX, mVelX = 0.0f;
    private float mPosY, mVelY = 0.0f;
    private float xMax, yMax;
    private float x, y;

    public PitchView(Context context) {
        super(context);
        setBackground(getResources().getDrawable(R.drawable.grass));
        Bitmap ballBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.golf);
        mBallSrc = Bitmap.createScaledBitmap(ballBitMap, BALL_DIAMETER, BALL_DIAMETER, true);
        Bitmap ballBitMap2 = BitmapFactory.decodeResource(getResources(), R.drawable.hole);
        mGoalSrc = Bitmap.createScaledBitmap(ballBitMap2, BALL_DIAMETER, BALL_DIAMETER, true);
        mContext = context;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        xMax = w - 100;
        yMax = h - 100;
        placeGoal();
    }

    public void updateBall(float xAccel, float yAccel) {
        mVelX += (xAccel * FRAME_TIME);
        mVelY += (yAccel * FRAME_TIME);
        mPosX -= mVelX;
        mPosY -= mVelY;

        if (mPosX > xMax) {
            mPosX = xMax;
        } else if (mPosX < 0) {
            mPosX = 0;
        }

        if (mPosY > yMax) {
            mPosY = yMax;
        } else if (mPosY < 0) {
            mPosY = 0;
        }

        invalidate();
        checkWin();

    }

    private void checkWin() {
        if(mPosX != 0 && mPosY != 0) {
            if(euclidianDistance() <= GOAL_DISTANCE) {
                Toast.makeText(mContext, "GOAL", Toast.LENGTH_LONG).show();
                placeGoal();
            }
        }
    }

    private double euclidianDistance() {
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