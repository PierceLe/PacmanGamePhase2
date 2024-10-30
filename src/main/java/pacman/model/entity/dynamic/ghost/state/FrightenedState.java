package pacman.model.entity.dynamic.ghost.state;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.level.Level;
import pacman.model.maze.Maze;

import java.util.*;

import static java.lang.Math.pow;
import static pacman.model.entity.dynamic.ghost.GhostImpl.minimumDirectionCount;

/**
 * Concrete class for FrightenedState
 */

public class FrightenedState implements GhostState{
    private final Ghost ghost;
    private static double DURATION = 0;
    private double duration;
    private static final int SCALING_POINT = 100;

    private final Image frightenedImage =  new Image("maze/ghosts/frightened.png");
    public FrightenedState(Ghost ghost){
        this.ghost = ghost;
    }
    @Override
    public Image getImage() {
        return frightenedImage;
    }
    @Override
    public void handleCollide(Level level, Renderable entity) {
        if (level.isPlayer(entity)) {
            ghost.reset();
            resetCurrentStateAndTransist();
            ghost.setFreezeCount(34);
            this.duration = DURATION;
            level.incrementGhostStreak();
            int streak = level.getStreakCount();
            int base = (int) pow(2, streak);
            System.out.println("Score: " + base * SCALING_POINT);
            level.incrementScore(base * SCALING_POINT);
        }
    }
    @Override
    public void update() {
        System.out.println(duration);
        if (this.duration <= 0){
            resetCurrentStateAndTransist();
            return;
        }
        this.updateDirection();
        ghost.getKinematicState().update();
        ghost.getBoundingBox().setTopLeft(ghost.getKinematicState().getPosition());
        this.duration -= 1;
    }

    @Override
    public void resetCurrentStateAndTransist() {
        this.duration = DURATION;
        ghost.setGhostMode(GhostMode.SCATTER);
        ghost.setState(GhostMode.SCATTER);

    }

    /**
     * Set the duration of the frightened state
     * @param duration, the duration of the frightened state
     */
    public void setDuration(double duration) {
        this.duration = duration;
        System.out.println(duration);
        DURATION = duration;
    }

    /**
     * Update the direction of the ghost, in frightened mode, ghost will randomly select the direction
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
     * Reset the tick count for the frightened state if pacman eats a power pellet
     */
    public void resetTickCount(){
        this.duration = DURATION;
    }

    /**
     * Select the direction for the ghost to travel in
     * @param possibleDirections, the possible directions the ghost can travel in
     * @return the direction for the ghost to travel in
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

        List<Direction> directions = new ArrayList<>(distances.keySet());

        // select the direction that will reach the target location fastest
        return directions.get(new Random().nextInt(directions.size()));
    }

}
