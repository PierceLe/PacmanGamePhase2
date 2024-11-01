package pacman.model.entity.dynamic.ghost.chasestrategy;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.maze.MazeCreator;

/**
 * Defines the chase strategy for ghosts in Pac-Man.
 */
public interface ChaseStrategy {

    /**
     * Determines the target position for the ghost to chase the player.
     *
     * @param ghost                the ghost using this strategy
     * @param playerKinematicState the kinematic state of the player
     * @return target position for chasing
     */
    Vector2D chasing(Ghost ghost, KinematicState playerKinematicState);

    /**
     * Converts an absolute position to tile coordinates.
     *
     * @param position absolute position to convert
     * @return position in tile coordinates
     */
    default Vector2D convertToTileCoordinates(Vector2D position) {
        int xTile = (int) (position.getX() / MazeCreator.RESIZING_FACTOR);
        int yTile = (int) (position.getY() / MazeCreator.RESIZING_FACTOR);
        return new Vector2D(xTile, yTile);
    }
}