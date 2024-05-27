package deco2800.thomas.cutscenes;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.TextureManager;

import java.util.HashMap;
import java.util.Map;

public class AnimationContainer {

    private Stage stage;

    /* different orbs to be get */
    Image container;
    Image tundra;
    Image desert;
    Image volcano;
    Image swamp;

    private Map<String, Image> orbsMap;

    public AnimationContainer(){
        this.stage = GameManager.get().getStage();

        container = new Image(GameManager.get().
                getManager(TextureManager.class).getTexture("filledContainer"));
        tundra = new Image(GameManager.get().
                getManager(TextureManager.class).getTexture("tundra"));
        desert = new Image(GameManager.get().
                getManager(TextureManager.class).getTexture("desert"));
        volcano = new Image(GameManager.get().
                getManager(TextureManager.class).getTexture("volcano"));
        swamp = new Image(GameManager.get().
                getManager(TextureManager.class).getTexture("swamp"));

        container.setPosition(stage.getWidth()/2 - container.getWidth()/2,0);

        orbsMap = new HashMap<>();
        orbsMap.put("tundra", tundra);
        orbsMap.put("desert", desert);
        orbsMap.put("volcano", volcano);
        orbsMap.put("swamp", swamp);

        stage.addActor(container);
        container.setVisible(false);

    }

    public void action(String bossName){
        // display the container
        container.setVisible(true);
        // scale while move to centre
        container.setScale(1.0F,1.0F);
        ScaleToAction scaleToCentre = Actions.scaleTo(2.0F,2.0F,0.7F);
        // scale while move back
        ScaleToAction scaleBack = Actions.scaleTo(1.0F,1.0F, 0.8F);

        // move into centre of the screen
        MoveToAction moveToCentre = Actions.moveTo(stage.getWidth()/2 - container.getWidth()/2 + 30,
            stage.getHeight()/2 - container.getHeight()/2 + 40, 0.7F);
        // move into orb bar after pickup
        MoveToAction moveToOrbBar = Actions.moveTo(stage.getWidth()/2 - container.getWidth()/2, 0, 0.8F);

        // delay before rotate
        DelayAction delayAction = Actions.delay(0.3F);

        // rotate
        container.setOrigin(container.getWidth()/2, container.getHeight()/2);
        container.setRotation(90);
        RotateToAction rotateTundra = Actions.rotateTo(450, 1.2F);
        RotateToAction rotateSwamp = Actions.rotateTo(270, 0.8F);
        RotateToAction rotateDesert = Actions.rotateTo(360, 0.9F);
        RotateToAction rotateVolcano = Actions.rotateTo(90, 0.6F);

        // set container into transparent
        container.getColor().a = 1.0F; // set as visible
        AlphaAction alphaTrans = Actions.alpha(0.0F, 0.8F);
        AlphaAction alphaShow = Actions.alpha(1.0F, 0.1F);
        // rotate and transparent together
        ParallelAction alphaMoveScale = Actions.parallel(moveToOrbBar, alphaTrans, scaleBack);
        ParallelAction scaleMove = Actions.parallel(moveToCentre, scaleToCentre);
        // move -> delay -> alphaRotate
        SequenceAction order = Actions.sequence();
        if (bossName.equals("tundraBossEnemy")){
            order  = Actions.sequence(scaleMove, delayAction, rotateTundra, alphaMoveScale);
        }
        if (bossName.equals("swampBossEnemy")){
            order  = Actions.sequence(scaleMove, delayAction, rotateSwamp, alphaMoveScale);
        }
        if (bossName.equals("desertBossEnemy")){
            order  = Actions.sequence(scaleMove, delayAction, rotateDesert, alphaMoveScale);
        }
        if (bossName.equals("volcanoBossEnemy")){
            order  = Actions.sequence(scaleMove, delayAction, rotateVolcano, alphaMoveScale);
        }
        // after actions, reset to origin state
        RunnableAction runnableAction = Actions.run(new Runnable() {
            @Override
            public void run() {
                container.setVisible(false);
                container.getColor().a = 1.0F;
            }
        });
        AfterAction afterAction = Actions.after(runnableAction);

        container.addAction(order);
        container.addAction(afterAction);
    }

}
