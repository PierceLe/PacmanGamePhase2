package pacman.model.entity.dynamic.player.decorator;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.model.entity.dynamic.player.observer.PlayerPositionObserver;
import pacman.model.level.Level;

import java.util.Set;

public class BasePacmanDecorator implements PacmanComponent, Controllable {
    protected PacmanComponent pacman;

    public BasePacmanDecorator(PacmanComponent pacman) {
        this.pacman = pacman;
    }


    @Override
    public Image getImage() {
        return pacman.getImage();
    }

    @Override
    public Vector2D getPosition() {
        return pacman.getPosition();
    }

    @Override
    public void setPosition(Vector2D position) {
        pacman.setPosition(position);
    }

    @Override
    public Vector2D getPositionBeforeLastUpdate() {
        return pacman.getPositionBeforeLastUpdate();
    }

    @Override
    public void update() {
        pacman.update();
    }

    @Override
    public void setSpeed(double speed) {
        pacman.setSpeed(speed);
    }

    @Override
    public void up() {
        pacman.up();
    }

    @Override
    public void down() {
        pacman.down();
    }

    @Override
    public void left() {
        pacman.left();
    }

    @Override
    public void right() {
        pacman.right();
    }

    @Override
    public Renderable.Layer getLayer() {
        return pacman.getLayer();
    }

    @Override
    public void collideWith(Level level, Renderable renderable) {
        pacman.collideWith(level, renderable);
    }

    @Override
    public boolean collidesWith(Renderable renderable) {
        return pacman.collidesWith(renderable);
    }

    @Override
    public void reset() {
        pacman.reset();
    }

    @Override
    public void registerObserver(PlayerPositionObserver observer) {
        pacman.registerObserver(observer);
    }

    @Override
    public void removeObserver(PlayerPositionObserver observer) {
        pacman.removeObserver(observer);
    }

    @Override
    public void notifyObservers() {
        pacman.notifyObservers();
    }

    @Override
    public BoundingBox getBoundingBox() {
        return pacman.getBoundingBox();
    }

    @Override
    public double getHeight() {
        return pacman.getHeight();
    }

    @Override
    public double getWidth() {
        return pacman.getWidth();
    }

    @Override
    public void setPossibleDirections(Set<Direction> direction) {
        pacman.setPossibleDirections(direction);
    }

    @Override
    public Direction getDirection() {
        return pacman.getDirection();
    }

    @Override
    public Vector2D getCenter() {
        return pacman.getCenter();
    }

    @Override
    public void switchImage() {
        pacman.switchImage();
    }

    @Override
    public ConcretePacmanComponent getPacman() {
        return pacman.getPacman();
    }
}
