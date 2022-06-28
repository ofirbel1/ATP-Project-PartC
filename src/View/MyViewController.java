package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.MyMazeGenerator;

import java.io.FileInputStream;
import java.net.UnknownHostException;
import java.util.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.Observer;
import java.util.ResourceBundle;

public class MyViewController implements Initializable,IView, Observer {
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;
    public Button solveMaze;
    public MyViewModel myViewModel;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();

    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }

    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }

    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }

    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
    }

    public void generateMaze(ActionEvent actionEvent) throws UnknownHostException {
        mazeDisplayer.setSolution(null);

        int rows = Integer.valueOf(textField_mazeRows.getText());
        int cols = Integer.valueOf(textField_mazeColumns.getText());

        solveMaze.setDisable(false);

        myViewModel.generateMaze(rows,cols);
    }

    public void solveMaze(ActionEvent actionEvent) {
        myViewModel.solveMaze();
        solveMaze.setDisable(true);
    }

    public void openFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showOpenDialog(null);
        //...
    }

    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP -> myViewModel.playerMoved(-1,1); // up right
            case NUMPAD7 -> myViewModel.playerMoved(-1,-1); // up left
            case NUMPAD3 -> myViewModel.playerMoved(1,1); //down right
            case NUMPAD1 -> myViewModel.playerMoved(1,-1); //down left
            case NUMPAD4 -> myViewModel.playerMoved(0,-1); // left
            case NUMPAD6 -> myViewModel.playerMoved(0,1); // right
            case NUMPAD8 -> myViewModel.playerMoved(-1,0); //up
            case NUMPAD2 -> myViewModel.playerMoved(1,0); //down
        }

        keyEvent.consume();
    }

    public void mouseDragged(MouseEvent mouseEvent){
        double movedX = mouseEvent.getX()/mazeDisplayer.getCellWidth()-myViewModel.getCurrCol();
        double movedY = mouseEvent.getY()/mazeDisplayer.getCellHeight()-myViewModel.getCurrRow();
        int x,y;
        if(movedX>1){
            x=1;
        }
        else if(movedX<-1){
            x=-1;
        }
        else{
            x=0;
        }

        if(movedY>1){
            y=1;
        }
        else if(movedY<-1){
            y=-1;
        }
        else{
            y=0;
        }
        myViewModel.playerMoved(y,x);
    }
    public void setPlayerPosition(int row, int col){
        mazeDisplayer.setPlayerPosition(row, col);
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
        //check if won
        if(row == myViewModel.getMaze().getGoalPosition().getRow()&&col == myViewModel.getMaze().getGoalPosition().getColumn()){
            win();
        }
    }
    public void win(){
        String image = mazeDisplayer.getWinnerImage();
        Image winnerImage = null;
        try{
            winnerImage =new Image(new FileInputStream(image));
        }
        catch (Exception e){
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Winnerrr!");
        ImageView imageView =new ImageView(winnerImage);
        alert.setGraphic(imageView);
        alert.showAndWait();
    }
    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    public void aboutAction(ActionEvent actionEvent){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("developers names:Ofir Belenky and Liron Zvi\n we used prim algorithm to create our maze\n and solved it using best algorithm");
        alert.show();
    }

    public void helpAction(ActionEvent actionEvent){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("to start game:\n enter num cols and rows\n press new at option bar");
        alert.show();
    }

    public MyViewController(){
        myViewModel = new MyViewModel(new MyModel());
        myViewModel.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        String changeMade = (String) arg;

        switch (changeMade) {
            case "mazeGenerate" -> mazeDisplayer.drawMaze(myViewModel.getMaze().getMaze(),myViewModel.getMaze());
            case "ChangedRowCol" -> setPlayerPosition(myViewModel.getCurrRow(), myViewModel.getCurrCol());
            case "mazeSolve" -> mazeDisplayer.setSolution(myViewModel.getSolution());
        }
    }
}
