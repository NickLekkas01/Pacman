import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static java.lang.Thread.sleep;

public class Main implements KeyListener, ActionListener{
    public static int HEIGHT = 42;
    public static int WIDTH = 40;

    public static int prevPacmanPosX = 25;
    public static int prevPacmanPosY = 19;
    public static int PacmanPosX = 25;
    public static int PacmanPosY = 19;

    public static int prevInkyPosX = 6;
    public static int prevInkyPosY = 4;
    public static int InkyPosX = 6;
    public static int InkyPosY = 4;

    public static int prevBlinkyPosX = 6;
    public static int prevBlinkyPosY = 35;
    public static int BlinkyPosX = 6;
    public static int BlinkyPosY = 35;

    public static int prevPinkyPosX = 38;
    public static int prevPinkyPosY = 4;
    public static int PinkyPosX = 38;
    public static int PinkyPosY = 4;

    public static int prevClydePosX = 38;
    public static int prevClydePosY = 35;
    public static int ClydePosX = 38;
    public static int ClydePosY = 35;

    public static char[][] PACMAN_TABLE;

    public static int direction;
    static JFrame frame;
    static JTextField tf;
    static JLabel lbl;
    static JButton btn;
    public Main() {
        frame = new JFrame();
        lbl = new JLabel();
        tf = new JTextField(15);
        tf.addKeyListener(this);
        btn = new JButton("Clear");
        btn.addActionListener(this);
        JPanel panel = new JPanel();
        panel.add(tf);
        panel.add(btn);

        frame.setLayout(new BorderLayout());
        frame.add(lbl, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 100);
        frame.setVisible(true);
    }
    public static void main(String args[]) throws IOException, InterruptedException {

        new Main();
        PACMAN_TABLE = new char[HEIGHT][WIDTH];

        /* Read pacman map from file pac.txt */
        readFromFile("pac.txt", PACMAN_TABLE);

        //Printing the pacman map
        printMap();

        while(true){
            /*Check if Lost */
            if(Lost()){
                System.out.println("You lost. GAME OVER");
                return;
            }
            /*Check if Won*/
            if(Won()){
                System.out.println("You won. Congratulations!");
                return;
            }

            /* Move pacman with wasd or WASD */
            int [] pacmanPos = {PacmanPosX, PacmanPosY};
            prevPacmanPosX = PacmanPosX;
            prevPacmanPosY = PacmanPosY;
            pacmanPos = PacManMovement(PACMAN_TABLE, pacmanPos);
            PacmanPosX = pacmanPos[0];
            PacmanPosY = pacmanPos[1];

            /* Inky ghost*/
            int [] inkyPos = {InkyPosX, InkyPosY};
            prevInkyPosX = InkyPosX;
            prevInkyPosY = InkyPosY;
            inkyPos = ghostMovementChase(PACMAN_TABLE, inkyPos);
            InkyPosX = inkyPos[0];
            InkyPosY = inkyPos[1];

            /* Blinky ghost*/
            int [] blinkyPos = {BlinkyPosX, BlinkyPosY};
            prevBlinkyPosX = BlinkyPosX;
            prevBlinkyPosY = BlinkyPosY;
            blinkyPos = ghostMovement(PACMAN_TABLE, blinkyPos);
            BlinkyPosX = blinkyPos[0];
            BlinkyPosY = blinkyPos[1];

            /* Pinky ghost*/
            int [] pinkyPos = {PinkyPosX, PinkyPosY};
            prevPinkyPosX = PinkyPosX;
            prevPinkyPosY = PinkyPosY;
            pinkyPos = ghostMovement(PACMAN_TABLE, pinkyPos);
            PinkyPosX = pinkyPos[0];
            PinkyPosY = pinkyPos[1];

            /* Clyde ghost*/
            int [] clydePos = {ClydePosX, ClydePosY};
            prevClydePosX = ClydePosX;
            prevClydePosY = ClydePosY;
            clydePos = ghostMovement(PACMAN_TABLE, clydePos);
            ClydePosX = clydePos[0];
            ClydePosY = clydePos[1];

            clearScreen();

            //Printing the pacman map
            printMap();
            sleep(400);

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            if(PacmanPosY-1 >=0 && PACMAN_TABLE[PacmanPosX][PacmanPosY - 1] != '#' && PACMAN_TABLE[PacmanPosX][PacmanPosY - 1] != 'x')
                direction = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            if(PacmanPosY+1 < WIDTH && PACMAN_TABLE[PacmanPosX][PacmanPosY + 1] != '#' && PACMAN_TABLE[PacmanPosX][PacmanPosY + 1] != 'x')
                direction = 1;
        }

        if (key == KeyEvent.VK_UP) {
            if(PacmanPosX-1 >= 0 && PACMAN_TABLE[PacmanPosX - 1 ][PacmanPosY ] != '#' && PACMAN_TABLE[PacmanPosX - 1 ][PacmanPosY ] != 'x')
                direction = 2;
        }

        if (key == KeyEvent.VK_DOWN) {
            if(PacmanPosX+1 < HEIGHT && PACMAN_TABLE[PacmanPosX+1][PacmanPosY] != '#' && PACMAN_TABLE[PacmanPosX+1][PacmanPosY] != 'x')
                direction = 3;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            if(PacmanPosY-1 >=0 && PACMAN_TABLE[PacmanPosX][PacmanPosY - 1] != '#' && PACMAN_TABLE[PacmanPosX][PacmanPosY - 1] != 'x')
                direction = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            if(PacmanPosY+1 < WIDTH && PACMAN_TABLE[PacmanPosX][PacmanPosY + 1] != '#' && PACMAN_TABLE[PacmanPosX][PacmanPosY + 1] != 'x')
                direction = 1;
        }

        if (key == KeyEvent.VK_UP) {
            if(PacmanPosX-1 >= 0 && PACMAN_TABLE[PacmanPosX - 1 ][PacmanPosY ] != '#' && PACMAN_TABLE[PacmanPosX - 1 ][PacmanPosY ] != 'x')
                direction = 2;
        }

        if (key == KeyEvent.VK_DOWN) {
            if(PacmanPosX+1 < HEIGHT && PACMAN_TABLE[PacmanPosX+1][PacmanPosY] != '#' && PACMAN_TABLE[PacmanPosX+1][PacmanPosY] != 'x')
                direction = 3;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            if(PacmanPosY-1 >=0 && PACMAN_TABLE[PacmanPosX][PacmanPosY - 1] != '#' && PACMAN_TABLE[PacmanPosX][PacmanPosY - 1] != 'x')
                direction = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            if(PacmanPosY+1 < WIDTH && PACMAN_TABLE[PacmanPosX][PacmanPosY + 1] != '#' && PACMAN_TABLE[PacmanPosX][PacmanPosY + 1] != 'x')
                direction = 1;
        }

        if (key == KeyEvent.VK_UP) {
            if(PacmanPosX-1 >= 0 && PACMAN_TABLE[PacmanPosX - 1 ][PacmanPosY ] != '#' && PACMAN_TABLE[PacmanPosX - 1 ][PacmanPosY ] != 'x')
                direction = 2;
        }

        if (key == KeyEvent.VK_DOWN) {
            if(PacmanPosX+1 < HEIGHT && PACMAN_TABLE[PacmanPosX+1][PacmanPosY] != '#' && PACMAN_TABLE[PacmanPosX+1][PacmanPosY] != 'x')
                direction = 3;
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        tf.setText("");
    }

    private static void clearScreen() throws IOException, InterruptedException {
        final String os = System.getProperty("os.name");
        if (os.contains("Windows"))
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        else {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    private static void printMap() {
        for(int i = 0; i < HEIGHT; i++){
            for(int j = 0; j < WIDTH; j++) {
                if(i == InkyPosX && j == InkyPosY) System.out.print("\u001B[31m"+"1");
                else if(i == BlinkyPosX && j == BlinkyPosY) System.out.print("\u001B[32m"+"2");
                else if(i == PinkyPosX && j == PinkyPosY) System.out.print("\u001B[35m"+"3");
                else if(i == ClydePosX && j == ClydePosY) System.out.print("\u001B[36m"+"4");
                else if(PACMAN_TABLE[i][j] == '#')System.out.print("\u001B[34m"+PACMAN_TABLE[i][j]);
                else if(PACMAN_TABLE[i][j] == 'C') System.out.print("\u001B[33m"+PACMAN_TABLE[i][j]);
                else System.out.print("\u001B[37m"+PACMAN_TABLE[i][j]);
            }
            System.out.println();
        }
    }

    private static void readFromFile(String file, char [][]Table){
        try{
            File obj = new File(file);
            Scanner myReader = new Scanner(obj);
            int i = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                for(int j = 0; j < data.length(); j++){
//                    System.out.print(data.charAt(j));
                    Table[i][j] = data.charAt(j);
                }
                System.out.println();
                i++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private static int []ghostMovementChase(char[][] pacman_table, int []ghostPos) {
        Random r = new Random();
        int num;
        while(true){
//            num = r.nextInt(4);
            if(ghostPos[0] <= PacmanPosX && ghostPos[1] <= PacmanPosY){
                num = r.nextInt(2);
                if(num == 0){
                    if(ghostPos[0] + 1 < HEIGHT && pacman_table[ghostPos[0]+1][ghostPos[1]] != '#'){
                        ghostPos[0] = ghostPos[0] + 1;
                        break;
                    }
                }
                else if(num == 1){
                    if(ghostPos[1] + 1 < WIDTH && pacman_table[ghostPos[0]][ghostPos[1]+1] != '#' ){
                        ghostPos[1] = ghostPos[1] + 1;
                        break;
                    }
                }
                if(pacman_table[ghostPos[0]+1][ghostPos[1]] == '#' && pacman_table[ghostPos[0]][ghostPos[1]+1] == '#'){
                    while(true){
                        num = r.nextInt(2);
                        if(num == 0) {
                            ghostPos[0] = ghostPos[0] - 1;
                            break;
                        }else {
                            ghostPos[1] = ghostPos[1] - 1;
                            break;
                        }
                    }
                }

            }
            else if( ghostPos[0] <= PacmanPosX && ghostPos[1] > PacmanPosY) {
                num = r.nextInt(2);
                if (num == 0) {
                    if(ghostPos[0] + 1 < HEIGHT && pacman_table[ghostPos[0]+1][ghostPos[1]] != '#'){
                        ghostPos[0] = ghostPos[0] + 1;
                        break;
                    }
                } else if (num == 1) {
                    if(ghostPos[1] - 1 >= 0 && pacman_table[ghostPos[0]][ghostPos[1]-1] != '#'){
                        ghostPos[1] = ghostPos[1] - 1;
                        break;
                    }
                }
                if(pacman_table[ghostPos[0]+1][ghostPos[1]] == '#' && pacman_table[ghostPos[0]][ghostPos[1]-1] == '#'){
                    while(true){
                        num = r.nextInt(2);
                        if(num == 0) {
                            ghostPos[0] = ghostPos[0] - 1;
                            break;
                        }else {
                            ghostPos[1] = ghostPos[1] + 1;
                            break;
                        }
                    }
                }
            }
            else if( ghostPos[0] > PacmanPosX && ghostPos[1] <= PacmanPosY) {
                num = r.nextInt(2);
                if (num == 0) {
                    if(ghostPos[0] - 1 >= 0 && pacman_table[ghostPos[0]-1][ghostPos[1]] != '#'){
                        ghostPos[0] = ghostPos[0] - 1;
                        break;
                    }
                } else if (num == 1) {
                    if(ghostPos[1] + 1 < WIDTH && pacman_table[ghostPos[0]][ghostPos[1]+1] != '#' ){
                        ghostPos[1] = ghostPos[1] + 1;
                        break;
                    }
                }
                if(pacman_table[ghostPos[0]-1][ghostPos[1]] == '#' && pacman_table[ghostPos[0]][ghostPos[1]+1] == '#'){
                    while(true){
                        num = r.nextInt(2);
                        if(num == 0) {
                            ghostPos[0] = ghostPos[0] + 1;
                            break;
                        }else {
                            ghostPos[1] = ghostPos[1] - 1;
                            break;
                        }
                    }
                }
            }
            else if( ghostPos[0] > PacmanPosX && ghostPos[1] > PacmanPosY ){
                num = r.nextInt(2);
                if (num == 0) {
                    if(ghostPos[0] - 1 >= 0 && pacman_table[ghostPos[0]-1][ghostPos[1]] != '#'){
                        ghostPos[0] = ghostPos[0] - 1;
                        break;
                    }
                } else if (num == 1) {
                    if(ghostPos[1] - 1 >= 0 && pacman_table[ghostPos[0]][ghostPos[1]-1] != '#'){
                        ghostPos[1] = ghostPos[1] - 1;
                        break;
                    }
                }
                if(pacman_table[ghostPos[0]-1][ghostPos[1]] == '#' && pacman_table[ghostPos[0]][ghostPos[1]-1] == '#'){
                    while(true){
                        num = r.nextInt(2);
                        if(num == 0) {
                            ghostPos[0] = ghostPos[0] + 1;
                            break;
                        }else {
                            ghostPos[1] = ghostPos[1] + 1;
                            break;
                        }
                    }
                }
            }
        }
        return ghostPos;
    }

    private static int []ghostMovement(char[][] pacman_table, int []ghostPos) {
        Random r = new Random();
        int num;
        while(true){
            num = r.nextInt(4);
            if(num == 0){
                if(ghostPos[1] - 1 >= 0 && pacman_table[ghostPos[0]][ghostPos[1]-1] != '#'){
                    ghostPos[1] = ghostPos[1] - 1;
                    break;
                }
            }
            else if(num == 1){
                if(ghostPos[1] + 1 < WIDTH && pacman_table[ghostPos[0]][ghostPos[1]+1] != '#' ){
                    ghostPos[1] = ghostPos[1] + 1;
                    break;
                }
            }
            else if(num == 2){
                if(ghostPos[0] - 1 >= 0 && pacman_table[ghostPos[0]-1][ghostPos[1]] != '#'){
                    ghostPos[0] = ghostPos[0] - 1;
                    break;
                }
            }
            else if(num == 3){
                if(ghostPos[0] + 1 < HEIGHT && pacman_table[ghostPos[0]+1][ghostPos[1]] != '#'){
                    ghostPos[0] = ghostPos[0] + 1;
                    break;
                }
            }

        }
        return ghostPos;
    }

    private static int []PacManMovement(char[][] pacman_table, int []pacmanPos) {
        Scanner keyboard = new Scanner(System.in);
        pacman_table[pacmanPos[0]][pacmanPos[1]] = ' ';
//        while(true){
//            char ch = keyboard.next().charAt(0);
            if(direction == 0){
  //            if(ch == 'A' || ch == 'a'){
                if(pacmanPos[1] - 1 >= 0 && pacman_table[pacmanPos[0]][pacmanPos[1]-1] != '#' && pacman_table[pacmanPos[0]][pacmanPos[1]-1] != 'x'){
                    pacmanPos[1] = pacmanPos[1] - 1;
//                    break;
                }
            }
            else if(direction == 1){
//            else if(ch == 'D' || ch == 'd'){
                if(pacmanPos[1] + 1 < HEIGHT && pacman_table[pacmanPos[0]][pacmanPos[1]+1] != '#' && pacman_table[pacmanPos[0]][pacmanPos[1]+1] != 'x'){
                    pacmanPos[1] = pacmanPos[1] + 1;
//                    break;
                }
            }
            else if(direction == 2){
//            else if(ch == 'W' || ch == 'w'){
                if(pacmanPos[0] - 1 >= 0 && pacman_table[pacmanPos[0]-1][pacmanPos[1]] != '#' && pacman_table[pacmanPos[0]-1][pacmanPos[1]] != 'x'){
                    pacmanPos[0] = pacmanPos[0] - 1;
//                    break;
                }
            }
            else if(direction == 3){
//            else if(ch == 'S' || ch == 's'){
                if(pacmanPos[0] + 1 < WIDTH && pacman_table[pacmanPos[0]+1][pacmanPos[1]] != '#' && pacman_table[pacmanPos[0]+1][pacmanPos[1]] != 'x'){
                    pacmanPos[0] = pacmanPos[0] + 1;
//                    break;
                }
            }

//        }


        pacman_table[pacmanPos[0]][pacmanPos[1]] = 'C';
        return pacmanPos;
    }

    private static boolean Won() {
        boolean won = true;
        for(int i = 0; i < HEIGHT; i++){
            for (int j = 0; j < WIDTH; j++){
                if(PACMAN_TABLE[i][j] == '.') {
                    won = false;
                    return won;
                }
            }
        }
        return won;
    }

    private static boolean Lost() {
        if(PacmanPosX == InkyPosX && PacmanPosY == InkyPosY)
            return true;
        if(PacmanPosX == BlinkyPosX && PacmanPosY == BlinkyPosY)
            return true;
        if(PacmanPosX == PinkyPosX && PacmanPosY == PinkyPosY)
            return true;
        if(PacmanPosX == ClydePosX && PacmanPosY == ClydePosY)
            return true;

        if(prevPacmanPosX == InkyPosX && prevPacmanPosY == InkyPosY)
            return true;
        if(prevPacmanPosX == BlinkyPosX && prevPacmanPosY == BlinkyPosY)
            return true;
        if(prevPacmanPosX == PinkyPosX && prevPacmanPosY == PinkyPosY)
            return true;
        if(prevPacmanPosX == ClydePosX && prevPacmanPosY == ClydePosY)
            return true;

        if(PacmanPosX == prevInkyPosX && PacmanPosY == prevInkyPosY)
            return true;
        if(PacmanPosX == prevBlinkyPosX && PacmanPosY == prevBlinkyPosY)
            return true;
        if(PacmanPosX == prevPinkyPosX && PacmanPosY == prevPinkyPosY)
            return true;
        if(PacmanPosX == prevClydePosX && PacmanPosY == prevClydePosY)
            return true;
        return false;
    }
}
