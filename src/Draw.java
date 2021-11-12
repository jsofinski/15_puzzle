import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Draw {
    short size = 0;
    short[] array = {};

    public Draw(short size) {
        this.size = size;
        this.array = new short[size * size];
    }
    JFrame frame = new JFrame("15 puzzle");
    JPanel panel = new JPanel();

    public void init() throws IOException {
        frame.setSize(115 * size, 120 * size);
        panel.setLayout(null);
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public void redraw(byte[] array) throws IOException {
        panel = new JPanel();
        panel.setLayout(null);
        String path = "res/1.png";
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                path = "res/" + (array[i * size +j]) + ".png";
                if (array[i * size + j] == (size) * (size)) {
                    path = "res/16.png";
                }
                File file = new File(path);
                BufferedImage image = ImageIO.read(file);
                JLabel label = new JLabel(new ImageIcon(image));
                label.setBounds(j * 110, i * 110, 100, 100);
                panel.add(label);
            }
        }
        frame.getContentPane().removeAll();
        frame.add(panel);
        frame.repaint();
        frame.setVisible(true);
    }
}
