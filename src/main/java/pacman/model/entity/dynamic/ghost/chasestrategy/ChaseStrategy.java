package pacman.model.entity.dynamic.ghost.chasestrategy;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;

public interface ChaseStrategy {
    Vector2D chasing(Ghost ghost, KinematicState playerKinematicState);
}
