package StarterCode_Feb16;

public class Queen extends Piece{

    public Queen(int x, int y, Side side, Board b) {
        super(x, y, side, b);
    }

    @Override
    public boolean canMove(int destX, int destY) {
        return (Math.abs(this.x - destX) == Math.abs(this.y  - destY)) || (this.x == destX || this.y == destY);
    }

    @Override
    public String getSymbol() {
        return this.getSide() == Side.BLACK ? "♛" : "♕";
    }
}
