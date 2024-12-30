import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SpiralRun extends JPanel implements ActionListener, KeyListener
{
    boolean init=false;

    int windowWidth = 700;
    int windowHeight = 700;
    int originX = windowWidth / 2;
    int originY = windowHeight / 2;

    double scaleFactor = 1.0;

    ball ball;
    
    int velocityX = -4;
    int velocityY = 0;
    int gravity = 1;
    int ballX = windowWidth/2 - 20;
    int ballY = windowHeight/2 - 90;
    int ballRadius = 10;
    int dotRadius = 4;
    
    ArrayList<wallDots> wallDots;
    double maxTheta = 4 * Math.PI; 
    double preMaxTheta=0;

    ArrayList<obstacleDots> obstacleDots;
    Random random = new Random();

    double score=0;

    Timer gameLoop;
    Timer placeWallDotTimer;
    Timer zoomTimer;
    boolean gameOver = false;

    SpiralRun()
    {
        setPreferredSize(new Dimension(windowWidth, windowHeight));
        setFocusable(true);
        addKeyListener(this);

        ball = new ball(Color.WHITE);
        wallDots = new ArrayList<wallDots>();
        obstacleDots = new ArrayList<>();
        
        addWall(preMaxTheta);
        preMaxTheta = maxTheta;
        
        placeWallDotTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                addWall(preMaxTheta);
                preMaxTheta = preMaxTheta + 0.1;
            }
        });

        zoomTimer = new Timer(1400, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                dotRadius += 1;
                ballRadius += 1;
            }
        });
    
        placeWallDotTimer.start();
        zoomTimer.start();
        gameLoop = new Timer(1000/60, this);
        gameLoop.start();
    }

    class ball {
        private int x = ballX;
        private int y = ballY;
        private int radius = ballRadius;
        private Color color;

        public ball(Color color) {
            this.color = color;
        }

        public void draw(Graphics2D g2) {
            g2.setColor(color);
            g2.fillOval(x - radius, y - radius, radius * 2, radius * 2);
        }
    }

    class wallDots
    {
        int x = windowWidth / 2;;
        int y = windowHeight / 2;
        int radius = dotRadius;
        boolean hasObs = false;
        boolean passed = false;
        double theta;

        wallDots()
        {
            
        }
    }

    class obstacleDots {
        int x=0;
        int y=0;
        int radius = dotRadius;
        Color dotsColor;
    
        public obstacleDots() {

        }
    }

    public void addWall(double preMaxTheta)
    {
        double scale = 20; 
        double maxTheta = 4 * Math.PI;
        // System.out.println(preMaxTheta + " " + maxTheta);
        double step = 0.1;

        if(init == false)
            for (double theta = preMaxTheta; theta < maxTheta; theta += step) 
            {
                wallDots wallDot = new wallDots();
                double r = theta;

                double x = r * Math.cos(theta);
                double y = r * Math.sin(theta);
                
                wallDot.x = wallDot.x + (int) (x * scale);
                wallDot.y = wallDot.y - (int) (y * scale);
                wallDot.theta = Math.atan2(wallDot.y - 400, wallDot.x - 400);
                wallDots.add(wallDot);
                init = true;

                if (random.nextDouble() < 0.15 && wallDots.size()>100) 
                { 
                    wallDot.hasObs = true;
                    Random dotNumber = new Random();
                    int randDots = dotNumber.nextInt(7-3) + 3;
                    Color randColor = new Color((int)(Math.random() * 0x1000000));
                    for (int i = 0; i < randDots; i++) 
                    {
                        obstacleDots obstacleDot = new obstacleDots();
                        obstacleDot.dotsColor = randColor;
                
                        if(wallDot.x > 400 && wallDot.y<400)
                        {
                            obstacleDot.x = wallDot.x - (int)(8*(i+1)*Math.cos(theta));
                            obstacleDot.y = wallDot.y + (int)(8*(i+1)*Math.sin(theta));
                        }
                        else if(wallDot.x == 400 && wallDot.y<400)
                        {
                            obstacleDot.y = wallDot.y + (int)(8*(i+1)*Math.sin(theta));
                        }
                        else if(wallDot.x<400 && wallDot.y<400)
                        {
                            obstacleDot.x = wallDot.x - (int)(8*(i+1)*Math.cos(theta));
                            obstacleDot.y = wallDot.y + (int)(8*(i+1)*Math.sin(theta));
                        }
                        else if(wallDot.x > 400 && wallDot.y>400)
                        {
                            obstacleDot.x = wallDot.x - (int)(8*(i+1)*Math.cos(theta));
                            obstacleDot.y = wallDot.y + (int)(8*(i+1)*Math.sin(theta));
                        }
                        else if(wallDot.x == 400 && wallDot.y>400)
                        {
                            obstacleDot.y = wallDot.y - (int)(8*(i+1)*Math.sin(theta));
                        }
                        else if(wallDot.x<400 && wallDot.y>400)
                        {
                            obstacleDot.x = wallDot.x - (int)(8*(i+1)*Math.cos(theta));
                            obstacleDot.y = wallDot.y + (int)(8*(i+1)*Math.sin(theta));
                        }
                        
                        obstacleDots.add(obstacleDot);
                        // System.out.println(obstacleDot.x + " " + obstacleDot.y);    
                    }
                    // System.out.println("stop");
                }
            }
        else
        {
            wallDots wallDot = new wallDots();
            double r = preMaxTheta;

            double x = r * Math.cos(preMaxTheta);
            double y = r * Math.sin(preMaxTheta);
                
            wallDot.x = wallDot.x + (int) (x * scale);
            wallDot.y = wallDot.y - (int) (y * scale);
            wallDot.theta = Math.atan2(wallDot.y - 400, wallDot.x - 400);
            wallDots.add(wallDot);

            if (random.nextDouble() < 0.15 && wallDots.size()>90) 
            { 
                wallDot.hasObs = true;
                Random dotNumber = new Random();
                int randDots = dotNumber.nextInt(7-3) + 3;
                Color randColor = new Color((int)(Math.random() * 0x1000000));
                for (int i = 0; i < randDots; i++) 
                {
                    obstacleDots obstacleDot = new obstacleDots();
                    obstacleDot.dotsColor = randColor;
                
                    if(wallDot.x > 400 && wallDot.y<400)
                    {
                        obstacleDot.x = wallDot.x - (int)(8*(i+1)*Math.cos(preMaxTheta));
                        obstacleDot.y = wallDot.y + (int)(8*(i+1)*Math.sin(preMaxTheta));
                    }
                    else if(wallDot.x == 400 && wallDot.y<400)
                    {
                        obstacleDot.y = wallDot.y + (int)(8*(i+1)*Math.sin(preMaxTheta));
                    }
                    else if(wallDot.x<400 && wallDot.y<400)
                    {
                        obstacleDot.x = wallDot.x - (int)(8*(i+1)*Math.cos(preMaxTheta));
                        obstacleDot.y = wallDot.y + (int)(8*(i+1)*Math.sin(preMaxTheta));
                    }
                    else if(wallDot.x > 400 && wallDot.y>400)
                    {
                        obstacleDot.x = wallDot.x - (int)(8*(i+1)*Math.cos(preMaxTheta));
                        obstacleDot.y = wallDot.y + (int)(8*(i+1)*Math.sin(preMaxTheta));
                    }
                    else if(wallDot.x == 400 && wallDot.y>400)
                    {
                        obstacleDot.y = wallDot.y - (int)(8*(i+1)*Math.sin(preMaxTheta));
                    }
                    else if(wallDot.x<400 && wallDot.y>400)
                    {
                        obstacleDot.x = wallDot.x - (int)(8*(i+1)*Math.cos(preMaxTheta));
                        obstacleDot.y = wallDot.y + (int)(8*(i+1)*Math.sin(preMaxTheta));
                    }
                    
                    obstacleDots.add(obstacleDot);
                }
            }
        }

        
    }

    int mode =2;

    public void move()
    {
        velocityY += gravity;

        // System.out.println((ball.x/(Math.pow(Math.pow(ball.x,2)+Math.pow(ball.y,2), 0.5))));
        if((ball.x > originX && ball.y < originY) || (ball.x==originX && ball.y < originY)) //1
        {
            mode=1;
            ball.y += velocityY;
            if(ball.x + ball.y < 800 || (ball.x == originX && ball.y < originY)) //ENN
            {
                ball.x -= 3;
            }
            else if(ball.x + ball.y > 800 || (ball.x + ball.y == 800)) //ENE
            {
                ball.x -= 2;
            }
        }
        else if(((ball.x < originX && ball.y < originY) || (ball.x < originX && ball.y == originY))&&mode!=3) //2
        {
            mode=2;
            if(ball.x > ball.y || (ball.x == ball.y)) //WNN
            {
                ball.y += velocityY;
                ball.x -= 3;
            }
            else if(ball.x < ball.y || (ball.x < originX && ball.y == originY)) //WNW
            {
                ball.y += velocityY;
                ball.x -= 2;
            }
        }
        else if((ball.x < originX && ball.y > originY) || (ball.x==originX && ball.y > originY) || mode==3) //3
        {
            mode=3;
            ball.x +=2;
            if(ball.x + ball.y <800 || (ball.x + ball.y == 800)) //WSN
            {
                ball.y += velocityY;
            }
            else if(ball.x + ball.y >800 || (ball.x == originX && ball.y > originY)) //WSS
            {
                ball.y += velocityY;
            }
        }
        else if(((ball.x > originX && ball.y > originY) || (ball.x>originX && ball.y == originY))&& mode!=1) //4
        {
            mode=4;

            if(ball.x + ball.y <800 || (ball.x + ball.y == 800)) //ESE
            {
                ball.y += velocityY;
                ball.x +=2;
            }
            else if(ball.x + ball.y >800 || (ball.x == originX && ball.y > originY)) //ESS
            {
                ball.y += velocityY;
                ball.x +=3;
            }
        }
    }

    public void collision()
    {
        for (wallDots wallDot : wallDots) 
        {
            double distance = Math.sqrt(Math.pow(ball.x - wallDot.x, 2) + Math.pow(ball.y - wallDot.y, 2));
            if (distance <= ball.radius + wallDot.radius) {
                ball.color = Color.red;
                gameOver=true;
            }
            
            if(wallDot.hasObs && !wallDot.passed && (Math.abs((wallDot.y - 400)*(ball.x - 400)) == Math.abs((ball.y - 400)*(wallDot.x - 400))))
            {
                wallDot.passed = true;
                score += 1;
            }
        }

        for (obstacleDots obDot : obstacleDots) 
        {
            double distance = Math.sqrt(Math.pow(ball.x - obDot.x, 2) + Math.pow(ball.y - obDot.y, 2));
            if (distance <= ball.radius + obDot.radius) {
                ball.color = Color.red;
                gameOver=true;
            }
        }
    }
    
    double windowCenter = 0;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D gb = (Graphics2D) g;

        gb.setColor(Color.BLACK);
        gb.fillRect(0, -0, windowWidth, windowHeight);

        gb.setColor(Color.GRAY);
        gb.drawLine(0, originY, windowWidth, originY); 
        gb.drawLine(originX, 0, originX, windowHeight);

        // g.setFont(new Font("Ariel", Font.PLAIN, 15));
        // if(ball.y>getHeight())
        // {
        //     gb.drawString("You win! Score: " + String.valueOf(score), 4*windowWidth/9, 20);
        // }
        // else if(gameOver)
        // {
        //     gb.drawString("Total Score: " + String.valueOf(score), 4*windowWidth/9, 20);
        // }
        // else
        // {
        //     gb.drawString("Score: " + String.valueOf(score), 4*windowWidth/9, 20);
        // }

        g2.translate((getWidth() / 2), (getHeight()/2));
        g2.scale(scaleFactor, scaleFactor);
        if(scaleFactor < 0.5)
        {
            scaleFactor -= 0.0003;
        }
        else
        {
            scaleFactor -= 0.0007;
        }
        g2.translate(-(getWidth() / 2), -(getHeight()/2));


        g2.setColor(Color.pink);

        for(int i=0; i<wallDots.size(); i++)
        {
            wallDots wallDot = wallDots.get(i);
            g2.fillOval(wallDot.x, wallDot.y, wallDot.radius, wallDot.radius);
        }

        for (int i=0; i<obstacleDots.size(); i++) {
            obstacleDots obDot = obstacleDots.get(i);
            g2.setColor(obDot.dotsColor);
            g2.fillOval(obDot.x, obDot.y, obDot.radius, obDot.radius);
        }

        if (ball != null) {
            ball.draw(g2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        move();
        collision();
        repaint();

        if(gameOver)
        {
            placeWallDotTimer.stop();
            zoomTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_SPACE)
        {
            if(gameOver)
            {
                scaleFactor=1;
                windowCenter=0;
                ball.x = ballX;
                ball.y = ballY;
                ball.color = Color.white;
                dotRadius = 4;
                ballRadius = 10;
                velocityY = 0;
                mode =2;
                wallDots.clear();
                obstacleDots.clear();
                maxTheta = 4 * Math.PI; 
                preMaxTheta=0;
                init = false;
                addWall(preMaxTheta);
                preMaxTheta = maxTheta;
                score = 0;
                gameOver = false;
                gameLoop.start();
                zoomTimer.start();
                placeWallDotTimer.start();
            }

            // if(jumpDirection)
                velocityY = -9;
            // else
            //     velocityX = -9;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
    
    }
}
