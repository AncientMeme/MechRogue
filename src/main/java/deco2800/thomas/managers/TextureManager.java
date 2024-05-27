package deco2800.thomas.managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import deco2800.thomas.util.Vector2;
import org.lwjgl.Sys;

import java.util.*;
import java.util.logging.Logger;

/**
 * Texture manager acts as a cache between the file system and the renderers.
 * This allows all textures to be read into memory at the start of the game saving
 * file reads from being completed during rendering.
 * <p>
 * With this in mind don't load textures you're not going to use.
 * Textures that are not used should probably (at some point) be removed
 * from the list and then read from disk when needed again using some type
 * of reference counting
 *
 * @author Tim Hadwen
 */
public class TextureManager extends AbstractManager {

    static final Logger LOG = Logger.getLogger(TextureManager.class.getPackage().getName());

	/**
	 * The width of the tile to use then positioning the tile.
	 */
	public static final int TILE_WIDTH = 320;
	
	/**
	 * The height of the tile to use when positioning the tile.
	 */
	public static final int TILE_HEIGHT = 278;

	
	//private final Logger log = LoggerFactory.getLogger(TextureManager.class);


    /**
     * A HashMap of all atlas identifiers with string keys
     */
    private Map<String, String> textureMap = new HashMap<>();
    private Map<String, Vector2> textureScaling = new HashMap<>();

    /**
     * A HashMap of all spritesheets with string keys
     */
    private Map<String, SpriteSheet> spritesheetMap = new HashMap<>();

    private TextureAtlas atlas;

