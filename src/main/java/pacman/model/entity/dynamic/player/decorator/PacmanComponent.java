package pacman.model.entity.dynamic.player.decorator;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.Renderable.Layer;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.dynamic.player.observer.PlayerPositionObserver;
import pacman.model.level.Level;

import java.util.Set;

/**
 * Interface for Pac-Man components, defining core functionality and behaviors.
 */
public interface PacmanComponent {

    /**
     * Gets the current image representing Pac-Man.
     *
     * @return current Pac-Man image
     */
    Image getImage();

    /**
     * Gets the current position of Pac-Man.
     *
     * @return current position
     */
    Vector2D getPosition();

    /**
     * Sets the position of Pac-Man.
     *
     * @param position new position
     */
    void setPosition(Vector2D position);

    /**
     * Gets the position before the last update.
     *
     * @return previous position
     */
    Vector2D getPositionBeforeLastUpdate();

    /**
     * Updates Pac-Man's state.
     */
    void update();

    /**
     * Sets the speed of Pac-Man.
     *
     * @param speed new speed
     */
    void setSpeed(double speed);

    /**
     * Moves Pac-Man up.
     */
    void up();

    /**
     * Moves Pac-Man down.
     */
    void down();

    /**
     * Moves Pac-Man left.
     */
    void left();

    /**
     * Moves Pac-Man right.
     */
    void right();

    /**
     * Gets the rendering layer for Pac-Man.
     *
     * @return rendering layer
     */
    Layer getLayer();

    /**
     * Handles collision with a level element or renderable object.
     *
     * @param level      current level
     * @param renderable colliding object
     */
    void collideWith(Level level, Renderable renderable);

    /**
     * Checks if Pac-Man collides with another renderable object.
     *
     * @param renderable object to check collision with
     * @return true if colliding, false otherwise
     */
    boolean collidesWith(Renderable renderable);

    /**
     * Resets Pac-Man's position and state.
     */
    void reset();

    /**
     * Registers an observer to track Pac-Man's position.
     *
     * @param observer position observer
     */
    void registerObserver(PlayerPositionObserver observer);

    /**
     * Removes a position observer.
     *
     * @param observer position observer to remove
     */
    void removeObserver(PlayerPositionObserver observer);

    /**
     * Notifies all observers of Pac-Man's position.
     */
    void notifyObservers();

    /**
     * Gets the bounding box for collision detection.
     *
     * @return bounding box
     */
    BoundingBox getBoundingBox();

    /**
     * Gets the height of Pac-Man.
     *
     * @return height
     */
    double getHeight();

    /**
     * Gets the width of Pac-Man.
     *
     * @return width
     */
    double getWidth();

    /**
     * Sets possible movement directions.
     *
     * @param direction set of possible directions
     */
    void setPossibleDirections(Set<Direction> direction);

    /**
     * Gets the current movement direction.
     *
     * @return current direction
     */
    Direction getDirection();

    /**
     * Gets the center position of Pac-Man.
     *
     * @return center position
     */
    Vector2D getCenter();

    /**
     * Switches Pac-Man's image, typically for animation.
     */
    void switchImage();

    /**
     * Gets the base Pac-Man component.
     *
     * @return base Pac-Man component
     */
    ConcretePacmanComponent getPacman();
}