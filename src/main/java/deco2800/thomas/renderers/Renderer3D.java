package deco2800.thomas.renderers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.utils.Array;
import deco2800.thomas.entities.*;
import deco2800.thomas.managers.*;
import deco2800.thomas.ui.BuffBar;
import deco2800.thomas.ui.HealthBar;
import deco2800.thomas.ui.IndicatorRenderer;
import deco2800.thomas.util.*;
import deco2800.thomas.worlds.AbstractWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.OrthographicCamera;

import deco2800.thomas.tasks.AbstractTask;
import deco2800.thomas.tasks.MovementTask;
import deco2800.thomas.worlds.Tile;

/**
 * A ~simple~ complex square renderer for DECO2800 games
 * 
 * @author Tim Hadwen & Lachlan Healey & Jenna Macdonald
 */
public class Renderer3D implements Renderer {

	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(Renderer3D.class);

	BitmapFont font;
	
	//mouse cursor
	private static final String TEXTURE_SELECTION = "selection";
	private static final String TEXTURE_DESTINATION = "selection";
	private static final String TEXTURE_PATH = "path";

	private static final String TEXTURE_COLLISION_BOX = "collision_debug_box";
	private static final String TEXTURE_COLLISION_POINT = "collision_debug_point";

	private int tilesSkipped = 0;

	private TextureManager textureManager = GameManager.getManagerFromInstance(TextureManager.class);
    private ParticleManager particleManager = GameManager.getManagerFromInstance(ParticleManager.class);

	private float cloudOffset = 0.0f;

	/**
	 * Renders onto a batch, given a renderables with entities It is expected
	 * that AbstractWorld contains some entities and a Map to read tiles from
	 * 
	 * @param batch
	 *            Batch to render onto
	 */
	@Override
	public void render(SpriteBatch batch, OrthographicCamera camera) {
		cloudOffset += 1.0f;

		if (font == null) {
			font = new BitmapFont();
			font.getData().setScale(1f);
		}

		// Render tiles onto the map
		List<Tile> tileMap = GameManager.get().getWorld().getTileMap();
		List<Tile> tilesToBeSkipped = new ArrayList<>();

		batch.begin();
		// Render elements section by section
		//	tiles will render the static entity attaced to each tile after the tile is rendered

		tilesSkipped = 0;
		for (Tile t: tileMap) {
			// Render each tile
			renderTile(batch, camera, tileMap, tilesToBeSkipped, t);

			// Render each undiscovered area
		}

		renderAbstractEntities(batch, camera);

		renderWorldParticleEffects(batch, camera, GameManager.get().getWorld());

		renderMouse(batch);
		renderMouse(batch);

		debugRender(batch, camera);

		if (GameManager.get().getCollisions()) {
			renderCollisionMap(batch);
			renderCollisionProbes(batch, camera);
		}

		batch.end();
	}

	/**
	 * Sets the tint for rendering the next object with cloud based shadows
	 * @param batch batch to set tint of
	 * @param x x position of item being rendered
	 * @param y y position of item being rendered
	 * @param overrideColour override colour. If set to white, then it is ignored
	 */
	private void setTintBasedOnClouds(SpriteBatch batch, float x, float y, Color overrideColour, boolean isWater) {
		if (overrideColour.equals(Color.WHITE) && GameManager.get().getShowAdvRender()) {
			float cloudVal = GameManager.get().getManager(NoiseManager.class).simplexOctaves2DRange(
					(x + cloudOffset) / 1750.0f,
					y / 750.0f,
					3, 2.0f, 0.5f, 0.75f, 1.0f);

			if (isWater) {
				float waterShimmer = GameManager.get().getManager(NoiseManager.class).simplexOctaves2DRange(
						(x - cloudOffset / 4) / 200.0f, (y + cloudOffset / 4) / 150.0f,
						3, 2.0f, 0.5f, 0.75f, 1.0f);

				float noiseVal = 1 - ((1 - waterShimmer) + (1 - cloudVal));
				batch.setColor(noiseVal, noiseVal, noiseVal, 1.0f);
			} else {
				float noiseVal = cloudVal;
				batch.setColor(noiseVal, noiseVal, noiseVal, 1.0f);
			}
		} else {
			batch.setColor(overrideColour);
		}
	}

