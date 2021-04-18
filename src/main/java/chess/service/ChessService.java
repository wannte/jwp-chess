package chess.service;

import chess.controller.dto.*;
import chess.dao.CommandDao;
import chess.domain.game.BoardFactory;
import chess.domain.game.Game;
import chess.domain.location.Position;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChessService {
    private final CommandDao commandDao;

    public ChessService(CommandDao commandDao) {
        this.commandDao = commandDao;
    }

    public void move(Long roomId, String from, String to) {
        Game game = newGame(roomId);
        game.move(Position.from(from), Position.from(to));
        commandDao.insert(roomId, from, to);
    }

    public Map<String, Object> load(Long roomId) {
        Game game = newGame(roomId);
        Map<String, Object> model = makeChessModel(game);
        model.put("room", new RoomDto(roomId, ""));
        return model;
    }

    private Game newGame(Long roomId) {
        Game game = new Game(BoardFactory.create());
        List<MoveDto> moves = commandDao.selectOf(roomId);
        for (MoveDto move : moves) {
            game.move(Position.from(move.getFrom()), Position.from(move.getTo()));
        }
        return game;
    }

    private Map<String, Object> makeChessModel(Game game) {
        Map<String, Object> model = new HashMap<>();
        setBoard(new BoardDto(game.allBoard()).getMaps(), model);
        setGameInformation(new ScoreDtos(game.score()).getScoreDtos(), new ColorDto(game.currentPlayer()), game.isEnd(), model);
        return model;
    }

    private void setBoard(Map<PositionDto, PieceDto> board, Map<String, Object> model) {
        for (PositionDto positionDTO : board.keySet()) {
            model.put(positionDTO.getKey(), board.get(positionDTO));
        }
    }

    private void setGameInformation(List<ScoreDto> score,
                                    ColorDto color,
                                    boolean isFinished,
                                    Map<String, Object> model) {
        model.put("scores", score);
        model.put("turn", color);

        if (isFinished) {
            model.put("winner", color);
        }
    }
}