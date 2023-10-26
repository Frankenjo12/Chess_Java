package hr.algebra.chess.model.pieces;

import hr.algebra.chess.model.GameBoard;
import hr.algebra.chess.model.Piece;
import hr.algebra.chess.model.Team;
import hr.algebra.chess.model.Tile;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static hr.algebra.chess.utils.GameUtils.findTile;
import static hr.algebra.chess.utils.GameUtils.getTileLocation;
import static hr.algebra.chess.utils.MovementUtils.*;

public class Bishop extends Piece {
    public Bishop(int locationX, int locationY, ImageView img, Team teamColor) {
        super(locationX, locationY, img, teamColor);
    }

    @Override
    public List<Tile> getMovableTiles(Button selectedFigure) {
        Pair<Integer, Integer> tileLocation = getTileLocation(findTile(selectedFigure));
        List<Tile> movableTiles = new ArrayList<>();
        Team teamColor = Objects.requireNonNull(findTile(selectedFigure)).getPiece().getTeamColor();

        checkMainDiagonalMovement(movableTiles, Objects.requireNonNull(tileLocation), teamColor);
        checkSecondaryDiagonalMovement(movableTiles, Objects.requireNonNull(tileLocation), teamColor);

        return movableTiles;
    }
}