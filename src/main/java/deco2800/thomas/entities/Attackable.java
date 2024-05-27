package deco2800.thomas.entities;

/**
 * Interface that peons with the ability to attack will implement
 */
public interface Attackable {
    //once weapon class has been implemented, damage parameter can be removed
    void attack(Peon enemy, int damage);
}
