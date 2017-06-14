package game.ui;

import game.MineSweeper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MineSweeperPanel extends JPanel {
    private static final int SIZE = 10;
    public MineSweeperCell[][] buttonArray = new MineSweeperCell[SIZE][SIZE];

    public MineSweeperPanel() {
        MineSweeper mineSweeper = new MineSweeper();
        setLayout(new GridLayout(SIZE, SIZE));
        MouseListener listener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MineSweeperCell selectedCell = (MineSweeperCell) e.getSource();
                if(SwingUtilities.isRightMouseButton(e)&&selectedCell.isEnabled()){
                    mineSweeper.toggleSeal(selectedCell.row, selectedCell.col);
                    if(mineSweeper.getCellState(selectedCell.row, selectedCell.col) == MineSweeper.CellState.SEALED) {
                        selectedCell.setText("M");
                        checkGameWin(mineSweeper);
                    }
                    else if(mineSweeper.getCellState(selectedCell.row, selectedCell.col) == MineSweeper.CellState.UNEXPOSED)
                        selectedCell.setText("");

                }
                else if(SwingUtilities.isLeftMouseButton(e)&&selectedCell.isEnabled()){
                    if(mineSweeper.isMine(selectedCell.row, selectedCell.col)
                            &&mineSweeper.getCellState(selectedCell.row, selectedCell.col)!= MineSweeper.CellState.SEALED){
                        mineSweeper.exposeACell(selectedCell.row, selectedCell.col);
                        selectedCell.setText("X");
                        selectedCell.setEnabled(false);
                        gameOver(mineSweeper);
                    }
                    else if(mineSweeper.isAnAdjacentCell(selectedCell.row, selectedCell.col)) {
                        mineSweeper.exposeACell(selectedCell.row, selectedCell.col);
                        selectedCell.setText(Integer.toString(mineSweeper.getMineInfo(selectedCell.row, selectedCell.col)));
                        selectedCell.setEnabled(false);
                        checkGameWin(mineSweeper);
                    }
                    else{
                        mineSweeper.exposeACell(selectedCell.row, selectedCell.col);
                        for(int row =  0; row < SIZE; row++)
                            for(int col = 0; col < SIZE; col++){
                                if(mineSweeper.getCellState(row, col) == MineSweeper.CellState.EXPOSED){
                                    buttonArray[row][col].setText(Integer.toString(mineSweeper.getMineInfo(row,col)));
                                    buttonArray[row][col].setEnabled(false);
                                }
                            }
                        checkGameWin(mineSweeper);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        };

        for(int row = 0; row < SIZE; row++){
            for(int col = 0; col < SIZE; col++){
                buttonArray[row][col] = new MineSweeperCell(row, col);
                buttonArray[row][col].addMouseListener(listener);
                add(buttonArray[row][col]);
            }
        }
    }

    private static void createAndShowGui(){
        MineSweeperPanel panel = new MineSweeperPanel();

        JFrame frame = new JFrame("MineSweeper");
        Dimension d = new Dimension(800,800);
        frame.setPreferredSize(d);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();;
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater (new Runnable() {
            @Override
            public void run() { createAndShowGui(); }
        });
    }

    public void gameOver(MineSweeper mineSweeper){
        for(int row =  0; row < SIZE; row++)
            for(int col = 0; col < SIZE; col++){
                if(buttonArray[row][col].isEnabled() == true) {
                    if (mineSweeper.getMineInfo(row, col) > -1)
                        buttonArray[row][col].setText(Integer.toString(mineSweeper.getMineInfo(row, col)));
                    else
                        buttonArray[row][col].setText("X");
                    buttonArray[row][col].setEnabled(false);
                }
            }
        JOptionPane.showMessageDialog(null, "You Lost! Game Over");
    }

    public void checkGameWin(MineSweeper mineSweeper) {
        if (mineSweeper.getGameStatus() == MineSweeper.GameStatus.WON) {
            for (int row = 0; row < SIZE; row++)
                for (int col = 0; col < SIZE; col++)
                    buttonArray[row][col].setEnabled(false);
            JOptionPane.showMessageDialog(null, "You Won!");
        }
    }
}