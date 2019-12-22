import java.io.Serializable;

public class Book implements Serializable {
   final private String TITLE;
   final private String DESCRIPTION;
   final private String AUTHOR;
   final private String GENRES;
   final private Integer LOCATION;
   final private boolean AVAILABILITY;

   public Book(String title, String description, String author, String genres, int location, boolean availability) {
      this.TITLE = title;
      this.DESCRIPTION = description;
      this.AUTHOR = author;
      this.GENRES = genres;
      this.LOCATION = location;
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

   public Integer getLOCATION() {
      return LOCATION;
   }
}
