package pacman.model.factories.ghosts;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.ghost.ClydeGhost;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.ghost.chasestrategy.ClydeChaseStrategy;
import pacman.model.entity.dynamic.physics.Vector2D;

public class ClydeGhostFactory extends GhostFactory {
    private final Vector2D targetCorner;
    private static final Image image = new Image("maze/ghosts/clyde.png");


    public ClydeGhostFactory() {
        super(image);
        this.targetCorner = new Vector2D(0, BOTTOM_Y_POSITION_OF_MAP);
    }

    @Override
    public Ghost generateSpecificGhost(Vector2D position) {
        return new ClydeGhost(image
                ,generateBoundingBox(position)
                ,generateKinematicState(position)
                ,GhostMode.SCATTER
                ,targetCorner
                , new ClydeChaseStrategy()
        );
    }
}
