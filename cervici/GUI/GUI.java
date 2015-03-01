/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cervici.GUI;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 *
 * @author malecka1
 */
public class GUI extends JFrame {

    JFrame frame;
    JSplitPane splitPane;
    JPanel leftPanel;
    JPanel rightPanel;
    RightBoard rightBoard;
    Dimension leftBoardSize;
    LeftBoard leftBoard;
    int difficultyIndex;

    /**
     * Constructor of gui
     */
    public GUI() {
        this.leftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        this.rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        this.rightBoard = new RightBoard();
        this.leftBoard = new LeftBoard();
        this.difficultyIndex = 1;        // sets default dificulty to normal
    }
/**
 * Sets up a new game
 * Initializes both panels.
 * @param playerCount number of players
 */
    public void initGame(int playerCount) {
        leftPanel.removeAll();                      // clear both panels
        rightPanel.removeAll();
        rightBoard = new RightBoard();
        rightBoard.setPreferredSize(leftBoardSize);
        rightPanel.add(rightBoard);
        leftBoardSize = new Dimension((int) ((frame.getWidth() - 6) * 4 / 5), frame.getHeight() - 48);// set correct sizes
        leftBoard.setPreferredSize(leftBoardSize);
        leftBoard = new LeftBoard(leftBoardSize.width, leftBoardSize.height, playerCount, rightBoard, difficultyIndex);
        leftBoard.setBackground(Color.black);
        leftPanel.add(leftBoard);
        setDividerFrameBoardsPositions();           // updates board
        leftBoard.addNotify();                      // in fact game starts right now!
    }

    /**
     * Creates new resizable window
     */
    public void createWindow() {
        frame = new JFrame("Červíci v1.0");                     // frame label
        //frame.setResizable(false);                              // deny windows size change
        frame.setMinimumSize(new Dimension(1040, 826));         // sets minimum size for frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // tap cross to exit
        frame.setJMenuBar(createMenu());                        // windows's menu
        initComponents(frame.getContentPane());                 // get and init content of frame
        frame.pack();                                           // pack all content back into frame
        frame.setLocationRelativeTo(null);                      // windows appears in center of screen
        SwingUtilities.invokeLater(new Runnable() {             // always set frames visible in this way
            @Override
            public void run() {
                frame.setVisible(true);                         // makes window visible
            }
        });
        frame.addComponentListener(new ComponentAdapter() {     // catches windows resizing
            @Override
            public void componentResized(ComponentEvent e) {
                setDividerFrameBoardsPositions();
            }
        });
    }

    private void setDividerFrameBoardsPositions() {
        SwingUtilities.invokeLater(new Runnable() {
            // everything about resize, size, whatever for JSplitPane into invokeLater()
            @Override
            public void run() {
                int w = (int) (frame.getWidth() - 6);// bylo 16
                int h = (int) (frame.getHeight() - 48);// bylo 58
                frame.setPreferredSize(new Dimension(w + 16, h + 58));
                splitPane.setPreferredSize(new Dimension(w, h));
                splitPane.setDividerLocation(4 * splitPane.getSize().width / 5);// very important -> sets divider location in JSplitPane
                rightBoard.setPreferredSize(new Dimension((int) (w * 1 / 5), h));
                leftBoardSize = new Dimension((int) (w * 4 / 5), h);
                leftBoard.setPreferredSize(leftBoardSize);
            }
        });
    }

