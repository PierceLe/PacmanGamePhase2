package pacman.model.entity.dynamic.ghost.chasestrategy;

import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.KinematicState;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.maze.MazeCreator;

public class PinkyChaseStrategy implements ChaseStrategy {
    private static final Integer GRID = 4;
    @Override
    public Vector2D chasing(Ghost ghost, KinematicState playerKinematicState) {
        Vector2D pacmanPosition = convertToTileCoordinates(playerKinematicState.getPosition());
        Direction playerDirection = playerKinematicState.getDirection();
        if (playerDirection == Direction.UP) {
            pacmanPosition = pacmanPosition.add(new Vector2D(0, -GRID));
        } else if (playerDirection == Direction.DOWN) {
            pacmanPosition = pacmanPosition.add(new Vector2D(0, GRID));
        } else if (playerDirection == Direction.LEFT) {
            pacmanPosition = pacmanPosition.add(new Vector2D(-GRID, 0));
        } else if (playerDirection == Direction.RIGHT) {
            pacmanPosition = pacmanPosition.add(new Vector2D(GRID, 0));
        }
        int xBounded = Math.max(Math.min(26, (int) pacmanPosition.getX()), 1) * MazeCreator.RESIZING_FACTOR;
        int yBounded = Math.max(Math.min(26, (int) pacmanPosition.getY()), 4) * MazeCreator.RESIZING_FACTOR;
        return new Vector2D(xBounded, yBounded);
    }
}
