//package pacman.model.entity.dynamic.ghost.state;
//
//import pacman.model.entity.Renderable;
//import pacman.model.entity.dynamic.ghost.Ghost;
//import pacman.model.entity.dynamic.ghost.GhostImpl;
//import pacman.model.entity.dynamic.physics.Direction;
//import pacman.model.entity.dynamic.physics.Vector2D;
//import pacman.model.level.Level;
//import pacman.model.maze.Maze;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//
//public class NotFrightenedState implements GhostModeState {
//    @Override
//    public void handleCollision(Level level, Renderable renderable) {
//        if (level.isPlayer(renderable)) {
//            level.handleLoseLife();
//        }
//
//    }
//
//    @Override
//    public void update(Ghost ghost) {
//        updateDirection(ghost);
//        ghost.getKinematicState().update();
//        ghost.getBoundingBox().setTopLeft(ghost.getKinematicState().getPosition());
//    }
//
//    private void updateDirection(Ghost ghost) {
//        // Ghosts update their target location when they reach an intersection
//        if (Maze.isAtIntersection(ghost.getPossibleDirections())) {
//            ghost.setTargetLocation(ghost.getTargetLocation());
//        }
//
//        Direction newDirection = selectDirection(ghost.getPossibleDirections(), ghost);
//
//        // Ghosts have to continue in a direction for a minimum time before changing direction
//        if (ghost.getCurrentDirection() != newDirection) {
//            ghost.setCurrentDirectionCount(0);
//        }
//        ghost.setCurrentDirection(newDirection);
//
//        switch (ghost.getCurrentDirection()) {
//            case LEFT -> ghost.getKinematicState().left();
//            case RIGHT -> ghost.getKinematicState().right();
//            case UP -> ghost.getKinematicState().up();
//            case DOWN -> ghost.getKinematicState().down();
//        }
//    }
//
//
//    private Direction selectDirection(Set<Direction> possibleDirections, Ghost ghost) {
//        if (possibleDirections.isEmpty()) {
//            return ghost.getCurrentDirection();
//        }
//
//        // ghosts have to continue in a direction for a minimum time before changing direction
//        if (ghost.getCurrentDirection() != null && ghost.getCurrentDirectionCount() < GhostImpl.minimumDirectionCount) {
//            ghost.setCurrentDirectionCount(ghost.getCurrentDirectionCount() + 1);
//            return ghost.getCurrentDirection();
//        }
//
//        Map<Direction, Double> distances = new HashMap<>();
//
//        for (Direction direction : possibleDirections) {
//            // ghosts never choose to reverse travel
//            if (ghost.getCurrentDirection() == null ||
//                    ghost.getCurrentDirection() != ghost.getCurrentDirection().opposite()) {
//                distances.put(direction, Vector2D.calculateEuclideanDistance(ghost.getKinematicState().getPotentialPosition(direction), ghost.getTargetLocation()));
//            }
//        }
//
//        // only go the opposite way if trapped
//        if (distances.isEmpty()) {
//            return ghost.getCurrentDirection().opposite();
//        }
//
//        // select the direction that will reach the target location fastest
//        return Collections.min(distances.entrySet(), Map.Entry.comparingByValue()).getKey();
//    }
//}
