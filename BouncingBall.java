import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class BouncingBall implements Runnable {
    // Максимальный радиус, который может иметь мяч
    private static final int MAX_RADIUS = 15;
    // Минимальный радиус, который может иметь мяч
    private static final int MIN_RADIUS = 10;
    // Максимальная скорость, с которой может летать мяч
    private static final int MAX_SPEED = 14;

    private Field field;
    private int radius;
    private Color color;

    public int maxCount = 3;
    // Текущие координаты мяча
    private double x;
    private double y;

    private double speed;
    // Вертикальная и горизонтальная компонента скорости
    private double speedX;
    private double speedY;
    private double acceleration = 1.1;

    //@ область ракетки 1
    public double r1x;
    public double r1xw;
    public double r1y;
    public double r1yh;

    //@ область ракетки 2
    public double r2x;
    public double r2xw;
    public double r2y;
    public double r2yh;

    public BouncingBall(Field field) {
        // Необходимо иметь ссылку на поле, по которому прыгает мяч, 
        // чтобы отслеживать выход за его пределы 
        // через getWidth(), getHeight()
        this.field = field;
        // Радиус мяча случайного размера
        radius = Double.valueOf(Math.random()*(MAX_RADIUS - MIN_RADIUS)).intValue() + MIN_RADIUS;
        // Абсолютное значение скорости зависит от диаметра мяча, 
        // чем он больше, тем медленнее
        speed = Double.valueOf(Math.round(5*MAX_SPEED / radius)).intValue() - 2;
        if (speed>MAX_SPEED) {
            speed = MAX_SPEED;
        }
        // Начальное направление скорости тоже случайно, 
        // угол в пределах [-pi4; pi/4]+[pi3/4; pi5/4]
        double angle = 0;
        do{
            angle = Math.random()*2*Math.PI;
        }while(angle >= 0.7854 && angle <= 2.356 || angle >= 3.927 && angle <=5.498);
        // Вычисляются горизонтальная и вертикальная компоненты скорости
        speedX = 3*Math.cos(angle);
        speedY = 3*Math.sin(angle);
        // Цвет мяча выбирается случайно
        color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random());
        // Начальное положение мяча случайно
        x = field.getSize().getWidth()/2;
        y = Math.random()*(field.getSize().getHeight()-2*radius) + radius;
        // Создаѐм новый экземпляр потока, передавая аргументом 
        // ссылку на класс, реализующий Runnable (т.е. на себя)
        Thread thisThread = new Thread(this);
        // Запускаем поток
        thisThread.start();
    }

    // Метод run() исполняется внутри потока. Когда он завершает работу,
    // то завершается и поток
    public void run() {
        try {
            // Крутим бесконечный цикл, т.е. пока нас не прервут, 
            // мы не намерены завершаться
            while(true) {
                r1x = field.getRacket1().getX();
                r1xw = r1x - field.getRacket1().width;
                r1y = field.getRacket1().getY();
                r1yh = r1y - field.getRacket1().height;
                
                r2x = field.getRacket2().getX();
                r2xw = r2x - field.getRacket2().width;
                r2y = field.getRacket2().getY();
                r2yh = r2y - field.getRacket2().height;

                field.canMove(this);
                if (
                    x + speedX >= r1xw && x + speedX <= r1x
                    &&
                    y + speedY >= r1yh && y + speedY <= r1y
                ){
                    //@ отскок от ракетки 1
                    speedX = -speedX;
                    x = r1x - 1;
                    speedX *= acceleration;                         
                
                }else if (
                    x + speedX >= r2xw && x + speedX <= r2x
                    &&
                    y + speedY >= r2yh && y + speedY <= r2y
                ){
                    //@ отскок от ракетки 2
                    speedX = -speedX;
                    x = r2xw;
                    speedX *= acceleration;                         
                
                }else if (x + speedX <= radius) {
                    //@ достигли левой стенки
                    speedX = -speedX;
                    x = radius;

                    field.P2points++;

                    field.getFrame().getP2Counter().setText(field.P2points + "/" + field.maxPoints);
                    if(field.P2points == field.maxPoints){
                        field.getFrame().congrats(2);
                        field.getFrame().getP1Counter().setText(field.P1points + "/" + field.maxPoints);
                    }else{
                        field.getFrame().reload();
                    }
                } else if (x + speedX >= field.getWidth() - radius) {
                    //@ достгли правой стенки
                    speedX = -speedX;
                    x = Double.valueOf(field.getWidth()-radius).intValue();

                    field.P1points++;

                    field.getFrame().getP1Counter().setText(field.P1points + "/" + field.maxPoints);
                    if(field.P1points == field.maxPoints){
                        field.getFrame().congrats(1);
                        field.getFrame().getP2Counter().setText(field.P2points + "/" + field.maxPoints);
                    }else{
                        field.getFrame().reload();
                    }
                } else if (y + speedY <= radius) {
                    // Достигли верхней стенки
                    speedY = -speedY;
                    y = radius;
                    speedY *= 1.0;
                } else if (y + speedY >= field.getHeight() - radius) {
                    // Достигли нижней стенки
                    speedY = -speedY;
                    y = Double.valueOf(field.getHeight()-radius).intValue();
                    speedY *= 1.0;
                } else{
                    // Просто смещаемся
                    x += speedX;
                    y += speedY;
                }

                Thread.sleep(5);
            }
        } catch (InterruptedException ex) {

        }
    }

    //@ установка мяча в позицию по умолчанию
    public void reload(){
        double angle = 0;
        do{
            angle = Math.random()*2*Math.PI;
        }while(angle >= 0.7854 && angle <= 2.356 || angle >= 3.927 && angle <=5.498);
        speedX = 3*Math.cos(angle);
        speedY = 3*Math.sin(angle);
        x = field.getSize().getWidth()/2;
        y = Math.random()*(field.getSize().getHeight()-2*radius) + radius;
    }

    // Метод прорисовки самого себя
    public void paint(Graphics2D canvas) {
        canvas.setColor(color);
        canvas.setPaint(color);
        Ellipse2D.Double ball = new Ellipse2D.Double(x - radius, y - radius, 2 * radius, 2 * radius);
        canvas.draw(ball);
        canvas.fill(ball);

        canvas.setColor(Color.BLACK);
        canvas.setPaint(Color.BLACK);
        Ellipse2D.Double dot = new Ellipse2D.Double(x, y, 1, 1);
        canvas.draw(dot);
        canvas.fill(dot);

        canvas.setColor(Color.PINK);
        canvas.setPaint(Color.PINK);
        Ellipse2D.Double dot2 = new Ellipse2D.Double(x+radius, y+radius, 1, 1);
        canvas.draw(dot2);
        canvas.fill(dot2);
    }
}
