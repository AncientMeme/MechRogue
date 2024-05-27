package deco2800.thomas.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import deco2800.thomas.entities.buffs.SpeedBuff;
import deco2800.thomas.entities.enemies.EnemyPeon;
import deco2800.thomas.entities.friendlyNPCs.FriendlyPeon;
import deco2800.thomas.items.*;
import deco2800.thomas.managers.*;
import deco2800.thomas.items.DroppedItem;
import deco2800.thomas.items.Sword;
import deco2800.thomas.managers.DialogueManager;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.InputManager;
import deco2800.thomas.managers.SpriteSheetAnimation;
import deco2800.thomas.observers.*;

import deco2800.thomas.renderers.InventoryRenderer;
import deco2800.thomas.renderers.PauseMenuRenderer;

import deco2800.thomas.tasks.ActionMechTask;
import deco2800.thomas.tasks.PauseGameTask;
import deco2800.thomas.tasks.PlayerPickupItemTask;
import deco2800.thomas.util.SquareVector;
import deco2800.thomas.util.WorldUtil;
import java.io.IOException;
import deco2800.thomas.worlds.TestWorld;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerPeon extends Peon implements TouchDownObserver,
        KeyDownObserver, KeyUpObserver, TileEventObservable {
    // PlayerPeon Logger
    static final Logger LOGGER = Logger.getLogger(PlayerPeon.class.getPackage().getName());

    /* Keycodes for the wasd keys mappings */
    private static final int UP = 51;
    private static final int DOWN = 47;
    private static final int LEFT = 29;
    private static final int RIGHT = 32;

    /* Direction Constant for player direction */
    private static final int FACING_UP = 0;
    private static final int FACING_DOWN = 1;
    private static final int FACING_LEFT = 2;
    private static final int FACING_RIGHT = 3;

    /* Keycode for k button press */
    private static final int K_KEY = 39;

    //Pickup item is currently mapped to the 'E' key
    private static final int PICKUP = 33;

    //Pickup item is currently mapped to the 'F' key
    private static final int WITHMECH = 34;

    //Save Game is currently mapped to the 'P' Key
    private static final int PAUSEGAME = 44;

    //Booleans(?) to determine whether a wasd key has been pressed
    private int wPressed = 0;
    private int aPressed = 0;
    private int sPressed = 0;
    private int dPressed = 0;
    private int pickupPressed = 0;
    private int pauseGamePressed = 0;
    private int mechPressed = 0;
    private int n0Pressed = 0;
    private int n1Pressed = 0;
    private int n2Pressed = 0;
    private int n3Pressed = 0;
    private int n4Pressed = 0;
    private int n5Pressed = 0;
    private int n6Pressed = 0;
    private int n7Pressed = 0;
    private int n8Pressed = 0;
    private int n9Pressed = 0;
    private int itemIdx = 0;
    private  boolean equipCompleted=true;

    /* Time between clicks in millisecond for click to be registered as double click*/
    private static final int DOUBLE_CLICK_INTERVAL = 250;

    /* Time of last click in milliseconds */
    private long lastTouchDown = 0;

    private CopyOnWriteArrayList<TileEventObserver> tileEventObserversList = new CopyOnWriteArrayList<TileEventObserver>();

    private float weaponRange = 2;
    private int weaponDamage = 2;
    private int currentDirection;
    private UnrenderedItem equippedWeapon = null;//currently equipped weapon

    //Player Friendly NPCInteraction Range
    private float talkRange = 4;

    //Player peon level && exp && stat points
    private int level;
    private int exp;
    private int statPoints;

    //Amount of damage the player will inflict when attacking
    private int strength;

    //Modifier to how much damage a player will take when attacked
    private int defense;

    /* Current State of the player */
    private PlayerState playerState;
    private long lastAttack;

    /* The direction the player is currently facing*/
    private int playerDirection;

    // Knight Animations
    private SpriteSheetAnimation[] knightIdleAnimations;
    private SpriteSheetAnimation[] knightWalkAnimations;
    private SpriteSheetAnimation[] knightAttackAnimations;

    // Mech
    private MechState mechState;
    private Mech mech;

    // Mech Animation
    private SpriteSheetAnimation[] mechIdleAnimations;
    private SpriteSheetAnimation[] mechWalkAnimations;
    private SpriteSheetAnimation[] mechAttackAnimations;
    private SpriteSheetAnimation[] mechRangeAnimations;

    /* Keep track of the velocity of the player peon */

    public PlayerPeon(float row, float col, float speed, int health) {
        super(row, col, speed, health);
        this.setObjectName("playerPeon");
        //Adds the player's starting sword
        this.addToPlayerInventory(new Sword("Iron Sword","001",1, 1, 1, 1, "weapon_001"));

        // Initialize level and exp
        this.level = 1;
        this.exp = 0;

        //Initialize player stats
        strength = 5;
        defense = 0;
        statPoints = 0;
        this.speed = speed;

        //Mech
        mechState = MechState.MECH_OFF;
        mech = new Mech(0, 20, 0.04f, 30);

        // Add observers to player
        GameManager.getManagerFromInstance(InputManager.class).addTouchDownListener(this);
        GameManager.getManagerFromInstance(InputManager.class).addKeyDownListener(this);
        GameManager.getManagerFromInstance(InputManager.class).addKeyUpListener(this);

        // Initalize player states and direction for animations
        playerDirection = FACING_DOWN;
        playerState = PlayerState.IDLE;
        lastAttack = 0;

        // Initailize player knight animations
        knightIdleAnimations = new SpriteSheetAnimation[4];
        knightWalkAnimations = new SpriteSheetAnimation[4];
        knightAttackAnimations = new SpriteSheetAnimation[4];

        // Initailize player mech animations
        mechIdleAnimations = new SpriteSheetAnimation[4];
        mechWalkAnimations = new SpriteSheetAnimation[4];
        mechAttackAnimations = new SpriteSheetAnimation[4];
        mechRangeAnimations = new SpriteSheetAnimation[4];

        // Populate the list of animations
        registerAnimation();
    }

    /**
     * Helper function to initialize player animations
     * */
    public void registerAnimation() {
        // Get the types of animations and directions
        String[] directions = new String[]{"up", "down", "left", "right"};
        String[] types = new String[]{"player_idle_", "player_walk_",
                "player_attack_", "mech_idle_", "mech_walk_", "mech_attack_",
                "mech_ranged_"};
        SpriteSheetAnimation[][] animations = new SpriteSheetAnimation[][]{
                knightIdleAnimations, knightWalkAnimations,
                knightAttackAnimations, mechIdleAnimations,
                mechWalkAnimations, mechAttackAnimations, mechRangeAnimations};


        // Loop through the types and directions
        for (int i = 0; i < types.length; ++i) {
            for (int j = 0; j < directions.length; ++j) {
                // Get the correct animation
                String animationName = types[i].concat(directions[j]);
                SpriteSheetAnimation anim = new SpriteSheetAnimation(animationName, 0.1f);

                // Put the animation into the correct array
                animations[i][j] = anim;
            }
        }
    }

    /**
     * Death handler for player
     */
    @Override
    public void handleDeath() {
        // Delete entity from the world
        this.dispose();
        // Delete outgoing damage indicators
        getDamageIndicators().forEach(indicator -> indicator.getLabel().setVisible(false));
    }

	@Override
    public void onTick(long i) {
        // Check for player movement
        if (PauseMenuRenderer.isPaused()) {
            return;
        }
        playerMovement();
        playerPickupItem();
        pauseGameAction();
        actionMech();

        useItem();

        // Check the player's state then animate the player
        setPlayerState();
        animatePlayer();

        // Notify TileObservers
        notifyObserver();

        // Tick buffs
        tickBuffs();

        // Tick indicators
        tickIndicators();

        if(getTask() != null && getTask().isAlive()) {
            getTask().onTick(i);

            if (getTask().isComplete()) {
                setTask(null);
            }
        }
    }

    @Override
    public void notifyTouchDown(int screenX, int screenY, int pointer, int button) {
        if (PauseMenuRenderer.isPaused()) {
            return;
        }
        float[] mouse = WorldUtil.screenToWorldCoordinates(Gdx.input.getX(), Gdx.input.getY());
        float[] clickedPosition = WorldUtil.worldCoordinatesToColRow(mouse[0], mouse[1]);
        boolean isRightClick = button == 1;

        LOGGER.log(Level.INFO, "mouse: {0} {1}", new Object[]{mouse[0], mouse[1]});
        LOGGER.log(Level.INFO, "clickedPosition: {0} {1} with button {2}",
                new Object[]{clickedPosition[0], clickedPosition[1], button});

        // Check if player can attack
        boolean canAttack = (playerState == PlayerState.IDLE ||
                        playerState == PlayerState.WALK);

        //notify if right click
        if (isRightClick && this.getMechState().equals(MechState.MECH_ON) &&
                canAttack) {
            mechAttack(clickedPosition[0], clickedPosition[1]);
            // Get attacking angle for animation purpose
            checkAttackDirection(clickedPosition[0], clickedPosition[1]);
            playerState = PlayerState.RANGED;
            lastAttack = System.currentTimeMillis();
        }

        // get npc on clicked position
        AgentEntity npc = GameManager.get().getHoveredAgentEntity();

        //if clicked on an npc
        if (npc != null) {
            //and is a double click
            if (System.currentTimeMillis() <= lastTouchDown + DOUBLE_CLICK_INTERVAL) {
                //determine what action this double click ensues
                if (npc instanceof EnemyPeon && distance(npc) <= weaponRange &&
                        canAttack) {
                    attack((Peon) npc, getAttackDamage());
                    checkAttackDirection(clickedPosition[0],
                            clickedPosition[1]);
                    playerState = PlayerState.ATTACK;
                    lastAttack = System.currentTimeMillis();
                    LOGGER.log(Level.INFO, "state: {0}", new Object[]{playerState});
                }
                if (npc instanceof FriendlyPeon && distance(npc) <= talkRange) {
                    interact(((FriendlyPeon) npc).getGreeting());
                    DialogueManager DM = DialogueManager.get();
                    DM.startDialogue(((FriendlyPeon) npc).getDialoguePath());
//                    DM.addDialogue(((FriendlyPeon) npc).getGreeting());
                    if (DM.isEqual(DM.getLastNPC(), npc)) {
                        DM.getTextBubble().toggleWindowOpen();
                        DM.setLastNPC(null);
                    } else {
                        DM.getTextBubble().setSpeaker(((FriendlyPeon) npc).getPeonFace());
                        DM.getTextBubble().setTextColor(((FriendlyPeon) npc).getTextColor());
                        if (DM.getLastNPC() == null) {
                            DM.getTextBubble().toggleWindowOpen();
                        }
                        ((FriendlyPeon) npc).playBgm();
                        DM.setLastNPC((FriendlyPeon) npc);
                    }
                }
            } else {
                //if not double click, store time of this click
                lastTouchDown = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void notifyKeyDown(int keycode) {
        switch (keycode) {
            case UP:
                wPressed = 1;
                playerDirection = FACING_UP;
                break;
            case LEFT:
                aPressed = 1;
                playerDirection = FACING_LEFT;
                break;
            case DOWN:
                sPressed = 1;
                playerDirection = FACING_DOWN;
                break;
            case RIGHT:
                dPressed = 1;
                playerDirection = FACING_RIGHT;
                break;
            case PICKUP:
                pickupPressed = 1;
                break;
            case WITHMECH:
                mechPressed = 1;
                break;
            case Input.Keys.NUM_0:
                System.out.println("0 pressed!");
                n0Pressed = 1;
                itemIdx=9;
                equipCompleted=false;
                break;
            case Input.Keys.NUM_1:
                n1Pressed = 1;
                itemIdx=0;
                equipCompleted=false;
                break;
            case Input.Keys.NUM_2:
                n2Pressed = 1;
                itemIdx=1;
                equipCompleted=false;
                break;
            case Input.Keys.NUM_3:
                n3Pressed = 1;
                itemIdx=2;
                equipCompleted=false;
                break;
            case Input.Keys.NUM_4:
                n4Pressed = 1;
                itemIdx=3;
                equipCompleted=false;
                break;
            case Input.Keys.NUM_5:
                n5Pressed = 1;
                itemIdx=4;
                equipCompleted=false;
                break;
            case Input.Keys.NUM_6:
                n6Pressed = 1;
                itemIdx=5;
                equipCompleted=false;
                break;
            case Input.Keys.NUM_7:
                n7Pressed = 1;
                itemIdx=6;
                equipCompleted=false;
                break;
            case Input.Keys.NUM_8:
                n8Pressed = 1;
                itemIdx=7;
                equipCompleted=false;
                break;
            case Input.Keys.NUM_9:
                n9Pressed = 1;
                itemIdx=8;
                equipCompleted=false;
            case PAUSEGAME:
                pauseGamePressed = 1;
                break;
            default:
                break;

        }
        currentDirection = keycode;
    }

    @Override
    public void notifyKeyUp(int keycode) {
        switch (keycode) {
            case UP:
                wPressed = 0;
                break;
            case LEFT:
                aPressed = 0;
                break;
            case DOWN:
                sPressed = 0;
                break;
            case RIGHT:
                dPressed = 0;
                break;
            case PICKUP:
                pickupPressed = 0;
                break;
            case WITHMECH:
                mechPressed = 0;
                break;
            case PAUSEGAME:
                pauseGamePressed = 0;
                break;
            case Input.Keys.NUM_0:
                System.out.println("0 unpressed!");
                n0Pressed = 0;
                break;
            case Input.Keys.NUM_1:
                n1Pressed = 0;
                break;
            case Input.Keys.NUM_2:
                n2Pressed = 0;
                break;
            case Input.Keys.NUM_3:
                n3Pressed = 0;
                break;
            case Input.Keys.NUM_4:
                n4Pressed = 0;
                break;
            case Input.Keys.NUM_5:
                n5Pressed = 0;
                break;
            case Input.Keys.NUM_6:
                n6Pressed = 0;
                break;
            case Input.Keys.NUM_7:
                n7Pressed = 0;
                break;
            case Input.Keys.NUM_8:
                n8Pressed = 0;
                break;
            case Input.Keys.NUM_9:
                n9Pressed = 0;
                break;
            default:
                LOGGER.log(Level.INFO, "keycode is {0}", new Object[]{keycode});
                break;
        }
    }

    /**
     * Check for player walking direction
     * */
    private void checkWalkingDirection() {
        // Left and right takes priority
        if(playerState != PlayerState.ATTACK) {
            if(aPressed == 1) {
                playerDirection = FACING_LEFT;
            } else if (dPressed == 1) {
                playerDirection = FACING_RIGHT;
            } else if(wPressed == 1) {
                playerDirection = FACING_UP;
            } else if (sPressed == 1) {
                playerDirection = FACING_DOWN;
            }
        }
    }

    /**
     * Check for player attacking direction
     * */
    private void checkAttackDirection(float x, float y) {
        SquareVector target = new SquareVector(x, y);

        float angrad = getDirectionTo(this, target);
        float angle = (float) Math.toDegrees(angrad);
        LOGGER.log(Level.INFO, "attack angle: {0}", angle);
        if(-45 <= angle && angle <= 45) {
            playerDirection = FACING_UP;
        } else if (45 < angle && angle <= 135) {
            playerDirection = FACING_RIGHT;
        } else if (-45 > angle && angle >= -135) {
            playerDirection = FACING_LEFT;
        } else if (angle < -135 || angle > 135) {
            playerDirection = FACING_DOWN;
        }
    }

    /**
     * Checks the player's current and set the respective state
     * */
    private void setPlayerState() {
        boolean notMoving = (wPressed == 0) && (aPressed == 0) &&
                (sPressed == 0) && (dPressed == 0);
        boolean notAttacking = (playerState != PlayerState.ATTACK &&
                        playerState != PlayerState.RANGED);

        if(notAttacking) {
            // Check if the player is moving and not attacking
            if(notMoving) {
                playerState = PlayerState.IDLE;
            } else {
                playerState = PlayerState.WALK;
            }
        } else {
            // Check if the attack cooldown is over
            long currentTime = System.currentTimeMillis();
            long nextAttackTime = lastAttack + 450;
            if(playerState == PlayerState.RANGED) {
                nextAttackTime += 150;
            }

            // Change state back to idle or walking
            if(currentTime >= nextAttackTime) {
                // Reset the current attack animation
                getAnimation().resetAnimationFrame();

                // Change the player state
                if(notMoving) {
                    playerState = PlayerState.IDLE;
                } else {
                    playerState = PlayerState.WALK;
                }
            }
        }
    }

    /**
     * Animates the player according to the current state
     * */
    private void animatePlayer() {
        SpriteSheetAnimation animation = mechIdleAnimations[playerDirection];
        if(mechState == MechState.MECH_ON) {
            switch (playerState) {
                case IDLE:
                    animation = mechIdleAnimations[playerDirection];
                    break;
                case WALK:
                    animation = mechWalkAnimations[playerDirection];
                    break;
                case ATTACK:
                    animation = mechAttackAnimations[playerDirection];
                    break;
                case RANGED:
                    animation = mechRangeAnimations[playerDirection];
                    break;
            }
        } else {
            switch (playerState) {
                case IDLE:
                    animation = knightIdleAnimations[playerDirection];
                    break;
                case WALK:
                    animation = knightWalkAnimations[playerDirection];
                    break;
                case ATTACK:
                    animation = knightAttackAnimations[playerDirection];
                    break;
            }
        }

        this.setAnimation(animation);
    }

    /**
     * Allow player to continuously move as long as at least one key is held
     * down and not moving into an obstacle. The direction of movement is based on what keys are held down
     * */
    public void playerMovement() {
        SquareVector target = getPosition();
        if (wPressed == 1) {
            target = target.add(new SquareVector(0, 1));
        }
        if (aPressed == 1) {
            target = target.add(new SquareVector(-1, 0));
        }
        if (sPressed == 1) {
            target = target.add(new SquareVector(0, -1));
        }
        if (dPressed == 1) {
            target = target.add(new SquareVector(1, 0));
        }

        if (!checkPlayerCollision(target)) {
            moveTowards(target);
        }

        // Check where the player is facing
        checkWalkingDirection();
    }

    /**
     * If the pause game button is pressed, creates a new pause game task.
     */
    void pauseGameAction() {
        if (pauseGamePressed == 1) {
            setTask(new PauseGameTask(this));
        }
    }
    /**
     * Allow the player to use or equip item
     */
    public void useItem() {
        if (n0Pressed == 1 ||n1Pressed==1||n2Pressed==1||n3Pressed == 1 ||n4Pressed==1||
                n5Pressed == 1 ||n6Pressed==1||n7Pressed == 1 ||n8Pressed==1||n9Pressed==1) {
            UnrenderedItem item = null;
            if(!equipCompleted&&this.getPlayerInventory().size()>itemIdx) {
                item = getPlayerInventory().getItem(itemIdx);
            }
            if (!equipCompleted&&item!=null) {
                if (item.getItemType().equals("weapon")) {
                    UnrenderedItem holding = equip(item);
                    getPlayerInventory().removeItem(item.getItemName());
                    System.out.println(equippedWeapon.getItemName()+", damage: "+((Weapon)equippedWeapon).getDamage());
                    if (holding!=null) {
                        InventoryRenderer.dropItem(this,holding);
                    }
                }else if (item.getItemType().equals("potion")) {
                    UnrenderedItem holding = usePotion(item);
                    getPlayerInventory().removeItem(item.getItemName());
                }
            }
            equipCompleted=true;
        }
    }


    /**
     * Returns the direction from the source to the target entity
     * @param source Source entity.
     * @param target Target position.
     * @returns float of the direction to the target in radians.
     */
    public static float getDirectionTo(AbstractEntity source, SquareVector target) {
        float rowDiff = target.getRow() - source.getRow();
        float colDiff = target.getCol() - source.getCol();
        return (float)Math.atan2(colDiff, rowDiff);
    }

    /**
     * Checks the target position to see if there is an entity that exists that the player can collide with when moving
     * @param target the position to be checked
     * @return true if there is an entity that the player will collide with, false otherwise
     */
    public boolean checkPlayerCollision(SquareVector target) {
        // Calculate the target position in the world relative to the player
        SquareVector predictedPosition = new SquareVector(
                this.getCol() + (float)Math.sin(getDirectionTo(this, target)) * this.getColRenderLength()/2,
                this.getRow() + (float)Math.cos(getDirectionTo(this, target)) * this.getRowRenderLength()/2);

        // SquareVector to help centre the collision probe with the rendered player
        SquareVector centre = new SquareVector(this.getColRenderLength()/2, this.getRowRenderLength()/2);

        // Check if there is a static object at the target position
        AbstractEntity collidingEntity = GameManager.get().getWorld().probeCollisionMap(predictedPosition.add(centre));
        if (collidingEntity != null) {
            return true;
        }
        return false;
    }

    /**
     * Adds tileEventObserver to Observable list
     *
     * @param observer Event that is triggered when player enters tiles
     */
    @Override
    public void registerObserver(TileEventObserver observer) {
        tileEventObserversList.add(observer);
    }

    /**
     * Removes tileEventObserver to Observable list
     *
     * @param observer Event that is triggered when player enters tiles
     */
    @Override
    public void unregisterObserver(TileEventObserver observer) {
        for (Iterator<TileEventObserver> iterator = tileEventObserversList.iterator(); iterator.hasNext(); ) {
            TileEventObserver value = iterator.next();
            if (value.equals(observer)) {
                System.out.println("got here");
                iterator.remove();
            }
        }
    }

    /**
     * Notifies observers if the entity moves tiles. To be called onTick
     *
     * TODO: Implement a more efficient way for the peon to check if its moved tiles so
     * TODO: -- that the observer doesn't have to loop through the list every tick
     *
     * @param hasChangedTile
     */
    @Override
    public void hasChangedTile(boolean hasChangedTile) {
        //if the player changes tiles
        notifyObserver();
    }

    /**
     * Notifies observers of change in tile
     */
    @Override
    public void notifyObserver() {
        tileEventObserversList.forEach((event) -> notifyEvent(event));
    }

    /**
     * Helper method to notify observers
     *
     * @param event Event that is triggered when player enters tiles
     */
    @Override
    public void notifyEvent(TileEventObserver event) {
        TerrainManager.Biome biome = GameManager.get().getWorld().terrainManager.getBiomeAtTile((int)Math.floor(getCol()), (int)Math.floor(getRow()));
        switch (biome) {
            case TUNDRA:
                event.notifyEvent(-1.0f, 1.0f, this, event);
                break;
            case SWAMP:
                event.notifyEvent(1.0f, 1.0f, this, event);
                break;
            case DESERT:
                event.notifyEvent(1.0f, -1.0f, this, event);
                break;
            case VOLCANO:
                event.notifyEvent(-1.0f, -1.0f, this, event);
                break;
            case PLAINS:
                // Make plains the same as swamp
                event.notifyEvent(1.0f, 1.0f, this, event);
                break;
        }
    }


    public int getExp() {
        return exp;
    }

    /**
     * Set the player exp. When the exp is less than the boundary,
     * it will return null, otherwise it will set the exp
     * */
    public void setExp(int exp) {
        if (exp < 0) {
            return;
        }
        this.exp = exp;
    }

    /**
     * Getter for player level
     * @return Player's current level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Set the player level. When the exp is less than or greater than
     * the boundary,
     * it will return null, otherwise it will set the level
     * */
    public void setLevel(int level) {
        if (level < 0 || level > 100) {
            return;
        }
        this.level = level;
    }

    public int getNextLevelExp(){
        return (level) * 100;
    }

    /**
     * Player's level increases, exp gets set back to 0 and increment StatPoints by 1.
     */
    public void levelUp() {
        setLevel(getLevel() + 1);
        setExp(0);
        setStatPoints(statPoints + 3);
    }

    /**
     * An add Exp function. When the exp is less than the boundary or level reach
     * the max value = 100, it will return null.
     * If the exp added can reach next level exp,
     * player will level up and exp will be cleared
     * otherwise the exp will be added
     * */
    public void addExp(int exp) {
        if (exp < 0) {
            return;
        }
        if (this.level == 100) {
            // Max Level
            return;
        }
        if (this.exp + exp >= getNextLevelExp()) {
            levelUp();
        } else {
            this.exp += exp;
        }
    }

    /**
     * Getter method for strength stat including weapon damage
     * @return the amount of damage the player will inflict when attacking
     */
    public int getStrength() {
        return strength;
    }

    /**
     * Sets the player's strength to newStrength plus the current weaponDamage
     * @requires newStrength > 0
     * @param newStrength the new base amount of damage the player will inflict when attacking
     *                    (not including weapon dmg)
     */
    public void setStrength(int newStrength) {
        strength = newStrength;
    }

    /**
     * Combines the player's strength and their current weapon damage
     * @return the amount of damage a player will do when attacking
     */
    public int getAttackDamage() {
        int damageBuff = 0;
        if (mechState == MechState.MECH_ON) {
            damageBuff = mech.getStrength();
        }
        return getStrength() + weaponDamage + damageBuff;
    }

    /**
     * Getter method for defense stat
     * @return the overall defense of the player
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Getter method for overall defense (when player is in mech)
     * @return overall player defense
     */
    public int getOverallDefense() {
        int defenseBuff = 0;
        if (mechState == MechState.MECH_ON) {
            defenseBuff = mech.getDefense();
        }
        return defense + defenseBuff;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public void addHealth(int amount) {
        if (mechState == MechState.MECH_ON && amount < 0) {
            mech.setHealth(mech.getHealth() + amount);
            // Check if the mech's health is depleted
            if (mech.getHealth() == 0) {
                // Exit mech and leave it in the 'broke' state
                mechState = MechState.MECH_BROKE;
                setSpeed(getSpeed() + mech.getSpeedDeduction());
                setTask(new ActionMechTask(this,false));

            }
            return;
        }
        super.addHealth(amount);
    }

    @Override
    public void addHealth(int amount, DamageIndicator indicator) {
        if (mechState == MechState.MECH_ON && amount < 0) {
            mech.setHealth(mech.getHealth() + amount);
            // Check if the mech's health is depleted
            if (mech.getHealth() == 0) {
                // Exit mech and leave it in the 'broke' state
                setSpeed(getSpeed() - mech.getSpeedDeduction());
                mechState = MechState.MECH_BROKE;
                setTask(new ActionMechTask(this,false));
            }
            return;
        }
        super.addHealth(amount, indicator);
    }

    /**
     * Sets the player's defense to newDefense
     * @requires newDefense > 0
     * @param newDefense the new defense value to be set
     */
    public void setDefense(int newDefense) {
        this.defense = newDefense;
    }

    /**
     * Gets the player's remaining stat points
     * @return Player's remaining stat points
     */
    public int getStatPoints() {
        return statPoints;
    }

    /**
     * Sets player stat points to new stat point value
     * @param newValue new stat point value
     */
    public void setStatPoints(int newValue) {
        if (newValue < 0) {
            return;
        }
        statPoints = newValue;
    }

    /**
     * Picks up a nearby item if the player presses "E";
     */
    public void playerPickupItem() {
        if (pickupPressed == 1) {
            LinkedList<DroppedItem> items = GameManager.get().getWorld().getWorldItems().getItems((int) getRow(), (int) getCol());
            if((items.size() > 0) && (items.getFirst().getItem().getItemName() != "Mech")) {
                setTask(new PlayerPickupItemTask(this, items.getFirst()));
            }
        }
    }

    /**
     * Enter,repair or exit mech using the 'F' key
     */
    public void actionMech() {
        if (mechPressed == 1) {

            //get MechSuit item
            DroppedItem item = GameManager.get()
                    .getWorld()
                    .getWorldItems()
                    .getItem((int)getRow(), (int)getCol(), "Mech");

            if (mechState == MechState.MECH_ON) {
                //get off mech
                demountMech();
            } else if (mechState == MechState.MECH_BROKE &&
                    this.getPlayerInventory().contains("Tool Kit") && item != null) {
                //fix mech with toolkit and remove toolkit from inventory
                fixMech();
            } else if (mechState == MechState.MECH_OFF && item != null) {
                mountMech(item);
            }
        }
        mechPressed = 0;
    }

    /**
     * Returns player's current mech state
     * @return player mech state
     */
    public MechState getMechState() {
        return mechState;
    }

    /**
     * Returns current player mech
     * @return player mech
     */
    public Mech getMech() {
        return mech;
    }

    private void mechAttack(float x, float y) {
        if (this.getMechState().equals(MechState.MECH_ON)) {
            SquareVector click = new SquareVector(x, y);
            float direction = getDirectionTo(this, click);

            MechProjectile mechProjectile = new MechProjectile(this.getCol(), this.getRow(), direction, 8, getAttackDamage(), 1f, this);
            mechProjectile.setTexture("mech_projectile_still");
            mechProjectile.setCollidable(true);
            GameManager.get().getWorld().addEntity(mechProjectile);
        }
    }

    /**
     * Helper method for actionMech() to demount player from Mech
     */
    private void demountMech() {
        mechState = MechState.MECH_OFF;
        setTask(new ActionMechTask(this, false));
        setSpeed(getSpeed() + mech.getSpeedDeduction());
    }

    /**
     * Helper method for actionMech() to fix broken Mech
     * MechState is changed and ToolKit is removed from inventory
     */
    private void fixMech() {
        mechState = MechState.MECH_OFF;
        this.getPlayerInventory().removeItem("Tool Kit");
        mech = new Mech(2, 5, 0.04f, 25);
        setTask(new ActionMechTask(this,false));
    }

    /**
     * Helper method for actionMech() to mount player onto Mech
     * @param item mechsuit item
     */
    private void mountMech(DroppedItem item) {
        //remove MechSuit item from game
        item.dispose();

        //get on mech
        mechState = MechState.MECH_ON;
        setTask(new ActionMechTask(this, true));

        //reset mech stats
        setSpeed(getSpeed() - mech.getSpeedDeduction());
    }

    public UnrenderedItem equip(UnrenderedItem theWeapon) {
        if (!theWeapon.getItemType().equals("weapon")){
            System.out.println("Can only equip weapon");
        }
        UnrenderedItem currentWeapon = equippedWeapon;
        equippedWeapon = theWeapon;
        weaponDamage = (int) ((Weapon)theWeapon).getDamage();
        weaponRange = (float) ((Weapon)theWeapon).getAttackRange();
        return currentWeapon;
    }

    public UnrenderedItem usePotion (UnrenderedItem potion) {
        if (!potion.getItemType().equals("potion")) {
            System.out.println("can not use not potion item");
        }
        if (potion.getItemName().equals("Health Potion")) {
            addHealth(getHealth()+((HealthPotion)potion).getHealingAmount()> getMaxHealth()?
                    getMaxHealth()-getHealth():
                    ((HealthPotion)potion).getHealingAmount());
        }else if (potion.getItemName().equals("Speed Potion")) {
            setSpeed(getSpeed()+((SpeedPotion)potion).getSpeedAmount());
        }
        return potion;
    }
}
