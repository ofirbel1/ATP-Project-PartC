package Model;

import Client.Client;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.Solution;

import java.util.Observable;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observer;

import Client.IClientStrategy;
public class MyModel extends Observable implements IModel {
    private Maze myMaze;
    private Solution myMazeSolution;
    private int currRow;
    private int currCol;
    @Override
    public void generateMaze(int rows,int cols) throws UnknownHostException {
        Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy()
         {
            @Override
            public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                try {
                    ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                    ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                    toServer.flush();
                    int[] mazeDimensions = new int[]{rows, cols};
                    toServer.writeObject(mazeDimensions); //send maze dimensions to server
                    toServer.flush();
                    byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed withMyCompressor) from server
                    InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                    byte[] decompressedMaze = new byte[rows*cols+1000]; //allocating byte[] for the decompressed maze
                    is.read(decompressedMaze);
                    myMaze = new Maze(decompressedMaze);
                    currRow = myMaze.getStartPosition().getRow();
                    currCol = myMaze.getStartPosition().getColumn();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        client.communicateWithServer();
        setChanged();
        notifyObservers("mazeGenerate");
        setChanged();
        notifyObservers("ChangedRowCol");
    }
    @Override
    public void solveMaze(){
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new
                    IClientStrategy() {
                        @Override
                        public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                            try {
                                ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                                ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                                toServer.flush();
                                MyMazeGenerator mg = new MyMazeGenerator();
                                Maze maze = myMaze;
                                toServer.writeObject(maze); //send maze to server
                                toServer.flush();
                                myMazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server%s", mazeSolution));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        setChanged();
        notifyObservers("mazeSolve");
    }

    @Override
    public Maze getMyMaze(){
        return myMaze;
    }

    @Override
    public Solution getMyMazeSolution(){
        return myMazeSolution;
    }

    @Override
    public int getCurrCol(){
        return currCol;
    }

    @Override
    public int getCurrRow(){
        return currRow;
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void setCurrRow(int row){
        currRow = row;
    }

    @Override
    public void setCurrCol(int col){
        currCol = col;
    }

    @Override
    public void playerMoved(int addRow,int addCol){
        int newCol = currCol + addCol;
        int newRow = currRow +addRow;
        //check down
        if(newRow <= myMaze.getMaze().length-1 && addRow==1&&addCol==0&&myMaze.getMaze()[newRow][newCol]!=1){
            currCol = newCol;
            currRow = newRow;
            setChanged();
            notifyObservers("ChangedRowCol");
        }
        //check up
        if(newRow>=0 &&addRow==-1&&addCol==0&&myMaze.getMaze()[newRow][newCol]!=1){
            currCol = newCol;
            currRow = newRow;
            setChanged();
            notifyObservers("ChangedRowCol");
        }
        //check left
        if(newCol>=0 && addCol==-1&&addRow==0&&myMaze.getMaze()[newRow][newCol]!=1){
            currCol = newCol;
            currRow = newRow;
            setChanged();
            notifyObservers("ChangedRowCol");
        }
        //check right
        if(newCol <=myMaze.getMaze()[0].length-1 && addCol==1&&addRow==0&&myMaze.getMaze()[newRow][newCol]!=1){
            currCol = newCol;
            currRow = newRow;
            setChanged();
            notifyObservers("ChangedRowCol");
        }
        //check up right
        if(newCol<=myMaze.getMaze()[0].length-1 && newRow>=0 && addCol==1 && addRow==-1 && myMaze.getMaze()[newRow][newCol]!=1 &&(myMaze.getMaze()[currRow-1][currCol]==0||myMaze.getMaze()[currRow][currCol+1]==0)){
            currCol = newCol;
            currRow = newRow;
            setChanged();
            notifyObservers("ChangedRowCol");
        }
        //check up left
        if(newCol>=0 && newRow>=0 && addCol==-1 && addRow==-1 && myMaze.getMaze()[newRow][newCol]!=1 &&(myMaze.getMaze()[currRow-1][currCol]==0||myMaze.getMaze()[currRow][currCol-1]==0)){
            currCol = newCol;
            currRow = newRow;
            setChanged();
            notifyObservers("ChangedRowCol");
        }
        //check down right
        if(newCol<=myMaze.getMaze()[0].length-1 && newRow<=myMaze.getMaze().length-1 && addCol==1 && addRow==1 && myMaze.getMaze()[newRow][newCol]!=1 &&(myMaze.getMaze()[currRow][currCol+1]==0||myMaze.getMaze()[currRow+1][currCol]==0)){
            currCol = newCol;
            currRow = newRow;
            setChanged();
            notifyObservers("ChangedRowCol");
        }
        //check down left
        if(newCol>=0 && newRow<=myMaze.getMaze().length-1 && addCol==-1 && addRow==1 && myMaze.getMaze()[newRow][newCol]!=1 &&(myMaze.getMaze()[currRow+1][currCol]==0||myMaze.getMaze()[currRow][currCol-1]==0)){
            currCol = newCol;
            currRow = newRow;
            setChanged();
            notifyObservers("ChangedRowCol");
        }
    }

    @Override
    public Solution getSolution(){
        return myMazeSolution;
    }
}
