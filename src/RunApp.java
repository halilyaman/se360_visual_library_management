public class RunApp {

   public static void main(String[] args) {
      RunApp app = new RunApp();
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
