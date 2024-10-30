package pacman.model.factories.ghosts;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.factories.RenderableFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Concrete renderable factory for Ghost objects
 */
public abstract class GhostFactory implements RenderableFactory {

    protected static final int RIGHT_X_POSITION_OF_MAP = 448;
    protected static final int TOP_Y_POSITION_OF_MAP = 16 * 3;
    protected static final int BOTTOM_Y_POSITION_OF_MAP = 16 * 34;

    private final Image image;


    public GhostFactory(Image image) {
        this.image = image;
    }


    protected BoundingBox generateBoundingBox(Vector2D position) {
        return new BoundingBoxImpl(position, image.getHeight(), image.getWidth());
    }


    protected KinematicState generateKinematicState(Vector2D position) {
        return new KinematicStateImpl.KinematicStateBuilder().setPosition(position).build();
    }

    @Override
    public Renderable createRenderable(
            Vector2D position
    ) {
        try {
            position = position.add(new Vector2D(4, -4));
            return generateSpecificGhost(position);

        } catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid ghost configuration | %s ", e));
        }
    }

    public abstract Ghost generateSpecificGhost(Vector2D position);

}
