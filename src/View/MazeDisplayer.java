package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

public class MazeDisplayer extends Canvas {
    private int[][] maze;
    Maze myMaze;
    private Solution solution;
    // player position:
    private int playerRow = 0;
    private int playerCol = 0;
    // wall and player images:
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty solutionFileName = new SimpleStringProperty();
    StringProperty goalImage = new SimpleStringProperty();
    StringProperty winnerImage = new SimpleStringProperty();
    double cellWidth,cellHeight;
    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public void setPlayerPosition(int row, int col) {
        this.playerRow = row;
        this.playerCol = col;
        draw();
    }

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public String getGoalImage(){return goalImage.get();}

    public void setGoalImage(String goalImage){this.goalImage.set(goalImage);}

    public String getWinnerImage(){return winnerImage.get();}

    public void setWinnerImage(String goalImage){this.winnerImage.set(goalImage);}

    public String imageFileNameWallProperty() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public void setSolutionFileName(String solutionFileName){this.solutionFileName.set(solutionFileName);}

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public String getSolutionFileName(){return solutionFileName.get();}

    public String imageFileNamePlayerProperty() {
        return imageFileNamePlayer.get();
    }

    public double getCellHeight() {
        return cellHeight;
    }

    public double getCellWidth() {
        return cellWidth;
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

    public void drawMaze(int[][] maze,Maze newMaze) {
        this.maze = maze;
        myMaze = newMaze;
        draw();
    }
    private void draw() {
        if(maze != null){
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int rows = maze.length;
            int cols = maze[0].length;

            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;
            this.cellHeight = cellHeight;
            this.cellWidth=cellWidth;
            GraphicsContext graphicsContext = getGraphicsContext2D();
            //clear the canvas:
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);

            drawMazeWalls(graphicsContext, cellHeight, cellWidth, rows, cols);
            drawPlayer(graphicsContext, cellHeight, cellWidth);
            drawSolution(graphicsContext,cellHeight,cellWidth);
            drawGoalImage(graphicsContext,cellHeight,cellWidth);
        }
    }

    private void drawMazeWalls(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        graphicsContext.setFill(Color.RED);

        Image wallImage = null;
        try{
            wallImage = new Image(new FileInputStream(getImageFileNameWall()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no wall image file");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(maze[i][j] == 1){
                    //if it is a wall:
                    double x = j * cellWidth;
                    double y = i * cellHeight;
                    if(wallImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);
                }
            }
        }
    }

    private void drawPlayer(GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = getPlayerCol() * cellWidth;
        double y = getPlayerRow() * cellHeight;
        graphicsContext.setFill(Color.GREEN);

        Image playerImage = null;
        try {
            playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        if(playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }
    public void setSolution(Solution solution){
        this.solution = solution;
        draw();
    }
    private void drawSolution(GraphicsContext graphicsContext, double cellHeight, double cellWidth){
        Image solutionImage = null;
        try {
            solutionImage = new Image(new FileInputStream(getSolutionFileName()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        if(solution==null){
            return;
        }
        Position sol;
        for (AState state : solution.getSolutionPath()){
            sol = (Position)(state).getState();
            graphicsContext.drawImage(solutionImage, sol.getColumnIndex()*cellWidth, sol.getRowIndex()*cellHeight, cellWidth, cellHeight);

        }
    }
    private void drawGoalImage(GraphicsContext graphicsContext, double cellHeight, double cellWidth){
        Image goalImage = null;
        try {
            goalImage = new Image(new FileInputStream(getGoalImage()));
        } catch (FileNotFoundException e) {
            System.out.println("There is no player image file");
        }
        graphicsContext.drawImage(goalImage, myMaze.getGoalPosition().getColumn()*cellWidth, myMaze.getGoalPosition().getRow()*cellHeight, cellWidth, cellHeight);
    }
}
