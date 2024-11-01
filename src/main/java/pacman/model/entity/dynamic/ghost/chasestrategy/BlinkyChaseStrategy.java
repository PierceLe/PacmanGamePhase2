package pacman.model.entity.dynamic.ghost.chasestrategy;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;

/**
 * Chase strategy for Blinky to follow the player's position.
 */
public class BlinkyChaseStrategy implements ChaseStrategy {

    /**
     * Calculates the target position for Blinky to chase the player.
     *
     * @param ghost                the ghost using this strategy
     * @param playerKinematicState the kinematic state of the player
     * @return player's position as target location
     */
    @Override
    public Vector2D chasing(Ghost ghost, KinematicState playerKinematicState) {
        return playerKinematicState.getPosition();
    }
}