	public void renderShadow(SpriteBatch batch, TextureRegion tr, float x, float y, float width, float height) {
		if (GameManager.get().getShowAdvRender()) {
			Affine2 affineTranslation = new Affine2();
			affineTranslation.setToTranslation(x, y);
			Affine2 affineShearTranslation = affineTranslation.shear(0.5f, 0.0f);
			batch.setColor(0.0f, 0.0f, 0.0f, 0.3f);
			batch.draw(tr, width, height, affineTranslation);
			batch.setColor(Color.WHITE);
		}
	}

	/**
	 * Render a single tile.
	 * @param batch the sprite batch.
	 * @param camera the camera.
	 * @param tileMap the tile map.
	 * @param tilesToBeSkipped a list of tiles to skip.
	 * @param tile the tile to render.
	 */
	private void renderTile(SpriteBatch batch, OrthographicCamera camera, List<Tile> tileMap, List<Tile> tilesToBeSkipped, Tile tile) {

        if (tilesToBeSkipped.contains(tile)) {
            return;
        }
        float[] tileWorldCord = WorldUtil.colRowToWorldCords(tile.getCol(), tile.getRow());

        if (WorldUtil.areCoordinatesOffScreen(tileWorldCord[0], tileWorldCord[1], camera)) {
            tilesSkipped++;
            GameManager.get().setTilesRendered(tileMap.size() - tilesSkipped);
            GameManager.get().setTilesCount(tileMap.size());
            return;
        }

        TextureRegion tex = textureManager.getTexture(tile.getTexture());
		Vector2 texScaling = textureManager.getTextureScaling(tile.getTexture());

        setTintBasedOnClouds(batch, tileWorldCord[0], tileWorldCord[1], Color.WHITE, tile.getTexture().equals("water"));

        batch.draw(tex, tileWorldCord[0], tileWorldCord[1], tex.getRegionWidth() * WorldUtil.SCALE_X * texScaling.getX(),
				tex.getRegionHeight() * WorldUtil.SCALE_Y * texScaling.getY());
		batch.setColor(Color.WHITE);
		GameManager.get().setTilesRendered(tileMap.size() - tilesSkipped);
		GameManager.get().setTilesCount(tileMap.size());
		

	}


	

	/**
	 * Render the tile under the mouse.
	 * @param batch the sprite batch.
	 */
    private void renderMouse(SpriteBatch batch) {
        Vector2 mousePosition = GameManager.getManagerFromInstance(InputManager.class).getMousePosition();

        TextureRegion tex = textureManager.getTexture(TEXTURE_SELECTION);

        // get mouse position
        float[] worldCoord = WorldUtil.screenToWorldCoordinates(mousePosition.getX(), mousePosition.getY());

        // snap to the tile under the mouse by converting mouse position to colrow then back to mouse coordinates
        float[] colrow = WorldUtil.worldCoordinatesToColRow(worldCoord[0], worldCoord[1]);

        float[] snapWorldCoord = WorldUtil.colRowToWorldCords(colrow[0], colrow[1] + 1);

        //Needs to getTile with a SquareVector for networking to work atm
        Tile tile = GameManager.get().getWorld().getTile(new SquareVector(colrow[0], colrow[1]));

        if (tile != null) {
            batch.draw(tex, (int) snapWorldCoord[0], (int) snapWorldCoord[1]  - (tex.getRegionHeight() * WorldUtil.SCALE_Y),
                    tex.getRegionWidth() * WorldUtil.SCALE_X,
                    tex.getRegionHeight() * WorldUtil.SCALE_Y);
        }

	}


