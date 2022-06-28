package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private IModel myModel;

    public MyViewModel(IModel model){
        myModel = model;
        myModel.assignObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }

    public void generateMaze(int rows,int cols) throws UnknownHostException {
        myModel.generateMaze(rows,cols);
    }

    public void solveMaze(){
        myModel.solveMaze();
    }

    public Maze getMaze(){
        return myModel.getMyMaze();
    }

    public int getCurrCol(){
        return myModel.getCurrCol();
    }

    public int getCurrRow(){
        return myModel.getCurrRow();
    }
    public void playerMoved(int row,int col){
        myModel.playerMoved(row,col);
    }
    public Solution getSolution(){
        return myModel.getSolution();
    }
}
