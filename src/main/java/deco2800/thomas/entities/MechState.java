package deco2800.thomas.entities;

import java.io.Serializable;

/**
 * An enum to track what state the mech is currently in
 */
public enum MechState implements Serializable {
    MECH_ON, MECH_OFF, MECH_BROKE
}
