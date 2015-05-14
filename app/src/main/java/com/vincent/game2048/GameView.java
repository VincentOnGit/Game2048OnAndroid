package com.vincent.game2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 2015/5/14.
 */
public class GameView extends GridLayout{
    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }

    private void initGameView() {
        setColumnCount(4);
        setBackgroundColor(0xffbbada0);
        setOnTouchListener(new OnTouchListener() {
            private float startX,startY,offsetX,offsetY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX=event.getX();
                        startY=event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX=event.getX()-startX;
                        offsetY=event.getY()-startY;
                        if(Math.abs(offsetX)>Math.abs(offsetY)) {
                            if(offsetX<-5) {
                                swipeLeft();
                            }else if(offsetX>5) {
                                swipeRight();
                            }
                        }
                        else {
                            if(offsetY<-5) {
                                swipeUp();
                            }else if(offsetY>5) {
                                swipeDown();
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int cardWidth = (Math.min(w,h)-10)/4;
        addCards(cardWidth,cardWidth);
        startGame();
    }

    private void addCards(int cardWidth,int cardHeight) {
        Card c;
        for(int i=0;i<4;++i) {
            for(int j=0;j<4;++j) {
                c = new Card(getContext());
                c.setNum(0);
                addView(c,cardWidth,cardHeight);
                cardsMap[i][j]=c;
            }
        }
    }

    private void startGame() {
        for(int i=0;i<4;++i) {
            for(int j=0;j<4;++j) {
                cardsMap[i][j].setNum(0);
            }
        }
        addRandomNum();
        addRandomNum();
    }
    private void reStartGame() {
        MainActivity.getMainActivity().clearScore();
        for(int i=0;i<4;++i) {
            for(int j=0;j<4;++j) {
                cardsMap[i][j].setNum(0);
            }
        }
        addRandomNum();
        addRandomNum();
    }
    private void addRandomNum() {
        emptyPoints.clear();
        for(int i=0;i<4;++i) {
            for(int j=0;j<4;++j) {
                if(cardsMap[i][j].getNum()<=0) {
                    emptyPoints.add(new Point(i,j));
                }
            }
        }
        Point p = emptyPoints.remove((int)(Math.random()*emptyPoints.size()));
        cardsMap[p.x][p.y].setNum(Math.random()>0.1? 2:4);
    }

    private void swipeLeft() {
        boolean merge = false;
        for(int i=0;i<4;++i) {
            for(int j=0;j<4;++j) {
                for(int col=j+1;col<4;++col) {
                    if(cardsMap[i][col].getNum()>0) {
                        if(cardsMap[i][j].getNum()<=0) {
                            cardsMap[i][j].setNum(cardsMap[i][col].getNum());
                            cardsMap[i][col].setNum(0);
                            --j;
                            merge = true;
                        }else if(cardsMap[i][j].equals(cardsMap[i][col])) {
                            cardsMap[i][j].setNum(cardsMap[i][j].getNum()*2);
                            cardsMap[i][col].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[i][j].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if(merge) {
            addRandomNum();
            checkComplete();
        }
    }
    private void swipeRight() {
        boolean merge = false;
        for(int i=0;i<4;++i) {
            for(int j=3;j>=0;--j) {
                for(int col=j-1;col>=0;--col) {
                    if(cardsMap[i][col].getNum()>0) {
                        if(cardsMap[i][j].getNum()<=0) {
                            cardsMap[i][j].setNum(cardsMap[i][col].getNum());
                            cardsMap[i][col].setNum(0);
                            ++j;
                            merge = true;
                        }else if(cardsMap[i][j].equals(cardsMap[i][col])) {
                            cardsMap[i][j].setNum(cardsMap[i][j].getNum()*2);
                            cardsMap[i][col].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[i][j].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if(merge) {
            addRandomNum();
            checkComplete();
        }
    }
    private void swipeUp() {
        boolean merge = false;
        for(int j=0;j<4;++j) {
            for(int i=0;i<4;++i) {
                for(int row=i+1;row<4;++row) {
                    if(cardsMap[row][j].getNum()>0) {
                        if(cardsMap[i][j].getNum()<=0) {
                            cardsMap[i][j].setNum(cardsMap[row][j].getNum());
                            cardsMap[row][j].setNum(0);
                            --i;
                            merge = true;
                        }else if(cardsMap[i][j].equals(cardsMap[row][j])) {
                            cardsMap[i][j].setNum(cardsMap[i][j].getNum()*2);
                            cardsMap[row][j].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[i][j].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if(merge) {
            addRandomNum();
            checkComplete();
        }
    }
    private void swipeDown() {
        boolean merge = false;
        for(int j=0;j<4;++j) {
            for(int i=3;i>=0;--i) {
                for(int row=i-1;row>=0;--row) {
                    if(cardsMap[row][j].getNum()>0) {
                        if(cardsMap[i][j].getNum()<=0) {
                            cardsMap[i][j].setNum(cardsMap[row][j].getNum());
                            cardsMap[row][j].setNum(0);
                            ++i;
                            merge = true;
                        }else if(cardsMap[i][j].equals(cardsMap[row][j])) {
                            cardsMap[i][j].setNum(cardsMap[i][j].getNum()*2);
                            cardsMap[row][j].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[i][j].getNum());
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if(merge) {
            addRandomNum();
            checkComplete();
        }
    }
    private void checkComplete() {
        boolean over = true;
        for(int i=0;i<4;++i) {
            for(int j=0;j<4;++j) {
                if(cardsMap[i][j].getNum()==0||
                        (j>0&&cardsMap[i][j].equals(cardsMap[i][j-1]))||
                        (j<3&&cardsMap[i][j].equals(cardsMap[i][j+1]))||
                        (i>0&&cardsMap[i][j].equals(cardsMap[i-1][j]))||
                        (i<3&&cardsMap[i][j].equals(cardsMap[i+1][j]))) {
                    over = false;
                    break;
                }
            }
            if(over==false) {
                break;
            }
        }
        if(over) {
            new AlertDialog.Builder(getContext()).setTitle("Oops~~~").setMessage("Game Over").setPositiveButton("Play Again",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    reStartGame();
                }
            }).show();
        }
    }
    private Card[][] cardsMap = new Card[4][4];
    private List<Point> emptyPoints = new ArrayList<Point>();
}
