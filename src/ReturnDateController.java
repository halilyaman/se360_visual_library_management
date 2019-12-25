import java.io.*;
import java.util.ArrayList;

public class ReturnDateController implements Runnable {
   ArrayList<String> borrowingRecords;
   ArrayList<String> deadLinePenalty;
   public ReturnDateController() {
      borrowingRecords = new ArrayList<>();
      deadLinePenalty = new ArrayList<>();
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

      int timeOut = 2000;
      long currentTime = System.currentTimeMillis();
      int twentyOneDaysInMillisecond = 20000; // 1814400000ms = 21days
      while (System.currentTimeMillis() - currentTime < timeOut) {
         for(int i = 0; i < borrowingRecords.size(); i++) {
            String[] parsed = borrowingRecords.get(i).split(",");

            if(System.currentTimeMillis() - Long.parseLong(parsed[1]) > twentyOneDaysInMillisecond) {
               deadLinePenalty.add(borrowingRecords.get(i));
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
      if(deadLinePenalty.size() != 0) {
         try {
            FileWriter fileWriter = new FileWriter("deadline_penalties.csv");
            PrintWriter writer = new PrintWriter(fileWriter);
            for(String line : deadLinePenalty) {
               writer.println(line);
            }
            writer.close();
         } catch (IOException ex) { }
      }
   }
}
