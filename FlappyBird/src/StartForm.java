import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartForm extends JFrame {

    public StartForm() {
        super("Flappy Bird - Start Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Welcome to Flappy Bird");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Menutup StartForm
                // Membuat dan menampilkan JFrame game FlappyBird
                JFrame frame = new JFrame("Flappy Bird");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(360, 640);
                frame.setLocationRelativeTo(null);
                frame.setResizable(false);

                FlappyBird flappyBird = new FlappyBird();
                frame.add(flappyBird);
                frame.pack();
                flappyBird.requestFocus();
                frame.setVisible(true);
            }
        });
        panel.add(startButton, BorderLayout.CENTER);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StartForm startForm = new StartForm();
                startForm.setVisible(true);
            }
        });
    }
}