	/**
	 * Highlight the tiles occupied by a solid object in the collision map.
	 * @param batch the sprite batch.
	 */
	private void renderCollisionMap(SpriteBatch batch) {
		TextureRegion tex = textureManager.getTexture(TEXTURE_COLLISION_BOX);

		List<SquareVector> positions = GameManager.get().getWorld().getWorldCollisionMap()
				.getRegisteredCollisionPositions();

		Vector2 collisionMapSize = GameManager.get().getWorld().getWorldCollisionMap().getSize();

		for (SquareVector position : positions) {
			float[] snapWorldCoord = WorldUtil.colRowToWorldCords(
					position.getCol() - (int)(collisionMapSize.getX() / 2),
					position.getRow() + 1 - (int)(collisionMapSize.getY() / 2));

			batch.draw(tex, (int) snapWorldCoord[0], (int) snapWorldCoord[1]  - (tex.getRegionHeight() * WorldUtil.SCALE_Y),
					tex.getRegionWidth() * WorldUtil.SCALE_X,
					tex.getRegionHeight() * WorldUtil.SCALE_Y);
		}
	}

	/**
	 * Draws points where the collision map is being probed from each frame.
	 * @param batch the sprite batch.
	 * @param camera scene camera
	 */
	private void renderCollisionProbes(SpriteBatch batch, OrthographicCamera camera) {
		TextureRegion tex = textureManager.getTexture(TEXTURE_COLLISION_POINT);

		List<SquareVector> positions = GameManager.get().getWorld().getCollisonProbeLog();

		for (SquareVector position : positions) {
			float[] tileWorldCord = WorldUtil.colRowToWorldCords(position.getCol(), position.getRow());

			// Don't render the probe if it's offscreen
			if (WorldUtil.areCoordinatesOffScreen(tileWorldCord[0], tileWorldCord[1], camera)) {
				continue;
			}

			float width = tex.getRegionHeight() * WorldUtil.SCALE_X;
			float height = tex.getRegionWidth() * WorldUtil.SCALE_Y;

			// Draw centered
			batch.draw(tex, tileWorldCord[0] - width / 2, tileWorldCord[1] - height / 2, width, height);
		}
	}

	private void renderWorldParticleEffects(SpriteBatch batch, OrthographicCamera camera, AbstractWorld world) {
		List<Integer> particleIDs = world.getParticleIDs();
		for (int particleID : particleIDs) {
			ParticleEffect pe = particleManager.getParticleEffect(particleID);
			pe.update(Gdx.graphics.getDeltaTime());
			pe.draw(batch);
		}
	}
	
