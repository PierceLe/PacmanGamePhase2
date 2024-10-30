package pacman.model.entity.dynamic.ghost.state;


import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.level.Level;
import pacman.model.maze.Maze;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static pacman.model.entity.dynamic.ghost.GhostImpl.minimumDirectionCount;


public class RegularState implements GhostState {
    private final Ghost ghost;

    public RegularState(Ghost ghost){
        this.ghost = ghost;
    }
    @Override
    public Image getImage() {
        return ghost.getImage();
    }

    @Override
    public void handleCollide(Level level, Renderable entity) {
        if (level.isPlayer(entity)) {
            level.handleLoseLife();
        }
    }
    @Override
    public void update() {
        this.updateDirection();
        ghost.getKinematicState().update();
        ghost.getBoundingBox().setTopLeft(ghost.getKinematicState().getPosition());
    }
    @Override
    public void resetCurrentStateAndTransist(){
        ghost.setState(GhostMode.FRIGHTENED);
        ghost.setGhostMode(GhostMode.FRIGHTENED);
    }

    /**
     * Update the direction for corresponding state, in regular state, ghost will behave default
     */
    private void updateDirection() {
        // Ghosts update their target location when they reach an intersection
        if (Maze.isAtIntersection(ghost.getPossibleDirections())) {
            ghost.setTargetLocation(ghost.getTargetLocation());
        }

        Direction newDirection = selectDirection(ghost.getPossibleDirections());

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
     * Select the direction for the ghost to move in
     * @param possibleDirections, all possible directions for the ghost to move in
     * @return a direction for the ghost to move in
     */
    private Direction selectDirection(Set<Direction> possibleDirections) {
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

        // select the direction that will reach the target location fastest
        return Collections.min(distances.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

}
