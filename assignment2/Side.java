package StarterCode_Feb16;

public enum Side {
    BLACK, WHITE;

    public static Side negate(Side s) {
        return s == Side.BLACK ? Side.WHITE : Side.BLACK;
    }
}
