import java.awt.Color;
import java.awt.Graphics2D;
// import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;

import java.awt.event.KeyAdapter;
// import java.awt.;

public class Racket implements Runnable {
    // Максимальный радиус, который может иметь мяч
    // private static final int MAX_RADIUS = 40;
    // Минимальный радиус, который может иметь мяч
    // private static final int MIN_RADIUS = 3;
    // Максимальная скорость, с которой может летать мяч
    // private static final int MAX_SPEED = 15;
    private Field field;
    // private int radius;
    private int height;
    private int width;
    private Color color;
    // Текущие координаты мяча
    private double x;
    private double y;

    private int keyCodeUp;
    private int keyCodeDown;

    public Racket(Field field, double x, double y, int keyCodeUp, int keyCodeDown) {
        
        this.field = field;
        this.keyCodeUp = keyCodeUp;
        this.keyCodeDown = keyCodeDown;

        height = 100;
        width = 10;

        color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
        this.x = x;
        this.y = y;
        Thread thisThread = new Thread(this);
        thisThread.start();
    }

    // Метод run() исполняется внутри потока. Когда он завершает работу,
    // то завершается и поток
    public void run() {
        try {
            // Крутим бесконечный цикл, т.е. пока нас не прервут, 
            // мы не намерены завершаться
            // while(true) {
            //     // Синхронизация потоков на самом объекте поля
            //     // Если движение разрешено - управление будет 
            //     // возвращено в метод
            //     // В противном случае - активный поток заснѐт
            //     field.canMove(this);
            //     if (x + speedX <= radius) {
            //         // Достигли левой стенки, отскакиваем право
            //         speedX = -speedX;
            //         x = radius;
            //     } else if (x + speedX >= field.getWidth() - radius) {
            //         // Достигли правой стенки, отскок влево
            //         speedX = -speedX;
            //         x = Double.valueOf(field.getWidth()-radius).intValue();
            //     } else if (y + speedY <= radius) {
            //         // Достигли верхней стенки
            //         speedY = -speedY;
            //         y = radius;
            //     } else if (y + speedY >= field.getHeight() - radius) {
            //         // Достигли нижней стенки
            //         speedY = -speedY;
            //         y = Double.valueOf(field.getHeight()-radius).intValue();
            //     } else {
            //         // Просто смещаемся
            //         x += speedX;
            //         y += speedY;
            //     }
            //     // Засыпаем на X миллисекунд, где X определяется 
            //     // исходя из скорости
            //     // Скорость = 1 (медленно), засыпаем на 15 мс.
            //     // Скорость = 15 (быстро), засыпаем на 1 мс.
                Thread.sleep(16000);
            // }
        } catch (InterruptedException ex) {
            // Если нас прервали, то ничего не делаем 
            // и просто выходим (завершаемся)
        }
    }

    // Метод прорисовки самого себя
    public void paint(Graphics2D canvas) {
        canvas.setColor(color);
        canvas.setPaint(color);
        Rectangle2D.Double ball = new Rectangle2D.Double(x - width, y - height, width, height);
        canvas.draw(ball);
        canvas.fill(ball);
    }

    public void setX(double x){
        this.x = x; 
    }

    public double getX(){
        return x;
    }

    public void setY(double y){
        this.y = y;
    }

    public double getY(){
        return y;
    }
}


