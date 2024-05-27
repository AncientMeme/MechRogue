package deco2800.thomas.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import deco2800.thomas.entities.DamageIndicator;
import deco2800.thomas.entities.Peon;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.util.Vector2;
import deco2800.thomas.util.WorldUtil;
import java.util.List;

public class IndicatorRenderer {
    Peon entity;
    float width;
    float height;

    /**
     * Buffbar constructor
     * @param entity entity to render the damage indicators onto
     * @param width width of entity
     * @param height height of entity
     */
    public IndicatorRenderer(Peon entity, float width, float height) {
        this.entity = entity;
        this.width = width;
        this.height = height;
    }

    /**
     * This method renders the indicators on screen, it utilises the Orthographic camera
     * as the positioning must be fixed in terms of the players camera - and not the game.
     * This method is implemented in Render3D for each Peon in the game, hence allowing
     * all Peon's to render their indicators.
     * @param camera the OrthographicCamera instance
     */
    public void render(OrthographicCamera camera) {
        List<DamageIndicator> activeIndicators = entity.getDamageIndicators();
        for (DamageIndicator indicator : activeIndicators) {
            // Get duration to use to modify the indicators positioning
            float duration = indicator.getDuration();
            // Get coordinates of entity to know where to place label
            float[] coordinates = WorldUtil.colRowToWorldCords(entity.getCol(), entity.getRow());
            Vector2 offset = indicator.getOffset();
            // Modifies position according to duration of indicator and width of entity
            float xCord = coordinates[0] + (width/1.35f) * offset.getX();
            float yCord = coordinates[1] + (height/1.35f) * offset.getY() + (30*duration);
            // Duration used to position label (high or low)
            // Adding duration to entity coordinates to initialise position
            Vector3 screenCoordinates = new Vector3(xCord, yCord, 0.0f);
            // Set camera to project the label on these coordinates
            camera.project(screenCoordinates);
            indicator.getLabel().setPosition(screenCoordinates.x, screenCoordinates.y);
            // Present to stage
            GameManager.get().getStage().addActor(indicator.getLabel());
        }
    }
}