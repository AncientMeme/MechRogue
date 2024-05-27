package deco2800.thomas.entities.enemies.desert;

import deco2800.thomas.entities.Peon;
import deco2800.thomas.entities.Projectile;
import deco2800.thomas.entities.Rock;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.NetworkManager;
import deco2800.thomas.util.SquareVector;
import deco2800.thomas.worlds.Tile;

import deco2800.thomas.worlds.AbstractWorld;

public class WebProjectile extends Projectile {

    Peon enemy;
    /**
     * A Web projectile that spawns a web when it dies.
     *
     * @param row
     * @param col
     * @param direction the direction for the projectile to travel in radians
     * @param speed     the speed of the projectile per second
     * @param damage    the damage of the projectile
     * @param lifetime  the lifetime of the projectile
     * @param enemy     the enemy to check against for collision detection (null means no enemy collision)
     */
    public WebProjectile(float row, float col, float direction, float speed, int damage, float lifetime, Peon enemy) {
        super(row, col, direction, speed, damage, lifetime, enemy);
        this.enemy = enemy;
        this.setTexture("web_projectile");
    }

    @Override
    public void dispose() {
        Web p = new Web(this.getCol(), this.getRow(),
                Projectile.getDirectionTo(this, this.enemy), 0f, 0, 5f, this.enemy);
        p.setTexture("web");
        GameManager.get().getWorld().addEntity(p);
        GameManager.get().getManager(NetworkManager.class).deleteEntity(this);
        GameManager.get().getWorld().deleteEntity(this);
    }
}
