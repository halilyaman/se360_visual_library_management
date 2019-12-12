import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

   /**
    * Create singleSearchResult. add it to result area
    */
   private void loadSearchResultComponents(String bookTitle) {

      try {
         if(DBConnection.connection == null) {
            DBConnection.getConnection();
         }
         Statement statement = DBConnection.connection.createStatement();
         ResultSet resultSet = statement.executeQuery("SELECT title, author, description, generes FROM books WHERE title LIKE '" + bookTitle + "';");
         while(resultSet.next()) {
            resultArea.add(new SingleSearchResult(
               resultSet.getString("title"),
               resultSet.getString("description"),
               resultSet.getString("author"),
               resultSet.getString("generes"),
               true
            ).getResultPanel());
         }
      } catch (SQLException e) {
         e.printStackTrace();
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

         new Thread(() -> {
            loadSearchResultComponents(searchBar.getText());
         }).start();
      }
   }
}
