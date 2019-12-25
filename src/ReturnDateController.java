import java.io.*;
import java.util.ArrayList;

public class ReturnDateController implements Runnable {
   ArrayList<String> borrowingRecords;

   public ReturnDateController() {
      borrowingRecords = new ArrayList<>();
   }

   @Override
   public void run() {
      try {
         FileReader fileReader = new FileReader("borrows.csv");
         BufferedReader reader = new BufferedReader(fileReader);
         String line;
         while ((line = reader.readLine()) != null) {
            borrowingRecords.add(line);
         }
         reader.close();
      } catch (IOException ex) { }

      int timeOut = 10000;
      long currentTime = System.currentTimeMillis();
      int twentyOneDaysInMillisecond = 1814400000; // 1814400000ms = 21days
      while (System.currentTimeMillis() - currentTime < timeOut) {
         for(int i = 0; i < borrowingRecords.size(); i++) {
            String[] parsed = borrowingRecords.get(i).split(",");

            if(System.currentTimeMillis() - Long.parseLong(parsed[1]) > twentyOneDaysInMillisecond) {
               borrowingRecords.remove(borrowingRecords.get(i));
               try {
                  FileWriter fileWriter = new FileWriter("borrows.csv");
                  PrintWriter writer = new PrintWriter(fileWriter);
                  for(String line : borrowingRecords) {
                     writer.println(line);
                  }
                  writer.close();
               } catch (IOException ex) { }
            }
         }
      }

   }
}
