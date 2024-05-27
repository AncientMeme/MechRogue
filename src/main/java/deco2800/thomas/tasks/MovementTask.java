package deco2800.thomas.tasks;

import java.util.List;

import deco2800.thomas.entities.AgentEntity;
import deco2800.thomas.managers.GameManager;
import deco2800.thomas.managers.PathFindingService;
import deco2800.thomas.renderers.PauseMenuRenderer;
import deco2800.thomas.util.IntegerPoint;
import deco2800.thomas.util.SquareVector;
import deco2800.thomas.worlds.Tile;

public class MovementTask extends AbstractTask{
	
	private boolean complete;
	private int moveCounter;
	
	private boolean computingPath = false;
	private boolean taskAlive = true;
	
	AgentEntity entity;
	SquareVector destination;
	
	private List<IntegerPoint> path;

	public MovementTask(AgentEntity entity, SquareVector destination) {
		super(entity);
		
		this.entity = entity;
		this.destination = destination;
		this.complete = false;    //path == null || path.isEmpty();
		this.moveCounter = 0;
	}

	@Override
	public void onTick(long tick) {
		if (PauseMenuRenderer.isPaused()) {
			return;
		}
		
		if(path != null) {
			// We have a path.
			if(path.isEmpty()) {
				complete = true;
			} else {
				entity.moveTowards(new SquareVector(path.get(0)));
				// This is a bit of a hack.
				if(entity.getPosition().isCloseEnoughToBeTheSame(new SquareVector(path.get(0)))) {
					path.remove(0);
					moveCounter++;
				}
			}
		} else if (computingPath) {
			// Change sprite to show waiting??

		} else if (taskAlive) {
			// Ask for a path.
			computingPath = true;
			GameManager.get().getManager(PathFindingService.class).addPath(this);
		}
	}

	@Override
	public boolean isComplete() {
		return complete;
	}

	public void setPath(List<IntegerPoint> path) {
		if (path == null) {
			taskAlive = false;
		}
		this.path = path;
		computingPath = false;
	}
	
	public List<IntegerPoint> getPath() {
		return path;
	}

	public int getMoveCounter() {
		return moveCounter;
	}

	@Override
	public boolean isAlive() {
		return taskAlive;
	}

}
