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


public interface GhostModeState {
    Image getImage();


    void handleCollision(Level level, Renderable renderable);


    void update();

    void collectPowerPellets();

    void resetCurrentStateAndTransist();

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

    default Direction selectDirection(Set<Direction> possibleDirections, Ghost ghost) {
        if (possibleDirections.isEmpty()) {
            return ghost.getCurrentDirection();
        }
        Direction currentDirection = ghost.getCurrentDirection();
        int currentDirectionCount = ghost.getCurrentDirectionCount();

        // ghosts have to continue in a direction for a minimum time before changing direction
        if (currentDirection != null && currentDirectionCount < minimumDirectionCount) {
            ghost.setCurrentDirectionCount(currentDirectionCount + 1);
            return currentDirection;
        }
        Map<Direction, Double> distances = new HashMap<>();
        KinematicState kinematicState = ghost.getKinematicState();
        for (Direction direction : possibleDirections) {
            // ghosts never choose to reverse travel
            if (currentDirection == null || direction != currentDirection.opposite()) {
                distances.put(direction, Vector2D.calculateEuclideanDistance(kinematicState.getPotentialPosition(direction), ghost.getTargetLocation()));
            }
        }

        // only go the opposite way if trapped
        if (distances.isEmpty()) {
            return currentDirection.opposite();
        }

        if (this instanceof NonFrightenedModeState) {
            return Collections.min(distances.entrySet(), Map.Entry.comparingByValue()).getKey();
        }
        List<Direction> directions = new ArrayList<>(distances.keySet());
        return directions.get(new Random().nextInt(directions.size()));
    }
}
