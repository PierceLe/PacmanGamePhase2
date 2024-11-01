package pacman.model.entity.dynamic.ghost;

import pacman.model.entity.dynamic.DynamicEntity;
import pacman.model.entity.dynamic.ghost.chasestrategy.ChaseStrategy;
import pacman.model.entity.dynamic.ghost.state.GhostModeState;
import pacman.model.entity.dynamic.ghost.state.StateRegistry;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.dynamic.player.observer.PlayerPositionObserver;

import java.util.Map;
import java.util.Set;

/**
 * Represents a Ghost entity in the Pac-Man game.
 */
public interface Ghost extends DynamicEntity, PlayerPositionObserver {

    /**
     * Sets speeds for each Ghost mode.
     *
     * @param speeds speeds for each Ghost mode
     */
    void setSpeeds(Map<GhostMode, Double> speeds);

    /**
     * Sets the current mode of the Ghost.
     *
     * @param ghostMode mode of the Ghost
     */
    void setGhostMode(GhostMode ghostMode);

    /**
     * Gets the chase strategy of the Ghost.
     *
     * @return chase strategy
     */
    ChaseStrategy getChaseStrategy();

    /**
     * Gets the target corner for the Ghost.
     *
     * @return target corner
     */
    Vector2D getTargetCorner();

    /**
     * Gets the current state of the Ghost.
     *
     * @return current state
     */
    GhostModeState getCurrentGhostState();

    /**
     * Sets the state based on Ghost mode.
     *
     * @param ghostMode mode of the Ghost
     */
    void setState(GhostMode ghostMode);

    /**
     * Gets the target location of the Ghost.
     *
     * @return target location
     */
    Vector2D getTargetLocation();

    /**
     * Sets the target location for the Ghost.
     *
     * @param targetLocation new target location
     */
    void setTargetLocation(Vector2D targetLocation);

    /**
     * Gets possible movement directions.
     *
     * @return set of possible directions
     */
    Set<Direction> getPossibleDirections();

    /**
     * Gets the current movement direction.
     *
     * @return current direction
     */
    Direction getCurrentDirection();

    /**
     * Sets the count of the current direction.
     *
     * @param currentDirectionCount count of current direction
     */
    void setCurrentDirectionCount(int currentDirectionCount);

    /**
     * Gets the count of the current direction.
     *
     * @return count of current direction
     */
    int getCurrentDirectionCount();

    /**
     * Sets the current movement direction.
     *
     * @param currentDirection new direction
     */
    void setCurrentDirection(Direction currentDirection);

    /**
     * Gets the kinematic state of the Ghost.
     *
     * @return kinematic state
     */
    KinematicState getKinematicState();

    /**
     * Resets the Ghost to its respawn position.
     */
    void playingRespawn();

    /**
     * Gets the state registry for the Ghost.
     *
     * @return state registry
     */
    StateRegistry getGhostStateRegistry();
}