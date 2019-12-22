import java.io.*;

public class LibrarySketchManager {
   public static Object loadLibrarySketch() {
      try {
         FileInputStream inputStream = new FileInputStream(new File("library_sketch.dat"));
         ObjectInputStream ois = new ObjectInputStream(inputStream);
         return ois.readObject();
      } catch (IOException | ClassNotFoundException e) {
         return null;
      }
   }

   public static void saveLibrarySketch(Node[][] nodes) {
      try {
         FileOutputStream outputStream = new FileOutputStream(new File("library_sketch.dat"));
         ObjectOutputStream oos = new ObjectOutputStream(outputStream);
         oos.writeObject(nodes);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
