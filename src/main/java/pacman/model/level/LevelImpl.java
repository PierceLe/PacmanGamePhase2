package pacman.model.level;

import org.json.simple.JSONObject;
import pacman.ConfigurationParseException;
import pacman.model.engine.observer.GameState;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.DynamicEntity;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.ghost.chasestrategy.BlinkyChaseStrategy;
import pacman.model.entity.dynamic.ghost.chasestrategy.InkyChaseStrategy;
import pacman.model.entity.dynamic.ghost.state.FrightenedModeState;
import pacman.model.entity.dynamic.ghost.state.GhostModeState;
import pacman.model.entity.dynamic.physics.PhysicsEngine;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.model.entity.dynamic.player.decorator.ConcretePacmanComponent;
import pacman.model.entity.dynamic.player.decorator.ConcretePacmanDecorator;
import pacman.model.entity.dynamic.player.decorator.PacmanComponent;
import pacman.model.entity.staticentity.StaticEntity;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.level.observer.LevelStateObserver;
import pacman.model.maze.Maze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Concrete implement of Pac-Man level
 */
public class LevelImpl implements Level {

    private static final int START_LEVEL_TIME = 100;
    private final Maze maze;
    private final List<LevelStateObserver> observers;
    private List<Renderable> renderables;
    private Controllable player;
    private List<Ghost> ghosts;
    private int tickCount;
    private Map<GhostMode, Integer> modeLengths;
    private int numLives;
    private int points;
    private GameState gameState;
    private List<Renderable> collectables;
    private GhostMode currentGhostMode;

    private int eatenStreak = 0;

    public LevelImpl(JSONObject levelConfiguration,
                     Maze maze) {
        this.renderables = new ArrayList<>();
        this.maze = maze;
        this.tickCount = 0;
        this.observers = new ArrayList<>();
        this.modeLengths = new HashMap<>();
        this.gameState = GameState.READY;
        this.currentGhostMode = GhostMode.SCATTER;
        this.points = 0;

        initLevel(new LevelConfigurationReader(levelConfiguration));
    }


    private void initLevel(LevelConfigurationReader levelConfigurationReader) {
        // Fetch all renderables for the level
        this.renderables = maze.getRenderables();

        // Set up player
        if (!(maze.getControllable() instanceof Controllable)) {
            throw new ConfigurationParseException("Player entity is not controllable");
        }
        this.player = (Controllable) maze.getControllable();
        this.player.setSpeed(levelConfigurationReader.getPlayerSpeed());
        setNumLives(maze.getNumLives());

        // Set up collectables
        this.collectables = new ArrayList<>(maze.getPellets());
        this.modeLengths = levelConfigurationReader.getGhostModeLengths();

        // Set up ghosts
        this.ghosts = maze.getGhosts().stream()
                .map(element -> (Ghost) element)
                .collect(Collectors.toList());
        Map<GhostMode, Double> ghostSpeeds = levelConfigurationReader.getGhostSpeeds();
        Ghost blinkyGhost = null;
        for (Ghost ghost : ghosts) {
            if (ghost.getChaseStrategy() instanceof BlinkyChaseStrategy) {
                blinkyGhost = ghost;
                break;
            }
        }
        for (Ghost ghost : ghosts) {
            if (ghost.getChaseStrategy() instanceof InkyChaseStrategy inkyChaseStrategy) {
                inkyChaseStrategy.setBlinkyGhost(blinkyGhost);
            }
            FrightenedModeState frightenedState = (FrightenedModeState) ghost.getGhostStateRegistry().getGhostState(GhostMode.FRIGHTENED);
            frightenedState.setModeLength(modeLengths.get(GhostMode.FRIGHTENED));
        }
        for (Ghost ghost : this.ghosts) {
            player.registerObserver(ghost);
            ghost.setSpeeds(ghostSpeeds);
            ghost.setGhostMode(this.currentGhostMode);
        }
    }

    @Override
    public List<Renderable> getRenderables() {
        return this.renderables;
    }

    private List<DynamicEntity> getDynamicEntities() {
        return renderables.stream().filter(e -> e instanceof DynamicEntity).map(e -> (DynamicEntity) e).collect(
                Collectors.toList());
    }

    private List<StaticEntity> getStaticEntities() {
        return renderables.stream().filter(e -> e instanceof StaticEntity).map(e -> (StaticEntity) e).collect(
                Collectors.toList());
    }

