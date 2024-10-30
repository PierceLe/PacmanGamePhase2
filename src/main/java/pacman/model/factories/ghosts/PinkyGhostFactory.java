package pacman.model.factories.ghosts;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.ghost.chasestrategy.PinkyChaseStrategy;
import pacman.model.entity.dynamic.physics.Vector2D;

public class PinkyGhostFactory extends GhostFactory {
    private final Vector2D targetCorner;
    private static final Image image = new Image("maze/ghosts/pinky.png");


    public PinkyGhostFactory() {
        super(image);
        this.targetCorner = new Vector2D(0, TOP_Y_POSITION_OF_MAP);
    }

    @Override
    public Ghost generateSpecificGhost(Vector2D position) {
        return new GhostImpl(image
                ,generateBoundingBox(position)
                ,generateKinematicState(position)
                ,GhostMode.SCATTER
                ,targetCorner
                , new PinkyChaseStrategy()
        );
    }
}
