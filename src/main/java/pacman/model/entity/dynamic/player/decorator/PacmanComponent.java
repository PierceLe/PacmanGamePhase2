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

public interface PacmanComponent {
    Image getImage();
    Vector2D getPosition();
    void setPosition(Vector2D position);
    Vector2D getPositionBeforeLastUpdate();
    void update();
    void setSpeed(double speed);
    void up();
    void down();
    void left();
    void right();
    Layer getLayer();
    void collideWith(Level level, Renderable renderable);
    boolean collidesWith(Renderable renderable);
    void reset();
    void registerObserver(PlayerPositionObserver observer);
    void removeObserver(PlayerPositionObserver observer);
    void notifyObservers();
    BoundingBox getBoundingBox();
    double getHeight();
    double getWidth();
    void setPossibleDirections(Set<Direction> direction);
    Direction getDirection();
    Vector2D getCenter();
    void switchImage();
    ConcretePacmanComponent getPacman();

}
