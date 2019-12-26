import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Result content:
 *  title,
 *  description,
 *  author,
 *  genres,
 *  isAvailable,
 */

public class SingleSearchResult {
   private JPanel resultPanel;
   private JPanel resultContentPanel;
   private JPanel leftContentPanel;
   private JPanel rightContentPanel;

   private JTextArea titleArea;
   private JTextArea descriptionArea;
   private JTextArea authorArea;
   private JTextArea genresArea;
   private JTextArea availabilityArea;
   private JButton navigationButton;
   private JButton borrowButton;
   private JButton returnButton;

   final private Book book;

   public SingleSearchResult(Book book) {
      this.book = book;
      searchBorrowedBooks();
   }

   public JPanel getResultPanel() {
      resultPanel = new JPanel();
      resultContentPanel = new JPanel();
      rightContentPanel = new JPanel();
      leftContentPanel = new JPanel();

      leftContentPanel.setLayout(new BoxLayout(leftContentPanel, BoxLayout.Y_AXIS));
      rightContentPanel.setLayout(new BoxLayout(rightContentPanel, BoxLayout.Y_AXIS));

      buildLeftContent();
      buildRightContent();

      resultContentPanel.setBackground(Color.DARK_GRAY);
      resultContentPanel.setLayout(new FlowLayout());
      resultContentPanel.setBorder(new EmptyBorder(20,0,0,0));
      resultContentPanel.add(leftContentPanel);
      resultContentPanel.add(rightContentPanel);

      resultPanel.setLayout(new BorderLayout());
      resultPanel.setPreferredSize(new Dimension(500, 300));
      resultPanel.setBorder(new EmptyBorder(20,10,0,20));
      resultPanel.add(resultContentPanel, BorderLayout.CENTER);

      // Wait for all components to be loaded.
      try {
         Thread.sleep(1);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }

      return resultPanel;
   }

   private void buildLeftContent() {
      titleArea = new JTextArea();
      descriptionArea = new JTextArea();
      JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
      JScrollPane titleScrollPane = new JScrollPane(titleArea);
      JPanel titlePanel = new JPanel();
      JPanel descriptionPanel = new JPanel();

      descriptionArea.setEditable(false);
      descriptionArea.setText(book.getDESCRIPTION());
      descriptionArea.setLineWrap(true);
      descriptionScrollPane.setPreferredSize(new Dimension(400, 150));
      descriptionPanel.setBorder(new EmptyBorder(10,10,10,10));

      titleArea.setEditable(false);
      titleArea.setText(book.getTITLE());
      titleScrollPane.setPreferredSize(new Dimension(400, 35));
      titlePanel.setBorder(new EmptyBorder(10,10,0,10));

      descriptionPanel.add(descriptionScrollPane);
      titlePanel.add(titleScrollPane);

      leftContentPanel.add(titlePanel);
      leftContentPanel.add(descriptionPanel);
   }

