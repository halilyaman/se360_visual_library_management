import java.io.*;

public class LibrarySketchManager {
   public static Object loadLibrarySketch(String name) {
      try {
         FileInputStream inputStream = new FileInputStream(new File(name));
         ObjectInputStream ois = new ObjectInputStream(inputStream);
         return ois.readObject();
      } catch (IOException | ClassNotFoundException e) {
         return null;
      }
   }

   public static void saveLibrarySketch(Node[][] nodes, String name) {
      try {
         FileOutputStream outputStream = new FileOutputStream(new File(name));
         ObjectOutputStream oos = new ObjectOutputStream(outputStream);
         oos.writeObject(nodes);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public static void saveLibraryPattern(int[][] pattern, String name) {
      try {
         FileOutputStream outputStream = new FileOutputStream(new File(name));
         ObjectOutputStream oos = new ObjectOutputStream(outputStream);
         oos.writeObject(pattern);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
