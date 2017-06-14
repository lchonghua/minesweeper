package game;

import java.util.Random;

public class MineSweeper {
    private static final int SIZE = 10;
    private static final int NUMBER_MINES = 10;

    private CellState[][] statusGrid = new CellState[SIZE][SIZE];
    private int[][] statusMines = new int[SIZE][SIZE];
    public enum CellState {UNEXPOSED, EXPOSED, SEALED};
    public enum GameStatus {INPROGRESS, LOST, WON};

    public MineSweeper() {
        initializeGrid();
        placeMines();
        setMineInformation();
    }

    protected MineSweeper(boolean test){
        initializeGrid();
        if(!test)
            placeMines();
    }

    private void initializeGrid(){
        for(int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                statusGrid[row][col] = CellState.UNEXPOSED;
                statusMines[row][col] = 0;
            }
        }
    }

    private void placeMines(){
        Random random = new Random();
        int numOfMinesToBePlaced = NUMBER_MINES;
        while(numOfMinesToBePlaced!=0){
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            if(statusMines[row][col] != -1){
                statusMines[row][col] = -1;
                numOfMinesToBePlaced --;
            }
        }
    }

    private void setMineInformation(){
        for(int row = 0; row < SIZE; row++)
            for(int col = 0; col < SIZE; col++){
                if(statusMines[row][col] == -1){
                    addMineCounts(row-1, col-1);
                    addMineCounts(row-1, col);
                    addMineCounts(row-1, col+1);
                    addMineCounts(row, col-1);
                    addMineCounts(row, col+1);
                    addMineCounts(row+1, col-1);
                    addMineCounts(row+1, col);
                    addMineCounts(row+1, col+1);
                }
            }
    }

    private void addMineCounts(int row, int col){
        if(isWithinGrid(row, col) && statusMines[row][col]!=-1)
            statusMines[row][col]++;
    }

    public int getMineInfo(int row, int col){ return statusMines[row][col]; }

    boolean isWithinGrid(int row, int col){
        return (row >= 0 && row <SIZE && col >= 0 && col < SIZE);
    }

    public void exposeACell(int row, int col){
        if(statusGrid[row][col] == CellState.UNEXPOSED && getGameStatus()== GameStatus.INPROGRESS) {
            statusGrid[row][col] = CellState.EXPOSED;

            if(!isAnAdjacentCell(row, col))
                exposeNeighborsOf(row, col);
        }
    }

    public void toggleSeal(int row, int col){
        if(getGameStatus()!= GameStatus.LOST){
            if(statusGrid[row][col] == CellState.UNEXPOSED)
                statusGrid[row][col] = CellState.SEALED;
            else if(statusGrid[row][col] == CellState.SEALED)
                statusGrid[row][col] = CellState.UNEXPOSED;
        }
    }

    public void exposeNeighborsOf(int row, int col){
        for(int i = row-1; i < row+2; i++)
            for(int j = col-1; j < col+2; j++){
                if(isWithinGrid(i, j)&& ((i*100+j)!=(row*100+col)))
                    exposeACell(i, j);
            }
    }

    public boolean isAnAdjacentCell(int row, int col){ return statusMines[row][col] > 0; }

    public boolean isMine(int row, int col){ return (statusMines[row][col] == -1); }

    public CellState getCellState(int row, int col){ return  statusGrid[row][col]; }

    public GameStatus getGameStatus(){
        GameStatus currentGameStatus = GameStatus.INPROGRESS;
        int cellCount = 0;
        outerloop:
        for(int row = 0; row < SIZE; row++){
            for(int col = 0; col < SIZE; col++){
                if(statusGrid[row][col] == CellState.EXPOSED && statusMines[row][col] == -1){
                    currentGameStatus = GameStatus.LOST;
                    break outerloop;
                }
                else if((statusGrid[row][col] == CellState.EXPOSED && statusMines[row][col] != -1)||
                        (statusGrid[row][col] == CellState.SEALED && statusMines[row][col] == -1))
                    cellCount++;
            }
        }
        if(cellCount == SIZE*SIZE)
            currentGameStatus = GameStatus.WON;

        return currentGameStatus;
    }
}