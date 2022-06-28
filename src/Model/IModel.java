package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.net.UnknownHostException;
import java.util.Observer;

public interface IModel {
    void generateMaze(int rows,int cols)throws UnknownHostException;
    void solveMaze();
    Maze getMyMaze();
    Solution getMyMazeSolution();
    void assignObserver(Observer o);
    int getCurrCol();
    int getCurrRow();
    void setCurrRow(int row);
    void setCurrCol(int row);
    public void playerMoved(int addRow,int addCol);
    public Solution getSolution();
}
