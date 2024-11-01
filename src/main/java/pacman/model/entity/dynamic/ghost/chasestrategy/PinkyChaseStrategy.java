package pacman.model.entity.dynamic.ghost.chasestrategy;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.maze.MazeCreator;

/**
 * Chase strategy for Pinky, targeting a position ahead of Pac-Man.
 */
public class PinkyChaseStrategy implements ChaseStrategy {
    private static final Integer GRID = 4;

    /**
     * Calculates Pinky's target position, ahead of Pac-Man in his current direction.
     *
     * @param ghost                the ghost using this strategy
     * @param playerKinematicState the kinematic state of the player
     * @return target position for Pinky
     */
    @Override
    public Vector2D chasing(Ghost ghost, KinematicState playerKinematicState) {
        Vector2D pacmanPosition = convertToTileCoordinates(playerKinematicState.getPosition());
        Direction playerDirection = playerKinematicState.getDirection();

        // Calculate position four tiles ahead of Pac-Man based on direction
        if (playerDirection == Direction.UP) {
            pacmanPosition = pacmanPosition.add(new Vector2D(0, -GRID));
        } else if (playerDirection == Direction.DOWN) {
            pacmanPosition = pacmanPosition.add(new Vector2D(0, GRID));
        } else if (playerDirection == Direction.LEFT) {
            pacmanPosition = pacmanPosition.add(new Vector2D(-GRID, 0));
        } else if (playerDirection == Direction.RIGHT) {
            pacmanPosition = pacmanPosition.add(new Vector2D(GRID, 0));
        }

        // Bound target position within maze limits
        int xBounded = Math.max(1, Math.min(26, (int) pacmanPosition.getX())) * MazeCreator.RESIZING_FACTOR;
        int yBounded = Math.max(4, Math.min(26, (int) pacmanPosition.getY())) * MazeCreator.RESIZING_FACTOR;

        return new Vector2D(xBounded, yBounded);
    }
}