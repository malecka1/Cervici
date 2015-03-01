/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cervici.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TimerTask;
import javax.swing.JPanel;
import java.util.Timer;

/**
 *
 * @author malecka1
 */
public class LeftBoard extends JPanel implements Runnable {

    int currentDelay;               // delay == speed of repainting - for dificulty settings use array
    int[] difficultyDelay;
    int playerCount;
    int gameBestScoreIndex;
    boolean gameOverNewHighScoreState = false;
    Random random = new Random();
    RightBoard rightBoard;
    boolean isOver;
    boolean[] playersActive;
    Color[] colors;
    Cervik[] cervici;
    int[] coordX;     // init position [0,0] is upper left corner
    int[] coordY;
    ArrayList<Point> allCoords;
    Set<String> events;
    Timer timer;        // timer from java.util.Timer

    public LeftBoard() {
    }

    public LeftBoard(int width, int height, final int playerCount, RightBoard righBoard, int difficultyIndex) {
        this.timer = new Timer();
        this.gameBestScoreIndex = 0;
        this.difficultyDelay = new int[4];
        this.difficultyDelay[0] = 30;
        this.difficultyDelay[1] = 20;
        this.difficultyDelay[2] = 12;
        this.difficultyDelay[3] = 6;
        this.currentDelay = this.difficultyDelay[difficultyIndex];
        this.isOver = false;
        this.playerCount = playerCount;
        this.rightBoard = righBoard;
        this.events = new HashSet<>();
        this.allCoords = new ArrayList<>();
        this.coordX = new int[4];
        this.coordY = new int[4];
        this.colors = new Color[4];
        colors[0] = Color.red;
        colors[1] = Color.green;
        colors[2] = Color.blue;
        colors[3] = Color.yellow;
        this.cervici = new Cervik[4];
        this.playersActive = new boolean[4];
        for (int i = 0; i < 4; i++) {// activate just playerCount
            if (i < playerCount) {
                playersActive[i] = true;
                rightBoard.enablePlayer(i);
                coordX[i] = random.nextInt(2);
                coordY[i] = random.nextInt(2);
                // makes random initial direction
                if (coordX[i] == 0 && coordY[i] == 0) {
                    if (random.nextBoolean()) {
                        coordX[i] = 1;
                    } else {
                        coordY[i] = 1;
                    }
                }
                if (random.nextBoolean()) {
                    coordX[i] *= -1;
                }
                if (random.nextBoolean()) {
                    coordY[i] *= -1;
                }
                cervici[i] = new Cervik(this, (random.nextInt(width - 250) + 125), (random.nextInt(height - 200) + 100), colors[i], coordX[i], coordY[i]);
            } else {
                playersActive[i] = false;
            }
        }
        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (playerCount > 1) {
                    if ("Q".equals(KeyEvent.getKeyText(e.getKeyCode())) || "W".equals(KeyEvent.getKeyText(e.getKeyCode()))
                            || "Left".equals(KeyEvent.getKeyText(e.getKeyCode())) || "Right".equals(KeyEvent.getKeyText(e.getKeyCode()))) {
                        events.add(KeyEvent.getKeyText(e.getKeyCode()));      // adds event to the list
                    }
                }
                if (playerCount > 2) {
                    if ("K".equals(KeyEvent.getKeyText(e.getKeyCode())) || "L".equals(KeyEvent.getKeyText(e.getKeyCode()))) {
                        events.add(KeyEvent.getKeyText(e.getKeyCode()));
                    }
                }
                if (playerCount > 3) {
                    if ("C".equals(KeyEvent.getKeyText(e.getKeyCode())) || "V".equals(KeyEvent.getKeyText(e.getKeyCode()))) {
                        events.add(KeyEvent.getKeyText(e.getKeyCode()));
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                events.remove(KeyEvent.getKeyText(e.getKeyCode()));
            }
        });
    }

    public int moveBall() {
        int p = 0;  // counter for active players
        for (int i = 0; i < playerCount; i++) {
            if (playersActive[i] == true) {
                p++;
                boolean state = cervici[i].move();
                boolean collision = cervici[i].collision();
                if (state == false || collision == true) {// if cervik hits sth
                    playersActive[i] = false;
                    if (rightBoard.getScore(gameBestScoreIndex) <= rightBoard.getScore(i)) {
                        gameBestScoreIndex = i; // store index of bestScore in this game
                    }
                    rightBoard.disablePlayer(i);
                } else {
                    rightBoard.updateScore(i);      // updates score
                }
            }
        }
        if (p == 0) {                             // no cervik left -> quit game
            return -1;
        }
        return 0;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);                    // paint background
        Graphics2D g2d = (Graphics2D) g;            // has more options than Graphics
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < playerCount; i++) {   // paints all cerviky
            cervici[i].paint(g2d);
        }
        if (isOver == true) {                     // shows game sum up when game is over
            gameOver(g);
        }
        Toolkit.getDefaultToolkit().sync();         // synchronizes the graphics state
    }

    public Rectangle bounds(int x, int y) {
        return (new Rectangle(x, y, 10, 10));
    }

    public void doEvents() {
        if (events.contains("Left")) {
            cervici[0].left();
        }
        if (events.contains("Right")) {
            cervici[0].right();
        }
        if (events.contains("Q")) {
            cervici[1].left();
        }
        if (events.contains("W")) {
            cervici[1].right();
        }
        if (events.contains("K")) {
            cervici[2].left();
        }
        if (events.contains("L")) {
            cervici[2].right();
        }
        if (events.contains("C")) {
            cervici[3].left();
        }
        if (events.contains("V")) {
            cervici[3].right();
        }
        events.clear();
    }

    @Override // this method is often used for initialization tasks
    public void addNotify() {
        super.addNotify();
        Thread animations = new Thread(this);       // animations using threads are the most effective and accurate way
        animations.start();
    }

    @Override
    public void run() {
        setFocusable(true);                         // this component listen keys now
        requestFocusInWindow();

        timer.scheduleAtFixedRate(new TimerTask() { // scheduled actions
            @Override
            public void run() {
                doEvents();
                int r = moveBall();
                headsCollision();
                repaint();              // repaints board
                if (r == -1) {          // checks game over
                    gameOverNewHighScoreState = rightBoard.bestScoreControll(gameBestScoreIndex);   // if true writes down new bestscore
                    isOver = true;
                    timer.cancel();     // "break" for scheduled TimerTask
                }
            }
        }, 400, currentDelay);    // init delay and period delay
    }

    /**
     * Checks for heads current positions
     */
    public void headsCollision() {
        for (int i = 0; i < playerCount; i++) {
            if (playersActive[i] == true) {
                for (int j = i + 1; j < playerCount; j++) {
                    if (playersActive[j] == true) {
                        cervici[i].bounds().intersection(cervici[j].bounds());
                    }
                }
            }
        }
    }

    private void gameOver(Graphics g) {
        Font font = new Font("Helvetica", Font.BOLD, 56);
        FontMetrics metr = getFontMetrics(font);
        g.setColor(Color.white);
        g.setFont(font);
        if (gameOverNewHighScoreState == true) {
            String msg = "Hráč " + (gameBestScoreIndex + 1) + " překonal rekord!";
            g.drawString(msg, (getWidth() - metr.stringWidth(msg)) / 2, getHeight() / 2 - 33);  // always in the middle of leftboard
            msg = "Nový rekord je " + rightBoard.scoresValues[gameBestScoreIndex] + ".";
            g.drawString(msg, (getWidth() - metr.stringWidth(msg)) / 2, getHeight() / 2 + 33);  // always in the middle of leftboard
        } else {
            String msg = "Game Over";
            g.drawString(msg, (getWidth() - metr.stringWidth(msg)) / 2, getHeight() / 2);       // always in the middle of leftboard
        }
    }
}
