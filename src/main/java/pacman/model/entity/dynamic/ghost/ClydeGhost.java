package pacman.model.entity.dynamic.ghost;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.ghost.chasestrategy.ChaseStrategy;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;

public class ClydeGhost extends GhostImpl{
    public ClydeGhost(Image image, BoundingBox boundingBox, KinematicState kinematicState, GhostMode ghostMode, Vector2D targetCorner, ChaseStrategy chaseStrategy) {
        super(image, boundingBox, kinematicState, ghostMode, targetCorner, chaseStrategy);
    }
}
