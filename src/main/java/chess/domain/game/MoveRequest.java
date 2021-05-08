package chess.domain.game;

public class MoveRequest {
    private final String from;
    private final String to;

    public MoveRequest(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
