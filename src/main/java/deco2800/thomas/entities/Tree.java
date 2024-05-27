package deco2800.thomas.entities;

import deco2800.thomas.Tickable;
import deco2800.thomas.worlds.AbstractWorld;
import deco2800.thomas.worlds.Tile;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tree extends StaticEntity implements Tickable {
	private final Logger LOGGER = LoggerFactory.getLogger(Tree.class);

	AbstractWorld world;
	

	public Tree(float col, float row, int renderOrder, List<Part> parts) {
		super(col, row, renderOrder, parts);
		LOGGER.info("Making a tree at {}, {}", col, row);
		this.setTexture("tree");
	}

	public Tree(Tile t, boolean obstructed) {
		super(t, RenderConstants.TREE_RENDER, "tree_1", obstructed);
	}

	public Tree(Tile t, boolean obstructed, String texture) {
		super(t, RenderConstants.TREE_RENDER, texture, obstructed);
	}


	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof Tree)) {
			return false;
		}
		Tree otherTree = (Tree) other;
		return this.getCol() == otherTree.getCol() && this.getRow() == otherTree.getRow() &&
				this.getHeight() == otherTree.getHeight();
	}


	/**
	 * Gets the hashCode of the tree.
	 *
	 * @return the hashCode of the tree
	 */
	@Override
	public int hashCode() {
		final float prime = 31;
		float result = 1;
		result = (result + super.getCol()) * prime;
		result = (result + super.getRow()) * prime;
		result = (result + super.getHeight()) * prime;
		return (int) result;
	}


	/**
	 * Animates the tree on every game tick.

	 * @param tick current game tick
	 */
	@Override
	public void onTick(long tick) {
		//unused
	}
}