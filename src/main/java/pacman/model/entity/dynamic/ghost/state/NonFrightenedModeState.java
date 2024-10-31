package pacman.model.entity.dynamic.ghost.state;


import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.level.Level;


public class NonFrightenedModeState implements GhostModeState {
    private final Ghost ghost;

    public NonFrightenedModeState(Ghost ghost){
        this.ghost = ghost;
    }
    @Override
    public Image getImage() {
        return ghost.getImage();
    }

    @Override
    public void handleCollision(Level level, Renderable renderable) {
        if (level.isPlayer(renderable)) {
            level.handleLoseLife();
        }
    }
    @Override
    public void update() {
        this.updateDirection(ghost);
        ghost.getKinematicState().update();
        ghost.getBoundingBox().setTopLeft(ghost.getKinematicState().getPosition());
    }

    @Override
    public void collectPowerPellets() {
        ghost.setState(GhostMode.FRIGHTENED);
        ghost.setGhostMode(GhostMode.FRIGHTENED);
    }

    @Override
    public void resetCurrentStateAndTransist(){
        ghost.setState(GhostMode.FRIGHTENED);
        ghost.setGhostMode(GhostMode.FRIGHTENED);
    }


}
