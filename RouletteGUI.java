import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class RouletteGUI extends JPanel {
    private int number = 0;
    private int balance = 1000; 
    private int betAmount = 0;
    private Color betColor = null;
    private JLabel balanceLabel = new JLabel();
    private static final Random rand = new Random();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawWheel(g, number);
    }

    private void drawWheel(Graphics g, int number) {
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 10;

        int centerX = width / 2;
        int centerY = height / 2;

        g.setColor(Color.BLACK);
        g.drawOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);

        for (int i = 0; i < 37; i++) {
            if (i == number) {
                g.setColor(Color.YELLOW);
            } 
            else if (i == 0) {
                g.setColor(Color.GREEN);
            } 
            else if (i % 2 == 0) {
                g.setColor(Color.RED);
            } 
            else {
                g.setColor(Color.BLACK);
            }

            double startAngle = i * 360.0 / 37;
            double arcAngle = 360.0 / 37;
            g.fillArc(centerX - radius, centerY - radius, 2 * radius, 2 * radius, (int)startAngle, (int)arcAngle);
        }
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 60)); 
        g.drawString(Integer.toString(number), centerX, centerY);
    }

    public void setNumber(int number) {
        this.number = number;
        repaint();
        resolveBet();
    }

    private void resolveBet() {
        Color numberColor = number == 0 ? Color.GREEN : (number % 2 == 0 ? Color.RED : Color.BLACK);
        
        if (numberColor.equals(Color.GREEN) && numberColor.equals(betColor)) {
            balance += betAmount * 35; 
        } 
        else if (numberColor.equals(betColor)) {
            balance += betAmount;
        } 
        else {
            balance -= betAmount;
        }

        balanceLabel.setText("Balance: $" + balance);
        betAmount = 0; 
        betColor = null; 

        if (balance <= 0) {
            JOptionPane.showMessageDialog(null, "Game Over! Balance can't be $0 or less.");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Roulette Wheel");
                RouletteGUI wheel = new RouletteGUI();
                frame.add(wheel, BorderLayout.CENTER);

                JPanel panel = new JPanel(new GridLayout(1, 0)); 
                frame.add(panel, BorderLayout.NORTH);

                JLabel betLabel = new JLabel("Bet Amount: ");
                panel.add(betLabel);

                JTextField betField = new JTextField(5);
                panel.add(betField);

                JButton betRedButton = new JButton("Bet Red");
                betRedButton.addActionListener(e -> {
                    int amount = Integer.parseInt(betField.getText());
                    if (amount <= wheel.balance) {
                        wheel.betColor = Color.RED;
                        wheel.betAmount = amount;
                    } 
                    else {
                        JOptionPane.showMessageDialog(null, "Bet amount exceeds balance");
                    }
                });
                panel.add(betRedButton);

                JButton betBlackButton = new JButton("Bet Black");
                betBlackButton.addActionListener(e -> {
                    int amount = Integer.parseInt(betField.getText());
                    if (amount <= wheel.balance) {
                        wheel.betColor = Color.BLACK;
                        wheel.betAmount = amount;
                    } 
                    else {
                        JOptionPane.showMessageDialog(null, "Bet amount exceeds balance");
                    }
                });
                panel.add(betBlackButton);

                JButton betGreenButton = new JButton("Bet Green");
                betGreenButton.addActionListener(e -> {
                    int amount = Integer.parseInt(betField.getText());
                    if (amount <= wheel.balance) {
                        wheel.betColor = Color.GREEN;
                        wheel.betAmount = amount;
                    } 
                    else {
                        JOptionPane.showMessageDialog(null, "Bet amount exceeds balance");
                    }
                });

                panel.add(betGreenButton);

                wheel.balanceLabel.setText("Balance: $" + wheel.balance);
                panel.add(wheel.balanceLabel);

                JButton button = new JButton("Spin");
                button.addActionListener(e -> wheel.setNumber(rand.nextInt(37)));
                frame.add(button, BorderLayout.SOUTH);

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 900);
                frame.setVisible(true);
            }
        });
    }
}







