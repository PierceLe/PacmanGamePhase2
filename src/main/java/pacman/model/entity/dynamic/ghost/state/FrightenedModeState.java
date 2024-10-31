package pacman.model.entity.dynamic.ghost.state;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.level.Level;
import static java.lang.Math.pow;

/**
 * Concrete class for FrightenedState
 */
public class FrightenedModeState implements GhostModeState {
    private final Ghost ghost;
    private static double MODE_LENGTH = 0;
    private double duration;
    private static final int POINT = 100;

    private final Image image =  new Image("maze/ghosts/frightened.png");

    public FrightenedModeState(Ghost ghost){
        this.ghost = ghost;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public void handleCollision(Level level, Renderable renderable) {
        if (level.isPlayer(renderable)) {
            ghost.reset();
            this.duration = MODE_LENGTH;
            ghost.setGhostMode(GhostMode.SCATTER);
            ghost.setState(GhostMode.SCATTER);
            ghost.playingRespawn();
            this.duration = MODE_LENGTH;
            level.incrementGhostStreak();
            int streak = level.getStreakCount();
            int base = (int) pow(2, streak);
            level.incrementScore(base * POINT);
        }
    }
    @Override
    public void update() {
        System.out.println(duration);
        if (this.duration <= 0){
            this.duration = MODE_LENGTH;
            ghost.setGhostMode(GhostMode.SCATTER);
            ghost.setState(GhostMode.SCATTER);
            return;
        }
        updateDirection(ghost);
        ghost.getKinematicState().update();
        ghost.getBoundingBox().setTopLeft(ghost.getKinematicState().getPosition());
        this.duration -= 1;
    }

    @Override
    public void collectPowerPellets() {
        this.duration = MODE_LENGTH;
    }


    public void setModeLength(double modeLength) {
        this.duration = modeLength;
        System.out.println(modeLength);
        MODE_LENGTH = modeLength;
    }
}
