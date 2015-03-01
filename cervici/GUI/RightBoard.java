/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cervici.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author malecNameka1
 */
public class RightBoard extends JPanel {

    JLabel[] names;
    JLabel[] scores;
    int[] scoresValues;
    GridBagConstraints cName;       // declaration of new constrains and then for
    GridBagConstraints cScores;     // declaration of insets (top, left, bottom, right)
    Font fontNames;                 // declaration of font styles
    Font fontScores;
    File f;
    Scanner scanner;

    /**
     * Constructor
     */
    public RightBoard() {
        this.f = new File("bestScore.txt");
        this.names = new JLabel[5]; // 5th is best entry
        this.scores = new JLabel[5];
        this.scoresValues = new int[5];
        for (int i = 0; i < 4; i++) {
            this.scoresValues[i] = 0;
        }
        try {
            scanner = new Scanner(f);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RightBoard.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (scanner.hasNextInt()) {    // reads bestScore from file
            this.scoresValues[4] = scanner.nextInt();
        } else {                        // if there is nothing in the file
            this.scoresValues[4] = 0;
        }

        this.setLayout(new GridBagLayout());        // GridBagLayout
        this.setBackground(Color.BLACK);
        this.cName = new GridBagConstraints();
        this.cScores = new GridBagConstraints();
        this.cName.insets = new Insets(23, 0, 0, 0);
        this.cScores.insets = new Insets(0, 0, 20, 0);
        this.fontNames = new Font("Sans Serif", Font.ITALIC | Font.BOLD, 17);
        this.fontScores = new Font("Monospaced", Font.BOLD, 44);
        initRightBoard();
    }

    public void enablePlayer(int index) {
        names[index].setForeground(Color.white);
    }

    public void disablePlayer(int index) {
        names[index].setForeground(Color.gray);
    }

    /**
     * Gets score
     * @param index of cervik
     * @return his current score
     */
    public int getScore(int index) {
        return scoresValues[index];
    }

    public void updateScore(int index) {
        if (++scoresValues[index] >= getScore(4)) {
            names[4].setForeground(Color.white);    // when score is better than bestScore highlight label
            scores[4].setText(String.format("%05d", scoresValues[index]));
        }
        scores[index].setText(String.format("%05d", scoresValues[index]));
    }

    /**
     * Control for best score up-to-date
     * @param index
     * @return true if score was overcome
     */
    public boolean bestScoreControll(int index) {
        if (getScore(index) >= scoresValues[4]) {
            // names[index] just overcame best score -> write it down
            try {
                try (FileWriter fw = new FileWriter(f); BufferedWriter out = new BufferedWriter(fw)) {
                    out.write(String.valueOf(getScore(index)));
                }
                scoresValues[4] = getScore(index);
                scores[4].setText(String.format("%05d", scoresValues[4]));
                return true;
            } catch (IOException ex) {
                Logger.getLogger(RightBoard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // nobody hasn't been better than best -> show game over
        return false;
    }

    public final void initRightBoard() {
        cName.gridx = 0;
        cName.gridy = 0;
        names[0] = new JLabel("Hráč 1:");
        names[0].setForeground(Color.gray);
        names[0].setFont(fontNames);
        add(names[0], cName);
        cScores.gridx = 0;
        cScores.gridy = 1;
        scores[0] = new JLabel(String.format("%05d", scoresValues[0]));
        scores[0].setForeground(Color.red);
        scores[0].setFont(fontScores);
        add(scores[0], cScores);
        cName.gridx = 0;
        cName.gridy = 2;
        names[1] = new JLabel("Hráč 2:");
        names[1].setForeground(Color.gray);
        names[1].setFont(fontNames);
        add(names[1], cName);
        cScores.gridx = 0;
        cScores.gridy = 3;
        scores[1] = new JLabel(String.format("%05d", scoresValues[1]));
        scores[1].setForeground(Color.green);
        scores[1].setFont(fontScores);
        add(scores[1], cScores);
        cName.gridx = 0;
        cName.gridy = 4;
        names[2] = new JLabel("Hráč 3:");
        names[2].setForeground(Color.gray);
        names[2].setFont(fontNames);
        add(names[2], cName);
        cScores.gridx = 0;
        cScores.gridy = 5;
        scores[2] = new JLabel(String.format("%05d", scoresValues[2]));
        scores[2].setForeground(Color.blue);
        scores[2].setFont(fontScores);
        add(scores[2], cScores);
        cName.gridx = 0;
        cName.gridy = 6;
        names[3] = new JLabel("Hráč 4:");
        names[3].setForeground(Color.gray);
        names[3].setFont(fontNames);
        add(names[3], cName);
        cScores.gridx = 0;
        cScores.gridy = 7;
        scores[3] = new JLabel(String.format("%05d", scoresValues[3]));
        scores[3].setForeground(Color.yellow);
        scores[3].setFont(fontScores);
        add(scores[3], cScores);
        cName.gridx = 0;
        cName.gridy = 8;
        add(Box.createRigidArea(new Dimension(0, 140)), cName);  // width, height
        cName.gridx = 0;
        cName.gridy = 9;
        names[4] = new JLabel("BEST:");
        names[4].setForeground(Color.gray);
        names[4].setFont(new Font("Sans Serif", Font.BOLD | Font.ITALIC, 34));
        add(names[4], cName);
        cScores.gridx = 0;
        cScores.gridy = 10;
        scores[4] = new JLabel(String.format("%05d", scoresValues[4]));
        scores[4].setForeground(Color.MAGENTA);
        scores[4].setFont(new Font("Monospaced", Font.BOLD, 44));
        add(scores[4], cScores);
    }
}
