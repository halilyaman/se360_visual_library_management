import java.io.Serializable;

public class Book implements Serializable {
   final private String TITLE;
   final private String DESCRIPTION;
   final private String AUTHOR;
   final private String GENRES;
   final private Integer LOCATION;
   final private String IMAGE_URL;
   final private boolean AVAILABILITY;

   public Book(String title, String description, String author, String genres, int location, String imageUrl, boolean availability) {
      this.TITLE = title;
      this.DESCRIPTION = description;
      this.AUTHOR = author;
      this.GENRES = genres;
      this.LOCATION = location;
      this.AVAILABILITY = availability;
      this.IMAGE_URL = imageUrl;
   }

   public String getIMAGE_URL() {
      return IMAGE_URL;
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
