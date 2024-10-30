package pacman.model.entity.dynamic.ghost.chasestrategy;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.maze.MazeCreator;

public interface ChaseStrategy {
    Vector2D chasing(Ghost ghost, KinematicState playerKinematicState);

    // Helper method to convert an absolute position to tile coordinates
    default Vector2D convertToTileCoordinates(Vector2D position) {
        int xTile = (int) (position.getX() / MazeCreator.RESIZING_FACTOR);
        int yTile = (int) (position.getY() / MazeCreator.RESIZING_FACTOR);
        return new Vector2D(xTile, yTile);
    }
}
