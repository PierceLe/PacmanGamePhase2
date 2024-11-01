package pacman.model.entity.dynamic.ghost.chasestrategy;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.maze.MazeCreator;

/**
 * Chase strategy for Clyde with distance-based behavior.
 */
public class ClydeChaseStrategy implements ChaseStrategy {
    private static final Integer GRID_DISTANCE = 8;

    /**
     * Calculates the target position for Clyde based on distance to the player.
     * Clyde chases the player if far, otherwise heads to his corner.
     *
     * @param ghost                the ghost using this strategy
     * @param playerKinematicState the kinematic state of the player
     * @return target position based on distance
     */
    @Override
    public Vector2D chasing(Ghost ghost, KinematicState playerKinematicState) {
        Vector2D ghostPosition = convertToTileCoordinates(ghost.getCenter());
        Vector2D pacmanPosition = convertToTileCoordinates(playerKinematicState.getPosition());

        double distanceToPacman = Vector2D.calculateEuclideanDistance(ghostPosition, pacmanPosition);

        if (distanceToPacman > GRID_DISTANCE) {
            return pacmanPosition;
        } else {
            return ghost.getTargetCorner();
        }
    }
}