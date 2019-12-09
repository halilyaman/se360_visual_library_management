import sun.invoke.empty.Empty;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

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

   final private String TITLE;
   final private String DESCRIPTION;
   final private String AUTHOR;
   final private String GENRES;
   private boolean isAvailable;

   public SingleSearchResult(
      String title,
      String description,
      String author,
      String genres,
      boolean isAvailable
   ) {
      TITLE = title;
      DESCRIPTION = description;
      AUTHOR = author;
      GENRES = genres;
      this.isAvailable = isAvailable;
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
      descriptionArea.setText(DESCRIPTION);
      descriptionArea.setLineWrap(true);
      descriptionScrollPane.setPreferredSize(new Dimension(400, 150));
      descriptionPanel.setBorder(new EmptyBorder(10,10,10,10));

      titleArea.setEditable(false);
      titleArea.setText(TITLE);
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
      navigationButton = new JButton("Show me the location");
      JPanel authorPanel = new JPanel();
      JPanel genresPanel = new JPanel();
      JPanel availabilityPanel = new JPanel();
      JPanel buttonPanel = new JPanel();
      JScrollPane authorScrollPane = new JScrollPane(authorArea);
      JScrollPane genresScrollPane = new JScrollPane(genresArea);
      JScrollPane availabilityScrollPane = new JScrollPane(availabilityArea);

      authorArea.setEditable(false);
      authorArea.setText("Author: " + AUTHOR);
      authorScrollPane.setPreferredSize(new Dimension(500, 35));
      authorPanel.setBorder(new EmptyBorder(10, 10, 16, 10));

      genresArea.setEditable(false);
      genresArea.setText("Genres: " + GENRES);
      genresScrollPane.setPreferredSize(new Dimension(500, 35));
      genresPanel.setBorder(new EmptyBorder(10,10,10,10));

      availabilityArea.setEditable(false);
      availabilityArea.setText(isAvailable ? "Available": "Not Available");
      availabilityScrollPane.setPreferredSize(new Dimension(500, 35));
      availabilityPanel.setBorder(new EmptyBorder(16,10,10,10));

      availabilityPanel.add(availabilityScrollPane);
      genresPanel.add(genresScrollPane);
      authorPanel.add(authorScrollPane);

      buttonPanel.setLayout(new BorderLayout());
      buttonPanel.setPreferredSize(new Dimension(500, 28));
      buttonPanel.add(navigationButton, BorderLayout.EAST);

      rightContentPanel.add(authorPanel);
      rightContentPanel.add(genresPanel);
      rightContentPanel.add(availabilityPanel);
      rightContentPanel.add(buttonPanel);
   }
}
