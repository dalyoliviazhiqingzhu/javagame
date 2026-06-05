import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Random;
import javax.swing.*;


public class Final{
    static circle2[] circles; // Keep track of all circles
    static Tri[] tris; // Keep track of all triangles
    static int speed = 50; // Initial speed

    public static void main(String[] args) {

        JFrame myFrame = new JFrame("gaming");
        myFrame.setPreferredSize(new Dimension(1000,1000));
        myFrame.setMinimumSize(new Dimension(1000,1000));
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //myframe.setLayout(null);
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // Create scenes
        JPanel menuScene = createMenuScene(mainPanel, cardLayout);
        JPanel gameScene = createGameScene(myFrame, mainPanel, cardLayout);
        JPanel endScene = createEndScene(mainPanel, cardLayout);

        // Add scenes to the main panel
        mainPanel.add(menuScene, "Menu");
        mainPanel.add(gameScene, "Game");
        mainPanel.add(endScene, "End");

        // Set the main panel as the content pane
        
        myFrame.setContentPane(mainPanel);
        myFrame.pack();
        myFrame.setVisible(true);

        // Show the initial scene
        cardLayout.show(mainPanel, "Menu");
    }
    private static JPanel createMenuScene(JPanel mainPanel, CardLayout cardLayout) {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(5,1));

        JLabel titleLabel = new JLabel("Welcome to Balls", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        menuPanel.add(titleLabel, BorderLayout.CENTER);

        //add rule board
        JLabel Rule = new JLabel( "<html><div style='text-align: center;'>"
        + "The player needs to let the Green sphere collect the yellow spheres to get a higher grade within the time range. "
        + "Player needs to avoid the moving Red polygon, or the game will be over."
        + "press space to stop and press again to resume. or select menue pause&exist to stop &exist."
        + "</div></html>", 
        SwingConstants.CENTER);
        menuPanel.add(Rule);

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> cardLayout.show(mainPanel, "Game"));

        // Create a JLabel to display the current speed
    JLabel speedLabel = new JLabel("Polygon speed: 1", SwingConstants.CENTER);
    menuPanel.add(speedLabel);
        // Create the slider to control speed
    JSlider speedSlider = new JSlider(0, 10, 1); // Min: 1, Max: 100, Initial: 50
    speedSlider.setMajorTickSpacing(1);
    speedSlider.setMinorTickSpacing(1);
    speedSlider.setPaintTicks(true);
    speedSlider.setPaintLabels(true);

    

    // Add a listener to update the speed label
    speedSlider.addChangeListener(e -> {
        int speed = speedSlider.getValue();
        speedLabel.setText("Polygon speed: " + speed);
        // Optionally, update the animation speed or other logic here
        // Example: animatedCircle.setSpeed(speed);
        for (Tri tri : tris) {
            tri.setSpeed(speed);
        }
    });
    menuPanel.add(speedSlider);

        menuPanel.add(startButton);

