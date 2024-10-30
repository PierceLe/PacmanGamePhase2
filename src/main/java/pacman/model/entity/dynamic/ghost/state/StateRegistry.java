package pacman.model.entity.dynamic.ghost.state;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;

import java.util.HashMap;

public class StateRegistry {
    private HashMap<GhostMode, GhostState> statesRegistry = new HashMap<>();
    public StateRegistry(Ghost ghost) {
        registerGhostState(GhostMode.FRIGHTENED, new FrightenedState(ghost));
        registerGhostState(GhostMode.SCATTER, new RegularState(ghost));
    }

    public GhostState getGhostState(GhostMode ghostMode) {
        return statesRegistry.get(ghostMode);
    }

    private void registerGhostState(GhostMode ghostMode, GhostState ghostState) {
        statesRegistry.put(ghostMode, ghostState);
    }
}
