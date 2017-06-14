package game;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import game.MineSweeper.CellState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MineSweeperTest {

    MineSweeper mineSweeper;
    private boolean exposeNeighborsOfCalled;
    private List<Integer> rowsAndColumns = new ArrayList<>();

    class MineSweeperWithExposeCell extends MineSweeper {

        public void exposeACell(int row, int column) {
            rowsAndColumns.add(row);
            rowsAndColumns.add(column);
        }
    }

    @Before
    public void setup(){
        mineSweeper = new MineSweeper();
        exposeNeighborsOfCalled = false;
    }

    @Test
    public void Canary(){
        assertTrue(true);
    }

    @Test
    public void exposeAnUnexposedCell(){
        mineSweeper.exposeACell(3, 3);

        assertEquals(CellState.EXPOSED, mineSweeper.getCellState(3, 3));
    }

    @Test
    public void exposeAnExposedCell(){
        mineSweeper.exposeACell(1,3);
        mineSweeper.exposeACell(1,3);

        assertEquals(CellState.EXPOSED, mineSweeper.getCellState(1, 3));
    }

    @Test
    public void exposeTwoCells(){
        MineSweeper mineSweeper = new MineSweeper(true);
        mineSweeper.exposeACell(1,3);
        mineSweeper.exposeACell(2,5);

        assertEquals(CellState.EXPOSED, mineSweeper.getCellState(1, 3));
        assertEquals(CellState.EXPOSED, mineSweeper.getCellState(2, 5));
    }

    @Test
    public void exposeCellCallsExposeNeighborOf(){
        MineSweeper mineSweeper = new MineSweeper(true){
            public void exposeNeighborsOf(int row, int col){ exposeNeighborsOfCalled = true; }
        };
        mineSweeper.exposeACell(3,4);

        assertEquals(CellState.EXPOSED, mineSweeper.getCellState(3,4));
        assertTrue(exposeNeighborsOfCalled);
    }

    @Test
    public void exposingAnExposedCellDoesNotCallExposeNeighborOf(){
        MineSweeper mineSweeper = new MineSweeper(){
            public void exposeNeighborsOf(int row, int col){ exposeNeighborsOfCalled = true; }
        };

        mineSweeper.exposeACell(3,4);

        exposeNeighborsOfCalled = false;
        mineSweeper.exposeACell(3,4);

        assertFalse(exposeNeighborsOfCalled);
    }

    @Test
    public void exposingAnAdjacentCellDoesNotExposeItsNeighbors(){
        MineSweeper mineSweeper = new MineSweeper(){
            public void exposeNeighborsOf(int row, int col){ exposeNeighborsOfCalled = true; }
            public boolean isAnAdjacentCell(int row, int col) { return true; }
        };

        mineSweeper.exposeACell(3, 5);

        assertFalse(exposeNeighborsOfCalled);
    }

    @Test
    public void exposeCellCallsExposeNeighborsOf(){
        MineSweeper mineSweeper = new MineSweeperWithExposeCell(){};
        mineSweeper.exposeNeighborsOf(3, 2);

        assertEquals(Arrays.asList(2, 1, 2, 2, 2, 3, 3, 1, 3, 3, 4, 1, 4, 2, 4, 3), rowsAndColumns);
    }

    @Test
    public void exposeNeighborsOfForTopLeftCorner(){
        MineSweeper mineSweeper = new MineSweeperWithExposeCell();
        mineSweeper.exposeNeighborsOf(0, 0);

        assertEquals(Arrays.asList(0, 1, 1, 0, 1, 1), rowsAndColumns);
    }

    @Test
    public void exposeNeighborsOfForTopRightCorner(){
        MineSweeper mineSweeper = new MineSweeperWithExposeCell();
        mineSweeper.exposeNeighborsOf(0, 9);

        assertEquals(Arrays.asList(0, 8, 1, 8, 1, 9), rowsAndColumns);
    }

    @Test
    public void exposeNeighborsOfForBottomLeftCorner(){
        MineSweeper mineSweeper = new MineSweeperWithExposeCell();
        mineSweeper.exposeNeighborsOf(9, 0);

        assertEquals(Arrays.asList(8, 0, 8, 1, 9, 1), rowsAndColumns);
    }

    @Test
    public void exposeNeighborsOfForBottomRightCorner(){
        MineSweeper mineSweeper = new MineSweeperWithExposeCell();
        mineSweeper.exposeNeighborsOf(9, 9);

        assertEquals(Arrays.asList(8 ,8, 8, 9, 9, 8), rowsAndColumns);
    }

    @Test
    public void exposeNeighborsOfForTopEdge(){
        MineSweeper mineSweeper = new MineSweeperWithExposeCell();
        mineSweeper.exposeNeighborsOf(0,1);

        assertEquals(Arrays.asList(0, 0, 0, 2, 1, 0, 1, 1, 1, 2), rowsAndColumns);
    }

    @Test
    public void exposeNeighborsOfForBottomEdge(){
        MineSweeper mineSweeper = new MineSweeperWithExposeCell();
        mineSweeper.exposeNeighborsOf(9, 1);

        assertEquals(Arrays.asList(8, 0, 8, 1, 8, 2, 9, 0, 9, 2), rowsAndColumns);
    }

    @Test
    public void exposeNeighborsOfForLeftEdge(){
        MineSweeper mineSweeper = new MineSweeperWithExposeCell();
        mineSweeper.exposeNeighborsOf(1, 0);

        assertEquals(Arrays.asList(0, 0, 0, 1, 1, 1, 2, 0, 2, 1), rowsAndColumns);
    }

    @Test
    public void exposeNeighborsOfForRightEdge(){
        MineSweeper mineSweeper = new MineSweeperWithExposeCell();
        mineSweeper.exposeNeighborsOf(1, 9);

        assertEquals(Arrays.asList(0, 8, 0, 9, 1, 8, 2, 8, 2, 9), rowsAndColumns);
    }

    @Test
    public void sealAnUnexposedCell(){
        mineSweeper.toggleSeal(2, 2);

        assertEquals(CellState.SEALED, mineSweeper.getCellState(2, 2));
    }

    @Test
    public void sealAnExposedCell(){
        mineSweeper.exposeACell(2, 2);
        mineSweeper.toggleSeal(2, 2);

        assertEquals(CellState.EXPOSED, mineSweeper.getCellState(2, 2));
    }

    @Test
    public void unsealASealedCell(){
        mineSweeper.toggleSeal(2, 2);
        mineSweeper.toggleSeal(2, 2);

        assertEquals(CellState.UNEXPOSED, mineSweeper.getCellState(2, 2));
    }

    @Test
    public void exposeASealedCell(){
        mineSweeper.toggleSeal(2, 2);
        mineSweeper.exposeACell(2, 2);

        assertEquals(CellState.SEALED, mineSweeper.getCellState(2, 2));
    }

    @Test
    public void exposeASealedCellDoNotCallExposeNeighborsOf(){
        MineSweeper mineSweeper = new MineSweeper(){
            public void exposeNeighborsOf(int row, int col){ exposeNeighborsOfCalled = true; }
        };
        mineSweeper.toggleSeal(2, 2);
        mineSweeper.exposeACell(2, 2);

        assertFalse(exposeNeighborsOfCalled);
    }

    @Test
    public void cannotExposeACellAfterExposingAMine(){
        int row, col = 0;
        outerloop:
        for(row = 0; row < 10; row++)
            for (col = 0; col < 9; col++)
                if (mineSweeper.isMine(row, col))
                    break outerloop;

        mineSweeper.exposeACell(row, col);
        mineSweeper.exposeACell(row, col + 1);

        assertEquals(CellState.UNEXPOSED, mineSweeper.getCellState(row, col+1));
    }

    @Test
    public void cannotSealACellAfterExposingAMine(){
        int row, col = 0;
        outerloop:
        for(row = 0; row < 10; row++)
            for (col = 0; col < 9; col++)
                if (mineSweeper.isMine(row, col))
                    break outerloop;

        mineSweeper.exposeACell(row, col);
        mineSweeper.toggleSeal(row, col+1);

        assertEquals(CellState.UNEXPOSED, mineSweeper.getCellState(row, col+1));
    }

    @Test
    public void verifyPlaceTenMines(){
        int mineCounts = 0;
        for(int row = 0; row < 10; row++)
            for(int col = 0; col < 10; col++)
                if(mineSweeper.isMine(row, col))
                    mineCounts++;

        assertEquals(10, mineCounts);
    }

    @Test
    public void verifyMinesAreRandom(){
        boolean twoMineGridsAreNotEqual = false;
        MineSweeper mineSweeper_1 = new MineSweeper();
        MineSweeper mineSweeper_2 = new MineSweeper();
        for(int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if ((mineSweeper_1.isMine(row, col) && !mineSweeper_2.isMine(row, col))
                        ||(!mineSweeper_1.isMine(row, col) && mineSweeper_2.isMine(row, col))){
                    twoMineGridsAreNotEqual = true;
                    break;
                }
            }
        }

        assertTrue(twoMineGridsAreNotEqual);
    }

    @Test
    public void verifyGameStatusIsInProgressAtStart(){

        assertEquals(MineSweeper.GameStatus.INPROGRESS, mineSweeper.getGameStatus());
    }

    @Test
    public void verifyGameStatusIsLostWhenExposingAMine(){
        int row, col = 0;
        outerloop:
        for(row = 0; row < 10; row++)
            for (col = 0; col < 10; col++)
                if (mineSweeper.isMine(row, col))
                    break outerloop;

        mineSweeper.exposeACell(row, col);
        assertEquals(MineSweeper.GameStatus.LOST, mineSweeper.getGameStatus());
    }

    @Test
    public void verifyGameStatusIsWonWhenSealAllMines(){
        MineSweeper mineSweeper = new MineSweeper(){
            public boolean isAnAdjacentCell(int row, int col){ return true; }
        };

        for(int row = 0; row < 10; row ++){
            for(int col = 0; col < 10; col++)
                if(mineSweeper.isMine(row, col))
                    mineSweeper.toggleSeal(row, col);
                else
                    mineSweeper.exposeACell(row,col);
        }

        assertEquals(MineSweeper.GameStatus.WON, mineSweeper.getGameStatus());
    }
}