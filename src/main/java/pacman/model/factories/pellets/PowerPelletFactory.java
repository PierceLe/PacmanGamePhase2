package pacman.model.factories.pellets;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.collectable.Pellet;
import pacman.model.factories.RenderableFactory;

/**
 * Concrete renderable factory for Pellet objects
 */
public class PowerPelletFactory implements RenderableFactory {
    private static final Image PELLET_IMAGE = new Image("maze/pellet.png");
    private static final Image SCALED_PELLET_IMAGE = new Image(
            "maze/pellet.png",
            PELLET_IMAGE.getWidth() * 2,
            PELLET_IMAGE.getHeight() * 2,
            true,
            true
    );
    private static final int NUM_POINTS = 50;
    private final Renderable.Layer layer = Renderable.Layer.BACKGROUND;

    @Override
    public Renderable createRenderable(
            Vector2D position
    ) {
        try {
            position = position.add(new Vector2D(-7, -7));
            BoundingBox boundingBox = new BoundingBoxImpl(
                    position,
                    SCALED_PELLET_IMAGE.getHeight(),
                    SCALED_PELLET_IMAGE.getWidth()
            );

            return new Pellet(
                    boundingBox,
                    layer,
                    SCALED_PELLET_IMAGE,
                    NUM_POINTS
            );

        } catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid pellet configuration | %s", e));
        }
    }
}