    /**
     * Constructor
     * Currently loads up all the textures but probably shouldn't/doesn't
     * need to.
     */
    public TextureManager() {
        try {
            atlas = new TextureAtlas("resources/textures.atlas");

            /* Default textures */
            textureMap.put("background", "background");
            textureMap.put("spacman_ded", "hero_models/knight_placeholder");
            textureMap.put("missing_texture", "debug/missingtexture");

            /* ----------- Biomes Tiles ------------ */

            /* Tundra */

            /* Directional Tiles */

            for (int i = 0; i < 256; i++) {
                textureMap.put("tundra_ground_" + i,
                        "terrain/tundra/snow/directional_tiles/snow_" + i);
                textureScaling.put("tundra_ground_" + i, new Vector2(7.74f, 7.74f));
            }

            textureMap.put("tundra_spawn_15", "terrain/tundra/snow/directional_tiles/snow_255");
            textureScaling.put("tundra_spawn_15", new Vector2(7.74f, 7.74f));

            /* Default Tiles */

            textureMap.put("tundra_water", "terrain/tundra/ice/directional_tiles/ice_15");
            textureScaling.put("tundra_water", new Vector2(7.74f, 7.74f));

            /* Swamp */

            /* Directional Tiles */
            for (int i = 0; i < 256; i++) {
                textureMap.put("swamp_ground_" + i,
                        "terrain/swamp/mud/directional_tiles/mud_" + i);
                textureScaling.put("swamp_ground_" + i, new Vector2(7.74f, 7.74f));
            }

            textureMap.put("swamp_spawn_15", "terrain/swamp/mud/directional_tiles/mud_255");
            textureScaling.put("swamp_spawn_15", new Vector2(7.74f, 7.74f));

            /* Default Tiles */
            textureMap.put("swamp_water", "terrain/swamp/water/directional_tiles/water_15");
            textureScaling.put("swamp_water", new Vector2(7.74f, 7.74f));


            /* Desert */
            for (int i = 0; i < 256; i++) {
                textureMap.put("desert_volcano_" + i,
                        "terrain/desert/sand/sand_volcano/desert_volcano_" + i);
                textureScaling.put("desert_volcano_" + i, new Vector2(7.74f, 7.74f));
            }
            /* Directional Tiles */

            /* Default Tiles */


            /* Volcano */

            /* Directional Tiles*/
            for (int i = 0; i < 256; i++) {
                textureMap.put("volcano_tundra_" + i,
                        "terrain/volcano/rock_to_snow/volcano_tundra_" + i);
                textureScaling.put("volcano_tundra_" + i, new Vector2(7.74f, 7.74f));
            }

            for (int i = 0; i < 256; i++) {
                textureMap.put("volcano_ground_" + i,
                        "terrain/volcano/ground/volcano_ground_" + i);
                textureScaling.put("volcano_ground_" + i, new Vector2(7.74f, 7.74f));
            }

            textureMap.put("volcano_spawn_15", "terrain/volcano/spawn/directional_tiles/volcano_spawn_15");
            textureScaling.put("volcano_spawn_15", new Vector2(7.74f, 7.74f));

            /* Default Tiles */
            textureMap.put("volcano_water", "terrain/volcano/lava/directional_tiles/lava_15");
            textureScaling.put("volcano_water", new Vector2(7.74f, 7.74f));


            /* General Terrain Textures */
            textureMap.put("water", "terrain/general/water");
            textureScaling.put("water", new Vector2(7.74f, 7.74f));

            textureMap.put("plains_grass", "terrain/general/plains_grass");
            textureScaling.put("plains_grass", new Vector2(7.74f, 7.74f));

            /* Utility Terrain Textures */
            textureMap.put("transparent_tile", "terrain/util/transparent_tile");
            textureScaling.put("transparent_tile", new Vector2(7.74f, 7.74f));

            textureMap.put("grass_10", "tiles/desert_tile_1");
            textureMap.put("grass_11", "tiles/desert_tile_2");
            textureMap.put("grass_12", "tiles/desert_tile_3");

            // Texture was sourced from background.jpg
            textureMap.put("fire", "tiles/fire");
            textureScaling.put("fire", new Vector2(10.0f, 10.0f));

            textureMap.put("selection", "debug/square-select");
            textureMap.put("path", "debug/square-select");
            
            
            textureMap.put("buildingB", "building/castletower_placeholder");
            
            textureMap.put("buildingA", "building/castletower_placeholder");

            /*Biome Environmental Features*/
            textureMap.put("tree_0", "trees/tundra8");
            textureMap.put("tree_1", "trees/tundra2-1");
            textureMap.put("tree_2", "trees/tundra2");
            textureMap.put("tree_3", "trees/swamp1");
            textureMap.put("tree_4", "trees/swamp2");
            textureMap.put("tree_5", "trees/cactus_desert");
            textureMap.put("tree_6", "trees/cactus_desert_02");
            textureMap.put("tree_7", "trees/DeadTree_01");
            textureMap.put("tree_8", "trees/DeadTree_02");

            textureMap.put("rock_0", "rocks/tundraRock2");
            textureMap.put("rock_1", "rocks/TundraRock2-3");

            textureMap.put("rock_2", "rocks/rockplaceholder2");

            textureMap.put("rock_3", "rocks/rock_desert");
            textureMap.put("rock_4", "rocks/rock_desert_02");

            textureMap.put("rock_5", "rocks/rock_01");
            textureMap.put("rock_6", "rocks/rock_02");

            textureMap.put("fenceN-S", "fences/fence_N-S");
            textureMap.put("fenceE-W", "fences/fence_E-W");
            textureMap.put("fenceN-W", "fences/fence_N-W");
            textureMap.put("fenceS-W", "fences/fence_S-W");
            textureMap.put("fenceN-E", "fences/fence_N-E");
            textureMap.put("fenceS-E", "fences/fence_S-E");

            /*NPCs*/
            textureMap.put("merchant_front","FriendlyNPC's/merchant_front");
            textureMap.put("merchant_left","FriendlyNPC's/merchant_left");
            textureMap.put("merchant_right","FriendlyNPC's/merchant_right");
            textureMap.put("merchant_cool","FriendlyNPC's/merchant_with_shades");
            textureMap.put("wizard_left","FriendlyNPC's/wizard_face_left");
            textureMap.put("wizard_right", "FriendlyNPC's/wizard_face_right");
            textureMap.put("wizard_front","FriendlyNPC's/wizard_face_toward");
            textureMap.put("camo_left","FriendlyNPC's/camo_left");
            textureMap.put("camo_right","FriendlyNPC's/camo_right");
            textureMap.put("snowman_left","FriendlyNPC's/snowman_left");
            textureMap.put("snowman_right","FriendlyNPC's/snowman_right");

            spritesheetMap.put("WizardNpc1",
                    new SpriteSheet("FriendlyNPC's/wizard1",
                            320, 320, 3, 3));

            spritesheetMap.put("Rogue",
                    new SpriteSheet("FriendlyNPC's/Rogue",
                            320, 320, 3, 3));

            spritesheetMap.put("Villager",
                    new SpriteSheet("FriendlyNPC's/Villager",
                            320, 320, 3, 3));

            /*NPCs Faces*/
            textureMap.put("VillagerFace","FriendlyNPC's/Villagerface");
            textureMap.put("wizard_dialog","FriendlyNPC's/wizard_NPC_dialog");
            textureMap.put("WizardFace", "FriendlyNPC's/WizardFace");
            textureMap.put("RogueFace", "FriendlyNPC's/RogueFace");

            /* NPC talking animation */
            spritesheetMap.put("VillagerTalking",
                    new SpriteSheet("FriendlyNPC's/MaleVillager_Spritesheet",
                            320, 320, 6, 6));

            // health bar
            textureMap.put("health_bar", "hero_models/bar");

            /* Player status bar */
            textureMap.put("status_bar", "UI/player_status_bar");
            textureMap.put("player_health_bar", "UI/player_health_bar");
            textureMap.put("player_exp_bar", "UI/player_exp_bar");
            textureMap.put("player_hurt_bar", "UI/player_hurt_bar");

            /* Player Sprites */
            textureMap.put("player_idle_down_still", "player/player_idle_down");
            spritesheetMap.put("player_idle_down",
                    new SpriteSheet("player/player_idle_down",
                            320, 320, 1, 1));
            spritesheetMap.put("player_idle_up",
                    new SpriteSheet("player/player_idle_up",
                            320, 320, 1, 1));
            spritesheetMap.put("player_idle_left",
                    new SpriteSheet("player/player_idle_left",
                            320, 320, 1, 1));
            spritesheetMap.put("player_idle_right",
                    new SpriteSheet("player/player_idle_right",
                            320, 320, 1, 1));

            /* Player Walking Animation */
            spritesheetMap.put("player_walk_down",
                    new SpriteSheet("player/player_walk_down",
                            400, 320, 4, 4));
            spritesheetMap.put("player_walk_up",
                    new SpriteSheet("player/player_walk_up",
                            400, 320, 4, 4));
            spritesheetMap.put("player_walk_left",
                    new SpriteSheet("player/player_walk_left",
                            320, 320, 4, 4));
            spritesheetMap.put("player_walk_right",
                    new SpriteSheet("player/player_walk_right",
                            320, 320, 4, 4));

            /* Player Attacking Animation */
            spritesheetMap.put("player_attack_down",
                    new SpriteSheet("player/player_attack_down",
                            320, 400, 4, 4));
            spritesheetMap.put("player_attack_up",
                    new SpriteSheet("player/player_attack_up",
                            320, 400, 4, 4));
            spritesheetMap.put("player_attack_left",
                    new SpriteSheet("player/player_attack_left",
                            400, 320, 4, 4));
            spritesheetMap.put("player_attack_right",
                    new SpriteSheet("player/player_attack_right",
                            400, 320, 4, 4));

            /* Player Orb Bar*/
            textureMap.put("ice_orb", "orbs/Pixel_art_orb_snowb");
            textureMap.put("desert_orb", "orbs/Pixel_art_orb_desertb");
            textureMap.put("swamp_orb", "orbs/Pixel_art_orb_swampb");
            textureMap.put("volcano_orb", "orbs/Pixel_art_orb_volcanob");
            textureMap.put("orbBar","orbs/orbBar");

            // player orb container
            textureMap.put("container", "animationContainer/emptyContainer");
            textureMap.put("desert", "animationContainer/desert");
            textureMap.put("tundra", "animationContainer/tundra");
            textureMap.put("volcano", "animationContainer/volcano");
            textureMap.put("swamp", "animationContainer/swamp");
            textureMap.put("desertPart", "animationContainer/desertPart");
            textureMap.put("swampPart", "animationContainer/swampPart");
            textureMap.put("tundraPart", "animationContainer/tundraPart");
            textureMap.put("volcanoPart", "animationContainer/volcanoPart");
            textureMap.put("basic", "animationContainer/basic");
            textureMap.put("orbsUI", "animationContainer/orbsUI");
            textureMap.put("filledContainer", "animationContainer/filledContainer");
            // death screen buttons
            textureMap.put("restartBtn", "death/tryAgain");
            textureMap.put("mainMenu", "death/backtomain");


            // mech
            textureMap.put("Mech_001", "UI/player_mech");
            textureMap.put("mech_health_bar", "UI/mech_health_bar");

            spritesheetMap.put("mech_idle_left",
                    new SpriteSheet("mech/mech_idle_left",
                            320, 320, 1, 1));
            spritesheetMap.put("mech_idle_right",
                    new SpriteSheet("mech/mech_idle_right",
                            320, 320, 1, 1));
            spritesheetMap.put("mech_idle_up",
                    new SpriteSheet("mech/mech_idle_up",
                            320, 320, 1, 1));
            spritesheetMap.put("mech_idle_down",
                    new SpriteSheet("mech/mech_idle_down",
                            320, 320, 1, 1));

            // mech walk
            spritesheetMap.put("mech_walk_left",
                    new SpriteSheet("mech/mech_walk_left",
                            320, 320, 4, 4));
            spritesheetMap.put("mech_walk_right",
                    new SpriteSheet("mech/mech_walk_right",
                            320, 320, 4, 4));
            spritesheetMap.put("mech_walk_up",
                    new SpriteSheet("mech/mech_walk_up",
                            320, 320, 4, 4));
            spritesheetMap.put("mech_walk_down",
                    new SpriteSheet("mech/mech_walk_down",
                            320, 320, 4, 4));

            // mech melee attack
            spritesheetMap.put("mech_attack_left",
                    new SpriteSheet("mech/mech_attack_left",
                            320, 320, 4, 4));
            spritesheetMap.put("mech_attack_right",
                    new SpriteSheet("mech/mech_attack_right",
                            320, 320, 4, 4));
            spritesheetMap.put("mech_attack_up",
                    new SpriteSheet("mech/mech_attack_up",
                            320, 320, 4, 4));
            spritesheetMap.put("mech_attack_down",
                    new SpriteSheet("mech/mech_attack_down",
                            320, 320, 4, 4));

            // mech ranged attack
            spritesheetMap.put("mech_ranged_left",
                    new SpriteSheet("mech/mech_ranged_left",
                            320, 320, 3, 6));
            spritesheetMap.put("mech_ranged_right",
                    new SpriteSheet("mech/mech_ranged_right",
                            320, 320, 3, 6));
            spritesheetMap.put("mech_ranged_up",
                    new SpriteSheet("mech/mech_ranged_up",
                            320, 320, 3, 6));
            spritesheetMap.put("mech_ranged_down",
                    new SpriteSheet("mech/mech_ranged_down",
                            320, 320, 3, 6));

            // mech breakdown
            textureMap.put("mech_breakdown_still", "mech/mech_breakdown_still");
            spritesheetMap.put("mech_breakdown",
                    new SpriteSheet("mech/mech_breakdown",
                            256, 256, 2, 4));

            // mech projectile
            spritesheetMap.put("mech_projectile",
                    new SpriteSheet("mech/mech_projectile",
                            256, 256, 4, 4));

            // mech projectile still
            textureMap.put("mech_projectile_still", "mech/mech_projectile_still");

            // stats window
            textureMap.put("stats_window", "UI/player_stat_base");

            // speech baloon window
            textureMap.put("textbubble","UI/speechbaloon");

            // game controls pop-up
            textureMap.put("game_controls_window", "UI/Game Controls");

            // game controls pop-up
            textureMap.put("game_controls_window2", "UI/gamecontrols2");


            /* Temporary projectile texture */
            textureMap.put("blindness_projectile", "enemies/blindness_projectile");

            /* Default orc test enemy */
            textureMap.put("dummy_enemy", "enemies/dummy_enemy");

            /* Boss enemies for the tundra area */
            textureMap.put("tundra_boss", "enemies/tundra/boss/tundra_boss");

            /* Common enemies for the tundra area */
            textureMap.put("tundra_common", "enemies/tundra/common/tundra_common");

            /* Melee enemies for the tundra area */
            textureMap.put("tundra_melee", "enemies/tundra/melee/tundra_melee");

            /* Ranged enemies for the tundra area */
            textureMap.put("tundra_ranged", "enemies/tundra/ranged/tundra_ranged");

            /* Slash effect texture */
            textureMap.put("slash_effect", "enemies/slash");

            /* Green Slash effect texture */
            textureMap.put("green_slash", "enemies/swamp/green_slash");

            /* Fireball effect texture */
            textureMap.put("fireball", "enemies/volcano/boss/fireball");

            /* Web Projectile effect texture */
            textureMap.put("web_projectile", "enemies/desert/boss/web_projectile");

            /* Web effect texture */
            textureMap.put("web", "enemies/desert/boss/web");

            /* Collision Debug Sprites */
            textureMap.put("collision_debug_box", "debug/collision_debug_box");
            textureMap.put("collision_debug_point", "debug/collision_debug_point");

            /* Tundra Boss Sprite Sheet */
            spritesheetMap.put("tundra_boss_spritesheet",
                    new SpriteSheet("enemies/tundra/boss/tundra_boss_spritesheet",
                            500, 500, 2, 4));

            /* Tundra Common Sprite Sheet */
            spritesheetMap.put("tundra_common_spritesheet",
                    new SpriteSheet("enemies/tundra/common/tundra_common_spritesheet",
                            250, 250, 2, 4));

            /* Tundra Melee Sprite Sheet */
            spritesheetMap.put("tundra_melee_spritesheet",
                    new SpriteSheet("enemies/tundra/melee/tundra_melee_spritesheet",
                            375, 375, 2, 3));

            /* Tundra Ranged Sprite Sheet */
            spritesheetMap.put("tundra_ranged_spritesheet",
                    new SpriteSheet("enemies/tundra/ranged/tundra_ranged_spritesheet",
                            375, 375, 2, 4));

            /* Desert Boss Sprite Sheet */
            spritesheetMap.put("desert_boss_spritesheet",
                    new SpriteSheet("enemies/desert/boss/desert_boss_spritesheet",
                            500, 357, 5, 6));

            /* Desert Boss Minion Sprite Sheet */
            spritesheetMap.put("desert_boss_minion_spritesheet",
                    new SpriteSheet("enemies/desert/boss/boss_minion/desert_boss_minion_spritesheet",
                            245, 175, 5, 6));

            /* Volcano Boss Sprite Sheet */
            spritesheetMap.put("volcano_boss_spritesheet",
                    new SpriteSheet("enemies/volcano/boss/volcano_boss_spritesheet_left",
                            320, 320, 5, 7));

            /* Swamp Boss Sprite Sheet */
            spritesheetMap.put("swamp_boss_spritesheet",
                    new SpriteSheet("enemies/swamp/boss/swamp_boss_spritesheet_left",
                            320, 320, 3, 3));

            /* Test spritesheet */
            spritesheetMap.put("missing_spritesheet",
                    new SpriteSheet("debug/missingtexture", 128, 128, 1, 1));

            /* Burn buff icon */
            textureMap.put("burn_buff", "enemies/buff_icons/burn");

            /* Slow buff icon */
            textureMap.put("freeze_buff", "enemies/buff_icons/freeze");

            /* Burn buff icon */
            textureMap.put("blind_buff", "enemies/buff_icons/blind");

            /* Poison buff icon */
            textureMap.put("poison_buff", "enemies/buff_icons/poison");

            /* Regen buff icon */
            textureMap.put("regen_buff", "enemies/buff_icons/regeneration");

            /* Strength buff icon */
            textureMap.put("strength_buff", "enemies/buff_icons/strength");

            /* Speed buff icon */
            textureMap.put("speed_buff", "enemies/buff_icons/speed");

            /* Unstoppable buff icon */
            textureMap.put("unstoppable_buff", "enemies/buff_icons/unstoppable");

            /* Solid 1x1 coloured pixels for buff outlines */
            textureMap.put("green_pixel", "enemies/buff_icons/pixels/green_pixel");
            textureMap.put("red_pixel", "enemies/buff_icons/pixels/red_pixel");

            /* Potion textures */
            textureMap.put("potion_001", "items/health_potion");
            textureMap.put("potion_002", "items/mana_potion");
            textureMap.put("potion_003","items/speed_potion");
            textureMap.put("extra_health","items/extra_health");

            /* Armour texture */
            textureMap.put("armour","items/armour");

            /* Item buttons */
            textureMap.put("item_button", "items/item_button");
            textureMap.put("item_button_pressed", "items/item_button_pressed");
            textureMap.put("item_window", "item_window");
            textureMap.put("placeholder","items/placeholder");

            /* Weapon textures */
            textureMap.put("weapon_001", "weapons/ironSword");
            textureMap.put("weapon_002", "weapons/onehanded");
            textureMap.put("weapon_003", "weapons/woodBow");
            textureMap.put("weapon_004", "weapons/greatAxe");
            textureMap.put("weapon_005", "weapons/dagger");
            textureMap.put("weapon_006", "weapons/wand");

            /* Overlay UI */
            textureMap.put("holder", "itemBar_placeholder");
            textureMap.put("invback", "itemBar1");
            textureMap.put("hovered", "itemBar2");
            textureMap.put("stats", "stats");
            textureMap.put("tool_kit_001", "items/tool_kit");

            /* Main menu UI */
            textureMap.put("CTS", "UI/CTS");
            textureMap.put("CTS-1", "UI/CTS-1");
            textureMap.put("LOADGAME", "UI/LOADGAME");
            textureMap.put("LOADGAME-1", "UI/LOADGAME-1");
            textureMap.put("SINGLEPLAYER", "UI/SINGLEPLAYER");
            textureMap.put("SINGLEPLAYER-1", "UI/SINGLEPLAYER-1");
            textureMap.put("SETTING", "UI/SETTING");
            textureMap.put("SETTING-1", "UI/SETTING-1");
            textureMap.put("Backbutton", "UI/Backbutton");
            textureMap.put("Settingscreen", "UI/Settingscreen");
            textureMap.put("Swordslider", "UI/Swordslider");
            textureMap.put("savegame-1", "UI/savegame-1");
            textureMap.put("savegame-2", "UI/savegame-2");
            textureMap.put("resume-1", "UI/resume-1");
            textureMap.put("resume-2", "UI/resume-2");
            textureMap.put("quit-1", "UI/quit-1");
            textureMap.put("quit-2", "UI/quit-2");


            // DO NOT COMMENT OR DELETE THIS LINE. ALSO ENSURE IT IS THE LAST LINE
            enforceProperAtlasSize();
        } catch (Exception e) {
            LOG.warning("Exception in texture manager");
        }
    }

