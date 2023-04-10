import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
// import java.awt.event.KeyListener;
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
        // Создать и сделать видимым главное окно приложения
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public boolean isGameOver;

    public JLabel getP1Counter(){
        return p1Counter;
    }
    public JLabel getP2Counter(){
        return p2Counter;
    }
    // Константы, задающие размер окна приложения, если оно 
    // не распахнуто на весь экран
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    private JMenuItem pauseMenuItem;
    private JMenuItem resumeMenuItem;

    public JLabel p1Counter;
    public JLabel p2Counter;
    // Поле, по которому прыгают мячи
    private Field field = new Field(this);

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
    }

    // Конструктор главного окна приложения
    public MainFrame() {
        super("Программирование и синхронизация потоков");

        this.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                field.takeKeyEvent(e);
            }

            public void keyReleased(KeyEvent e){
                if(!field.paused && e.getKeyCode() == 32 && !isGameOver){
                    field.pause();
                    pauseMenuItem.setEnabled(false);
                    resumeMenuItem.setEnabled(true);
                }else if(field.paused && e.getKeyCode() == 32 && !isGameOver){
                    field.resume();
                    pauseMenuItem.setEnabled(true);
                    resumeMenuItem.setEnabled(false);    
                }else if(isGameOver && e.getKeyCode() == 32){
                    field.P1points = 0;
                    field.P2points = 0;
                    reload();
                }else{
                    field.takeKeyEvent(e);
                }
            }
        });

        setSize(WIDTH, HEIGHT);

        Toolkit kit = Toolkit.getDefaultToolkit();

        setLocation((kit.getScreenSize().width - WIDTH)/2, (kit.getScreenSize().height - HEIGHT)/2);
        // Установить начальное состояние окна развѐрнутым на весь экран
        setExtendedState(MAXIMIZED_BOTH);
        // Создать меню
        
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        JMenu ballMenu = new JMenu("Мячи");
        Action addBallAction = new AbstractAction("Добавить мяч") {
            public void actionPerformed(ActionEvent event) {
                field.addBall();
                if (!pauseMenuItem.isEnabled() && !resumeMenuItem.isEnabled()) {
                    // Ни один из пунктов меню не являются 
                    // доступными - сделать доступным "Паузу"
                    pauseMenuItem.setEnabled(true);
                }
            }
        };
        menuBar.add(ballMenu);
        ballMenu.add(addBallAction);
        
        
        JMenu racketMenu = new JMenu("Игра");
        Action addRacketAction = new AbstractAction("Запустить игру"){
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
        menuBar.add(racketMenu);
        racketMenu.add(addRacketAction);
        

        JMenu controlMenu = new JMenu("Управление");
        menuBar.add(controlMenu);
        Action pauseAction = new AbstractAction("Приостановить движение"){
            public void actionPerformed(ActionEvent event) {
                field.pause();
                pauseMenuItem.setEnabled(false);
                resumeMenuItem.setEnabled(true);
            }
        };
        pauseMenuItem = controlMenu.add(pauseAction);
        pauseMenuItem.setEnabled(false);
        Action resumeAction = new AbstractAction("Возобновить движение") {
            public void actionPerformed(ActionEvent event) {
                field.resume();
                pauseMenuItem.setEnabled(true);
                resumeMenuItem.setEnabled(false);
            }
        };
        resumeMenuItem = controlMenu.add(resumeAction);
        resumeMenuItem.setEnabled(false);


        Box boxTop = Box.createHorizontalBox();

        JLabel p1Sign = new JLabel("Игрок 1:");
        p1Sign.setFont(new Font("Verdana", Font.PLAIN, 20));
        // p1Sign.setPreferredSize(new Dimension(100, 10));
        p1Counter = new JLabel("0");
        p1Counter.setFont(new Font("Verdana", Font.PLAIN, 20));
        // p1Counter.setPreferredSize(new Dimension(100, 10));
        
        JLabel p2Sign = new JLabel("Игрок 2:");
        p2Sign.setFont(new Font("Verdana", Font.PLAIN, 20));
        // p2Sign.setPreferredSize(new Dimension(100, 10));
        p2Counter = new JLabel("0");
        p2Counter.setFont(new Font("Verdana", Font.PLAIN, 20));
        // p2Counter.setPreferredSize(new Dimension(100, 10));


        boxTop.add(Box.createHorizontalStrut(30));
        boxTop.add(p1Sign);
        boxTop.add(Box.createHorizontalStrut(10));
        boxTop.add(p1Counter);

        boxTop.add(Box.createHorizontalGlue());

        boxTop.add(p2Sign);
        boxTop.add(Box.createHorizontalStrut(10));
        boxTop.add(p2Counter);
        boxTop.add(Box.createHorizontalStrut(30));


        // Добавить в центр граничной компоновки поле Field
        getContentPane().add(boxTop, BorderLayout.NORTH);
        getContentPane().add(field, BorderLayout.CENTER);

    }
    
}
