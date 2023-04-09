import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;
public class Field extends JPanel {
    private boolean paused;

    private Racket racket1;
    private Racket racket2;

    private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);
    private Timer repaintTimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            repaint();
        }
    });

    public Field() {

        setBackground(Color.WHITE);
        repaintTimer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
        for (BouncingBall ball: balls) {
            ball.paint(canvas);
        }
        if(racket1 != null){
            racket1.paint(canvas);
        }
    }
    public void addBall() {
        balls.add(new BouncingBall(this));
    }
    
    public void addRacket(){
        racket1 = new Racket(this, 0, 0, 38, 40);
        racket1 = new Racket(this, 0, 0, 87, 83);
    }
    
    public synchronized void pause() {
        paused = true;
    }
    public synchronized void resume() {
        paused = false;
        notifyAll();
    }
    public synchronized void canMove(BouncingBall ball) throws InterruptedException {
        if (paused) {
            wait();
        }
    }

    public synchronized void canMove(Racket racket) throws InterruptedException {
        if(paused){
            wait();
        }
    }
}
