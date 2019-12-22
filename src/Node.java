import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.Serializable;

public class Node implements Serializable {

   public static int BOOK_CAPACITY = 20;
   public static int SHELF_NUMBER = 5;

   private JPanel nodePanel = new JPanel();
   private JPanel inlineNodePanel = new JPanel();

   private NodeTypes nodeType;
   private int row;
   private int col;
   private Color color;
   private int distance;
   private int distanceToStartNode = Integer.MAX_VALUE;

   private Book[][] books;

   public Node(int row, int col, NodeTypes nodeType) {
      this.row = row;
      this.col = col;
      this.nodeType = nodeType;
      if(nodeType == NodeTypes.AvailableNode) {
         this.color = Color.BLUE; // 234, 237, 237
      } else if (nodeType == NodeTypes.StartNode) {
         this.color = Color.green;
      } else if(nodeType == NodeTypes.EndNode) {
         this.color = Color.red;
      } else if(nodeType == NodeTypes.WallNode) {
         this.color = Color.black;
      }
   }

   public int getDistanceToStartNode() {
      return distanceToStartNode;
   }

   public void setDistanceToStartNode(int distanceToStartNode) {
      this.distanceToStartNode = distanceToStartNode;
   }

   public int getDistance() {
      return distance;
   }

   public void setDistance(int distance) {
      this.distance = distance;
   }

   public void setNodePanel(JPanel nodePanel) {
      this.nodePanel = nodePanel;
   }

   public JPanel getInlineNodePanel() {
      return inlineNodePanel;
   }

   public void setInlineNodePanel(JPanel inlineNodePanel) {
      this.inlineNodePanel = inlineNodePanel;
   }

   public NodeTypes getNodeType() {
      return nodeType;
   }

   public void setNodeType(NodeTypes nodeType) {
      this.nodeType = nodeType;
      if(nodeType == NodeTypes.AvailableNode) {
         this.setColor(Color.BLUE);
      } else if(nodeType == NodeTypes.StartNode) {
         this.distanceToStartNode = 0;
         this.setColor(Color.green);
      } else if(nodeType == NodeTypes.EndNode) {
         this.setColor(Color.red);
      } else if(nodeType == NodeTypes.WallNode) {
         this.setColor(Color.black);
      } else if(nodeType == NodeTypes.PathNode) {
         this.setColor(Color.BLUE);
      } else if(nodeType == NodeTypes.SearchedNode) {
         this.setColor(Color.orange);
      }
   }

   public int getRow() {
      return row;
   }

   public void setRow(int row) {
      this.row = row;
   }

   public int getCol() {
      return col;
   }

   public void setCol(int col) {
      this.col = col;
   }

   public Color getColor() {
      return color;
   }

   public void setColor(Color color) {
      this.color = color;
      inlineNodePanel.setBackground(this.color);
   }

   public JPanel getNodePanel() {
      return nodePanel;
   }

   JPanel getNode() {
      nodePanel.setLayout(new BorderLayout());
      inlineNodePanel.setBackground(this.color);
      nodePanel.add(inlineNodePanel, BorderLayout.CENTER);
      return nodePanel;
   }

   public void setBooks(Book[][] books) {
      this.books = new Book[SHELF_NUMBER][BOOK_CAPACITY];
      this.books = books;
   }

   public void printBooks() {
      if(books != null) {
         for(int j = 0; j < SHELF_NUMBER; j++) {
            for (int i = 0; i < BOOK_CAPACITY; i++) {
               if(books[j][i] != null) {
                  System.out.println(books[j][i].getTITLE());
               }
            }
         }
      }
   }

   public boolean containDestinationPoint(int location_id) {
      for(int j = 0; j < SHELF_NUMBER; j++) {
         for (int i = 0; i < BOOK_CAPACITY; i++) {
            if(books[j][i] != null) {
               if (books[j][i].getLOCATION() == location_id) {
                  return true;
               }
            }
         }
      }
      return false;
   }
}
