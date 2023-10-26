package hr.algebra.chess.controllers;

import hr.algebra.chess.model.GameBoard;
import hr.algebra.chess.model.Team;
import hr.algebra.chess.model.Tile;
import hr.algebra.chess.model.pieces.King;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Objects;

import static hr.algebra.chess.utils.GameUtils.*;

public class GameController {
    public static final int NUM_ROWS = 8;
    public static final int NUM_COLS = 8;

    private static final Border SELECT_BORDER = new Border(new BorderStroke(
            Color.YELLOWGREEN,
            BorderStrokeStyle.SOLID,
            new CornerRadii(0),
            new BorderWidths(5),
            new Insets(0)
    ));
    private static final Border MOVABLE_BORDER = new Border(new BorderStroke(
            Color.LIMEGREEN,
            BorderStrokeStyle.DASHED,
            new CornerRadii(0),
            new BorderWidths(5),
            new Insets(3)
    ));

    @FXML
    private Button A1, A2, A3, A4, A5, A6, A7, A8,
            B1, B2, B3, B4, B5, B6, B7, B8,
            C1, C2, C3, C4, C5, C6, C7, C8,
            D1, D2, D3, D4, D5, D6, D7, D8,
            E1, E2, E3, E4, E5, E6, E7, E8,
            F1, F2, F3, F4, F5, F6, F7, F8,
            G1, G2, G3, G4, G5, G6, G7, G8,
            H1, H2, H3, H4, H5, H6, H7, H8;

    private static Team playerTurn;
    private static Button selectedFigure;
    private static List<Tile> movableTiles;
    private static boolean win = false;

    public void initialize() {
        Tile[][] board = new Tile[NUM_ROWS][NUM_COLS];

        board[0][0] = new Tile(A8);
        board[0][1] = new Tile(B8);
        board[0][2] = new Tile(C8);
        board[0][3] = new Tile(D8);
        board[0][4] = new Tile(E8);
        board[0][5] = new Tile(F8);
        board[0][6] = new Tile(G8);
        board[0][7] = new Tile(H8);
        board[1][0] = new Tile(A7);
        board[1][1] = new Tile(B7);
        board[1][2] = new Tile(C7);
        board[1][3] = new Tile(D7);
        board[1][4] = new Tile(E7);
        board[1][5] = new Tile(F7);
        board[1][6] = new Tile(G7);
        board[1][7] = new Tile(H7);
        board[2][0] = new Tile(A6);
        board[2][1] = new Tile(B6);
        board[2][2] = new Tile(C6);
        board[2][3] = new Tile(D6);
        board[2][4] = new Tile(E6);
        board[2][5] = new Tile(F6);
        board[2][6] = new Tile(G6);
        board[2][7] = new Tile(H6);
        board[3][0] = new Tile(A5);
        board[3][1] = new Tile(B5);
        board[3][2] = new Tile(C5);
        board[3][3] = new Tile(D5);
        board[3][4] = new Tile(E5);
        board[3][5] = new Tile(F5);
        board[3][6] = new Tile(G5);
        board[3][7] = new Tile(H5);
        board[4][0] = new Tile(A4);
        board[4][1] = new Tile(B4);
        board[4][2] = new Tile(C4);
        board[4][3] = new Tile(D4);
        board[4][4] = new Tile(E4);
        board[4][5] = new Tile(F4);
        board[4][6] = new Tile(G4);
        board[4][7] = new Tile(H4);
        board[5][0] = new Tile(A3);
        board[5][1] = new Tile(B3);
        board[5][2] = new Tile(C3);
        board[5][3] = new Tile(D3);
        board[5][4] = new Tile(E3);
        board[5][5] = new Tile(F3);
        board[5][6] = new Tile(G3);
        board[5][7] = new Tile(H3);
        board[6][0] = new Tile(A2);
        board[6][1] = new Tile(B2);
        board[6][2] = new Tile(C2);
        board[6][3] = new Tile(D2);
        board[6][4] = new Tile(E2);
        board[6][5] = new Tile(F2);
        board[6][6] = new Tile(G2);
        board[6][7] = new Tile(H2);
        board[7][0] = new Tile(A1);
        board[7][1] = new Tile(B1);
        board[7][2] = new Tile(C1);
        board[7][3] = new Tile(D1);
        board[7][4] = new Tile(E1);
        board[7][5] = new Tile(F1);
        board[7][6] = new Tile(G1);
        board[7][7] = new Tile(H1);

        GameBoard.gameBoard = board;
        GameBoard.setBoard();
        playerTurn = Team.White;
        selectedFigure = null;
        movableTiles = null;
    }

