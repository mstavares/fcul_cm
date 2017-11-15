package pdb.cm.fc.ul.pt.pdb.views;

/**
 * Created by nunonelas on 15/11/17.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import pdb.cm.fc.ul.pt.pdb.Maze;
import pdb.cm.fc.ul.pt.pdb.R;

public class MazeView extends View {

    //width and height of the whole maze and width of lines which
    //make the walls
    private int width, height, lineWidth;
    //size of the maze i.e. number of cells in it
    private int mazeSizeX, mazeSizeY;
    //width and height of cells in the maze
    float cellWidth, cellHeight;
    //the following store result of cellWidth+lineWidth
    //and cellHeight+lineWidth respectively
    float totalCellWidth, totalCellHeight;
    //the finishing point of the maze
    private int mazeFinishX, mazeFinishY;
    private Maze maze;
    private Activity context;
    private Paint line, red, background;

    private float mPosX, mVelX = 0.0f;
    private float mPosY, mVelY = 0.0f;
    private float xMax, yMax = 0.0f;


    private static final float FRAME_TIME = 0.266f;

    public MazeView(Context context, Maze maze) {
        super(context);
        this.context = (Activity)context;
        this.maze = maze;
        mazeFinishX = maze.getFinalX();
        mazeFinishY = maze.getFinalY();
        mazeSizeX = maze.getMazeWidth();
        mazeSizeY = maze.getMazeHeight();
        line = new Paint();
        line.setColor(getResources().getColor(R.color.line));
        red = new Paint();
        red.setColor(getResources().getColor(R.color.position));
        background = new Paint();
        background.setColor(getResources().getColor(R.color.game_bg));
        setFocusable(true);
        this.setFocusableInTouchMode(true);
    }
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int height = getScreenHeight();
        int width = getScreenWidth();
        //width = (w < h)?w:h;
        //height = width;         //for now square mazes
        lineWidth = 1;          //for now 1 pixel wide walls
        cellWidth = (width - ((float)mazeSizeX*lineWidth)) / mazeSizeX;
        totalCellWidth = cellWidth+lineWidth;
        cellHeight = (height - ((float)mazeSizeY*lineWidth)) / mazeSizeY;
        totalCellHeight = cellHeight+lineWidth;
        red.setTextSize(cellHeight*0.75f);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    protected void onDraw(Canvas canvas) {
        //fill in the background
        canvas.drawRect(0, 0, width, height, background);

        boolean[][] hLines = maze.getHorizontalLines();
        boolean[][] vLines = maze.getVerticalLines();
        //iterate over the boolean arrays to draw walls
        for(int i = 0; i < mazeSizeX; i++) {
            for(int j = 0; j < mazeSizeY; j++){
                float x = j * totalCellWidth;
                float y = i * totalCellHeight;
                if(j < mazeSizeX - 1 && vLines[i][j]) {
                    //we'll draw a vertical line
                    canvas.drawLine(x + cellWidth,   //start X
                            y,               //start Y
                            x + cellWidth,   //stop X
                            y + cellHeight,  //stop Y
                            line);
                }
                if(i < mazeSizeY - 1 && hLines[i][j]) {
                    //we'll draw a horizontal line
                    canvas.drawLine(x,               //startX
                            y + cellHeight,  //startY
                            x + cellWidth,   //stopX
                            y + cellHeight,  //stopY
                            line);
                }
            }
        }
        int currentX = maze.getCurrentX(),currentY = maze.getCurrentY();
        //draw the ball
        canvas.drawCircle((currentX * totalCellWidth)+(cellWidth/2),   //x of center
                (currentY * totalCellHeight)+(cellWidth/2),  //y of center
                (cellWidth*0.45f),                           //radius
                red);
        //draw the finishing point indicator
        canvas.drawText("F",
                (mazeFinishX * totalCellWidth)+(cellWidth*0.25f),
                (mazeFinishY * totalCellHeight)+(cellHeight*0.75f),
                red);
    }

    public void updateBall(float xAccel, float yAccel) {
        boolean moved = false;
        mVelX = (xAccel * FRAME_TIME);
        mVelY = (yAccel * FRAME_TIME);

        // left
        if (mVelX > 0) {
            moved = maze.move(Maze.LEFT);
        } //right
        else if (mVelX < 0) {
            moved = maze.move(Maze.RIGHT);
        }

        //up
        if (mVelY > 0) {
            moved = maze.move(Maze.UP);
        } //down
        else if (mVelY < 0) {
            moved = maze.move(Maze.DOWN);
        }

        if(moved) {
            invalidate();
        }
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}