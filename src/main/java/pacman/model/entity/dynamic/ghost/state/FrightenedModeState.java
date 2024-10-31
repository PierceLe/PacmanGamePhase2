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
    private static double DURATION = 0;
    private double duration;
    private static final int SCALING_POINT = 100;

    private final Image frightenedImage =  new Image("maze/ghosts/frightened.png");

    public FrightenedModeState(Ghost ghost){
        this.ghost = ghost;
    }

    @Override
    public Image getImage() {
        return frightenedImage;
    }

    @Override
    public void handleCollision(Level level, Renderable renderable) {
        if (level.isPlayer(renderable)) {
            ghost.reset();
            resetCurrentStateAndTransist();
            ghost.playingRespawn();
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
        updateDirection(ghost);
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

    public void setDuration(double duration) {
        this.duration = duration;
        System.out.println(duration);
        DURATION = duration;
    }

    public void resetTickCount(){
        this.duration = DURATION;
    }
}
