
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main
{
    private final JFrame frame;
    private final JPanel panel;
    private final JButton btnStart;
    private final JButton btnStop;
    private World world;
    
    public Main()
    {
        world = new World();
        world.setBounds(0, 0, 750, 500);
        
        btnStart = new JButton("Start");
        btnStart.setBounds(290, 512, 80, 25);
        btnStart.setFocusPainted(false);
        btnStart.addActionListener((e) -> {
            world.start();
            System.out.println("pressed start");
        });
        
        btnStop = new JButton("Stop");
        btnStop.setBounds(380, 512, 80, 25);
        btnStop.setFocusPainted(false);
        btnStop.addActionListener((e) -> {
            world.stop();
            System.out.println("pressed stop");
        });
        
        panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(750, 550));
        panel.add(world);
        panel.add(btnStart);
        panel.add(btnStop);
        
        frame = new JFrame("Elevator Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args)
    {
        Main main = new Main();
    }
}