    /**
	 * Render all the entities on in view, including movement tiles, and excluding undiscovered area.
	 * @param batch the sprite batch.
	 * @param camera the camera.
	 */
	private void renderAbstractEntities(SpriteBatch batch, OrthographicCamera camera) {
		List<AbstractEntity> entities = GameManager.get().getWorld().getSortedEntities();
		int entitiesSkipped = 0;
		logger.debug("NUMBER OF ENTITIES IN ENTITY RENDER LIST: {}", entities.size());

		float[] mouseWorldCoord = WorldUtil.screenToWorldCoordinates(Gdx.input.getX(), Gdx.input.getY());
		AgentEntity hoveredAgentEntity = null;
		for (AbstractEntity entity : entities) {
			float[] entityWorldCoord = WorldUtil.colRowToWorldCords(entity.getCol(), entity.getRow());
			// If it's offscreen
			if (WorldUtil.areCoordinatesOffScreen(entityWorldCoord[0], entityWorldCoord[1], camera)) {
				entitiesSkipped++;
				continue;
			}

			if (entity instanceof Peon) {
				/* Draw Peon */
				if (entity instanceof PlayerPeon) {
					// Place movement tiles
					if (GameManager.get().getShowPath()) {
						renderPeonMovementTiles(batch, camera, entity, entityWorldCoord);
					}
				}

				if (entity.isAnimated()) {
					SpriteSheetAnimation anim = entity.getAnimation();
					SpriteSheet spriteSheet = textureManager.getSpritesheet(anim.getSpritesheetIdentifier());

					renderAbstractEntitySpritesheet(batch, entity,
							spriteSheet.getCellTopLeft(anim.getAnimationFrame()), spriteSheet.getCellSize(),
							entityWorldCoord, spriteSheet.getTextureRegion(), camera);

					if (entity instanceof AgentEntity) {
						float screenWidth = spriteSheet.getCellSize().getX() * entity.getColRenderLength() * WorldUtil.SCALE_X;
						float screenHeight = spriteSheet.getCellSize().getY() * entity.getRowRenderLength() * WorldUtil.SCALE_Y;
						if (mouseWorldCoord[0] >= entityWorldCoord[0] &&
								mouseWorldCoord[1] >= entityWorldCoord[1] &&
								mouseWorldCoord[0] <= entityWorldCoord[0] + screenWidth &&
								mouseWorldCoord[1] <= entityWorldCoord[1] + screenHeight) {
							hoveredAgentEntity = (AgentEntity) entity;
						}
					}
				} else {
					TextureRegion tex = textureManager.getTexture(entity.getTexture());
					renderAbstractEntity(batch, entity, entityWorldCoord, tex, camera);
					if (entity instanceof AgentEntity) {
						float screenWidth = tex.getRegionWidth() * entity.getColRenderLength() * WorldUtil.SCALE_X;
						float screenHeight = tex.getRegionHeight() * entity.getRowRenderLength() * WorldUtil.SCALE_Y;
						if (mouseWorldCoord[0] >= entityWorldCoord[0] &&
								mouseWorldCoord[1] >= entityWorldCoord[1] &&
								mouseWorldCoord[0] <= entityWorldCoord[0] + screenWidth &&
								mouseWorldCoord[1] <= entityWorldCoord[1] + screenHeight) {
							hoveredAgentEntity = (AgentEntity) entity;
						}
					}
				}
			}

			if (entity instanceof StaticEntity) {
				TextureRegion tex = textureManager.getTexture(entity.getTexture());
				StaticEntity staticEntity = ((StaticEntity) entity);
				Set<SquareVector> childrenPosns = staticEntity.getChildrenPositions();

				for(SquareVector childpos: childrenPosns) {

					TextureRegion childTex = staticEntity.getTextureRegion(childpos);
					Vector2 childTexScaling = staticEntity.getTextureScaling(childpos);

					float[] childWorldCoord = WorldUtil.colRowToWorldCords(childpos.getCol(), childpos.getRow());

					// time for some funky math: we want to render the entity at the centre of the tile. 
					// this way centres of textures bigger than tile textures render exactly on the top of the tile centre
					// think of a massive tree with the tree trunk at the centre of the tile 
					// and it's branches and leaves over surrounding tiles 

					// We get the tile height and width :
					int w = GameManager.get().getWorld().getTile(childpos).getTextureRegion().getRegionWidth();
					int h = GameManager.get().getWorld().getTile(childpos).getTextureRegion().getRegionHeight();

					int drawX = (int) (childWorldCoord[0] + (w - childTex.getRegionWidth()) /2 * WorldUtil.SCALE_X);
					int drawY = (int) (childWorldCoord[1] + (h - childTex.getRegionHeight())/2 * WorldUtil.SCALE_Y);

					float renderWidth = childTex.getRegionWidth() * WorldUtil.SCALE_X * childTexScaling.getX();
					float renderHeight = childTex.getRegionHeight() * WorldUtil.SCALE_Y * childTexScaling.getY();

					renderShadow(batch, childTex, childWorldCoord[0], childWorldCoord[1], renderWidth, renderHeight);

					setTintBasedOnClouds(batch, childWorldCoord[0], childWorldCoord[1], Color.WHITE, false);

					batch.draw(childTex, childWorldCoord[0], childWorldCoord[1], renderWidth, renderHeight);

					batch.setColor(Color.WHITE);
				}
			}

			if (entity instanceof Projectile) {
				TextureRegion tex = textureManager.getTexture(entity.getTexture());
				// Render projectile Centered and rotated towards direction
				Projectile projectile = (Projectile)entity;
				renderAbstractEntityRotatedCenter(batch, entity, entityWorldCoord,
						tex, (float)Math.toDegrees(projectile.getDirection()));
			}
		}

		GameManager.get().setHoveredAgentEntity(hoveredAgentEntity);

		GameManager.get().setEntitiesRendered(entities.size() - entitiesSkipped);
		GameManager.get().setEntitiesCount(entities.size());
	}
	
	
	private void renderAbstractEntity(SpriteBatch batch, AbstractEntity entity, float[] entityWorldCord, TextureRegion tex, OrthographicCamera camera) {
        float x = entityWorldCord[0];
		float y = entityWorldCord[1];

        float width = tex.getRegionWidth() * entity.getColRenderLength() * WorldUtil.SCALE_X;
        float height = tex.getRegionHeight() * entity.getRowRenderLength() * WorldUtil.SCALE_Y;

		renderShadow(batch, tex, x, y, width, height);

		setTintBasedOnClouds(batch, entityWorldCord[0], entityWorldCord[1], entity.getColor(), false);
        batch.draw(tex, x, y, width, height);
		batch.setColor(Color.WHITE);

		// Render any attached particles (Rendered after object)
		int particleID = entity.getAttachedParticleID();
		if (particleID != 0) {
			ParticleEffect pe = particleManager.getParticleEffect(particleID);
			if (pe != null) {
				renderParticleEffect(batch, pe, x, y);
			}
		}

        //Render Health Bar
		HealthBar healthBar = new HealthBar(batch, entity, width, height, x, y);
		healthBar.render();


		// Render buff bar
		if (entity instanceof Peon) {
			BuffBar buffBar = new BuffBar(batch, (Peon)entity, width, height, x, y);
			buffBar.render();
		}

		if (entity instanceof Peon) {
			IndicatorRenderer indicatorRenderer = new IndicatorRenderer((Peon) entity, width, height);
			indicatorRenderer.render(camera);
		}
    }

