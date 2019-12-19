import javax.swing.*;
import java.awt.*;

public class NavigationPage {
   private JFrame frame;

   public void openNavigationPage() {
      frame = new JFrame();

      frame.add(new LibrarySketch());
      frame.setSize(new Dimension(LibrarySearchPanel.W_WIDTH, LibrarySearchPanel.W_HEIGHT));
      frame.setVisible(true);
      frame.setLocationRelativeTo(null);
      frame.setResizable(false);
   }

   private static class LibrarySketch extends JPanel {
      @Override
      public void paint(Graphics g) {
         super.paint(g);
         g.setColor(Color.GRAY);
         g.fillRect(0,0, LibrarySearchPanel.W_WIDTH, LibrarySearchPanel.W_HEIGHT);
      }
   }
}
