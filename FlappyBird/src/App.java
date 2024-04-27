/*Saya Rakha Dhifiargo Hariadi
 NIM 2209489 mengerjakan soal
 Latihan praktikum 7 dalam mata
 kuliah DPBO
 untuk keberkahanNya maka saya tidak
 melakukan kecurangan seperti
 yang telah dispesifikasikan. Aamiin.*/
import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args){
        JFrame frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 640);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
//        frame.setVisible(true);

        // buat objek JPanel
        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird);
        frame.pack();
        flappyBird.requestFocus();
        frame.setVisible(true);
    }
}