   private void buildRightContent() {
      authorArea = new JTextArea();
      genresArea = new JTextArea();
      availabilityArea = new JTextArea();
      navigationButton = new JButton("Where is the book?");
      navigationButton.addActionListener(new NavigationButtonListener());
      JPanel authorPanel = new JPanel();
      JPanel genresPanel = new JPanel();
      JPanel availabilityPanel = new JPanel();
      JPanel buttonPanel = new JPanel();
      JScrollPane authorScrollPane = new JScrollPane(authorArea);
      JScrollPane genresScrollPane = new JScrollPane(genresArea);
      JScrollPane availabilityScrollPane = new JScrollPane(availabilityArea);

      authorArea.setEditable(false);
      authorArea.setText("Author: " + book.getAUTHOR());
      authorScrollPane.setPreferredSize(new Dimension(500, 35));
      authorPanel.setBorder(new EmptyBorder(10, 10, 16, 10));

      genresArea.setEditable(false);
      genresArea.setText("Genres: " + book.getGENRES());
      genresScrollPane.setPreferredSize(new Dimension(500, 35));
      genresPanel.setBorder(new EmptyBorder(10,10,10,10));

      availabilityArea.setEditable(false);
      availabilityArea.setText(book.isAvailable() ? "Available": "Not Available");
      availabilityScrollPane.setPreferredSize(new Dimension(500, 35));
      availabilityPanel.setBorder(new EmptyBorder(16,10,10,10));

      availabilityPanel.add(availabilityScrollPane);
      genresPanel.add(genresScrollPane);
      authorPanel.add(authorScrollPane);

      buttonPanel.setLayout(new BorderLayout());
      buttonPanel.setPreferredSize(new Dimension(500, 28));
      buttonPanel.add(navigationButton, BorderLayout.EAST);

      if(Admin.isAdminOnline) {
         if(!book.isAvailable()) {
            returnButton = new JButton("Return");
            returnButton.addActionListener(new ReturnButtonListener());
            buttonPanel.add(returnButton, BorderLayout.CENTER);
         } else {
            borrowButton = new JButton("Borrow");
            borrowButton.addActionListener(new BorrowButtonListener());
            buttonPanel.add(borrowButton, BorderLayout.CENTER);
         }
      }

      rightContentPanel.add(authorPanel);
      rightContentPanel.add(genresPanel);
      rightContentPanel.add(availabilityPanel);
      rightContentPanel.add(buttonPanel);
   }

   private void searchBorrowedBooks() {
      try {
         FileReader fileReader = new FileReader("borrows.csv");
         BufferedReader reader = new BufferedReader(fileReader);

         String line;
         String[] parsed;
         while((line = reader.readLine()) != null) {
            parsed = line.split(",");
            if(Integer.parseInt(parsed[0]) == book.getLOCATION()) {
               book.setAvailable(false);
            }
         }
         reader.close();
      } catch(IOException ex) { }
   }



   class NavigationButtonListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         NavigationPage navigationPage = new NavigationPage(book.getLOCATION());
         new Thread(navigationPage::buildScreen).start();
      }
   }

   class BorrowButtonListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         int location = book.getLOCATION();
         Date date = new Date();
         long millis = date.getTime();
         String userId = JOptionPane.showInputDialog(null,"User ID:", "Form", JOptionPane.INFORMATION_MESSAGE);

         try {
            FileWriter fileWriter = new FileWriter("borrows.csv", true);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printWriter.print(location);
            printWriter.print(",");
            printWriter.print(millis);
            printWriter.print(",");
            printWriter.print(userId);
            printWriter.print(",");
            printWriter.println(book.getTITLE());
            printWriter.close();
         } catch(IOException ex) {
            ex.printStackTrace();
         }
         if(userId != null) {
            rightContentPanel.removeAll();
            searchBorrowedBooks();
            buildRightContent();
            rightContentPanel.updateUI();
         }
      }
   }

   class ReturnButtonListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         try {
            FileReader fileReader = new FileReader("borrows.csv");
            BufferedReader reader = new BufferedReader(fileReader);

            String userId = "";

            String line;
            ArrayList<String> lines = new ArrayList<>();
            String[] parsed = null;
            while((line = reader.readLine()) != null) {
               parsed = line.split(",");
               if(!(Integer.parseInt(parsed[0]) == book.getLOCATION())) {
                  lines.add(line);
               } else {
                  userId = parsed[2];
               }
            }
            reader.close();

            int selection = -1;
            if(parsed != null) {
               String message = "User ID: " + userId + "\nBook Title: " + book.getTITLE();
               selection = JOptionPane.showConfirmDialog(null,message, "Message", JOptionPane.YES_NO_OPTION);
            }

            if(selection == 0) {
               FileWriter fileWriter = new FileWriter("borrows.csv");
               PrintWriter writer = new PrintWriter(fileWriter);

               for (String borrowRecord : lines) {
                  writer.println(borrowRecord);
               }

               writer.close();

               book.setAvailable(true);

               rightContentPanel.removeAll();
               buildRightContent();
               rightContentPanel.updateUI();
            }
         } catch(IOException ex) {
            ex.printStackTrace();
         }
      }
   }
}
