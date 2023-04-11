import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.event.KeyEvent;

public class Racket implements Runnable {
   
    private Field field;
    public double height;
    public double width;
    private Color color;

    public double startPosition;

    private boolean moovingUp;
    private boolean moovingDown;

    private double x;
    private double y;

    private double speed = 5;

    private int keyCodeUp;
    private int keyCodeDown;

    public Racket(Field field, double x, double y, int keyCodeUp, int keyCodeDown) {
        
        this.field = field;
        this.keyCodeUp = keyCodeUp;
        this.keyCodeDown = keyCodeDown;

        height = 100;
        width = 10;

        color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
        this.x = x + width;
        this.y = y + height;
        startPosition = this.y;

        Thread thisThread = new Thread(this);
        thisThread.start();
    }

    public void run() {
        try {
            while(true) {
                field.canMove(this);

                if(y >= field.getHeight()){
                    
                }else if(moovingUp){
                    y += speed;
                }

                if(y <= height){
                    
                }else if(moovingDown){
                    y -= speed;
                }
                Thread.sleep(10);
            }
        } catch (InterruptedException ex) {

        }
    }

    // Метод прорисовки самого себя
    public void paint(Graphics2D canvas) {
        canvas.setColor(color);
        canvas.setPaint(color);
        Rectangle2D.Double racket = new Rectangle2D.Double(x - width, y - height, width, height);
        canvas.draw(racket);
        canvas.fill(racket);

        canvas.setColor(Color.BLACK);
        canvas.setPaint(Color.BLACK);
        Ellipse2D.Double dot = new Ellipse2D.Double(x, y, 1, 1);
        canvas.draw(dot);
        canvas.fill(dot);
    }

    public synchronized double getX(){
        return x;
    }
    
    public synchronized void setYDefault(){
        y = startPosition;
    }

    public synchronized double getY(){
        return y;
    }


    public int getKeyCodeUp(){
        return keyCodeUp;
    }

    public int getKeyCodeDown(){
        return keyCodeDown;
    }

    public synchronized void moveUp(KeyEvent e){
        if(e.getID() == KeyEvent.KEY_PRESSED){
            moovingUp = true;
        }else if(e.getID() == KeyEvent.KEY_RELEASED){
            moovingUp = false;
        }
    }

    public synchronized void moveDown(KeyEvent e){
        if(e.getID() == KeyEvent.KEY_PRESSED){
            moovingDown = true;
        }else if(e.getID() == KeyEvent.KEY_RELEASED){
            moovingDown = false;
        }
    }
}