        return menuPanel;
    }

    // Create the Game Scene
    private static JPanel createGameScene(JFrame frame, JPanel mainPanel, CardLayout cardLayout) {
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());

        


        /*JLabel gameLabel = new JLabel("Game Scene", SwingConstants.CENTER);
        gameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gamePanel.add(gameLabel, BorderLayout.CENTER);*/

        /*circle1 animatedCircle1 = new circle1();
        gamePanel.add(animatedCircle1, BorderLayout.CENTER); // Add animated circle to the center*/

        // Create a custom panel for the mouse-controlled oval
        MouseControlledOval mouseControlledOval = new MouseControlledOval();
        mouseControlledOval.setBounds(0, 20, 1000, 1000);
        gamePanel.add(mouseControlledOval);
        

        //int numCircles = 5; // Number of circles
        //circle2[] circles = new circle2[numCircles];

        // Use the shared `circles` array
    circles = new circle2[5];
    for (int i = 0; i < circles.length; i++) {
        circles[i] = new circle2();
        circles[i].setBounds(0, 20, 1000, 1000);
        gamePanel.add(circles[i]);
    }

    // Use the shared `tris` array
    tris = new Tri[3];
    for (int i = 0; i < tris.length; i++) {
        tris[i] = new Tri(speed);
        tris[i].setBounds(0, 20, 1000, 1000);
        gamePanel.add(tris[i]);
    }

        //endbutton
        /*JButton endButton = new JButton("End Game");
        endButton.addActionListener(e -> cardLayout.show(mainPanel, "End"));
        gamePanel.add(endButton, BorderLayout.SOUTH);*/

        boolean[] isPaused = {false}; // Track pause state (using an array for mutability in lambdas)

        // Timer for updating the label
        JLabel timerLabel = new JLabel("Time: 0s"); // Timer label
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Set font for better visibility
        timerLabel.setText("Time:0S");
        timerLabel.setBounds(500,900,300,100);
        gamePanel.add(timerLabel, BorderLayout.SOUTH); // Add timer label to the top
    
        Timer gameTimer = new Timer(1000, null); // Fires every 1 second
        final int[] elapsedTime = {0}; // Track elapsed time

    // Timer action listener
    gameTimer.addActionListener(e -> {
        if (!isPaused[0]) { // Update only if not paused
            elapsedTime[0]++;
            timerLabel.setText("Time: " + elapsedTime[0] + "s");
        }
    });

         // Grade tracker
        JLabel gradeLabel = new JLabel("Grade: 0", SwingConstants.CENTER);
        gradeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gradeLabel.setBounds(500,700,100,100);
        JPanel gradePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gradePanel.setBounds(500,700,200,200);
        gradePanel.add(gradeLabel);
        gamePanel.add(gradePanel, BorderLayout.SOUTH);
        final int[] grade = {0}; // Track grade
        // Add collision detection
    Timer collisionTimer = new Timer(10, e -> {
        Rectangle blueBounds = mouseControlledOval.getBounds();

        // Check collisions with green circles
        for (circle2 circle : circles) {
            if (blueBounds.intersects(circle.getBounds())) {
                grade[0]++;
                gradeLabel.setText("Grade: " + grade[0]);
                circle.respawn(); // Move the green circle to a new random position
            }
        }

        // Check collisions with red polygons
        for (Tri tri : tris) {
            if (blueBounds.intersects(tri.getBounds())) {
                JOptionPane.showMessageDialog(gamePanel, "Game Over! Final Grade: " + grade[0]);
                cardLayout.show(mainPanel, "End");
                gameTimer.stop(); // Stop the game timer
                grade[0] = 0;
                gradeLabel.setText("Grade:0");
                return;
            }
        }
    });
    collisionTimer.start();


        
        // Add KeyListener to pause and resume movement
    gamePanel.setFocusable(true); // Ensure the panel can gain focus for key events
    gamePanel.requestFocusInWindow(); // Request focus for the panel
    gamePanel.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) { // Check if space key is pressed
                isPaused[0] = !isPaused[0]; // Toggle pause state

                // Pause or resume all circles and triangles
                for (circle2 circle : circles) {
                    circle.setPaused(isPaused[0]);
                }
                for (Tri tri : tris) {
                    tri.setPaused(isPaused[0]);
                }
                if (isPaused[0]) {
                    mouseControlledOval.disconnectMouseControl();
                    gameTimer.stop(); // Stop the timer
                    collisionTimer.stop();
                } else {
                    mouseControlledOval.connectMouseControl();
                    gameTimer.start(); // Resume the timer
                    collisionTimer.start();
                }

                String state = isPaused[0] ? "paused" : "resumed";
                System.out.println("Game " + state); // Debugging output
            }
        }
    });
    

     /*// Request focus when switching to the game scene
     gamePanel.addComponentListener(new java.awt.event.ComponentAdapter() {
        @Override
        public void componentShown(java.awt.event.ComponentEvent evt) {
            gamePanel.requestFocusInWindow(); // Ensure focus is on the game panel
            gameTimer.start(); // Start the timer when the game scene is displayed
        }
    });*/
    



        // Create a JMenuBar
    JMenuBar menuBar = new JMenuBar();

    // Create the Game menu
    JMenu gameMenu = new JMenu("exist&Resume");

    // Create menu items
    JMenuItem resumeItem = new JMenuItem("Pause/Resume");
    JMenuItem endItem = new JMenuItem("End Game");

    // Add action listener to "Pause" menu item
    resumeItem.addActionListener(e -> {
        isPaused[0] = true; // Pause the game
        for (circle2 circle : circles) {
            circle.setPaused(true);
        }
        for (Tri tri : tris) {
            tri.setPaused(true);
        }
        JOptionPane.showMessageDialog(gamePanel, "Game Paused! Click OK to Resume.");
        isPaused[0] = false; // Automatically resume after dialog
        for (circle2 circle : circles) {
            circle.setPaused(false);
        }
        for (Tri tri : tris) {
            tri.setPaused(false);
        }
        mouseControlledOval.connectMouseControl();
        gameTimer.start(); // Resume the timer
        collisionTimer.start();
        
        //gamePanel.requestFocusInWindow(); // Return focus to the game panel
    });
    // Add action listener to "End Game" menu item
    endItem.addActionListener(e -> {
        int confirm = JOptionPane.showConfirmDialog(
                gamePanel,
                "Are you sure you want to end the game?",
                "Confirm End Game",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            cardLayout.show(mainPanel, "End");
            //gamePanel.requestFocusInWindow(); // Return focus to the game panel
            grade[0] = 0;
            gradeLabel.setText("Grade:0");
        }
    });
    // Ensure focus and timer management when switching scenes
    gamePanel.addComponentListener(new java.awt.event.ComponentAdapter() {
        @Override
        public void componentShown(java.awt.event.ComponentEvent evt) {
            gamePanel.requestFocusInWindow(); // Ensure focus is on the game panel
            gameTimer.start(); // Start the timer
            collisionTimer.start();
        }

        @Override
        public void componentHidden(java.awt.event.ComponentEvent evt) {
            gameTimer.stop(); // Stop the timer
            collisionTimer.stop();
        }
    });

    // Add menu items to the menu
    gameMenu.add(resumeItem);
    gameMenu.add(endItem);

    // Add the menu to the menu bar
    menuBar.add(gameMenu);
    //frame.setJMenuBar(menuBar);

    // Add the menu bar to the game panel
    gamePanel.setLayout(new BorderLayout());
    gamePanel.add(menuBar, BorderLayout.NORTH);

    
    


        return gamePanel;
    }

    // Create the End Scene
    private static JPanel createEndScene(JPanel mainPanel, CardLayout cardLayout) {
        JPanel endPanel = new JPanel();
        endPanel.setLayout(new BorderLayout());

        JLabel endLabel = new JLabel("Game Over", SwingConstants.CENTER);
        endLabel.setFont(new Font("Arial", Font.BOLD, 24));
        endPanel.add(endLabel, BorderLayout.CENTER);

        JButton menuButton = new JButton("Return to Menu");
        menuButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));
        endPanel.add(menuButton, BorderLayout.SOUTH);

        return endPanel;
    }
}
/*class circle1 extends JPanel {

    private int x = 0;  // X-coordinate of the oval
    private int y = 0;  // Y-coordinate of the oval
    private final int DIAMETER = 30;  // Diameter of the oval
    private int dx = 2; // Velocity in the x-direction
    private int dy = 2; // Velocity in the y-direction
    private final Random random = new Random();  // Random generator for coordinates
    private Timer myTimer;

    public circle1() {
        setOpaque(false); // Makes the panel transparent

        // Timer to update the oval position randomly
        myTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveSmoothly(); // Update the position
                repaint();    // Repaint the oval with new coordinates
            }
        });
        myTimer.start(); // Start the timer
    }
    private void moveSmoothly() {
        int panelWidth = getWidth();  // Get the width of the panel
        int panelHeight = getHeight();  // Get the height of the panel

        // Check for collision with panel borders and reverse direction if needed
        if (x + dx < 0 || x + DIAMETER + dx > panelWidth) {
            dx = -dx; // Reverse x-direction
        }
        if (y + dy < 0 || y + DIAMETER + dy > panelHeight) {
            dy = -dy; // Reverse y-direction
        }

        // Update the position incrementally
        x += dx;
        y += dy;
    }


    // Randomly update the position of the oval
    private void moveRandom() {
        int panelWidth = getWidth();  // Get the panel's width
        int panelHeight = getHeight();  // Get the panel's height

        // Ensure the oval stays within bounds
        x = random.nextInt(panelWidth - DIAMETER);
        y = random.nextInt(panelHeight - DIAMETER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillOval(x, y, DIAMETER, DIAMETER); // Draw the oval at (x, y)
    }
}*/
class circle2 extends JPanel {