    private void initComponents(Container contentPane) {
        leftPanel.setBackground(Color.BLACK);
        rightPanel.setBackground(Color.black);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);        // splits the window
        splitPane.setPreferredSize(new Dimension(1024, 768));           // window's init size
        ImageIcon image = new ImageIcon("images/welcomeScreen.png");
        JLabel label = new JLabel("", image, (int) JLabel.CENTER_ALIGNMENT);
        leftPanel.add(label);
        splitPane.setEnabled(false);                                    // this is very important -> disables moving divider!
        splitPane.setDividerSize(2);                                    // sets divider width
        rightPanel.add(rightBoard);                                     // adds container, cause I want init zeros on the right
        splitPane.add(rightPanel, JSplitPane.RIGHT);                    // adds panels to split pane
        splitPane.add(leftPanel, JSplitPane.LEFT);
        contentPane.add(splitPane);                                     // and splitpane into frame
    }

    private JMenuBar createMenu() {
        JMenuBar menu = new JMenuBar();
        menu.setBorderPainted(true);
        // options in menubar - New game
        menu.add(Box.createRigidArea(new Dimension(40, 0)));
        JMenuItem mi_newGame = new JMenuItem("Nová hra");
        mi_newGame.setBackground(Color.red);
        menu.add(mi_newGame);
        mi_newGame.setToolTipText("Spustit novou hru");
        mi_newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, ActionEvent.CTRL_MASK));
        menu.add(Box.createRigidArea(new Dimension(40, 0)));
        // options in menubar - Difficulty options
        final JMenuItem mi_difficultySet = new JMenuItem("Obtížnost: Normální");
        mi_difficultySet.setBackground(Color.green);
        menu.add(mi_difficultySet);
        mi_difficultySet.setToolTipText("Nastavit obtížnost pro novou hru");
        mi_difficultySet.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, ActionEvent.CTRL_MASK));
        menu.add(Box.createRigidArea(new Dimension(40, 0)));
        // options in menubar - Help
        JMenuItem mi_howTo = new JMenuItem("Nápověda");
        mi_howTo.setBackground(Color.blue);
        menu.add(mi_howTo);
        mi_howTo.setToolTipText("Ukázat nápovědu a ovládání hry");
        mi_howTo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, ActionEvent.CTRL_MASK));
        menu.add(Box.createRigidArea(new Dimension(40, 0)));
        // options in menubar - Exit
        JMenuItem mi_exit = new JMenuItem("Konec");
        menu.add(mi_exit);
        mi_exit.setBackground(Color.yellow);
        mi_exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.CTRL_MASK));
        mi_exit.setToolTipText("Ukončit hru");
        menu.add(Box.createRigidArea(new Dimension(40, 0)));

        // actions for buttons
        mi_newGame.addActionListener(
                new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Object[] optionsYN = {"Ano", "Ne"};
                        int g = JOptionPane.showOptionDialog(null,
                                "Začít novou hru?", "Nová hra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionsYN, optionsYN[0]);    // intead of 2nd null it's possible to have pictures
                        // if "yes" selected
                        if (g == 0) {
                            Object[] howManyPlayers = {"2 hráči", "3 hráči", "4 hráči"};
                            g = JOptionPane.showOptionDialog(null, "Zvolte počet hráčů:", "Počet hráčů", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, howManyPlayers, howManyPlayers[0]);
                            // initGame with g players
                            if (g >= 0) {
                                initGame(g + 2);
                            }
                        }
                    }
                ;
        });
        mi_exit.addActionListener(
                new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        Object[] optionsYN = {"Ano", "Ne"};
                        int g = JOptionPane.showOptionDialog(null,
                                "Opravdu chcete ukončit hru?", "Konec hry", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionsYN, optionsYN[1]);    // intead of 2nd null it's possible to have pictures
                        // if "yes" selected
                        if (g == 0) {
                            System.exit(0);
                        }
                    }
                ;
        });
        mi_difficultySet.addActionListener(
                new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Object[] difficultySettings = {"Nováček", "Normální", "Pokročilý", "Veterán"};
                        int g = JOptionPane.showOptionDialog(null, "Zvolte obtížnost:", "Obtížnost", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, difficultySettings, difficultySettings[1]);
                        // set next game difficulty & changes JMenu label
                        if (g == 0) {
                            mi_difficultySet.setText("Obtížnost: " + difficultySettings[g]);
                            difficultyIndex = g;
                        } else if (g == 1) {
                            mi_difficultySet.setText("Obtížnost: " + difficultySettings[g]);
                            difficultyIndex = g;
                        } else if (g == 2) {
                            mi_difficultySet.setText("Obtížnost: " + difficultySettings[g]);
                            difficultyIndex = g;
                        } else if (g == 3) {
                            mi_difficultySet.setText("Obtížnost: " + difficultySettings[g]);
                            difficultyIndex = g;
                        }
                    }
                ;
        }); 
        mi_howTo.addActionListener(
                new java.awt.event.ActionListener() {
                    @Override
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        JOptionPane.showMessageDialog(null,
                                  "Červíci je jednoduchá postřehovka,  \n"
                                + "ve které je cílem získat co nejvyšší\n"
                                + "skóre. Přitom se hráči musí udržet  \n"
                                + "na herní ploše a zároveň se musí    \n"
                                + "vyhýbat ostatním hráčům či sobě.    \n"
                                + "\n     Doporučená přístupnost: 1+     \n"
                                + "\n\nOvládání:\n"
                                + "                 vlevo      vpravo\n"
                                + "Hráč 1:       <-             ->\n"
                                + "Hráč 2:        Q             W\n"
                                + "Hráč 3:        K              L\n"
                                + "Hráč 4:        C              V\n"
                                + "\n\n         Kamil Maleček, 2014\n\n", "Nápověda & ovládání hry", JOptionPane.INFORMATION_MESSAGE);
                    }
                ;
        }); 
    return menu;
    }
}