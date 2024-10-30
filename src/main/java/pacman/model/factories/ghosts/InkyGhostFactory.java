package pacman.model.factories.ghosts;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.ghost.InkyGhost;
import pacman.model.entity.dynamic.ghost.chasestrategy.InkyChaseStrategy;
import pacman.model.entity.dynamic.physics.Vector2D;

public class InkyGhostFactory extends GhostFactory {
    private final Vector2D targetCorner;
    private static final Image image = new Image("maze/ghosts/inky.png");


    public InkyGhostFactory() {
        super(image);
        this.targetCorner = new Vector2D(RIGHT_X_POSITION_OF_MAP, BOTTOM_Y_POSITION_OF_MAP);
    }

    @Override
    public Ghost generateSpecificGhost(Vector2D position) {
        return new InkyGhost(image
                ,generateBoundingBox(position)
                ,generateKinematicState(position)
                ,GhostMode.SCATTER
                ,targetCorner
                , new InkyChaseStrategy()
        );
    }
}
