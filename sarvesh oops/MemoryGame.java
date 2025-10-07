import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class MemoryGame extends JFrame {
    private MemoryModel model;
    private JButton[] buttons;
    private int firstIndex = -1, secondIndex = -1, moves = 0, time = 0;
    private JLabel movesLabel, timerLabel;
    private Timer gameTimer;
    private ImageIcon backIcon; // image for the back of the card

    public MemoryGame() {
        setTitle("🃏 Memory Card Flip Game (Image Edition)");
        setSize(550, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        model = new MemoryModel();
        backIcon = new ImageIcon(new ImageIcon("images/back.png")
                .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        movesLabel = new JLabel("Moves: 0");
        timerLabel = new JLabel("Time: 0s");
        JButton restartBtn = new JButton("Restart");
        restartBtn.addActionListener(e -> restartGame());

        topPanel.add(movesLabel);
        topPanel.add(new JLabel(" | "));
        topPanel.add(timerLabel);
        topPanel.add(new JLabel(" | "));
        topPanel.add(restartBtn);
        add(topPanel, BorderLayout.NORTH);

        JPanel board = new JPanel();
        board.setLayout(new GridLayout(4, 4, 10, 10));
        board.setBackground(new Color(230, 230, 250));

        buttons = new JButton[model.getSize()];
        for (int i = 0; i < buttons.length; i++) {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(100, 100));
            btn.setBackground(Color.LIGHT_GRAY);
            btn.setIcon(backIcon);
            final int index = i;
            btn.addActionListener(e -> flipCard(index));
            buttons[i] = btn;
            board.add(btn);
        }
        add(board, BorderLayout.CENTER);

        startTimer();
        setVisible(true);
    }

    private void startTimer() {
        if (gameTimer != null) gameTimer.cancel();
        time = 0;
        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                time++;
                SwingUtilities.invokeLater(() ->
                    timerLabel.setText("Time: " + time + "s"));
            }
        }, 1000, 1000);
    }

    private void flipCard(int index) {
        if (model.isMatched(index) || firstIndex == index || secondIndex != -1)
            return;

        showCardImage(index);

        if (firstIndex == -1) {
            firstIndex = index;
        } else {
            secondIndex = index;
            moves++;
            movesLabel.setText("Moves: " + moves);

            Timer t = new Timer();
            t.schedule(new TimerTask() {
                public void run() {
                    checkMatch();
                }
            }, 900);
        }
    }

    private void showCardImage(int index) {
        ImageIcon img = new ImageIcon(
            new ImageIcon("images/" + model.getCardValue(index))
                .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)
        );
        buttons[index].setIcon(img);
    }

    private void hideCard(int index) {
        buttons[index].setIcon(backIcon);
    }

    private void checkMatch() {
        if (model.getCardValue(firstIndex).equals(model.getCardValue(secondIndex))) {
            model.setMatched(firstIndex, secondIndex);
            buttons[firstIndex].setBackground(new Color(60, 179, 113));
            buttons[secondIndex].setBackground(new Color(60, 179, 113));
        } else {
            hideCard(firstIndex);
            hideCard(secondIndex);
        }

        firstIndex = -1;
        secondIndex = -1;

        if (model.allMatched()) {
            gameTimer.cancel();
            JOptionPane.showMessageDialog(this,
                "🎉 You Win!\nMoves: " + moves + "\nTime: " + time + "s",
                "Game Over", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void restartGame() {
        model.resetGame();
        moves = 0;
        time = 0;
        movesLabel.setText("Moves: 0");
        timerLabel.setText("Time: 0s");
        firstIndex = -1;
        secondIndex = -1;

        for (JButton btn : buttons) {
            btn.setBackground(Color.LIGHT_GRAY);
            btn.setIcon(backIcon);
        }
        startTimer();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemoryGame());
    }
}
