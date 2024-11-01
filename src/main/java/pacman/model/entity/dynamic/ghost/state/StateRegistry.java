package pacman.model.entity.dynamic.ghost.state;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;

import java.util.HashMap;

/**
 * Manages and registers states for a ghost in the game (SCATTER-CHASE <-> FRIGHTENED->.
 */
public class StateRegistry {

    /** Stores the mapping of ghost modes to their corresponding states. */
    private final HashMap<GhostMode, GhostModeState> statesRegistry = new HashMap<>();

    /**
     * Initializes the StateRegistry for a given ghost, registering its default states.
     *
     * @param ghost the ghost for which the states are being managed
     */
    public StateRegistry(Ghost ghost) {
        registerGhostState(GhostMode.FRIGHTENED, new FrightenedModeState(ghost));
        registerGhostState(GhostMode.SCATTER, new NonFrightenedModeState(ghost));
    }

    /**
     * Retrieves the state associated with the specified ghost mode.
     *
     * @param ghostMode the mode of the ghost
     * @return the state corresponding to the specified ghost mode
     */
    public GhostModeState getGhostState(GhostMode ghostMode) {
        return statesRegistry.get(ghostMode);
    }

    /**
     * Registers a new state for the specified ghost mode.
     *
     * @param ghostMode  the mode of the ghost
     * @param ghostState the state to be associated with the specified mode
     */
    private void registerGhostState(GhostMode ghostMode, GhostModeState ghostState) {
        statesRegistry.put(ghostMode, ghostState);
    }
}