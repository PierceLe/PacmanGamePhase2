package pacman.model.entity.dynamic.ghost.chasestrategy;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.maze.MazeCreator;

/**
 * Chase strategy for Inky, using Pac-Man's position and Blinky's position.
 */
public class InkyChaseStrategy implements ChaseStrategy {
    private static final int OFFSET = 2;
    private Ghost blinkyGhost;

    /**
     * Calculates Inky's target position based on Pac-Man's direction and Blinky's position.
     *
     * @param ghost                the ghost using this strategy
     * @param playerKinematicState the kinematic state of the player
     * @return target position for Inky
     */
    @Override
    public Vector2D chasing(Ghost ghost, KinematicState playerKinematicState) {
        Vector2D pacmanPosition = convertToTileCoordinates(playerKinematicState.getPosition());
        Direction playerDirection = playerKinematicState.getDirection();

        // Calculate target two tiles ahead of Pac-Man based on direction
        Vector2D targetPosition;
        if (playerDirection == Direction.UP) {
            targetPosition = pacmanPosition.add(new Vector2D(0, -OFFSET));
        } else if (playerDirection == Direction.DOWN) {
            targetPosition = pacmanPosition.add(new Vector2D(0, OFFSET));
        } else if (playerDirection == Direction.LEFT) {
            targetPosition = pacmanPosition.add(new Vector2D(-OFFSET, 0));
        } else {
            targetPosition = pacmanPosition.add(new Vector2D(OFFSET, 0));
        }

        Vector2D blinkyPosition = convertToTileCoordinates(blinkyGhost.getCenter());
        Vector2D blinkyToTarget = new Vector2D(
                targetPosition.getX() - blinkyPosition.getX(),
                targetPosition.getY() - blinkyPosition.getY()
        );

        // Calculate Inky's final target by extending Blinky's vector
        Vector2D inkyTargetPosition = new Vector2D(
                blinkyPosition.getX() + blinkyToTarget.getX() * 2,
                blinkyPosition.getY() + blinkyToTarget.getY() * 2
        );

        // Bound target position within maze limits
        int xBounded = Math.max(1, Math.min(26, (int) inkyTargetPosition.getX())) * MazeCreator.RESIZING_FACTOR;
        int yBounded = Math.max(4, Math.min(32, (int) inkyTargetPosition.getY())) * MazeCreator.RESIZING_FACTOR;

        return new Vector2D(xBounded, yBounded);
    }

    /**
     * Sets Blinky's ghost reference for Inky's strategy calculations.
     *
     * @param ghost Blinky's ghost
     */
    public void setBlinkyGhost(Ghost ghost) {
        blinkyGhost = ghost;
    }
}