    /**
     * Gets the texture atlas used by the manager
     */
    public TextureAtlas getAtlas() {
        return atlas;
    }

    /**
     * Gets a texture region object for a given string id
     *
     * @param id Texture identifier
     * @return Texture region for given id
     */
    public TextureRegion getTexture(String id) {
        if (textureMap.containsKey(id)) {
            return getTextureRegion(textureMap.get(id));
        } else {
            //log.info("Texture map does not contain P{}, returning default texture.", id);
            return getTextureRegion(textureMap.get("missing_texture"));
        }
    }

    /**
     * Gets texture scaling for a given string id
     *
     * @param id Texture identifier
     * @return Scaling for texture
     */
    public Vector2 getTextureScaling(String id) {
        if (textureScaling.containsKey(id)) {
            return textureScaling.get(id);
        } else {
            // Return unit scaling if it does not exist
            return new Vector2(1.0f, 1.0f);
        }
    }

    /**
     * Gets a spritesheet object for a given string id
     *
     * @param id Spritesheet identifier
     * @return Spritesheet for given id
     */
    public SpriteSheet getSpritesheet(String id) {
        if (spritesheetMap.containsKey(id)) {
            return spritesheetMap.get(id);
        } else {
            //log.info("Spritesheet map does not contain P{}, returning default spritesheet.", id);
            return spritesheetMap.get("missing_spritesheet");
        }
    }

