import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;

public class KalahaGUI {
    private KalahaGame game;
    private final JButton[] buttons;
    private final JLabel currentPlayerLabel;
    private final JButton restartButton;
    private final JButton exitButton;

    public KalahaGUI() {
        game = new KalahaGame();
        buttons = new JButton[14];

        JFrame frame = new JFrame("Kalaha Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(2, 7));

        // create all the buttons
        // don't enable the 7th button of each row
        // size is set a little larger to accommodate double-digits
        for (int i = 0; i < 14; i++) {
            buttons[i] = new JButton(Integer.toString(game.getBoard()[i]));
            buttons[i].setPreferredSize(new Dimension(50, 50));
            buttons[i].setFont(new Font("Arial", Font.BOLD, 12));
            buttons[i].setEnabled(i != 6 && i != 13);
            buttons[i].addActionListener(new ButtonListener(i));
            boardPanel.add(buttons[i]);
        }

        currentPlayerLabel = new JLabel(MessageFormat.format("Current Player: Player {0}", game.getCurrentPlayer()));
        currentPlayerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel controlPanel = new JPanel(new FlowLayout());

        restartButton = new JButton("Restart");
        restartButton.addActionListener(new RestartButtonListener());
        controlPanel.add(restartButton);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ExitButtonListener());
        controlPanel.add(exitButton);

        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(currentPlayerLabel, BorderLayout.NORTH);
        frame.add(controlPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    private void updateGUI() {
        boolean isExtraTurn = game.getExtraTurn();
        int currentPlayer = game.getCurrentPlayer();
        int[] board = game.getBoard();

        for (int i = 0; i < 14; i++) {
            buttons[i].setText(Integer.toString(board[i]));
        }
        // display "Player x gets an extra turn!" if true
        // worked initially then stopped after some changes, couldn't retrace my steps.

        if (isExtraTurn) {
            currentPlayerLabel.setText(MessageFormat.format("Player {0} gets an extra turn!", currentPlayer));
        } else {
            currentPlayerLabel.setText(MessageFormat.format("Current Player: Player {0}", currentPlayer));
        }
    }

    private void endGame() {
        for (JButton button : buttons) {
            button.setEnabled(false);
        }

        int[] board = game.getBoard();
        int player1Score = board[6];
        int player2Score = board[13];

        String winner;
        if (player1Score > player2Score) {
            winner = "Player 1 wins!";
        } else if (player2Score > player1Score) {
            winner = "Player 2 wins!";
        } else {
            winner = "It's a tie!";
        }

        // display dialog on game end
        JOptionPane.showMessageDialog(null,
                MessageFormat.format("Game Over!\nPlayer 1 Score: {0}\nPlayer 2 Score: {1}\n{2}",
                        player1Score, player2Score, winner));
        restartButton.setEnabled(true);
    }

    private class ButtonListener implements ActionListener {
        private int index;

        public ButtonListener(int index) {
            this.index = index;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            game.makeMove(index);
            updateGUI();
            if (game.isGameOver()) {
                endGame();
            }
        }
    }
    private class RestartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            game = new KalahaGame();
            for (int i = 0; i < 14; i++) {
                buttons[i].setEnabled(i != 6 && i != 13);
            }
            updateGUI();
        }
    }

    private static class ExitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }
}
