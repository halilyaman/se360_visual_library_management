public class RunApp {

   public static void main(String[] args) {
      RunApp app = new RunApp();
      app.runApp();
   }

   public void runApp() {
      new LibrarySearchPanel(
         "Book Title",
         "Library System",
         "IUE Library ©",
         LibrarySearchPanel.DEFAULT_BG_COLOR
      );
   }
}
