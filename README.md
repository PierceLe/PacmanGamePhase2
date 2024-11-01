# How to Run the Program:
`gradle clean build run`

# Design Patterns in the Project

This project implements new several key design patterns, including the **Strategy**, **State** and **Decorator** patterns. These patterns are utilized to ensure flexibility, maintainability, and scalability of the codebase. Below are the participants and structures for each pattern used in the project.

---

## 1. Strategy

### Purpose:
The Strategy Pattern is used to implement different chase strategies for each ghost when they are in CHASE mode. Each ghost has its own unique strategy for targeting Pac-Man:
- **Pinky**: Four grid spaces ahead of Pac-Man
- **Blinky**: Pac-Man’s position
- **Clyde**: If more than 8 grid spaces away from Pac-Man (straight line distance), its target location is Pac-Man. Otherwise, its target location is the bottom-left corner
- **Inky**: Bashful’s target position is found by first determining the position two grid spaces ahead of Pac-Man, and then doubling the vector from Shadow/Blinky to that position.

### Participants:

- **Strategy**: `ChaseStrategy`
- **ConcreteStrategy**:
  - `BlinkyChaseStrategy`:
  - `ClydeChaseStrategy`:
  - `InkyChaseStrategy`:
  - `PinkyChaseStrategy`:
- **Context**: `GhostImpl`


This pattern allows each ghost to determine its target position independently based on its strategy, making it easy to modify or add new ghost behaviors.

---

## 2. State Strategy

### Purpose:
The State Pattern is used to manage the behavior and actions of ghosts, particularly differentiating the behavior in Frightened mode compared to other modes (SCATTER and CHASE).
### Participants:
- **Context**: `GhostImpl`
- **State**: `GhostModeState`
- **ConcreteState**: 
  - `FrightenedModeState`: Defines ghost behavior when Pac-Man consumes a power pellet, allowing ghosts to be eaten.
  - `NonFrightenedModeState`: Represents normal ghost behavior during SCATTER and CHASE modes.

The State Pattern enables ghosts to switch between behaviors seamlessly, such as moving randomly in Frightened mode or chasing/scattering based on their individual strategies. This makes it straightforward to manage state transitions and define unique behaviors for each mode.

---

## 3. Decorator Pattern

### Purpose:
The Decorator Pattern is used to add temporary functionality to Pac-Man without altering the base Pacman class. When Pac-Man eats a super pellet, the system dynamically adds a decorator that enables Pac-Man to eat ghosts temporarily. Once the effect wears off, the decorator is removed, and Pac-Man returns to his regular abilities. This approach allows for flexible management of temporary effects without complicating the main Pacman class.
### Participants:

#### **Factory Collection Singleton**
- **Component**: `PacmanComponent`
- **ConcreteComponent**: `PacmanComponent`
- **Decorator**: `BasePacmanDecorator`
- **ConcreteDecorator**: `ConcretePacmanDecorator`

This use of design patterns enhances the modularity and extensibility of the codebase, making it easier to add new features or modify existing behaviors without altering core game logic.

---