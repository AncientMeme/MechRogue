package deco2800.thomas.managers;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import deco2800.thomas.util.Vector2;

/**
 * Storage object for sprite sheets.
 * Stored in the texture manager.
 */
public class SpriteSheet {
    // Internal reference to sprite sheet texture
    private String atlasIdentifier;

    private int cellWidth;
    private int cellHeight;
    private int cellsPerRow;
    private int totalCellCount;

    /**
     * Constructs a sprite sheet from a path and some options
     * @param atlasIdentifier atlas identifier
     * @param cellWidth width of each cell
     * @param cellHeight height of each cell
     * @param cellsPerRow number of cells per row
     * @param totalCellCount total number of cells
     */
    public SpriteSheet(String atlasIdentifier, int cellWidth, int cellHeight, int cellsPerRow, int totalCellCount) {
        this.atlasIdentifier = atlasIdentifier;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.cellsPerRow = cellsPerRow;
        this.totalCellCount = totalCellCount;
    }

    /**
     * Calculates the top left coordinate of cell at a specified frame
     * @param frame specified frame to get the top left coords of
     */
    public Vector2 getCellTopLeft(int frame) {
        int rowNumber = (int)Math.floor((double)frame / cellsPerRow);
        int rowColNumber = frame - (rowNumber * cellsPerRow);
        int cellX = rowColNumber * cellWidth;
        int cellY = rowNumber * cellHeight;
        return new Vector2(cellX, cellY);
    }

    /**
     * Get total number of cells in sprite sheet
     */
    public int getCellCount() {
        return totalCellCount;
    }

    /**
     * Get cell size for the sprite sheet
     */
    public Vector2 getCellSize() {
        return new Vector2(cellWidth, cellHeight);
    }

    /**
     * Get atlas identifier
     */
    public String getAtlasIdentifier() {
        return atlasIdentifier;
    }

    /**
     * Helper method to get the texture region for this sprite sheet
     */
    public TextureRegion getTextureRegion() {
        return GameManager.getManagerFromInstance(TextureManager.class).getTextureRegion(getAtlasIdentifier());
    }
}
