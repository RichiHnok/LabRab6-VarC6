import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.Box;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JDialog;


public class MainFrame extends JFrame{
    // Главный метод приложения
    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    public boolean isGameOver;

    public JLabel p1Counter;
    public JLabel getP1Counter(){
        return p1Counter;
    }

    public JLabel p2Counter;
    public JLabel getP2Counter(){
        return p2Counter;
    }

    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    private JMenuItem pauseMenuItem;
    private JMenuItem resumeMenuItem;


    private Field field = new Field(this);

    //@ установка игровых объектов в начальное положение
    public void reload(){
        field.getRacket1().setYDefault();
        field.getRacket2().setYDefault();
        field.ball.reload();
        if(!pauseMenuItem.isEnabled() && !resumeMenuItem.isEnabled()){
            pauseMenuItem.setEnabled(true);
        }
        field.pause();
        pauseMenuItem.setEnabled(false);
        resumeMenuItem.setEnabled(true);
    }

    //@ операция завершения игры
    public void congrats(int player){
        JDialog congratsWindow = new JDialog(this, "Поздравление");
        Toolkit kit = Toolkit.getDefaultToolkit();
        congratsWindow.setLocation((kit.getScreenSize().width - 300)/2, (kit.getScreenSize().height - 120)/2);

        JLabel info = new JLabel("Поздравляем игрока " + player + " с победой!");

        Box box = Box.createHorizontalBox();
        box.add(Box.createHorizontalGlue());
        box.add(info);
        box.add(Box.createHorizontalGlue());

        reload();

        congratsWindow.setSize(300, 120);
        congratsWindow.add(box);
        congratsWindow.setVisible(true);

        if(!pauseMenuItem.isEnabled() && !resumeMenuItem.isEnabled()){
            pauseMenuItem.setEnabled(true);
        }
        field.pause();
        pauseMenuItem.setEnabled(false);
        resumeMenuItem.setEnabled(true);
        isGameOver = true;;
    }

    // Конструктор главного окна приложения
    public MainFrame() {
        super("Программирование и синхронизация потоков");

        //@ добавляем главному окну способность воспринимать клавиши
        this.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                field.takeKeyEvent(e);
            }

            public void keyReleased(KeyEvent e){
                if(!field.paused && e.getKeyCode() == 32 && !isGameOver){
                    //@ пауза игры при нажатии пробела
                    field.pause();
                    pauseMenuItem.setEnabled(false);
                    resumeMenuItem.setEnabled(true);
                }else if(field.paused && e.getKeyCode() == 32 && !isGameOver){
                    //@ продолжение игры при нажатии пробела
                    field.resume();
                    pauseMenuItem.setEnabled(true);
                    resumeMenuItem.setEnabled(false);    
                }else if(isGameOver && e.getKeyCode() == 32){
                    //@ перезапуск игры пробелом после её оканчания
                    isGameOver = false;
                    field.P1points = 0;
                    field.P2points = 0;
                    reload();
                    p1Counter.setText("0/" + field.maxPoints);
                    p2Counter.setText("0/" + field.maxPoints);
                }else{
                    //@ все остальные кнопки
                    field.takeKeyEvent(e);
                }
            }
        });

        setSize(WIDTH, HEIGHT);

        Toolkit kit = Toolkit.getDefaultToolkit();

        setLocation((kit.getScreenSize().width - WIDTH)/2, (kit.getScreenSize().height - HEIGHT)/2);
        setExtendedState(MAXIMIZED_BOTH);
        
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        //@ запуск игры
        JMenu gameMenu = new JMenu("Игра");
        Action addGameAction = new AbstractAction("Запустить игру"){
            public void actionPerformed(ActionEvent event){
                isGameOver = false;
                field.addRacket();
                // field.gameIsOn = true;
                field.addBall();
                if(!pauseMenuItem.isEnabled() && !resumeMenuItem.isEnabled()){
                    pauseMenuItem.setEnabled(true);
                }
                field.pause();
                pauseMenuItem.setEnabled(false);
                resumeMenuItem.setEnabled(true);
            }
        };
        menuBar.add(gameMenu);
        gameMenu.add(addGameAction);
        

        JMenu controlMenu = new JMenu("Управление");
        menuBar.add(controlMenu);
        //@ пауза
        Action pauseAction = new AbstractAction("Приостановить движение"){
            public void actionPerformed(ActionEvent event) {
                field.pause();
                pauseMenuItem.setEnabled(false);
                resumeMenuItem.setEnabled(true);
            }
        };
        pauseMenuItem = controlMenu.add(pauseAction);
        pauseMenuItem.setEnabled(false);
        //@ продолжение
        Action resumeAction = new AbstractAction("Возобновить движение") {
            public void actionPerformed(ActionEvent event) {
                field.resume();
                pauseMenuItem.setEnabled(true);
                resumeMenuItem.setEnabled(false);
            }
        };
        resumeMenuItem = controlMenu.add(resumeAction);
        resumeMenuItem.setEnabled(false);

        //@ панель со счётом
        Box boxTop = Box.createHorizontalBox();

        JLabel p1Sign = new JLabel("Игрок 1:");
        p1Sign.setFont(new Font("Verdana", Font.PLAIN, 20));
        p1Counter = new JLabel("0/" + field.maxPoints);
        p1Counter.setFont(new Font("Verdana", Font.PLAIN, 20));
        
        JLabel p2Sign = new JLabel("Игрок 2:");
        p2Sign.setFont(new Font("Verdana", Font.PLAIN, 20));
        p2Counter = new JLabel("0/" + field.maxPoints);
        p2Counter.setFont(new Font("Verdana", Font.PLAIN, 20));

        boxTop.add(Box.createHorizontalStrut(30));
        boxTop.add(p1Sign);
        boxTop.add(Box.createHorizontalStrut(10));
        boxTop.add(p1Counter);

        boxTop.add(Box.createHorizontalGlue());

        boxTop.add(p2Sign);
        boxTop.add(Box.createHorizontalStrut(10));
        boxTop.add(p2Counter);
        boxTop.add(Box.createHorizontalStrut(30));

        getContentPane().add(boxTop, BorderLayout.NORTH);

        // Добавить в центр граничной компоновки поле Field
        getContentPane().add(field, BorderLayout.CENTER);

    }
    
}
