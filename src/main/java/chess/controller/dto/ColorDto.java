package chess.controller.dto;

import chess.domain.piece.Color;

public class ColorDto {
    String color;

    public ColorDto(Color color) {
        this.color = color.name();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
