package chess.controller;

import chess.exception.DataAccessException;
import chess.service.ChessService;
import chess.service.RoomService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class SparkChessController {
    private final ChessService chessService;
    private final RoomService roomService;

    public SparkChessController(ChessService chessService, RoomService roomService) {
        this.chessService = chessService;
        this.roomService = roomService;
    }

    public void run() {
        exception(IllegalArgumentException.class, this::handleException);
        exception(DataAccessException.class, this::handleException);
    }


    private void handleException(RuntimeException e, Request req, Response res) {
        res.status(404);
        res.body(e.getMessage());
    }


    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
