/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cervici.GUI;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author malecka1
 */
public class Cervik {

    int x;  // position
    int y;
    int xa; // direction
    int ya;
    final int cervikThickness = 10;
    Color color;
    final ArrayList<Point> prevCoords;
    final LeftBoard leftBoard;
    int posCounter = 0;

    /**
     * Constructor of cervik
     * @param leftBoard leftboard
     * @param x x head position of cervik
     * @param y y head position of cervik
     * @param color color of cervik
     * @param xa x direction
     * @param ya y direction
     */
    public Cervik(LeftBoard leftBoard, int x, int y, Color color, int xa, int ya) {
        this.x = x;
        this.y = y;
        this.xa = xa;
        this.ya = ya;
        this.prevCoords = new ArrayList<>();
        this.leftBoard = leftBoard;
        this.color = color;
    }

    /**
     * Moves left
     */
    public void left() {
        if ( xa == 1 && ya == 1 ) {
            xa=1;
            ya=0;
        } else if ( xa == 1 && ya == 0 ) {
            xa=1;
            ya=-1;
        } else if ( xa == 1 && ya == -1 ) {
            xa=0;
            ya=-1;
        } else if ( xa == 0 && ya == -1 ) {
            xa=-1;
            ya=-1;
        } else if ( xa == -1 && ya == -1 ) {
            xa=-1;
            ya=0;
        } else if ( xa == -1 && ya == 0 ) {
            xa=-1;
            ya=1;
        } else if ( xa == -1 && ya == 1 ) {
            xa=0;
            ya=1;
        } else if ( xa == 0 && ya == 1 ) {
            xa=1;
            ya=1;
        }
    }
    
    /**
     * Moves right
     */
    public void right() {
        if ( xa == 1 && ya == 1 ) {
            //leftBoard.currentDelay*=0.8333; // set shorter delay
            xa=0;
            ya=1;
        } else if ( xa == 1 && ya == 0 ) {
            //leftBoard.currentDelay*=1.2;    // set longer delay
            xa=1;
            ya=1;
        } else if ( xa == 1 && ya == -1 ) {
            xa=1;
            ya=0;
        } else if ( xa == 0 && ya == -1 ) {
            xa=1;
            ya=-1;
        } else if ( xa == -1 && ya == -1 ) {
            xa=0;
            ya=-1;
        } else if ( xa == -1 && ya == 0 ) {
            xa=-1;
            ya=-1;
        } else if ( xa == -1 && ya == 1 ) {
            xa=-1;
            ya=0;
        } else if ( xa == 0 && ya == 1 ) {
            xa=-1;
            ya=1;
        }
    }
    
    /**
     * Sets cervik's head new position
     * @return true if cervik doesn't hit to border
     */
    public boolean move() {   // return false -> stops painting when cervik hits the border
        if (x + xa < 0) {
            return false;
        }
        if (x + xa > leftBoard.getWidth() - cervikThickness) {
            return false;
        }
        if (y + ya < 0) {
            return false;
        }
        if (y + ya > leftBoard.getHeight() - cervikThickness) {
            return false;
        }
        x = x + xa;
        y = y + ya;
        return true;
    }

    /**
     * Checks for collisions
     * @return 
     */
    public boolean collision() {
        for (int i = 0; i < leftBoard.allCoords.size()-leftBoard.playerCount; i++) {
            if (bounds().intersects(leftBoard.bounds(leftBoard.allCoords.get(i).x, leftBoard.allCoords.get(i).y))) {
                //System.out.println("C na poz: " + leftBoard.allCoords.get(i).x + " " + leftBoard.allCoords.get(i).y + "\takt: " + x + " " + y);
                return true;
            }
        }
        prevCoords.add(new Point(x, y));                    // add current position to listArray of previous
        if (posCounter % cervikThickness == 0) {
            leftBoard.allCoords.add(new Point(x, y));       // add current position to array of all visited positions
            //System.out.println("zapis pozice " + x + " " + y + "\tcounter: " + posCounter);
        }
        posCounter++;
        return false;
    }
    
    /**
     * Paint cervik with his previous positions
     * @param g graphics
     */
    public void paint(Graphics2D g) {
        g.setColor(color);                                  // paint all previous positions
        for (int i = 0; i < prevCoords.size(); i++) {
            g.fillOval(prevCoords.get(i).x, prevCoords.get(i).y, cervikThickness, cervikThickness);
        }
        g.setColor(Color.white);
        g.fillOval(x, y, cervikThickness, cervikThickness); // paint current position (= head of cervik)
    }
    
    /**
     * Makes rectangle around current cervik's position for collision checking
     * @return 
     */
    public Rectangle bounds() {
        return (new Rectangle(x, y, cervikThickness, cervikThickness));
    }
}
