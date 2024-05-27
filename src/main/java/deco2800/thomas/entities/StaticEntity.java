package deco2800.thomas.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import deco2800.thomas.managers.NetworkManager;
import deco2800.thomas.util.SquareVector;
import deco2800.thomas.util.Vector2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.TextureManager;
import deco2800.thomas.util.WorldUtil;
import deco2800.thomas.worlds.Tile;

import com.google.gson.annotations.Expose;

public class StaticEntity extends AbstractEntity {
	private final transient Logger log = LoggerFactory.getLogger(StaticEntity.class);

	//private transient HashMap<String, Object> dataToSave = new HashMap<>();

	private static final String ENTITY_ID_STRING = "staticEntityID";

	//pos, texture MUST BE A VALID POSITION ELSE IT WILL NULL!
	@Expose
	public Map<SquareVector, String> children;

	public StaticEntity() {
		super();
	}

	public StaticEntity(Tile tile, int renderOrder , String texture , boolean obstructed) {
		super(tile.getCol(), tile.getRow(), renderOrder);
		this.setObjectName(ENTITY_ID_STRING);
		
		children = new HashMap<>();
		children.put(tile.getCoordinates(), texture);
		if(!WorldUtil.validColRow(tile.getCoordinates())) {
			 log.debug(tile.getCoordinates() + " is Invalid:");
			 return;
		}
		tile.setParent(this);
		tile.setObstructed(obstructed);	
	}

	public StaticEntity(float col, float row, int renderOrder, List<Part> entityParts) {
		super(col, row, renderOrder);
		this.setObjectName(ENTITY_ID_STRING);

		Tile centre = GameManager.get().getWorld().getTile(this.getPosition());
		if (centre == null) {
			log.debug("Centre is null");
			return;
		}
		
		if(!WorldUtil.validColRow(centre.getCoordinates())) {
			 log.debug(centre.getCoordinates() + " Is Invalid:");
			 return;
		}

		children = new HashMap<>();

		for (Part part : entityParts) {
			Tile tile = textureToTile(part.getPosition(), this.getPosition());
			if (tile != null) {
				children.put(tile.getCoordinates(), part.textureString);
				//Tile child = GameManager.get().getWorld().getTile(part.getPostion());
				tile.setObstructed(part.isObstructed());
			}	
		}
	}
	

	public void setup() {
		if (children != null) {
			for (SquareVector childPosition : children.keySet()) {
				Tile child = GameManager.get().getWorld().getTile(childPosition);
				if (child != null) {
					child.setParent(this);
				}
			}
		}
	}
	

	@Override
	public void onTick(long i) {
		// Do the AI for the entity in here.
	}

	// This ensures the static entity is cleared from the collision map upon deletion
	@Override
	public void dispose() {
		GameManager.get().getManager(NetworkManager.class).deleteEntity(this);
		GameManager.get().getWorld().clearCollisionEntry(
				(int)Math.floor(this.getCol()),
				(int)Math.floor(this.getRow())
		);
		GameManager.get().getWorld().deleteEntity(this);
	}

	private Tile textureToTile(SquareVector offset, SquareVector centre) {
		if(!WorldUtil.validColRow(offset)) {
			 log.debug(offset + " Is Invaid:"); 
			 return null;
		}
		SquareVector targetTile = centre.add(offset);
		return GameManager.get().getWorld().getTile(targetTile);
	}
	
	public Set<SquareVector> getChildrenPositions() {
		return children.keySet();
	}

	public String getTexture(SquareVector childPosition) {
		return children.get(childPosition);
	}

	public TextureRegion getTextureRegion(SquareVector childPosition) {
		return GameManager.get().getManager(TextureManager.class).getTexture(getTexture(childPosition));
	}

	public Vector2 getTextureScaling(SquareVector childPosition) {
		return GameManager.get().getManager(TextureManager.class).getTextureScaling(getTexture(childPosition));
	}

	public void setChildren(Map<SquareVector, String> children) {
		this.children = children;
	}
}
