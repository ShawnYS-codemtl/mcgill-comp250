package StarterCode_Feb16;

public class Pawn extends Piece {

    public Pawn(int x, int y, Side side, Board b) {
        super(x, y, side, b);
    }

    @Override
    public boolean canMove(int destX, int destY) {

        if (this.getSide() == Side.BLACK){
            if (Math.abs(this.x - destX) == 1 && (this.y - destY) == -1){  // pawn attempt to move diagonal
                if (this.board.get(destX, destY) != null){  // if there is a Piece on destination square
                    Piece diagonal = this.board.get(destX, destY);
                    if (diagonal.getSide() == Side.WHITE){  // if Piece to capture is WHITE
                        return true;
                    }
                }
            }
            else if (this.y == 1){  // pawn on BLACK starting square
                return ((this.y - destY == -2 && this.x == destX && board.get(destX, destY) == null) || (this.y - destY == -1 && this.x == destX && board.get(destX, destY) == null));
            }
            else{
                return (this.y - destY == -1 && this.x == destX && board.get(destX, destY) == null);
            }
        }
        else if (this.getSide() == Side.WHITE){
            if (Math.abs(this.x - destX) == 1 && (this.y - destY) == 1) {  // pawn attempt to move diagonal
                if (this.board.get(destX, destY) != null) {  // if there is a Piece on destination square
                    Piece diagonal = this.board.get(destX, destY);
                    if (diagonal.getSide() == Side.BLACK) {  // if Piece to capture is BLACK
                        return true;
                    }
                }
            }

            else if (this.y == 6){ // pawn on WHITE starting square
                return ((this.y - destY == 2 && this.x == destX && board.get(destX, destY) == null) || (this.y - destY == 1 && this.x == destX && board.get(destX, destY) == null));
            }
            else{
                return (this.y - destY == 1 && this.x == destX && board.get(destX, destY) == null);
            }
        }
        return false;
    }

    @Override
    public String getSymbol() {
        return this.getSide() == Side.BLACK ? "♟" : "♙";
    }
}