    private double x = 200;  // Initial X-coordinate of the sphere
    private double y = 200;  // Initial Y-coordinate of the sphere
    private final int DIAMETER = 30;  // Diameter of the sphere
    private double angle = Math.random() * 2 * Math.PI; // Random starting angle
    private double speed = 2; // Speed of movement
    private Timer myTimer;
    private final Random random = new Random();

    public circle2() {
        setOpaque(false); // Makes the panel transparent
        int panelWidth =1000;  // Get the width of the panel
        int panelHeight = 1000;  // Get the height of the panel
        this.x = random.nextInt(panelWidth - DIAMETER);
        this.y = random.nextInt(panelHeight - DIAMETER);

        // Timer to update the position periodically
        myTimer = new Timer(10, e -> {
            moveRandomCurve(); // Update position
            repaint();         // Repaint with updated coordinates
        });
        myTimer.start(); // Start the timer
    }
    public void respawn() {
        int panelWidth = getParent().getWidth();
        int panelHeight = getParent().getHeight();
        x = random.nextInt(panelWidth - DIAMETER);
        y = random.nextInt(panelHeight - DIAMETER);
        repaint();
    }
    
    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, DIAMETER, DIAMETER);
    }

    // Incrementally update the position of the sphere
    private void moveRandomCurve() {
        int panelWidth = getWidth();  // Get the width of the panel
        int panelHeight = getHeight();  // Get the height of the panel

        // Update position using sine and cosine for curved motion
        x += speed * Math.cos(angle);
        
        y += speed * Math.sin(angle);
        

        // Gradually change the angle for randomness
        angle += Math.random() * 0.2 -0.1 ; // Small random adjustments to the angle

        // Check for collision with panel borders and reflect direction
        if (x < 0 || x + DIAMETER > panelWidth) {
            angle = Math.PI - angle; // Reflect horizontally
        }
        if (y < 0 || y + DIAMETER > panelHeight) {
            angle = -angle; // Reflect vertically
        }
    }
    public void setPaused(boolean paused) {
        if (paused) {
            myTimer.stop(); // Stop the timer
        } else {
            myTimer.start(); // Restart the timer
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN); // Change color if desired
        g.fillOval((int) x, (int) y, DIAMETER, DIAMETER); // Draw the sphere at (x, y)
    }
}