	private void renderAbstractEntitySpritesheet(SpriteBatch batch, AbstractEntity entity, Vector2 spritesheetTopLeft, Vector2 cellSize, float[] entityWorldCord, TextureRegion tex, OrthographicCamera camera) {
		float x = entityWorldCord[0];
		float y = entityWorldCord[1];

		float width = cellSize.getX() * entity.getColRenderLength() * WorldUtil.SCALE_X;
		float height = cellSize.getY() * entity.getRowRenderLength() * WorldUtil.SCALE_Y;

		TextureRegion texCopy = new TextureRegion(tex);
		texCopy.setRegionX(texCopy.getRegionX() + (int)spritesheetTopLeft.getX());
		texCopy.setRegionY(texCopy.getRegionY() + (int)spritesheetTopLeft.getY());
		texCopy.setRegionWidth((int)cellSize.getX());
		texCopy.setRegionHeight((int)cellSize.getY());

		renderShadow(batch, texCopy, x, y, width, height);

		setTintBasedOnClouds(batch, entityWorldCord[0], entityWorldCord[1], entity.getColor(), false);
		batch.draw(texCopy, x, y, width, height);
		batch.setColor(Color.WHITE);

		// Render any attached particles (Rendered after object)
		int particleID = entity.getAttachedParticleID();
		if (particleID != 0) {
			ParticleEffect pe = particleManager.getParticleEffect(particleID);
			if (pe != null) {
				renderParticleEffect(batch, pe, x, y);
			}
		}

		//Render Health Bar
		HealthBar bar = new HealthBar(batch, entity, width, height, x, y);
		bar.render();

		// Render buff bar
		if (entity instanceof Peon) {
			BuffBar buffBar = new BuffBar(batch, (Peon)entity, width, height, x, y);
			buffBar.render();
		}

		if (entity instanceof Peon) {
			IndicatorRenderer indicatorRenderer = new IndicatorRenderer((Peon) entity, width, height);
			indicatorRenderer.render(camera);
		}
	}

	private void renderAbstractEntityRotatedCenter(SpriteBatch batch, AbstractEntity entity, float[] entityWorldCord, TextureRegion tex, float rotation) {
		float width = tex.getRegionWidth() * entity.getColRenderLength() * WorldUtil.SCALE_X;
		float height = tex.getRegionHeight() * entity.getRowRenderLength() * WorldUtil.SCALE_Y;
		float x = entityWorldCord[0] - width / 2;
		float y = entityWorldCord[1] - height / 2;

		// Render any attached particles (Rendered before object)
		int particleID = entity.getAttachedParticleID();
		if (particleID != 0) {
			ParticleEffect pe = particleManager.getParticleEffect(particleID);
			if (pe != null) {
				renderParticleEffect(batch, pe, entityWorldCord[0], entityWorldCord[1]);
			}
		}

		setTintBasedOnClouds(batch, entityWorldCord[0], entityWorldCord[1], entity.getColor(), false);
		batch.draw(tex, x, y, width / 2, height / 2, width, height,
				1f, 1f, -rotation);
		batch.setColor(Color.WHITE);
	}
	