    @Override
    public void tick() {
        if (this.gameState != GameState.IN_PROGRESS) {

            if (tickCount >= START_LEVEL_TIME) {
                setGameState(GameState.IN_PROGRESS);
                tickCount = 0;
            }
        } else {
            if (tickCount == modeLengths.get(currentGhostMode)) {
                if (currentGhostMode == GhostMode.FRIGHTENED) {
                    deactivatePowerPelletEffect((ConcretePacmanDecorator) player);
                    eatenStreak = 0;
                }
                // update ghost mode
                this.currentGhostMode = GhostMode.getNextGhostMode(currentGhostMode);
                for (Ghost ghost : this.ghosts) {
                    ghost.setGhostMode(this.currentGhostMode);
                }
                tickCount = 0;
            }

            if (tickCount % ConcretePacmanComponent.PACMAN_IMAGE_SWAP_TICK_COUNT == 0) {
                this.player.switchImage();
            }

            // Update the dynamic entities
            List<DynamicEntity> dynamicEntities = getDynamicEntities();

            for (DynamicEntity dynamicEntity : dynamicEntities) {
                maze.updatePossibleDirections(dynamicEntity);
                dynamicEntity.update();
            }

            for (int i = 0; i < dynamicEntities.size(); ++i) {
                DynamicEntity dynamicEntityA = dynamicEntities.get(i);
                // handle collisions between dynamic entities
                for (int j = i + 1; j < dynamicEntities.size(); ++j) {
                    DynamicEntity dynamicEntityB = dynamicEntities.get(j);

                    if (dynamicEntityA.collidesWith(dynamicEntityB) ||
                            dynamicEntityB.collidesWith(dynamicEntityA)) {
                        dynamicEntityA.collideWith(this, dynamicEntityB);
                        dynamicEntityB.collideWith(this, dynamicEntityA);
                    }
                }

                // handle collisions between dynamic entities and static entities
                for (StaticEntity staticEntity : getStaticEntities()) {
                    if (dynamicEntityA.collidesWith(staticEntity)) {
                        dynamicEntityA.collideWith(this, staticEntity);
                        PhysicsEngine.resolveCollision(dynamicEntityA, staticEntity);
                    }
                }
            }
        }

        tickCount++;
    }

    @Override
    public boolean isPlayer(Renderable renderable) {
        return renderable == this.player;
    }

    @Override
    public boolean isCollectable(Renderable renderable) {
        return maze.getPellets().contains(renderable) && ((Collectable) renderable).isCollectable();
    }

    @Override
    public void collect(Collectable collectable) {
        if (collectable.getPoints() == 50) {
            this.currentGhostMode = GhostMode.FRIGHTENED;
            for (Ghost ghost : ghosts){
                GhostModeState ghostCurrentState = ghost.getCurrentGhostState();
                ghostCurrentState.collectPowerPellets();
            }
            activatePowerPelletEffect((PacmanComponent) player);
            eatenStreak = 0;
            tickCount = 0;
        }
        this.points += collectable.getPoints();
        notifyObserversWithScoreChange(collectable.getPoints());
        this.collectables.remove(collectable);
    }

    @Override
    public void handleLoseLife() {
        if (gameState == GameState.IN_PROGRESS) {
            deactivatePowerPelletEffect((ConcretePacmanDecorator) player);
            for (DynamicEntity dynamicEntity : getDynamicEntities()) {
                dynamicEntity.reset();
            }
            for (Ghost ghost : ghosts) {
                ghost.setGhostMode(GhostMode.SCATTER);
                ghost.setState(GhostMode.SCATTER);
            }
            setNumLives(numLives - 1);
            setGameState(GameState.READY);
            tickCount = 0;
        }
    }

    @Override
    public void moveLeft() {
        player.left();
    }

    @Override
    public void moveRight() {
        player.right();
    }

    @Override
    public void moveUp() {
        player.up();
    }

    @Override
    public void moveDown() {
        player.down();
    }

    @Override
    public boolean isLevelFinished() {
        return collectables.isEmpty();
    }

    @Override
    public void registerObserver(LevelStateObserver observer) {
        this.observers.add(observer);
        observer.updateNumLives(this.numLives);
        observer.updateGameState(this.gameState);
    }

    @Override
    public void removeObserver(LevelStateObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObserversWithNumLives() {
        for (LevelStateObserver observer : observers) {
            observer.updateNumLives(this.numLives);
        }
    }



    private void setGameState(GameState gameState) {
        this.gameState = gameState;
        notifyObserversWithGameState();
    }

    @Override
    public void notifyObserversWithGameState() {
        for (LevelStateObserver observer : observers) {
            observer.updateGameState(gameState);
        }
    }

    /**
     * Notifies observer of change in player's score
     */
    public void notifyObserversWithScoreChange(int scoreChange) {
        for (LevelStateObserver observer : observers) {
            observer.updateScore(scoreChange);
        }
    }

    @Override
    public int getPoints() {
        return this.points;
    }

    @Override
    public int getNumLives() {
        return this.numLives;
    }

    private void setNumLives(int numLives) {
        this.numLives = numLives;
        notifyObserversWithNumLives();
    }

    @Override
    public void handleGameEnd() {
        this.renderables.removeAll(getDynamicEntities());
    }

    @Override
    public int getNumStreak() {
        return eatenStreak;
    }

    @Override
    public void incrementScore(int scoreIncremented) {
        this.points += scoreIncremented;
        notifyObserversWithScoreChange(scoreIncremented);
    }

    @Override
    public void incrementStreakCount() {
        if (eatenStreak == ghosts.size()) {
            eatenStreak = 0;
        }
        this.eatenStreak += 1;
    }

    public void activatePowerPelletEffect(PacmanComponent wrappedPlayer) {
        renderables.remove(player);
        player = new ConcretePacmanDecorator(wrappedPlayer);
        renderables.add(player);
    }

    public void deactivatePowerPelletEffect(ConcretePacmanDecorator pacman) {
        Controllable normalPlayer = pacman.getPacman();
        renderables.remove(player);
        player = normalPlayer;
        renderables.add(player);
    }
}
