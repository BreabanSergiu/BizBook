package socialnetwork.domain;



public class Photo extends Entity<Long>{

    String URL;

    public Photo(String URL) {
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "URL='" + URL + '\'' +
                '}';
    }
}
