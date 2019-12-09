import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LibrarySearchPanel {
   final private static int W_HEIGHT = 720;
   final private static int W_WIDTH = 1280;
   public final static Color DEFAULT_BG_COLOR = new Color(247, 130, 12);

   private JFrame frame;
   private JPanel panel;
   private JPanel centerPanel;
   private JPanel resultArea;
   private JPanel upperPanel;
   private JTextField searchBar;
   private JButton searchButton;
   private JPanel bottomPanel;

   final private String TEXT_FIELD_LABEL;
   final private Color BG_COLOR;
   final private String WINDOW_TITLE;
   final private String BOTTOM_TEXT;

   public LibrarySearchPanel(String textFieldLabel,
                             String windowTitle,
                             String bottomText,
                             Color bgColor)
   {
      this.TEXT_FIELD_LABEL = textFieldLabel;
      this.BG_COLOR = bgColor;
      this.WINDOW_TITLE = windowTitle;
      this.BOTTOM_TEXT = bottomText;
      this.buildScreen();
   }

   private void buildScreen() {
      frame = new JFrame(WINDOW_TITLE);

      panel = new JPanel();
      panel.setLayout(new BorderLayout());
      panel.setBackground(BG_COLOR);

      panel.add(buildUpperPanel(), BorderLayout.NORTH);
      panel.add(buildBottomPanel(), BorderLayout.SOUTH);
      panel.add(buildCenterPanel(), BorderLayout.CENTER);

      frame.getContentPane().add(panel, BorderLayout.CENTER);
      frame.setSize(W_WIDTH, W_HEIGHT);
      frame.setLocationRelativeTo(null);
      frame.setResizable(false);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }

   private void loadSearchResultComponents() {
      for(int i = 0; i<10; i++) {
         SingleSearchResult ssr = new SingleSearchResult(
            "The Art of Super Mario Odyssey",
            "Take a globetrotting journey all over the world--and beyond!--with this companion art book to the hit video game for the Nintendo Switch(TM) system!\n" +
               "\n" +
               "In October of 2017, Super Mario Odyssey(TM) took the gaming world by storm. Now, discover the art and expertise that went into creating one of Nintendo's best-loved games!\n" +
               "\n" +
               " This full-color volume clocks in at over 350 pages and features concept art, preliminary sketches, and notes from the development team, plus insight into some early ideas that didn't make it into the game itself! Explore the world of Super Mario Odyssey from every angle, including screen shots, marketing material, and more, to fully appreciate this captivating adventure",
            "Nintendo",
            "Science, General, Fiction, Poetry",
            true
         );
         resultArea.add(ssr.getResultPanel());
      }
      centerPanel.updateUI();
   }

   private JComponent buildCenterPanel() {
      centerPanel = new JPanel();
      resultArea = new JPanel();

      centerPanel.setBackground(BG_COLOR);
      centerPanel.setLayout(new BorderLayout());
      centerPanel.setBorder(new EmptyBorder(30,100,30,100));

      resultArea.setLayout(new BoxLayout(resultArea, BoxLayout.Y_AXIS));
      resultArea.setBackground(BG_COLOR);

      return centerPanel;
   }

   private JComponent buildUpperPanel() {
      upperPanel = new JPanel();
      upperPanel.setLayout(new FlowLayout());
      upperPanel.setBorder(new EmptyBorder(300,0,0,0));
      upperPanel.setBackground(BG_COLOR);

      searchBar = new JTextField(20);
      searchBar.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

      final JLabel textFieldLabel = new JLabel(TEXT_FIELD_LABEL + ":");
      // TODO: give a decision about color: "white" or "new Color(102,0,204)"
      textFieldLabel.setForeground(Color.WHITE);
      textFieldLabel.setFont(new Font("Courier new", Font.BOLD, 20));

      final Icon searchIcon = new ImageIcon("assets/search_icon.png");
      searchButton = new JButton("Search", searchIcon);
      searchButton.addActionListener(new SearchButtonListener());

      upperPanel.add(textFieldLabel);
      upperPanel.add(searchBar);
      upperPanel.add(searchButton);

      return upperPanel;
   }

   private JComponent buildBottomPanel() {
      bottomPanel = new JPanel();
      bottomPanel.setLayout(new FlowLayout());
      bottomPanel.setBorder(new EmptyBorder(0,0,10,0));
      bottomPanel.setBackground(BG_COLOR);

      final JLabel copyright = new JLabel(BOTTOM_TEXT);
      bottomPanel.add(copyright);

      return bottomPanel;
   }

   class SearchButtonListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         panel.add(buildCenterPanel(), BorderLayout.CENTER);

         upperPanel.setBorder(new EmptyBorder(20,0,0,0));

         JScrollPane scrollableArea = new JScrollPane(resultArea);
         centerPanel.add(scrollableArea, BorderLayout.CENTER);
         scrollableArea.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));

         new Thread(LibrarySearchPanel.this::loadSearchResultComponents).start();
      }
   }
}
