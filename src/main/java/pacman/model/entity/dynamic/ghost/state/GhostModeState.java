package pacman.model.entity.dynamic.ghost.state;

import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.level.Level;

public interface GhostModeState {
    void handleCollision(Level level, Renderable entity);
    void update(Ghost ghost);

}
