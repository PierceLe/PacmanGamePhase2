package pacman.model.entity.dynamic.ghost.state;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;

import java.util.HashMap;

public class StateRegistry {
    private final HashMap<GhostMode, GhostModeState> statesRegistry = new HashMap<>();
    public StateRegistry(Ghost ghost) {
        registerGhostState(GhostMode.FRIGHTENED, new FrightenedModeState(ghost));
        registerGhostState(GhostMode.SCATTER, new NonFrightenedModeState(ghost));
    }

    public GhostModeState getGhostState(GhostMode ghostMode) {
        return statesRegistry.get(ghostMode);
    }

    private void registerGhostState(GhostMode ghostMode, GhostModeState ghostState) {
        statesRegistry.put(ghostMode, ghostState);
    }
}
