package deco2800.thomas.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import deco2800.thomas.entities.PlayerPeon;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.TextureManager;
import deco2800.thomas.util.SquareVector;

import java.util.HashMap;
import java.util.Map;

public class OrbBar {
//    private PlayerPeon player;
    private Stage stage;

    /* different orbs */
    Image ice;
    Image desert;
    Image volcano;
    Image swamp;

    private static final String orbBar = "orbBar";
    // a map that contains orbs with the corresponding bossName
    private Map<String, TextureRegion> orbsSkinMap;

    public OrbBar(){
        this.stage = GameManager.get().getStage();

        /* set the orbs */
        // set origin image
        ice = new Image(GameManager.get().
                getManager(TextureManager.class).getTexture(orbBar));
        desert = new Image(GameManager.get().
                getManager(TextureManager.class).getTexture(orbBar));
        volcano = new Image(GameManager.get().
                getManager(TextureManager.class).getTexture(orbBar));
        swamp = new Image(GameManager.get().
                getManager(TextureManager.class).getTexture(orbBar));
        // set position
        ice.setPosition(1100, 58);
        desert.setPosition(1000,58);
        volcano.setPosition(900,58);
        swamp.setPosition(800,58);

        // add orb bar into stage
        stage.addActor(ice);
        stage.addActor(desert);
        stage.addActor(volcano);
        stage.addActor(swamp);

        // set orbsSkinMap
        orbsSkinMap = new HashMap<>();
        orbsSkinMap.put("tundraBossEnemy", GameManager.get().
                getManager(TextureManager.class).getTexture("ice_orb"));
        orbsSkinMap.put("swampBossEnemy", GameManager.get().
                getManager(TextureManager.class).getTexture("swamp_orb"));
        orbsSkinMap.put("volcanoBossEnemy", GameManager.get().
                getManager(TextureManager.class).getTexture("volcano_orb"));
        orbsSkinMap.put("desertBossEnemy", GameManager.get().
                getManager(TextureManager.class).getTexture("desert_orb"));

    }

    public void pickUp(String bossName){
        switch (bossName) {
            case "tundraBossEnemy":
                ice = new Image(orbsSkinMap.get(bossName));
                ice.setPosition(1100, 58);
                stage.addActor(ice);
                break;
            case "swampBossEnemy":
                swamp = new Image(orbsSkinMap.get(bossName));
                swamp.setPosition(1000, 58);
                stage.addActor(swamp);
                break;
            case "volcanoBossEnemy":
                volcano = new Image(orbsSkinMap.get(bossName));
                volcano.setPosition(900, 58);
                stage.addActor(volcano);
                break;
            default:
                desert = new Image(orbsSkinMap.get(bossName));
                desert.setPosition(800, 58);
                stage.addActor(desert);
                break;
        }
    }

}
