import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {


    final int[] x = new int[GamePanelConstant.GAME_UNIT];
    final int[] y = new int[GamePanelConstant.GAME_UNIT];

    int bodyParts = 6;

    int applesEaten = 0;
    int appleX;
    int appleY;

    char direction = 'D';

    boolean running = false;

    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(GamePanelConstant.SCREEN_WIDTH, GamePanelConstant.SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(GamePanelConstant.DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
//            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
//                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//            }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, GamePanelConstant.UNIT_SIZE, GamePanelConstant.UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], GamePanelConstant.UNIT_SIZE, GamePanelConstant.UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], GamePanelConstant.UNIT_SIZE, GamePanelConstant.UNIT_SIZE);
                }
            }
            g.setColor(Color.blue);
            g.setFont(new Font("Roboto", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score:" + applesEaten, (GamePanelConstant.SCREEN_WIDTH - metrics.stringWidth("Score:" + applesEaten)) / 2, g.getFont().getSize());

        } else {
            gameOver(g);
        }

    }

    public void newApple() {
        appleX = random.nextInt(GamePanelConstant.SCREEN_WIDTH / GamePanelConstant.UNIT_SIZE) * GamePanelConstant.UNIT_SIZE;
        appleY = random.nextInt(GamePanelConstant.SCREEN_HEIGHT / GamePanelConstant.UNIT_SIZE) * GamePanelConstant.UNIT_SIZE;

    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - GamePanelConstant.UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + GamePanelConstant.UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - GamePanelConstant.UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + GamePanelConstant.UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            this.newApple();
            applesEaten++;
        }
    }

    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            //        Check if head collides with body
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }
        //        check if head collides with left border
        if (x[0] < 0) {
            running = false;
        }
        //            check if head collides with right border
        if (x[0] > GamePanelConstant.SCREEN_WIDTH) {
            running = false;
        }
        //            check if head collides with top border
        if (y[0] < 0) {
            running = false;
        }
        //            check if head collides with bottom border
        if (y[0] > GamePanelConstant.SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
//        Score
        g.setColor(Color.blue);
        g.setFont(new Font("Roboto", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score:" + applesEaten, (GamePanelConstant.SCREEN_WIDTH - metrics1.stringWidth("Score:" + applesEaten)) / 2, g.getFont().getSize());


        g.setColor(Color.blue);
        g.setFont(new Font("Roboto", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game over", (GamePanelConstant.SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, GamePanelConstant.SCREEN_HEIGHT / 2);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent event) {
            switch (event.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
