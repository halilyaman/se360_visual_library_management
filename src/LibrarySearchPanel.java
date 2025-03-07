import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LibrarySearchPanel {
   final public static int W_HEIGHT = 720;
   final public static int W_WIDTH = 1280;
   final public static Color DEFAULT_BG_COLOR = new Color(247, 130, 12);

   private JFrame frame;
   private JPanel panel;
   private JPanel centerPanel;
   private JPanel resultArea;
   private JPanel upperPanel;
   private JTextField searchBar;
   private JButton searchButton;
   private JPanel bottomPanel;
   private JScrollPane scrollableArea;

   private JButton adminButton;
   private JButton signOutButton;

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

      buttonVisibilityController();

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

      String[] splittedTitle = bookTitle.split(" ");
      String[] queries = new String[splittedTitle.length];
      for(int i = 0; i < queries.length; i++) {
         queries[i] = "%" + splittedTitle[i] + "%";
      }

      ArrayList<Book> results = new ArrayList<>();
      int size = 0;
      try {
         if(DBConnection.connection == null) {
            DBConnection.getConnection();
         }
         Statement statement = DBConnection.connection.createStatement();
         ResultSet resultSet = statement.executeQuery(
            "SELECT * FROM books WHERE book_title LIKE '" + bookTitle + "';");

         // firstly collect the results in list, then update ui from that list
         while(resultSet.next()) {
            results.add(new Book(
               resultSet.getString("book_title"),
               resultSet.getString("book_desc"),
               resultSet.getString("book_authors"),
               resultSet.getString("genres"),
               resultSet.getInt("location"),
               resultSet.getString("image_url"),
               true
            ));
            size++;
         }

         for(int i = 0; i < queries.length; i++) {
            resultSet = statement.executeQuery(
               "SELECT * FROM books WHERE book_title LIKE '" + queries[i] + "';");
            while(resultSet.next()) {
               results.add(new Book(
                  resultSet.getString("book_title"),
                  resultSet.getString("book_desc"),
                  resultSet.getString("book_authors"),
                  resultSet.getString("genres"),
                  resultSet.getInt("location"),
                  resultSet.getString("image_url"),
                  true
               ));
               size++;
               if(size > 50)
                  break;
            }
         }
      } catch (SQLException e) {
         e.printStackTrace();
      } finally {
         updateResultArea(results, size);
         centerPanel.updateUI();
      }
   }

   private void updateResultArea(ArrayList<Book> results, int size) {
      resultArea.removeAll();

      if(size == 0) {
         showNotFoundMessage();
      } else {
         for(int i = 0; i < size; i++) {
            resultArea.add(new SingleSearchResult(results.get(i)).getResultPanel());
         }
      }

   }

   private void showNotFoundMessage() {
      scrollableArea.setVisible(false);
      JOptionPane.showMessageDialog(frame, "Book Doesn't Exist :(", null, JOptionPane.ERROR_MESSAGE);
   }

   private JComponent buildCenterPanel() {
      centerPanel = new JPanel();

      centerPanel.setBackground(BG_COLOR);
      centerPanel.setLayout(new BorderLayout());
      centerPanel.setBorder(new EmptyBorder(30,100,30,100));

      resultArea = new JPanel();
      resultArea.setLayout(new BoxLayout(resultArea, BoxLayout.Y_AXIS));
      resultArea.setBackground(BG_COLOR);

      scrollableArea = new JScrollPane(resultArea);
      scrollableArea.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
      scrollableArea.setVisible(false);
      centerPanel.add(scrollableArea, BorderLayout.CENTER);

      return centerPanel;
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

   private JComponent buildUpperPanel() {
      upperPanel = new JPanel();
      upperPanel.setLayout(new FlowLayout());
      upperPanel.setBorder(new EmptyBorder(300,0,0,0));
      upperPanel.setBackground(BG_COLOR);

      searchBar = new JTextField(20);
      searchBar.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

      final JLabel textFieldLabel = new JLabel(TEXT_FIELD_LABEL + ":");
      textFieldLabel.setForeground(Color.WHITE);
      textFieldLabel.setFont(new Font("Courier new", Font.BOLD, 20));

      final Icon searchIcon = new ImageIcon("assets/search_icon.png");
      searchButton = new JButton("Search", searchIcon);
      searchButton.addActionListener(new SearchButtonListener());

      signOutButton = new JButton("Sign Out");
      signOutButton.addActionListener(new SignOutButtonListener());

      adminButton = new JButton("Admin");
      adminButton.addActionListener(new AdminButtonListener());

      upperPanel.add(textFieldLabel);
      upperPanel.add(searchBar);
      upperPanel.add(searchButton);
      upperPanel.add(adminButton);
      upperPanel.add(signOutButton);

      return upperPanel;
   }

   private void buttonVisibilityController() {
      if(Admin.isAdminOnline) {
         adminButton.setVisible(false);
         signOutButton.setVisible(true);
      } else {
         adminButton.setVisible(true);
         signOutButton.setVisible(false);
      }

      scrollableArea.setVisible(false);
      upperPanel.setBorder(new EmptyBorder(300,0,0,0));
   }

   class SearchButtonListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         upperPanel.setBorder(new EmptyBorder(20,0,0,0));
         scrollableArea.setVisible(true);

         Thread controllerThread = new Thread(new ReturnDateController());
         controllerThread.start();

         new Thread(() -> {
            loadSearchResultComponents(searchBar.getText());
         }).start();
      }
   }

   class AdminButtonListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         String username = JOptionPane.showInputDialog("Username");
         String password = JOptionPane.showInputDialog("Password");

         if(username.equals(Admin.USERNAME) && password.equals(Admin.PASSWORD)) {
            Admin.isAdminOnline = true;
         }

         if(!Admin.isAdminOnline) {
            JOptionPane.showMessageDialog(null, "Wrong username or password.","Message", JOptionPane.ERROR_MESSAGE);
         } else {
            JOptionPane.showMessageDialog(null, "Access Approved", "Message", JOptionPane.INFORMATION_MESSAGE);
         }

         buttonVisibilityController();
         upperPanel.updateUI();
      }
   }

   class SignOutButtonListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         Admin.isAdminOnline = false;

         buttonVisibilityController();
      }
   }
}
