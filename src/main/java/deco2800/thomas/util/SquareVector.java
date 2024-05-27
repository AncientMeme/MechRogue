package deco2800.thomas.util;

import java.io.Serializable;
import deco2800.thomas.entities.AgentEntity;
import java.util.Random;

public class SquareVector implements Serializable {
	private float col; // col corresponds to the X coordinate
    private float row; // row corresponds to the Y coordinate

    /**
     * Constructor for a SquareVector
     * @param col the col value for the vector
     * @param row the row value for the vector
     */
    public SquareVector(float col, float row) {
        this.col = col;
        this.row = row;
    }
    
    public SquareVector(SquareVector vector) {
    	this.col = vector.col;
    	this.row = vector.row;
    }

    public SquareVector(String vector) {
        String[] pos = vector.split(",",2);
    	this.col = Float.parseFloat(pos[0]);
    	this.row = Float.parseFloat(pos[1]);
    }

    /**
     * Helper function to allow constructing from an IntegerPoint
     * @param point
     */
    public SquareVector(IntegerPoint point) {
        this.col = point.getX();
        this.row = point.getY();
    }


    public SquareVector() {

    }

    /**
     * Getter for the col value of the vector
     * @return the col value
     */
    public float getCol() {
        return col;
    }

    /**
     * Getter for the row value of the vector
     * @return the row value
     */
    public float getRow() {
        return row;
    }

    /**
     * Setter for the column value
     * @param col the column value to be set
     */
    public void setCol(float col) {
        this.col = col;
    }

    /**
     * Setter for the row value
     * @param col the row value to be set
     */
    public void setRow(float col) {
        this.row = col;
    }

    public SquareVector add(SquareVector toAdd) {
        float row = getRow() + toAdd.getRow();
        float col = getCol() + toAdd.getCol();
        return new SquareVector(col, row);
    }

    /**
     * Calculates the distance between two coordinates on a 2D plane. Based off of the cubeDistance function.
     * Uses Manhattan distance.
     * @param vcol the x coordinate
     * @param vrow the y coordinate
     * @return the distance between the two coordinates
     */
    public float distance(float vcol, float vrow) {
        return distance(new SquareVector(vcol, vrow));
    }
    

    public float distance(SquareVector vector) {
        return Math.abs(Math.round(getCol()) - Math.round(vector.getCol()))
                    + (float) Math.abs(Math.round(getRow()) - Math.round(vector.getRow()));
    }
    
    private float distanceAsCartesian(SquareVector point) {
        return (float) Math.sqrt(Math.pow(point.col - col, 2) + Math.pow(point.row - row, 2));
    }
    

    public SquareVector moveToward(SquareVector point, double distance) {
    	//System.out.println(distance(point));
        if (distanceAsCartesian(point) < distance) {
            return new SquareVector(point.col, point.row);
        }
        
        double deltaCol = this.col - point.col;
        double deltaRow = this.row - point.row;
        double angle;

        angle = Math.atan2(deltaRow, deltaCol) + Math.PI;

        double xShift = Math.cos(angle) * distance;
        double yShift = Math.sin(angle) * distance;
        
        //System.out.println(String.format("    dCol: %.2f, dRow: %.2f, angle: %.2f, colShift: %.2f, rowShift: %.2f", deltaCol, deltaRow, angle, xShift, yShift));

        return this.add(new SquareVector((float)xShift, (float)yShift));
    }
    
    public boolean isCloseEnoughToBeTheSame(SquareVector vector) {
    	return MathUtil.floatEquality(this.getCol(), vector.getCol())
				&& MathUtil.floatEquality(this.getRow(), vector.getRow());
    }

    public boolean isCloseEnoughToBeTheSame(SquareVector vector, float e) {
        return MathUtil.floatEquality(this.getCol(), vector.getCol(), e)
                && MathUtil.floatEquality(this.getRow(), vector.getRow(), e);
    }

    /**
     * Generates a Square vector a random distance away within a range
     * and returns it
     *
     * @param range The distance
     * @return SquareVector a random distance away within range
     */
    public SquareVector randomDistance(Float range) {
        float col;
        float row;

        Random r = new Random();
        float colRandom = -range + r.nextFloat() * (2*range);
        float rowRandom = -range + r.nextFloat() * (2*range);

        col = this.getCol();
        row = this.getRow();
        SquareVector position = new SquareVector(col, row);

        position.setCol(col + colRandom);
        position.setRow(row + rowRandom);

        return position;
    }

    /**
     * Equals Method returns true iff the two objects are equal 
     * based on their col and row values.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SquareVector)) {
            return false;
        }
        SquareVector vector = (SquareVector) obj;
		return isCloseEnoughToBeTheSame(vector);
    }

    @Override
    public int hashCode() {
        return ((31 * (int) this.getCol()) + 17) * (int) this.getRow();
    }
    
    @Override
    public String toString() {
    	return String.format("%f, %f", col, row);
    }

    public static SquareVector fromAngleRadius(float angleDeg, float radius) {
        return new SquareVector(
                (float)Math.cos(Math.toRadians(angleDeg)) * radius,
                (float)Math.sin(Math.toRadians(angleDeg)) * radius
        );
    }

}
