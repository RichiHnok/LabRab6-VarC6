import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class BouncingBall implements Runnable {
    // Максимальный радиус, который может иметь мяч
    private static final int MAX_RADIUS = 10;
    // Минимальный радиус, который может иметь мяч
    private static final int MIN_RADIUS = 5;
    // Максимальная скорость, с которой может летать мяч
    private static final int MAX_SPEED = 8;
    private Field field;
    private int radius;
    private Color color;
    // Текущие координаты мяча
    private double x;
    private double y;
    // Вертикальная и горизонтальная компонента скорости
    private double speed;
    private double speedX;
    private double speedY;

    public double r1x;
    public double r1xw;
    public double r1y;
    public double r1yh;

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
        // угол в пределах от 0 до 2PI
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
        x = Math.random()*(field.getSize().getWidth()-2*radius) + radius;
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
                // Синхронизация потоков на самом объекте поля
                // Если движение разрешено - управление будет 
                // возвращено в метод
                // В противном случае - активный поток заснѐт
                field.canMove(this);
                if (
                    x + speedX >= r1xw && x + speedX <= r1x
                    &&
                    y + speedY >= r1yh && y + speedY <= r1y
                ){
                    speedX = -speedX;
                    x = r1x - 1;
                    speedX *= 1.0;                         
                
                }else if (
                    x + speedX >= r2xw && x + speedX <= r2x
                    &&
                    y + speedY >= r2yh && y + speedY <= r2y
                ){
                    speedX = -speedX;
                    x = r2xw;
                    speedX *= 1.0;                         
                
                }else if (x + speedX <= radius) {
                    // Достигли левой стенки, отскакиваем право
                    speedX = -speedX;
                    x = radius;
                    speedX *= 1.0; 
                } else if (x + speedX >= field.getWidth() - radius) {
                    // Достигли правой стенки, отскок влево
                    speedX = -speedX;
                    x = Double.valueOf(field.getWidth()-radius).intValue();
                    speedX *= 1.0; 
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

                // if(x - radius >= field.getRacket1().getX() &&
                //     x - radius <= field.getRacket1().getX() + field.getRacket1().width
                // ){
                //     speedX = -speedX;
                //     x = radius;
                //     speedX *= 1.05; 
                // }

                // if(x + 2*radius >= field.getRacket2().getX() && y + radius >= field.getRacket2().up && y + radius <= field.getRacket2().down){
                //     speedX = -speedX;
                //     x = Double.valueOf(field.getWidth()-radius).intValue();
                //     speedX *= 1.05; 
                // }
                // Засыпаем на X миллисекунд, где X определяется 
                // исходя из скорости
                // Скорость = 1 (медленно), засыпаем на 15 мс.
                // Скорость = 15 (быстро), засыпаем на 1 мс.
                Thread.sleep(5);
            }
        } catch (InterruptedException ex) {
            // Если нас прервали, то ничего не делаем 
            // и просто выходим (завершаемся)
        }
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