    /**
     * Gets the texture region from texture atlas
     * @param atlasIdentifier atlas identifier
     * @return texture region
     */
    public TextureRegion getTextureRegion(String atlasIdentifier) {
        TextureRegion tr = atlas.findRegion(atlasIdentifier);
        if (tr == null) {
            System.out.println("You have entered an invalid atlas identifier: '" + atlasIdentifier + "'. Exiting");
            System.exit(1);
        }
        return tr;
    }

    /**
     * Checks whether or not a texture is available.
     *
     * @param id Texture identifier
     * @return If texture is available or not.
     */
    public boolean hasTexture(String id) {
        return textureMap.containsKey(id);
    }

    /**
     * Checks whether there exist textures in the atlas that are not used.
     * Also checks whether there exists references to textures that are not in the atlas.
     *
     * Logs a nicely formatted message if this function is not happy
     */
    private void enforceProperAtlasSize() {
        List<String> excessRegions = new ArrayList<>();
        List<String> excessDefinitions = new ArrayList<>();
        boolean shouldQuit = false;

        System.out.println("Verifying atlas and textureMap/spritesheetMap sizes");
        // Check for excess regions (This is to enforce no unused textures going into atlas :D)
        Array<TextureAtlas.AtlasRegion> atlasRegions = atlas.getRegions();
        for (TextureAtlas.AtlasRegion atlasRegion : atlasRegions) {
            if (!textureMap.containsValue(atlasRegion.name)) {
                // Not contained in texture map. Lets check spritesheets
                boolean spriteSheetContains = false;
                for (SpriteSheet spriteSheet : spritesheetMap.values()) {
                    if (spriteSheet.getAtlasIdentifier().equals(atlasRegion.name)) {
                        spriteSheetContains = true;
                        break;
                    }
                }

                if (!spriteSheetContains) {
                    // Not contained in spritesheet map either. Add to list
                    shouldQuit = true;
                    excessRegions.add(atlasRegion.name);
                }
            }
        }

        // Check for excess definitions / definitions that are not in the atlas
        // First check texturemap
        for (String atlasIdentifier : textureMap.values()) {
            TextureAtlas.AtlasRegion region = atlas.findRegion(atlasIdentifier);
            if (region == null) {
                // Texture does not exist in atlas. Quit and add to list to display to the log
                shouldQuit = true;
                excessDefinitions.add(atlasIdentifier);
            }
        }

        // now check spritesheetmap
        for (SpriteSheet spriteSheet : spritesheetMap.values()) {
            TextureAtlas.AtlasRegion region = atlas.findRegion(spriteSheet.getAtlasIdentifier());
            if (region == null) {
                // Texture does not exist in atlas. Quit and add to list to display to the log
                shouldQuit = true;
                excessDefinitions.add(spriteSheet.getAtlasIdentifier());
            }
        }

        // Log to the user
        if (shouldQuit) {
            if (excessRegions.size() > 0) {
                System.out.println("\tTextures that are not used in the game have been found in the resources/used_images folder:");
                for (String atlasIdentifier : excessRegions) {
                    System.out.println(String.format("\t\tresources/used_images/%s", atlasIdentifier));
                }
            }

            if (excessDefinitions.size() > 0) {
                System.out.println("\tTextures that are referenced by the game could not be found in the resources/used_images folder:");
                for (String atlasIdentifier : excessDefinitions) {
                    System.out.println(String.format("\t\tresources/used_images/%s", atlasIdentifier));
                }
            }

            System.out.println("\n\tWiki guide for texture manager: https://gitlab.com/uqdeco2800/2020-studio-6/2020-henry/-/wikis/New-Texture-Manager/New-Texture-Manager-&-HOWTO");

            System.exit(1);
        } else {
            System.out.println("\tAtlas and textureMap/spritesheetMap sizes conform.");
        }
    }
}
