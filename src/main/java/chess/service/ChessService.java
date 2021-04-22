package chess.service;

import chess.controller.dto.*;
import chess.dao.CommandDao;
import chess.domain.game.BoardFactory;
import chess.domain.game.Game;
import chess.domain.location.Position;
import chess.domain.piece.Color;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

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

    public void load(Long roomId, Model model) {
        Game game = newGame(roomId);
        makeChessModel(game, model);
        model.addAttribute("room", new RoomDto(roomId, ""));
    }

    private Game newGame(Long roomId) {
        Game game = new Game(BoardFactory.create());
        List<MoveDto> moves = commandDao.findAllCommandOf(roomId);
        for (MoveDto move : moves) {
            game.move(Position.from(move.getFrom()), Position.from(move.getTo()));
        }
        return game;
    }

    private void makeChessModel(Game game, Model model) {
        setBoard(new BoardDto(game.allBoard()).getMaps(), model);
        setGameScore(game, model);
        setGameInformation(new ColorDto(game.currentPlayer()), game.isEnd(), model);
    }

    private void setBoard(Map<PositionDto, PieceDto> board, Model model) {
        for (PositionDto positionDto : board.keySet()) {
            model.addAttribute(positionDto.getKey(), board.get(positionDto));
        }
    }

    private void setGameScore(Game game, Model model) {
        model.addAttribute("blackScore", new ScoreDto(Color.BLACK, game.score(Color.BLACK)));
        model.addAttribute("whiteScore", new ScoreDto(Color.WHITE, game.score(Color.WHITE)));
    }

    private void setGameInformation(ColorDto color,
                                    boolean isFinished,
                                    Model model) {
        model.addAttribute("turn", color);
        if (isFinished) {
            model.addAttribute("winner", color);
        }
    }
}