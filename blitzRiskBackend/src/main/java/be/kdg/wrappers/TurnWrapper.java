package be.kdg.wrappers;

import be.kdg.model.Move;
import be.kdg.model.Turn;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marlies on 9/03/2015.
 */
public class TurnWrapper {
    private int id;
    private int number;
    private int gameId;
    private List<MoveWrapper> moveWrapperList;
    private int playerId;

    public TurnWrapper(Turn turn) {
        id = turn.getId();
        number = turn.getNumber();
        gameId = turn.getGame().getId();
        List<MoveWrapper> moveWrappers = new ArrayList<>();
        for (Move move: turn.getMoves()) {
            moveWrappers.add(new MoveWrapper(move));
        }
        playerId = turn.getPlayer().getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public List<MoveWrapper> getMoveWrapperList() {
        return moveWrapperList;
    }

    public void setMoveWrapperList(List<MoveWrapper> moveWrapperList) {
        this.moveWrapperList = moveWrapperList;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