	private void renderPeonMovementTiles(SpriteBatch batch, OrthographicCamera camera, AbstractEntity entity, float[] entityWorldCord) {
		Peon actor = (Peon) entity;
		AbstractTask task = actor.getTask();
		if (task instanceof MovementTask) {
			if (((MovementTask)task).getPath() == null) { //related to issue #8
				return;
			}
			List<IntegerPoint> path = ((MovementTask)task).getPath();
			for (IntegerPoint pos : path) {
				// Place transparent tiles for the path, but place a non-transparent tile for the destination
				TextureRegion tex = path.get(path.size() - 1).equals(pos) ?
						textureManager.getTexture(TEXTURE_DESTINATION) : textureManager.getTexture(TEXTURE_PATH);
				float[] tileWorldCord = WorldUtil.colRowToWorldCords((float)pos.getX(), (float)pos.getY());
				if (WorldUtil.areCoordinatesOffScreen(tileWorldCord[0], tileWorldCord[1], camera)) {
					tilesSkipped++;
					continue;
				}
				batch.draw(tex, tileWorldCord[0],
						tileWorldCord[1]// + ((tile.getElevation() + 1) * elevationZeroThiccness * WorldUtil.SCALE_Y)
						, tex.getRegionWidth() * WorldUtil.SCALE_X,
						tex.getRegionHeight() * WorldUtil.SCALE_Y);

			}
//			if (!path.isEmpty()) {
//				// draw Peon
//				Texture tex = textureManager.getTexture(entity.getTexture());
//				batch.draw(tex, entityWorldCord[0], entityWorldCord[1] + entity.getHeight(),// + path.get(0).getElevation()) * elevationZeroThiccness * WorldUtil.SCALE_Y,
//						tex.getWidth() * entity.getColRenderLength() * WorldUtil.SCALE_X,
//						tex.getHeight() * entity.getRowRenderLength() * WorldUtil.SCALE_Y);
//			}
		}
	}
	
	private void debugRender(SpriteBatch batch, OrthographicCamera camera) {

		if (GameManager.get().getShowCoords()) {
			List<Tile> tileMap = GameManager.get().getWorld().getTileMap();
			for (Tile tile : tileMap) {
				float[] tileWorldCord = WorldUtil.colRowToWorldCords(tile.getCol(), tile.getRow());

				if (!WorldUtil.areCoordinatesOffScreen(tileWorldCord[0], tileWorldCord[1], camera)) {
					font.draw(batch, 
							tile.toString(),
							//String.format("%.0f, %.0f, %d",tileWorldCord[0], tileWorldCord[1], tileMap.indexOf(tile)),
							tileWorldCord[0] + WorldUtil.TILE_WIDTH / 4.5f,
							tileWorldCord[1]);// + ((tile.getElevation() + 1) * elevationZeroThiccness * WorldUtil.SCALE_Y)
							//+ WorldUtil.TILE_HEIGHT-10);			
				}

			}
		}

		if (GameManager.get().getShowCoordsEntity()) {
			List<AbstractEntity> entities = GameManager.get().getWorld().getEntities();
			for (AbstractEntity entity : entities) {
				float[] tileWorldCord = WorldUtil.colRowToWorldCords(entity.getCol(), entity.getRow());

				if (!WorldUtil.areCoordinatesOffScreen(tileWorldCord[0], tileWorldCord[1], camera)) {
					font.draw(batch, String.format("%.0f, %.0f", entity.getCol(), entity.getRow()),
							tileWorldCord[0], tileWorldCord[1]);
				}
			}
		}
	}

	private void renderParticleEffect(SpriteBatch batch, ParticleEffect particleEffect, float x, float y) {
		Array<ParticleEmitter> emitters = particleEffect.getEmitters();
		for (ParticleEmitter emitter : emitters) {
			emitter.setPosition(x, y);
		}
		particleEffect.update(Gdx.graphics.getDeltaTime());
		particleEffect.draw(batch);
	}
}