class Tri extends JPanel {

    private double x = 200;  // Initial X-coordinate of the sphere
    private double y = 200;  // Initial Y-coordinate of the sphere
    private final int DIAMETER = 30;  // Diameter of the sphere
    private double angle = Math.random() * 2 * Math.PI; // Random starting angle
    private double speed; // Speed of movement
    private Timer myTimer;
    private final Random random = new Random();
    //private final Random random = new Random();

    public void setSpeed(double speed) {
        this.speed = (int) speed;
    }
    public void respawn() {
        int panelWidth = getParent().getWidth();
        int panelHeight = getParent().getHeight();
        x = random.nextInt(panelWidth - DIAMETER);
        y = random.nextInt(panelHeight - DIAMETER);
        repaint();
    }
    
    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, DIAMETER, DIAMETER);
    }

    public Tri(double speed) {
        setOpaque(false); // Makes the panel transparent
       
        this.speed = speed;
        // Timer to update the position periodically
        myTimer = new Timer(10, e -> {
            moveRandomCurve(); // Update position
            repaint();         // Repaint with updated coordinates
        });
        myTimer.start(); // Start the timer
    }

    // Incrementally update the position of the sphere
    private void moveRandomCurve() {
        int panelWidth = getWidth();  // Get the width of the panel
        int panelHeight = getHeight();  // Get the height of the panel

        // Update position using sine and cosine for curved motion
        x += speed * Math.cos(angle);
        
        y += speed * Math.sin(angle);
        

        // Gradually change the angle for randomness
        angle += Math.random() * 0.2 - 0.1; // Small random adjustments to the angle

        // Check for collision with panel borders and reflect direction
        if (x < 0 || x + DIAMETER > panelWidth) {
            angle = Math.PI - angle; // Reflect horizontally
        }
        if (y < 0 || y + DIAMETER > panelHeight) {
            angle = -angle; // Reflect vertically
        }
    }
    public void setPaused(boolean paused) {
        if (paused) {
            myTimer.stop(); // Stop the timer
        } else {
            myTimer.start(); // Restart the timer
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        //g.fillRect((int) x, (int) y, DIAMETER, DIAMETER);

        // Define the vertices of the triangle
        int[] xPoints = {(int) x, (int) x + DIAMETER / 2, (int) x - DIAMETER / 2};
        int[] yPoints = {(int) y, (int) y + DIAMETER, (int) y + DIAMETER};

        // Draw the triangle
        g.fillPolygon(xPoints, yPoints, 3);
        
    }
}

class MouseControlledOval extends JPanel {
    private int x = 0; // X-coordinate of the oval
    private int y = 0; // Y-coordinate of the oval
    private final int DIAMETER = 50; // Diameter of the oval
    private MouseMotionAdapter mouseAdapter; // Declare the adapter
    @Override
public Rectangle getBounds() {
    return new Rectangle(x, y, DIAMETER, DIAMETER);
}


    public MouseControlledOval() {
        setOpaque(false); // Make the background transparent

        // Create the MouseMotionAdapter
        mouseAdapter = new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                // Update the position of the oval to follow the mouse
                x = e.getX() - DIAMETER / 2; // Center the oval around the mouse
                y = e.getY() - DIAMETER / 2;
                repaint(); // Redraw the oval at the new position
            }
        };

        // Add the mouse listener initially
        addMouseMotionListener(mouseAdapter);
    }

    // Enable mouse control
    public void connectMouseControl() {
        addMouseMotionListener(mouseAdapter);
    }

    // Disable mouse control
    public void disconnectMouseControl() {
        removeMouseMotionListener(mouseAdapter);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE); // Set the color of the oval
        g.fillOval(x, y, DIAMETER, DIAMETER); // Draw the oval at (x, y)
    }
}
