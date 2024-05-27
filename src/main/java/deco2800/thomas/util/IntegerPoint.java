package deco2800.thomas.util;

import java.util.Objects;

/**
 * Class to store a point comprised of two integers
 */
public class IntegerPoint {
    int x;
    int y;

    /**
     * Constructs integer point at x, y
     * @param x
     * @param y
     */
    public IntegerPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs integer point at floored vector position
     * @param vector square vector to initialise from
     */
    public IntegerPoint(SquareVector vector) {
        this.x = (int)Math.floor(vector.getCol());
        this.y = (int)Math.floor(vector.getRow());
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Setters
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Helper function to add two IntegerPoints. Not done in place
     * @param b other integer point
     * @return New IntegerPoint constructed from this + b
     */
    public IntegerPoint add(IntegerPoint b) {
        return new IntegerPoint(this.x + b.x, this.y + b.y);
    }

    @Override
    public String toString() {
        return String.format("{X: %d, Y: %d}", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntegerPoint that = (IntegerPoint) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
