
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

enum NodeTypes {
   StartNode,
   EndNode,
   AvailableNode,
   WallNode,
   PathNode,
   SearchedNode,
}

public class NavigationPage {
   final private int ROW_SIZE = 50; // 50
   final private int COL_SIZE = 130; // 130

   private JFrame frame;
   private JPanel panel;
   private JPanel bottomPanel;
   private JPanel gridViewPanel;
   private Node[][] nodes;

   //index0 start node, index1 end node
   private Node[] previousNodes;

   private Node tempNode;

   private int startNodeRow = -1;
   private int startNodeCol = -1;

   final private int destination_id;

   public NavigationPage(int destination_id) {
      previousNodes = new Node[2];
      nodes = (Node[][]) LibrarySketchManager.loadLibrarySketch();
      this.destination_id = destination_id;
      setNodes();
      updateStartNodeLocation();
   }

   void buildScreen() {
      frame = new JFrame();
      panel = new JPanel();
      bottomPanel = new JPanel();
      gridViewPanel = new JPanel();

      panel.setLayout(new BorderLayout());
      panel.setBackground(Color.WHITE);
      gridViewPanel.setLayout(new GridLayout(ROW_SIZE, COL_SIZE));
      gridViewPanel.setBorder(new EmptyBorder(80,50,80,50));
      buildGridView();

      JButton backButton = new JButton("Back");
      JButton run = new JButton("Run");
      JButton saveSketch = new JButton("Save Sketch");

      saveSketch.addActionListener(new SaveSketchListener());
      run.addActionListener(new RunAlgorithmButtonListener());
      backButton.addActionListener(new BackButtonListener());

      bottomPanel.setLayout(new FlowLayout());
      bottomPanel.add(backButton);
      bottomPanel.add(run);
      bottomPanel.add(saveSketch);
      bottomPanel.setBackground(Color.BLUE);

      panel.add(gridViewPanel, BorderLayout.CENTER);
      panel.add(bottomPanel, BorderLayout.SOUTH);

      frame.setLayout(null);
      frame.setSize(new Dimension(LibrarySearchPanel.W_WIDTH, LibrarySearchPanel.W_HEIGHT));
      frame.setContentPane(panel);
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setLocationRelativeTo(null);
      frame.setResizable(false);
      frame.setVisible(true);
   }

   private void buildGridView() {
      for(int row = 0; row < ROW_SIZE; row++) {
         for(int col = 0; col < COL_SIZE; col++) {
            gridViewPanel.add(nodes[row][col].getNode());
         }
      }
   }

   private void setNodes() {
      if(nodes == null) {
         nodes = new Node[ROW_SIZE][COL_SIZE];
         for(int row = 0; row < ROW_SIZE; row++) {
            for(int col = 0; col < COL_SIZE; col++) {
               nodes[row][col] = new Node(row, col, NodeTypes.AvailableNode);
               nodes[row][col].getNodePanel().addMouseListener(new NodeMouseListener(nodes[row][col]));
               nodes[row][col].getNodePanel().addMouseMotionListener(new NodeMouseMotionListener());
            }
         }
      } else {
         LinkedList<Book> allBooks = getAllBooksFromDatabase();
         for(int row = 0; row < ROW_SIZE; row++) {
            for(int col = 0; col < COL_SIZE; col++) {
               nodes[row][col].getNodePanel().addMouseListener(new NodeMouseListener(nodes[row][col]));
               nodes[row][col].getNodePanel().addMouseMotionListener(new NodeMouseMotionListener());
               fillShelvesWithBooks(row, col, allBooks);
               if(nodes[row][col].getNodeType() == NodeTypes.WallNode) {
                  if (nodes[row][col].containDestinationPoint(destination_id)) {
                     nodes[row][col].setNodeType(NodeTypes.EndNode);
                  }
               }
            }
         }
      }
   }

