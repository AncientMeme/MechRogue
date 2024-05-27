package deco2800.thomas.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import deco2800.thomas.worlds.AbstractWorld;
import deco2800.thomas.worlds.Tile;

public class BFSPathfinder extends Pathfinder {

	@SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(BFSPathfinder.class);


	@Override
	public List<IntegerPoint> pathfind(AbstractWorld world, SquareVector origin, SquareVector destination) {
		IntegerPoint originRounded = new IntegerPoint(
				(int)Math.floor(origin.getCol()), (int)Math.floor(origin.getRow())
		);

		IntegerPoint destinationRounded = new IntegerPoint(
				(int)Math.floor(destination.getCol()), (int)Math.floor(destination.getRow())
		);

		// Out of bounds or is obstructed
		if (world.probeCollisionMapMutex(origin) != null) {
			return null;
		}

		return pathfindBFS(world, originRounded, destinationRounded);
	}

	/**
	 * Main algorithm used to pathfind. Uses Breadth first search to traverse the world's tiles
	 * @param world World to traverse over
	 * @param origin Starting point
	 * @param destination Ending point
	 * @return List of points to travel from origin to destination
	 */
	private List<IntegerPoint> pathfindBFS(AbstractWorld world, IntegerPoint origin, IntegerPoint destination) {
		LinkedList<IntegerPoint> queue = new LinkedList<IntegerPoint>();
		Set<IntegerPoint> closedSet = new HashSet<IntegerPoint>();
		Map<IntegerPoint, IntegerPoint> path = new HashMap<IntegerPoint, IntegerPoint>();
		queue.add(origin);

		IntegerPoint root;

		while (!queue.isEmpty()) {
			//log.info("Queue: {}    Closed: {}", queue.size(), closedSet.size());
			root = queue.remove();

			if (root.equals(destination)) {
				return reconstructPath(destination, path);
			}

			IntegerPoint[] directions = {
					new IntegerPoint(0, 1), new IntegerPoint(0, -1),
					new IntegerPoint(1, 0), new IntegerPoint(-1, 0)
			};
			for (IntegerPoint direction : directions) {
				IntegerPoint neighbourPoint = root.add(direction);
				// Check if already checked
				if (closedSet.contains(neighbourPoint) || queue.contains(neighbourPoint)) {
					continue;
				}

				// Check if obstructed (can do conditional collision here later)
				boolean obstructed = world.probeCollisionMapMutex(
						new SquareVector(
								(float)neighbourPoint.getX() + .5f, (float)neighbourPoint.getY() + .5f
						)
				) != null;
				if (obstructed) {
					continue;
				}

				// If a plausible tile, add to map
				path.put(neighbourPoint, root);
				queue.add(neighbourPoint);
			}

			closedSet.add(root);
		}
		return null;
	}

	/**
	 * Function used to trace back the path using the destination and pathmap
	 * @param destination destination point
	 * @param pathMap path map linking moves together
	 * @return list of integer points to travel to to reach destination
	 */
	private List<IntegerPoint> reconstructPath(IntegerPoint destination, Map<IntegerPoint, IntegerPoint> pathMap) {
		IntegerPoint root = destination;
		List<IntegerPoint> path = new ArrayList<IntegerPoint>();
		path.add(root);
		while(pathMap.get(root) != null) {
			root = pathMap.get(root);
			path.add(0, root);
		}
		return path;
	}
}