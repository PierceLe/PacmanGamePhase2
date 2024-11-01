package pacman.model.entity.dynamic.ghost.state;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.level.Level;
import pacman.model.maze.Maze;

import java.util.*;

import static pacman.model.entity.dynamic.ghost.GhostImpl.minimumDirectionCount;

/**
 * Represents the state of a ghost, defining its behavior and actions in Frightened mode comparing to another mode (SCATTER).
 */
public interface GhostModeState {

    /**
     * Gets the current image representing the ghost's mode.
     *
     * @return ghost image
     */
    Image getImage();

    /**
     * Handles collision events with other objects in the level.
     *
     * @param level      the current game level
     * @param renderable the object the ghost collides with
     */
    void handleCollision(Level level, Renderable renderable);

    /**
     * Updates the state of the ghost.
     */
    void update();

    /**
     * Collects power pellets, typically to modify ghost behavior.
     */
    void collectPowerPellets();

    /**
     * Updates the direction of the ghost based on its current location and target.
     *
     * @param ghost the ghost whose direction is being updated
     */
    default void updateDirection(Ghost ghost) {
        // Ghosts update their target location when they reach an intersection
        if (Maze.isAtIntersection(ghost.getPossibleDirections())) {
            ghost.setTargetLocation(ghost.getTargetLocation());
        }

        Direction newDirection = selectDirection(ghost.getPossibleDirections(), ghost);

        // Ghosts have to continue in a direction for a minimum time before changing direction
        if (ghost.getCurrentDirection() != newDirection) {
            ghost.setCurrentDirectionCount(0);
        }
        ghost.setCurrentDirection(newDirection);
        KinematicState kinematicState = ghost.getKinematicState();

        switch (ghost.getCurrentDirection()) {
            case LEFT -> kinematicState.left();
            case RIGHT -> kinematicState.right();
            case UP -> kinematicState.up();
            case DOWN -> kinematicState.down();
        }
    }

    /**
     * Selects the best direction for the ghost to move based on its possible directions
     * and current target.
     *
     * @param possibleDirections the set of possible movement directions
     * @param ghost              the ghost whose direction is being selected
     * @return the chosen direction for the ghost
     */
    default Direction selectDirection(Set<Direction> possibleDirections, Ghost ghost) {
        if (possibleDirections.isEmpty()) {
            return ghost.getCurrentDirection();
        }
        Direction currentDirection = ghost.getCurrentDirection();
        int currentDirectionCount = ghost.getCurrentDirectionCount();

        // Ghosts must continue in a direction for a minimum time before changing direction
        if (currentDirection != null && currentDirectionCount < minimumDirectionCount) {
            ghost.setCurrentDirectionCount(currentDirectionCount + 1);
            return currentDirection;
        }
        Map<Direction, Double> distances = new HashMap<>();
        KinematicState kinematicState = ghost.getKinematicState();
        for (Direction direction : possibleDirections) {
            // Ghosts never choose to reverse travel
            if (currentDirection == null || direction != currentDirection.opposite()) {
                distances.put(direction, Vector2D.calculateEuclideanDistance(
                        kinematicState.getPotentialPosition(direction), ghost.getTargetLocation()));
            }
        }

        // Only go the opposite way if trapped
        if (distances.isEmpty()) {
            return currentDirection.opposite();
        }

        // Non-frightened mode aims for the closest target, while frightened mode is random
        if (this instanceof NonFrightenedModeState) {
            return Collections.min(distances.entrySet(), Map.Entry.comparingByValue()).getKey();
        }
        List<Direction> directions = new ArrayList<>(distances.keySet());
        return directions.get(new Random().nextInt(directions.size()));
    }
}