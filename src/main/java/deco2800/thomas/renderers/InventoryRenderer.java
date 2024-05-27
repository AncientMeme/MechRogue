package deco2800.thomas.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import deco2800.thomas.entities.PlayerPeon;
import deco2800.thomas.items.*;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.TextureManager;
import deco2800.thomas.util.SquareVector;
import deco2800.thomas.worlds.TestWorld;
import org.lwjgl.Sys;

/**
 * Inventory Renderer is  used to render the player's inventory on screen.
 */
public class InventoryRenderer implements Renderer {
    //The font used. At the moment uses the debug font.
    private BitmapFont font;
    private Skin skin;
    private OrthographicCamera camera = GameManager.get().getCamera();
    //The player's inventory to render
    private PlayerPeon player;

    private int num;

    private Table table;

    private Label stats;
    private WorldItems worldItems = GameManager.get().getWorld().worldItems;

    private boolean touched;
    public InventoryRenderer(PlayerPeon player){
        super();
        skin = GameManager.get().getSkin();
        this.player = player;


        num= player.getPlayerInventory().numOfItems();
        this.table = new Table();
        setTable(true);
        createLabel();
        (GameManager.get().getStage()).addActor(stats);
        (GameManager.get().getStage()).addActor(table);
    }

    @Override
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        //unused
    }

    /**
     * Renders each item in the inventory
     */
    private void renderItem(SpriteBatch batch, Camera camera, int line, String string) {
        font.draw(batch, string, camera.position.x - camera.viewportWidth / 2 + 1000,
                camera.position.y + camera.viewportHeight / 2 - line * 40 - 120);
    }

    public void setPlayer(PlayerPeon player){
        this.player = player;
    }

    /**
     * @author: Jianchuan Yu
     * Initialize the inventory table every time the inventory changed
     * @param changed whether the inventory has been changed(add or drop items)
     */
    public void setTable(boolean changed) {
        //if no changes on inventory, do nothing
        if (!changed){
            return;
        }
        //each time refresh inventory display, clear all children
        table.clearChildren();

        //Empty background image for container table
        Drawable background = new TextureRegionDrawable(GameManager.get().getManager(TextureManager.class)
                .getTexture("holder"));

        float imgHeight = background.getMinHeight();
        float imgWidth = background.getMinWidth();

        int i = 0;//Count up to 10, show only first 10 items
        for(UnrenderedItem item: player.getPlayerInventory()){
            if (i<10){
                if (item != null) {
                    Table innerTable = new Table();
                    ItemImg img = new ItemImg(item, GameManager.get().getManager(TextureManager.class)
                            .getTexture(item.getItemId()));
                    img.setBounds(img.getImageX(), img.getImageY(), imgWidth, imgHeight);
                    img.setZIndex(1);
                    img.setName(img.item.getItemName());
                    img.setItemIdx(i);
                    innerTable.add(img);
                    innerTable.background(new TextureRegionDrawable(GameManager.get().getManager(TextureManager.class)
                            .getTexture("invback")));
                    table.add(innerTable);
                }else {
                    addPlaceholder(this.table);
                }
                i++;
            }else {
                break;
            }
        }

        if (i < 10) {
            while (i < 10) {
                addPlaceholder(this.table);
                i++;
            }
        }

        //table attributes...
        table.background(background);
        table.setHeight(imgHeight/4);
        table.setWidth(imgWidth*10/4-20);
        table.left();
        table.setFillParent(false);
        table.layout();
        table.setTouchable(Touchable.childrenOnly);


        num = player.getPlayerInventory().numOfItems();//Update num to equal current number of items

        //Bind listeners to the children
        for(Actor t: table.getChildren()) {

            t.addListener(new ClickListener() {
                //The original background of the parent
                Drawable parentBack = ((Table) t).getBackground();

                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
                    ItemImg img = (ItemImg) event.getTarget();
                    if (img.item!=null){
                        if (img.item.getItemType() == "weapon") {
                            //System.out.println(((Weapon) img.item).toString());
                            stats.setText(((Weapon) img.item).toString());
                            stats.setPosition(t.getX() + 100, 470);
                            stats.setVisible(true);

                        }else {
                            //System.out.println(img.item.toString());
                            stats.setText( img.item.toString());
                            stats.setPosition(t.getX() + 100, 470);
                            stats.setVisible(true);
                        }

                    }
                    //Different background when hovered
                    ((Table)img.getParent()).setBackground(new TextureRegionDrawable(GameManager.get()
                            .getManager(TextureManager.class).getTexture("hovered")));
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor){
                    ItemImg img = (ItemImg) event.getTarget();
                    if (stats != null ){
                        stats.setVisible(false);
                    }
                    ((Table)img.getParent()).setBackground(parentBack);
                }
            });

            t.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                    if(x-event.getTarget().getX() <75 && ((ItemImg)event.getTarget()).item!=null){
                        touched = true;
                        return true;
                    }
                    return false;
                }

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer){
                    if(touched){
                        event.getTarget().setPosition(Gdx.input.getX()-t.getX()-251,
                                Math.abs(GameManager.get().getCamera().position.y/2)-Gdx.input.getY()-100);
                    }
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                    Table parent = (Table) t.getParent();
                    float parentTableX = parent.getX();
                    UnrenderedItem item = ((ItemImg)event.getTarget()).item;
                    touched=false;
                    if (item != null){

                        if(Gdx.input.getX()>parentTableX+parent.getWidth()
                            || Gdx.input.getX()< parentTableX
                            || Gdx.input.getY()>parent.getHeight()) {
                            player.getPlayerInventory().removeItem(((Table) t).getChild(0).getName());
                            dropItem(player,item);
                        }else {
                           event.getTarget().setPosition(0, 0);
                        }
                    }
                }
            });
        }
    }

    /**
     * Create and insert a placeholder table into the given table
     * @param table
     */
    private void addPlaceholder(Table table) {
        Table innerTable = new Table();
        ItemImg img = new ItemImg(null, GameManager.get().getManager(TextureManager.class)
                .getTexture("holder"));
        innerTable.add(img);
        innerTable.background(new TextureRegionDrawable(GameManager.get().getManager(TextureManager.class)
                .getTexture("invback")));

        table.add(innerTable);
    }

    /**
     * Add a dropped item to the world when
     * @param item the item to be dropped
     */
    public static void dropItem(PlayerPeon player,UnrenderedItem item) {
        DroppedItem dropItem = new DroppedItem((int)player.getRow(), (int)player.getCol(), 1, item);
        GameManager.get().getWorld().getWorldItems().addItem((int)player.getRow(),(int)player.getCol(),dropItem);
        GameManager.get().getWorld().addStaticEntity(dropItem);
    }

    /**
     * update item table's state and return table
     * @return item table with new state
     */
    public Table getTable() {

        if (player.getPlayerInventory().numOfItems()!=num){
            setTable(player.getPlayerInventory().numOfItems()!=num);
        }
        return this.table;
    }

    /**
     * @author: jason793259295@gmail.com
     * A image widget wrapping an unranderedItem
     */
    public static class ItemImg extends Image {
        public UnrenderedItem item;
        public int itemIdx;
        public ItemImg (UnrenderedItem item, Texture texture){
            super(texture);
            this.item = item;
        }
        public ItemImg (UnrenderedItem item, TextureRegion textureRegion){
            super(textureRegion);
            this.item = item;
        }
        public void setItemIdx(int idx) {
            itemIdx=idx;
        }

    }

    private void createLabel() {
        this.stats = new Label("Default",skin);
        this.stats.setSize(300,200);
        //stats.setPosition(t.getX() + 100,t.getY() + 470);
        this.stats.setPosition(100, 470);
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = new BitmapFont();
        style.background = new TextureRegionDrawable(GameManager.get()
                .getManager(TextureManager.class).getTexture("stats"));
        this.stats.setStyle(style);
        this.stats.setAlignment(Align.center);
        this.stats.setVisible(false);

    }


}
