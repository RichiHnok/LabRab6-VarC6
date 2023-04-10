import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

public class Field extends JPanel {
    public boolean paused;

    private MainFrame frame;

    private Racket racket1;
    // private int keyCodeUp1;
    // private int keyCodeDown1;
    private Racket racket2;
    // private int keyCodeUp2;
    // private int keyCodeDown2;

    public int P1points = 0;
    public int P2points = 0;

    public BouncingBall ball;
    // private ArrayList<BouncingBall> balls = new ArrayList<BouncingBall>(10);

    private Timer repaintTimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            repaint();
        }
    });

    public Field(MainFrame frame) {
        this.frame = frame;
        setBackground(Color.WHITE);
        repaintTimer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D canvas = (Graphics2D) g;
        // for (BouncingBall ball: balls) {
        //     ball.paint(canvas);
        // }
        if(ball != null){
            ball.paint(canvas);
        }
        if(racket1 != null){
            racket1.paint(canvas);
        }

        if(racket2 != null){
            racket2.paint(canvas);
        }
    }

    public void addBall() {
        System.out.println(this.getSize().getWidth());
        System.out.println(this.getHeight());
        // balls.add(new BouncingBall(this));
        ball = new BouncingBall(this);
    }
    
    public void addRacket(){
        racket1 = new Racket(this, this.getSize().getWidth()/15, this.getSize().getHeight()/2, 83, 87);
        // keyCodeUp1 = racket1.getKeyCodeUp();
        // keyCodeDown1 = racket1.getKeyCodeDown();
        // System.out.println(this.getSize().getWidth());
        // System.out.println(this.getHeight());
        racket2 = new Racket(this, this.getSize().getWidth()/15*14, this.getSize().getHeight()/2, 40, 38);
        // keyCodeUp2 = racket2.getKeyCodeUp();
        // keyCodeDown2 = racket2.getKeyCodeDown();

    }

    public void takeKeyEvent(KeyEvent e) {
        int keyCode = e.getKeyCode();
        System.out.println(keyCode);
        if(keyCode == racket1.getKeyCodeUp()){
            racket1.moveUp(e);
        }else if(keyCode == racket1.getKeyCodeDown()){
            racket1.moveDown(e);
        }else if(keyCode == racket2.getKeyCodeUp()){
            racket2.moveUp(e);
        }else if(keyCode == racket2.getKeyCodeDown()){
            racket2.moveDown(e);
        }
    }

    // public void takeKeyReleasedEvent(KeyEvent e){
    //     int keyCode = e.getKeyCode();
    //     if(keyCode == 32){
    //         if(!paused){
    //             pause
    //         }
    //     }
    // }
    
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
    
    public Racket getRacket1(){
        return racket1;
    }

    public Racket getRacket2(){
        return racket2;
    }

    public MainFrame getFrame(){
        return frame;
    }
}
