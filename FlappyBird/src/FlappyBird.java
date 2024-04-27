import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    // Tambahkan variabel untuk menandai status permainan
    boolean gameOver = false;
    // Variabel untuk skor
    int score = 0;
    JLabel scoreLabel;
    int frameWidth = 360;
    int frameHeight = 640;

    // image attributes
    Image backgroundImage;
    Image birdImage;
    Image lowerPipeImage;
    Image upperPipeImage;

    // player
    int playerStartPosX = frameWidth / 8;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;
    Player player;

    // pipes attribute
    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    ArrayList<Pipe> pipes;

    // game logic
    Timer gameLoop;
    Timer pipesCooldown;
    int gravity = 1;

    // constructor
    public FlappyBird() {
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setFocusable(true);
        addKeyListener(this);
//        setBackground(Color.cyan);

        // Inisialisasi skor awal
        score = 0;

        // Inisialisasi JLabel untuk menampilkan skor
        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(20, 20, 200, 30);
        add(scoreLabel);

        // load images
        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("assets/bintang.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();

        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);
        pipes = new ArrayList<Pipe>();

        // pipes cooldown timer
        pipesCooldown = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        pipesCooldown.start();

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    public void placePipes() {
        int randomPipePosY = (int) (pipeStartPosY - pipeHeight/4 - Math.random() * (pipeHeight/2));
        int openingSpace = frameHeight/4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPipePosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, randomPipePosY + pipeHeight + openingSpace, pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

        if (gameOver) {
            // Gambar teks "Game Over" dan "Press R to Restart"
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            FontMetrics fontMetrics = g.getFontMetrics();
            String gameOverText = "Game Over";
            String restartText = "Press R to Restart";
            int gameOverX = (frameWidth - fontMetrics.stringWidth(gameOverText)) / 2;
            int restartX = (frameWidth - fontMetrics.stringWidth(restartText)) / 2;
            int textY = frameHeight / 2;
            g.drawString(gameOverText, gameOverX, textY - 50);
            g.drawString(restartText, restartX, textY + 50);
        }
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, frameWidth, frameHeight, null);

        g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
        }
    }

    public void move() {
        if (!gameOver) {
            player.setVelocityY(player.getVelocityY() + gravity);
            player.setPosY(player.getPosY() + player.getVelocityY());
            player.setPosY(Math.max(player.getPosY(), 0));

            for (Pipe pipe : pipes) {
                // Check collision with pipes
                if (player.getPosX() < pipe.getPosX() + pipe.getWidth() && player.getPosX() + player.getWidth() > pipe.getPosX() &&
                        player.getPosY() < pipe.getPosY() + pipe.getHeight() && player.getPosY() + player.getHeight() > pipe.getPosY()) {
                    // Collision detected, game over
                    gameOver = true;
                    break;
                }

                // Check if player passes the pipe
                if (!pipe.isPassed() && pipe.getPosX() + pipe.getWidth() < player.getPosX()) {
                    // Ensure the pipe being passed is the upper one (to avoid double-scoring)
                    if (pipe.getImage() == upperPipeImage) {
                        pipe.setPassed(true);
                        score++; // Increase the score
                        scoreLabel.setText("Score: " + score); // Update the score label
                    }
                }
            }

            // Check for game over conditions
            if (player.getPosY() + player.getHeight() >= frameHeight || player.getPosY() <= 0) {
                // Player hits the bottom or top of the frame
                gameOver = true;
            }

            // Update pipe positions
            if (!gameOver) {
                for (Pipe pipe : pipes) {
                    pipe.setPosX(pipe.getPosX() + pipe.getVelocityX());
                }
            }
        }
    }



    // Implementasikan metode restartGame()
    private void restartGame() {
        // Reset semua variabel dan kondisi permainan
        gameOver = false;
        score = 0; // Reset skor menjadi 0
        scoreLabel.setText("Score: " + score); // Update label skor
        player.setPosY(playerStartPosY);
        pipes.clear();
        pipesCooldown.restart();
        gameLoop.restart();
        // Repaint panel
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.setVelocityY(-10);
        } else if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            restartGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
