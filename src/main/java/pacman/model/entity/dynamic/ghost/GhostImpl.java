package pacman.model.entity.dynamic.ghost;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.chasestrategy.ChaseStrategy;
import pacman.model.entity.dynamic.ghost.state.FrightenedModeState;
import pacman.model.entity.dynamic.ghost.state.GhostModeState;
import pacman.model.entity.dynamic.ghost.state.StateRegistry;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.level.Level;

import java.util.*;

/**
 * Concrete implementation of Ghost entity in Pac-Man Game
 */
public class GhostImpl implements Ghost {

    public static final int minimumDirectionCount = 8;
    private static final int RESPAWN_TIME = 30;
    private final Layer layer = Layer.FOREGROUND;
    private final Image image;
    private final BoundingBox boundingBox;
    private final Vector2D startingPosition;
    private final Vector2D targetCorner;
    private KinematicState kinematicState;
    private GhostMode ghostMode;
    private Vector2D targetLocation;
    private Vector2D playerPosition;
    private Direction currentDirection;
    private Set<Direction> possibleDirections;
    private Map<GhostMode, Double> speeds;
    private int currentDirectionCount = 0;
    private final ChaseStrategy chaseStrategy;
    private int respawning = 0;
    protected GhostMode currentGhostState;
    private final StateRegistry ghostStatesRegistry;




    public GhostImpl(Image image, BoundingBox boundingBox, KinematicState kinematicState, GhostMode ghostMode, Vector2D targetCorner, ChaseStrategy chaseStrategy) {
        this.image = image;
        this.boundingBox = boundingBox;
        this.kinematicState = kinematicState;
        this.startingPosition = kinematicState.getPosition();
        this.ghostMode = ghostMode;
        this.possibleDirections = new HashSet<>();
        this.targetCorner = targetCorner;
        this.targetLocation = getTargetLocation();
        this.currentDirection = null;
        this.chaseStrategy = chaseStrategy;
        currentGhostState = GhostMode.SCATTER;
        this.ghostStatesRegistry = new StateRegistry(this);

    }

    @Override
    public void setSpeeds(Map<GhostMode, Double> speeds) {
        this.speeds = speeds;
    }

    @Override
    // State pattern
    public Image getImage() {
        if (ghostStatesRegistry.getGhostState(currentGhostState) instanceof FrightenedModeState) {
            return ghostStatesRegistry.getGhostState(currentGhostState).getImage();
        }
        return image;
    }

    @Override
    public void update() {
        if (respawning > 0) {
            respawning = respawning - 1;
            return;
        }
        ghostStatesRegistry.getGhostState(currentGhostState).update();
    }

    @Override
    public void playingRespawn() {
        respawning = RESPAWN_TIME;
    }

    @Override
    public StateRegistry getGhostStateRegistry() {
        return ghostStatesRegistry;
    }

    @Override
    public Vector2D getTargetLocation() {
        return switch (this.ghostMode) {
            case CHASE -> this.playerPosition;
            case SCATTER -> this.targetCorner;
            case FRIGHTENED -> new Vector2D(5, 5);
        };
    }

    @Override
    public void setTargetLocation(Vector2D targetLocation) {
        this.targetLocation = targetLocation;
    }

    @Override
    public Set<Direction> getPossibleDirections() {
        return possibleDirections;
    }

    @Override
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    @Override
    public void setCurrentDirectionCount(int currentDirectionCount) {
        this.currentDirectionCount = currentDirectionCount;
    }

    @Override
    public int getCurrentDirectionCount() {
        return currentDirectionCount;
    }

    @Override
    public void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }

    @Override
    public KinematicState getKinematicState() {
        return this.kinematicState;
    }


    @Override
    public void setGhostMode(GhostMode ghostMode) {
        this.ghostMode = ghostMode;
        this.kinematicState.setSpeed(speeds.get(ghostMode));
        // ensure direction is switched
        this.currentDirectionCount = minimumDirectionCount;
    }

    @Override
    public Vector2D getTargetCorner() {
        return targetCorner;
    }

    @Override
    public GhostModeState getCurrentGhostState() {
        return ghostStatesRegistry.getGhostState(currentGhostState);
    }

    @Override
    public void setState(GhostMode ghostMode) {
        currentGhostState = ghostMode;
    }

    @Override
    public ChaseStrategy getChaseStrategy() {
        return chaseStrategy;
    }

    @Override
    public boolean collidesWith(Renderable renderable) {
        return boundingBox.collidesWith(kinematicState.getSpeed(), kinematicState.getDirection(), renderable.getBoundingBox());
    }

    @Override
    public void collideWith(Level level, Renderable renderable) {
        ghostStatesRegistry.getGhostState(currentGhostState).handleCollision(level, renderable);
    }

    @Override
    public void update(KinematicState playerKinematicSate) {
        this.playerPosition = chaseStrategy.chasing(this, playerKinematicSate);
    }

    @Override
    public Vector2D getPositionBeforeLastUpdate() {
        return this.kinematicState.getPreviousPosition();
    }

    @Override
    public double getHeight() {
        return this.boundingBox.getHeight();
    }

    @Override
    public double getWidth() {
        return this.boundingBox.getWidth();
    }

    @Override
    public Vector2D getPosition() {
        return this.kinematicState.getPosition();
    }

    @Override
    public void setPosition(Vector2D position) {
        this.kinematicState.setPosition(position);
    }

    @Override
    public Layer getLayer() {
        return this.layer;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public void reset() {
        // return ghost to starting position
        this.kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(startingPosition)
                .build();
        this.boundingBox.setTopLeft(startingPosition);
        this.ghostMode = GhostMode.SCATTER;
        this.currentDirectionCount = minimumDirectionCount;
    }

    @Override
    public void setPossibleDirections(Set<Direction> possibleDirections) {
        this.possibleDirections = possibleDirections;
    }

    @Override
    public Direction getDirection() {
        return this.kinematicState.getDirection();
    }

    @Override
    public Vector2D getCenter() {
        return new Vector2D(boundingBox.getMiddleX(), boundingBox.getMiddleY());
    }
}
