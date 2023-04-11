import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.KeyEvent;

public class Field extends JPanel {
    public boolean paused;

    //@ ссылка на главное окно
    private MainFrame frame;
    //@ до скольки очков игра продолжается
    public int maxPoints = 3;

    public int P1points = 0;
    public int P2points = 0;

    private Racket racket1;
    private Racket racket2;

    public BouncingBall ball;

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
        ball = new BouncingBall(this);
    }
    
    public void addRacket(){
        //& коды клавиш: 83 - w; 87 - s; 40 - ArrowUp; 38 - ArrowDown
        racket1 = new Racket(this, this.getSize().getWidth()/15, this.getSize().getHeight()/2, 83, 87);
        racket2 = new Racket(this, this.getSize().getWidth()/15*14, this.getSize().getHeight()/2, 40, 38);
    }

    //@ обработчик клавиш, переданных главным окном
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
    
    public synchronized void pause() {
        paused = true;
    }
    public synchronized void resume() {
        paused = false;
        notifyAll();
    }

    public synchronized void canMove(BouncingBall ball) throws InterruptedException {
        if(paused) wait();
    }

    public synchronized void canMove(Racket racket) throws InterruptedException {
        if(paused) wait();
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
