package pdb.cm.fc.ul.pt.pdb.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.Display;
import android.view.View;

import pdb.cm.fc.ul.pt.pdb.R;

public class BallView extends View {

    private static final float FRAME_TIME = 0.266f;
    private static final int BALL_DIAMETER = 100;
    private Bitmap mBallSrc;

    private float mPosX, mVelX = 0.0f;
    private float mPosY, mVelY = 0.0f;
    private float xMax, yMax;

    public BallView(Context context) {
        super(context);
        Bitmap ballBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_ball);
        mBallSrc = Bitmap.createScaledBitmap(ballBitMap, BALL_DIAMETER, BALL_DIAMETER, true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        xMax = w - 100;
        yMax = h - 100;
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

    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBallSrc, mPosX, mPosY, null);
        invalidate();
    }


}
