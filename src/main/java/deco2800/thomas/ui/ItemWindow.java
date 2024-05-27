package deco2800.thomas.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import deco2800.thomas.entities.PlayerPeon;
import deco2800.thomas.items.UnrenderedItem;
import deco2800.thomas.items.Weapon;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.TextureManager;
import deco2800.thomas.renderers.InventoryRenderer;

import deco2800.thomas.renderers.InventoryRenderer.ItemImg;




public class ItemWindow {
    Label[] labels;
    private int num;
    //public Table itemWin;
    private ItemView itemView;
    private int itemRowCount;

    private static boolean windowOpen = false;
    private PlayerPeon player;

    public ItemWindow(PlayerPeon player) {
        this.player = player;
        Stage stage = GameManager.get().getStage();
        Skin skin = GameManager.get().getSkin();
        itemView = new ItemView();
        num = player.getPlayerInventory().numOfItems();
        setTable(true);

        stage.addActor(itemView);
        dispose();
        //stage.setDebugAll(true);
    }

    public void setTable(boolean changed){
        if (!changed){
            return;
        }

        itemView.clearChildren();
        Image background = new Image(GameManager.get()
                .getManager(TextureManager.class)
                .getTexture("item_window"));

        itemView.background(background.getDrawable());

        itemView.left();
        itemView.setFillParent(false);
        itemView.layout();
        itemView.setPosition(0, 0);

        itemView.setWidth(481);
        itemView.setHeight(361);
        itemView.pad(20);
        int itemCount = 0;
        itemRowCount=0;
        for(UnrenderedItem item : player.getPlayerInventory()){
            if (item != null) {
                Table innerTable = new Table();
                ItemImg img = new ItemImg(item, GameManager.get().getManager(TextureManager.class)
                        .getTexture(item.getItemId()));
                //img.setBounds(img.getImageX(), img.getImageY(), imgWidth, imgHeight);
                img.setZIndex(1);
                img.setName(img.item.getItemName());

                innerTable.add(img);

                innerTable.background(new TextureRegionDrawable(GameManager.get().getManager(TextureManager.class)
                        .getTexture("holder")));
                innerTable.padRight(5);

                itemView.add(innerTable);
                itemCount++;
                itemRowCount++;
                if (itemRowCount>5){
                    itemRowCount=1;
                }
            }
        }
        while (itemCount<=19) {
            addPlaceholder(itemView);
            itemCount++;
            itemRowCount++;
            if (itemRowCount>5){
                itemRowCount=1;
            }
        }

        itemView.validate();
        num = player.getPlayerInventory().numOfItems();//Update num to equal current number of items


    }

    public Table getTable() {

        if (player.getPlayerInventory().numOfItems()!=num){
            setTable(player.getPlayerInventory().numOfItems()!=num);
        }
        return itemView;
    }



    public void toggleWindowOpen() {
        windowOpen = !windowOpen ;
        if (windowOpen) {
            create();
        } else {
            dispose();
        }
    }

    public void create() {

        //for (Button button : buttons) {
            //button.setVisible(true);
        //}

        itemView.setVisible(true);

    }



    public void dispose() {
        //if (labels == null || buttons == null || background == null) {
            //return;
        //}
        //for (Button button : buttons) {
            //button.setVisible(false);
        //}

        itemView.setVisible(false);
    }

    private void addPlaceholder(Table table) {
        Table innerTable = new Table();
        ItemImg img = new ItemImg(null, GameManager.get().getManager(TextureManager.class)
                .getTexture("holder"));
        innerTable.add(img);
        innerTable.background(new TextureRegionDrawable(GameManager.get().getManager(TextureManager.class)
                .getTexture("holder")));

        table.add(innerTable);
    }

    public class ItemView extends Table{
        public ItemView(){
            super();
        }

        @Override
        public <T extends Actor> Cell<T> add (T actor) {
            float oldWith = getWidth();
            Cell<T> cell = super.add(actor);
            if(itemRowCount>=5) {
                removeActor(actor);
                row();
                cell = super.add(actor);
            }
            return cell;
        }
    }

}
