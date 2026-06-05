# Balls Game

## Overview

**Balls Game** is a Java Swing desktop game. The player controls a blue oval with the mouse, collects green circles to increase the score, and avoids moving red triangles. The game includes a start menu, speed control, pause/resume controls, score tracking, a timer, and a game-over screen.

## Game Objective

Move the blue player oval around the game window and collect as many green circles as possible. Each green circle collected adds 1 point to the grade. Avoid the red triangles. If the player touches a red triangle, the game ends and the final grade is shown.

## Features

- Java Swing GUI application
- Menu scene, game scene, and end scene using `CardLayout`
- Mouse-controlled player object
- Randomly moving collectible circles
- Randomly moving enemy triangles
- Collision detection between the player, collectibles, and enemies
- Grade/score display
- Timer display
- Polygon speed slider
- Pause and resume with the space bar
- Pause/resume and end-game options in the menu bar

## Controls

| Action | Control |
|---|---|
| Move player | Move the mouse |
| Start game | Click **Start Game** |
| Pause / resume | Press **Space** |
| Pause from menu | Use **exist&Resume > Pause/Resume** |
| End game | Use **exist&Resume > End Game** |
| Return to menu | Click **Return to Menu** on the end screen |

## How to Run

### Requirements

- Java JDK installed
- A terminal or command prompt

### Compile

```bash
javac Final.java
```

### Run

```bash
java Final
```

Make sure `Final.java` is in the current folder when compiling and running the program.

## File Structure

```text
Final.java        Main source code for the game
Final.class       Compiled class file for the main game
circle2.class     Compiled class file for the collectible circle class
Tri.class         Compiled class file for the red triangle enemy class
README.md         Project documentation
```

## Main Classes

### `Final`

The main class of the program. It creates the game window, sets up the `CardLayout`, and manages the menu, game, and end scenes.

### `circle2`

Represents the green collectible circles. Each circle moves in a curved random path and respawns in a new position after the player collects it.

### `Tri`

Represents the red triangle enemies. Each triangle moves in a curved random path. The triangle speed can be changed from the menu slider.

### `MouseControlledOval`

Represents the blue player object. It follows the mouse position and is used for collision detection with circles and triangles.

## Gameplay Logic

1. The game opens on the menu screen.
2. The player can adjust the polygon speed using the slider.
3. After clicking **Start Game**, the game scene appears.
4. The blue oval follows the mouse.
5. Green circles move randomly around the screen.
6. Red triangles move randomly around the screen.
7. When the blue oval touches a green circle, the grade increases by 1 and the circle respawns.
8. When the blue oval touches a red triangle, the game ends.
9. The player can return to the menu from the game-over screen.

## Notes

- The game window is set to 1000 × 1000 pixels.
- The code uses Swing `Timer` objects for animation, collision checking, and time tracking.
- The menu text says “yellow spheres,” but the current source code draws the collectible spheres in green.
- The enemy menu title is written as `exist&Resume`; this could be renamed to `Exit & Resume` for clearer wording.

## Possible Future Improvements

- Add a fixed time limit or countdown mode.
- Add difficulty levels.
- Reset the timer when restarting the game.
- Improve collision accuracy for triangle shapes.
- Add sound effects when collecting circles or losing the game.
- Add a high-score system.
- Rename classes using Java naming conventions, such as `Circle2` instead of `circle2`.
