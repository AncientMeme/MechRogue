package deco2800.thomas.entities;

import com.badlogic.gdx.graphics.Color;
import deco2800.thomas.GameScreen;
import deco2800.thomas.Tickable;
import deco2800.thomas.entities.buffs.*;
import deco2800.thomas.entities.enemies.EnemyPeon;
import deco2800.thomas.items.Inventory;
import deco2800.thomas.items.UnrenderedItem;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.TaskPool;
import deco2800.thomas.managers.TerrainManager;
import deco2800.thomas.tasks.AbstractTask;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Peon extends AgentEntity implements Tickable, HasHealth, Attackable {
	private transient AbstractTask task;

	static final Logger LOGGER = Logger.getLogger(Peon.class.getPackage().getName());

	private int health;
	private int maxHealth;
	private Inventory playerInventory;

	/* List of active buffs on this peon */
	private List<Buff> buffs;

	/* List of damage indicators being displayed */
	private List<DamageIndicator> indicators;

	public Peon() {
		super();
		this.setTexture("spacman_ded");
		this.setObjectName("Peon");
		this.setHeight(1);
		this.speed = 0.05f;
		this.playerInventory = new Inventory();
		this.buffs = new ArrayList<>();
		this.indicators = new ArrayList<>();
	}

	/**
	 * Peon constructor
     */
	public Peon(float row, float col, float speed, int health) {
		super(row, col, RenderConstants.PEON_RENDER, speed);
		this.setTexture("spacman_ded");
		this.playerInventory = new Inventory();

		// Setup the health system
		if (health < 0) {
			throw new IllegalArgumentException();
		}
		this.maxHealth = health;
		this.health = maxHealth;
		this.buffs = new ArrayList<>();
		this.indicators = new ArrayList<>();
	}

	/**
	 * Returns the list of active buffs on this Peon
	 * @return active buffs on this Peon
	 */
	public List<Buff> getBuffs() {
		return buffs;
	}

	/**
	 * Adds a damage indicator to this Peon
	 * @param indicator the damage indicator to add
	 */
	public void addIndicator(DamageIndicator indicator) {
		indicators.add(indicator);
	}

	public void tickIndicators() {
		for (DamageIndicator indicator : indicators) {
			indicator.onTick();
			if (!indicator.isAlive()) {
				indicator.getLabel().setVisible(false);
			}
		}
		// cull indicators afterwards to not get a ConcurrentModificationException
		indicators.removeIf(indicator -> !indicator.isAlive());
	}

	/**
	 * Returns non-modifiable list of indicators on this Peon
	 * @return a list of active DamageIndicator's
	 */
	public List<DamageIndicator> getDamageIndicators() {
		return indicators;
	}

	/**
	 * Adds a buff to this Peon
	 * @param buff the buff to add
	 */
	public void addBuff(Buff buff) {
		boolean alreadyExists = false;
		for (Buff activeBuff : buffs) {
			if (buff.getClass() == activeBuff.getClass()) {
				alreadyExists = true;
				// Only reset lifetime if new lifetime is greater
				if (buff.getLifetime() > activeBuff.getLifetime()) {
					activeBuff.setLifetime(buff.getLifetime());
					activeBuff.setInitialLifetime(buff.getLifetime());
				}
			}
		}
		if (!alreadyExists) {
			if (buff.getClass() == UnstoppableBuff.class) {
				UnstoppableBuff uBuff = (UnstoppableBuff) buff;
				float lifetime = uBuff.getLifetime();
				float percent = uBuff.getPercentageIncrease();
				int hps = uBuff.getHps();
				buffs.add(new RegenBuff(lifetime, hps));
				buffs.add(new StrengthBuff(lifetime, percent));
				buffs.add(new SpeedBuff(lifetime, percent));
			} else {
				buffs.add(buff);
			}
			buff.setTarget(this);
			// Set buff peon ground truth speed
			if (buff instanceof FreezeBuff) {
				FreezeBuff freezeBuff = (FreezeBuff)buff;
				freezeBuff.setActualPeonSpeed(speed);
				attachParticle("resources/particle_files/freeze.party");
			}
			if (buff instanceof SpeedBuff) {
				SpeedBuff speedBuff = (SpeedBuff)buff;
				speedBuff.setActualPeonSpeed(speed);
			}
		}
	}

 	@Override
	public void onTick(long i) {
		if(task != null && task.isAlive()) {
			if(task.isComplete()) {
				this.task = GameManager.getManagerFromInstance(TaskPool.class).getTask(this);
			}
			task.onTick(i);
		} else {
			task = GameManager.getManagerFromInstance(TaskPool.class).getTask(this);
		}
		tickBuffs();
		tickIndicators();
	}

	/**
	 * Ticks all buffs
	 */
	public void tickBuffs() {
		// Handles timing of buff effects
		for (Buff buff : buffs) {
			buff.onTick();
		}
		// Check if any buffs are dead and remove if so
		for (Buff buff : buffs) {
			if (!buff.isAlive()) {
				buff.die();
				removeAttachedParticle();
			}
		}
		// cull buffs afterwards to not get a ConcurrentModificationException
		buffs.removeIf(buff -> !buff.isAlive());
	}
	
	public int getHealth() {
		return health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	@Override
	public double getProportionalHealth() {
		double hp = getHealth();
		double maxHp = getMaxHealth();
		double proportion = hp / maxHp;

		return Math.round(proportion * 100.0) / 100.0;
	}

	/**
	 * Set the peon's health to the value entered. If the value exceeds max
	 * health, then health will equal to max health. If the value is lower than
	 * 0, then health will be set to 0.
	 *
	 * @param health Set the peon's health to the input value
	 * */
	public void setHealth(int health) {
		if(health > maxHealth) {
			this.health = maxHealth;
		} else if (health <= 0) {
			this.health = 0;
			this.handleDeath();
		} else {
			this.health = health;
		}
	}

	public void setMaxHealth(int maxHealth) {
		// Lower the health if max health is lesser than current health
		if(maxHealth < 0) {
			this.maxHealth = 0;
			this.handleDeath();
		} else {
			this.maxHealth = maxHealth;
		}

		// Ensure the health is always lesser or equal to max health
		if(health > maxHealth) {
			this.health = maxHealth;
		}
	}

	public void addHealth(int amount) {
		addIndicator(new DamageIndicator(amount, Color.RED));
		health += amount;

		// Check if health value is out of bounds
		if(health > maxHealth) {
			this.health = maxHealth;
		} else if (health <= 0) {
			this.health = 0;
			this.handleDeath();
		}
	}

	public void addHealth(int amount, DamageIndicator indicator) {
		addIndicator(indicator);
		health += amount;

		// Check if health value is out of bounds
		if(health > maxHealth) {
			this.health = maxHealth;
		} else if (health <= 0) {
			this.health = 0;
			this.handleDeath();
		}
	}

	// Basic handler that should be overridden
	public void handleDeath() {
		//unused
	}

	/**
	 * Initiates an attack on an enemy. Enemy health decreases by weapon damage
	 * @param enemy enemy being attacked
	 * @param damage the damage inflicted by the weapon
	 * @requires damage >= 0
	 */
	public void attack(Peon enemy, int damage) {
		// Does not attack if the current peon is blinded
		for (Buff buff : buffs) {
			if (buff instanceof  StrengthBuff ) {
				StrengthBuff sBuff = (StrengthBuff) buff;
				damage*=1+sBuff.getStrength();
			}
			if (buff instanceof BlindBuff) {
				System.out.println(this.getEntityID() + " is blinded");
				return;
			} else {
				buff.affectPeon();
			}
		}
		//If enemy is the player, adjust the damage by the player's defense stat
		if (enemy instanceof PlayerPeon && ((PlayerPeon) enemy).getDefense() != 0) {
			enemy.addHealth(damage * (((PlayerPeon) enemy).getOverallDefense()/10) * -1);
		} else {
			enemy.addHealth(damage * -1);
		}
		LOGGER.log(Level.INFO, "{0} was hit", new Object[]{enemy.getEntityID()});
		//add exp when kill enemy
		if(enemy.getHealth() <= 0) {
			//kill enemy
			if (enemy instanceof EnemyPeon && this instanceof PlayerPeon) {
				((PlayerPeon) this).addExp(((EnemyPeon) enemy).getExperience());
			}
		}
	}

	public void interact(String greeting){
		System.out.println(greeting);
	}

	protected void setTask(AbstractTask task) {
		this.task = task;
	}
	
	public AbstractTask getTask() {
		return task;
	}

	/**
	 * Returns the player's inventory
	 */
	public Inventory getPlayerInventory(){
		return playerInventory;
	}

	/**
	 * Adds item to the player's inventory
	 * @param item Item to be added
	 */
	public void addToPlayerInventory(UnrenderedItem item){
		playerInventory.addItem(item);
	}
	@Override
	public void setSpeed(float speed) {
		this.speed = speed;

		// Update buffs ground truth for peons speed
		for (Buff activeBuff : buffs) {
			if (activeBuff instanceof FreezeBuff) {
				FreezeBuff freezeBuff = (FreezeBuff)activeBuff;
				freezeBuff.setActualPeonSpeed(speed);
			}
			if (activeBuff instanceof SpeedBuff) {
				SpeedBuff speedBuff = (SpeedBuff)activeBuff;
				speedBuff.setActualPeonSpeed(speed);
			}
 		}
	}

	/**
	 * variant of set speed that does not update buffs speed ground truth.
	 * Used by buffs to quietly alter speed of peon
	 * @param speed new speed
	 */
	public void setSpeedByBuff(float speed) {
		this.speed = speed;
	}
}
