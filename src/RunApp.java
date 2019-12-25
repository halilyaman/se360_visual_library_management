public class RunApp {

   public static void main(String[] args) {
      RunApp app = new RunApp();
      Thread controllerThread = new Thread(new ReturnDateController());
      controllerThread.start();
      app.runApp();
   }

   public void runApp() {
      new LibrarySearchPanel(
         "Book Title",
         "Library System",
         "Labyrinth Library ©",

         LibrarySearchPanel.DEFAULT_BG_COLOR
      );
   }
}