   private LinkedList<Book> getAllBooksFromDatabase() {
      LinkedList<Book> results = new LinkedList<>();
      try {
         if(DBConnection.connection == null) {
            DBConnection.getConnection();
         }
         Statement statement = DBConnection.connection.createStatement();
         ResultSet resultSet = statement.executeQuery(
            "SELECT * FROM books ORDER BY genres;");

         // firstly collect the results in list, then update ui from that list
         while(resultSet.next()) {
            results.add(new Book(
               resultSet.getString("book_title"),
               resultSet.getString("book_desc"),
               resultSet.getString("book_authors"),
               resultSet.getString("genres"),
               resultSet.getInt("location"),
               true
            ));
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return results;
   }

   private void fillShelvesWithBooks(int row, int col, LinkedList<Book> allBooks) {
      if(nodes[row][col].getNodeType() == NodeTypes.WallNode) {
         Book[][] books = new Book[Node.SHELF_NUMBER][Node.BOOK_CAPACITY];
         for(int shelf = 0; shelf < Node.SHELF_NUMBER; shelf++) {
            for (int i = 0; i < Node.BOOK_CAPACITY; i++) {
               if (!allBooks.isEmpty()) {
                  books[shelf][i] = allBooks.removeLast();
               }
            }
         }
         nodes[row][col].setBooks(books);
      }
   }

   private void setDistanceToCurrentNode(int nodeRow, int nodeCol) {
      int distance;
      for(int row = 0; row < ROW_SIZE; row++) {
         for(int col = 0; col < COL_SIZE; col++) {
            distance = Math.abs(nodeRow - row) + Math.abs(nodeCol - col);
            nodes[row][col].setDistance(distance);
         }
      }
   }

   public synchronized void runDijkstra() {
      int distance = 1;
      if(startNodeCol > -1 && startNodeRow > -1) {
         Queue<Node> queue = new LinkedList<>();
         queue.offer(nodes[startNodeRow][startNodeCol]);
         while(!queue.isEmpty()) {
            Node currentNode = queue.remove();
            setDistanceToCurrentNode(currentNode.getRow(), currentNode.getCol());
            ArrayList<Node> children = new ArrayList<>();
            for(int row = 0; row < ROW_SIZE; row++) {
               for(int col = 0; col < COL_SIZE; col++) {
                  if(nodes[row][col].getDistance() == 1) {
                     children.add(nodes[row][col]);
                  }
               }
            }

            for(Node child: children) {
               if(child.getNodeType() == NodeTypes.AvailableNode) {
                  try {
                     Thread.sleep(10);
                  } catch(InterruptedException ex) {
                     ex.printStackTrace();
                  }
                  child.setNodeType(NodeTypes.SearchedNode);
                  child.setDistanceToStartNode(distance++);
                  queue.offer(child);
               } else if(child.getNodeType() == NodeTypes.EndNode) {
                  drawShortestPath(child.getRow(), child.getCol());
                  return;
               }
            }
         }
      }
   }

   private void drawShortestPath(int nodeRow, int nodeCol) {
      Queue<Node> queue = new LinkedList<>();
      queue.offer(nodes[nodeRow][nodeCol]);
      while(!queue.isEmpty()) {
         Node currentNode = queue.remove();
         setDistanceToCurrentNode(currentNode.getRow(), currentNode.getCol());
         ArrayList<Node> children = new ArrayList<>();
         for(int row = 0; row < ROW_SIZE; row++) {
            for(int col = 0; col < COL_SIZE; col++) {
               if(nodes[row][col].getDistance() == 1) {
                  children.add(nodes[row][col]);
               }
            }
         }
         int minDistance = Integer.MAX_VALUE;
         Node closestNode = null;
         for(Node child : children) {
            if(child.getNodeType() == NodeTypes.SearchedNode || child.getNodeType() == NodeTypes.StartNode) {
               if(child.getNodeType() == NodeTypes.StartNode) {
                  return;
               }
               if(child.getDistanceToStartNode() < minDistance) {
                  minDistance = child.getDistanceToStartNode();
                  closestNode = child;
               }
            }
         }
         if(closestNode != null) {
            try{
               Thread.sleep(20);
            } catch(InterruptedException ex) {
               ex.printStackTrace();
            }
            closestNode.setNodeType(NodeTypes.PathNode);
            queue.offer(closestNode);
         }
      }
   }

   private void updateStartNodeLocation() {
      for(int row = 0; row < ROW_SIZE; row++) {
         for(int col = 0; col < COL_SIZE; col++) {
            if(nodes[row][col].getNodeType() == NodeTypes.StartNode) {
               startNodeRow = row;
               startNodeCol = col;
            }
         }
      }
   }

   private void updateNode(int val, Node selectedNode) {
      if(val == 1) {
         if(!(previousNodes[0] == null)) {
            if(selectedNode.getNodeType() == NodeTypes.EndNode) {
               previousNodes[1] = null;
            }
            previousNodes[0].setNodeType(NodeTypes.AvailableNode);
         }
         previousNodes[0] = selectedNode;
         selectedNode.setNodeType(NodeTypes.StartNode);
         startNodeRow = selectedNode.getRow();
         startNodeCol = selectedNode.getCol();
      } else if(val == 0) {
         if(!(previousNodes[1] == null)) {
            if(selectedNode.getNodeType() == NodeTypes.StartNode) {
               previousNodes[0] = null;
            }
            previousNodes[1].setNodeType(NodeTypes.AvailableNode);
         }
         previousNodes[1] = selectedNode;
         selectedNode.setNodeType(NodeTypes.EndNode);
      } else if(val == 2) {
         selectedNode.setNodeType(NodeTypes.AvailableNode);
      }
   }

   class NodeMouseListener implements MouseListener {

      private Node selectedNode;
      private String[] optionTextList = {"end", "start", "empty"};

      NodeMouseListener(Node selectedNode) {
         this.selectedNode = selectedNode;
      }

      @Override
      public void mouseClicked(MouseEvent e) {
         int val;
         val = JOptionPane.showOptionDialog(
            selectedNode.getNodePanel(),
            "Choose the Type of the Node",
            "",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            optionTextList,
            optionTextList[1]
         );
         updateNode(val, selectedNode);
         selectedNode.printBooks();
      }

      @Override
      public void mousePressed(MouseEvent e) {

      }

      @Override
      public void mouseReleased(MouseEvent e) {

      }

      @Override
      public void mouseEntered(MouseEvent e) {
         tempNode = selectedNode;
      }

      @Override
      public void mouseExited(MouseEvent e) {

      }
   }

   class NodeMouseMotionListener implements MouseMotionListener {
      @Override
      public void mouseDragged(MouseEvent e) {
         tempNode.setNodeType(NodeTypes.WallNode);
      }

      @Override
      public void mouseMoved(MouseEvent e) {

      }
   }

   class RunAlgorithmButtonListener implements ActionListener {
      @Override
      public synchronized void actionPerformed(ActionEvent e) {
         Thread dijkstraThread = new Thread(() -> {
            runDijkstra();
         });
         dijkstraThread.start();
      }
   }

   class BackButtonListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         frame.dispose();
      }
   }

   class SaveSketchListener implements ActionListener {
      @Override
      public void actionPerformed(ActionEvent e) {
         LibrarySketchManager.saveLibrarySketch(nodes);
      }
   }
}
