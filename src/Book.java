public class Book {
   final private String TITLE;
   final private String DESCRIPTION;
   final private String AUTHOR;
   final private String GENRES;
   final private boolean AVAILABILITY;

   public Book(String title, String description, String author, String genres, boolean availability) {
      this.TITLE = title;
      this.DESCRIPTION = description;
      this.AUTHOR = author;
      this.GENRES = genres;
      this.AVAILABILITY = availability;
   }

   public String getTITLE() {
      return TITLE;
   }

   public String getDESCRIPTION() {
      return DESCRIPTION;
   }

   public String getAUTHOR() {
      return AUTHOR;
   }

   public String getGENRES() {
      return GENRES;
   }

   public boolean isAvailable() {
      return AVAILABILITY;
   }
}