    public void buttonPressed(ActionEvent event) {
        Button clickedButton = (Button)event.getSource();
        //check if one of your figures is selected
        if(selectedFigure == null && clickedButton.getGraphic() != null && isYourColor(clickedButton)) {
            selectFigure(clickedButton);
            setMovableTiles();
        }
        //check if your selected figure was changed to another figure
        else if(selectedFigure != null && clickedButton.getGraphic() != null && isYourColor(clickedButton)) {
            selectedFigure.setBorder(null);
            removeMarks();
            selectFigure(clickedButton);
            setMovableTiles();
        }
        //check if you selected an enemy figure for attack
        else if(selectedFigure != null && clickedButton.getGraphic() != null && !isYourColor(clickedButton) && isMovableTile(clickedButton)) {
            removeEnemy(clickedButton);
            moveToPosition(clickedButton);
            removeMarks();
            if(win) {
                endGame();
            }
            else {
                changeTurn();
            }
            win = false;
        }
        //check if you selected an empty space for movement
        else if(selectedFigure != null && clickedButton.getGraphic() == null && isMovableTile(clickedButton)) {
            moveToPosition(clickedButton);
            changeTurn();
            removeMarks();
        }
    }

    private void endGame() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("End of the game");
        alert.setHeaderText(null);
        alert.setContentText(playerTurn.toString() + " wins!");
        alert.showAndWait();

        GameBoard.clearBoard();
        GameBoard.setBoard();
        playerTurn = Team.White;
    }

    private void removeMarks() {
        for(int i = 0; i < NUM_ROWS; i++)
        {
            for(int j = 0; j < NUM_COLS; j++)
            {
                if(movableTiles.contains(GameBoard.gameBoard[i][j]))
                {
                    GameBoard.gameBoard[i][j].getButton().setBorder(null);
                }
            }
        }
    }

    private void setMovableTiles() {
        movableTiles = Objects.requireNonNull(findTile(selectedFigure)).getPiece().getMovableTiles(selectedFigure);
        for(int i = 0; i < NUM_ROWS; i++)
        {
            for(int j = 0; j < NUM_COLS; j++)
            {
                if(movableTiles.contains(GameBoard.gameBoard[i][j]))
                {
                    GameBoard.gameBoard[i][j].getButton().setBorder(MOVABLE_BORDER);
                }
            }
        }
    }

    private boolean isMovableTile(Button clickedButton) {
        return movableTiles.contains(findTile(clickedButton));
    }

    private void selectFigure(Button clickedButton) {
        clickedButton.setBorder(SELECT_BORDER);
        selectedFigure = clickedButton;
    }

    private void moveToPosition(Button clickedButton) {
        Tile selectedTile = findTile(clickedButton);
        Objects.requireNonNull(selectedTile).setPiece(Objects.requireNonNull(findTile(selectedFigure)).getPiece());
        Objects.requireNonNull(selectedTile).getButton().setGraphic(selectedFigure.getGraphic());
        Objects.requireNonNull(findTile(selectedFigure)).setPiece(null);
        selectedFigure.setGraphic(null);
        selectedFigure.setBorder(null);
        selectedFigure = null;
    }

    private void removeEnemy(Button clickedButton) {
        Tile selectedTile = findTile(clickedButton);
        checkWin(Objects.requireNonNull(selectedTile));
        Objects.requireNonNull(selectedTile).setPiece(null);
        selectedTile.getButton().setGraphic(null);
    }

    private void checkWin(Tile selectedTile) {
        if(selectedTile.getPiece().getClass() == King.class)
        {
            win = true;
        }
    }

    private void changeTurn() {
        if(playerTurn == Team.White)
        {
            playerTurn = Team.Black;
            return;
        }
        playerTurn = Team.White;
    }

    private boolean isYourColor(Button clickedButton) {
        Tile selectedTile = findTile(clickedButton);
        return Objects.requireNonNull(selectedTile).getPiece().getTeamColor() == playerTurn;
    }
}