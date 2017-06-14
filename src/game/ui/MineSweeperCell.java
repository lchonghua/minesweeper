package game.ui;

import javax.swing.*;
import java.awt.*;

public class MineSweeperCell extends JToggleButton {
    public final int row;
    public final int col;
    public MineSweeperCell(int theRow, int theColumn) {
        row = theRow;
        col = theColumn;
        setSize(80, 80);
    }
}
