package instagram.model;

public class Report {

    private int currentLoop;
    private int photosLiked;
    private int photosCommented;

    public void incrementPhotoLiked() {
        photosLiked++;
    }

    public void incrementCurrentLoop() {
        currentLoop++;
    }

    public void incrementPhotosCommented() {
        photosCommented++;
    }

    public int getCurrentLoop() {
        return currentLoop;
    }

    public void setCurrentLoop(int currentLoop) {
        this.currentLoop = currentLoop;
    }

    public int getPhotosLiked() {
        return photosLiked;
    }

    public void setPhotosLiked(int photosLiked) {
        this.photosLiked = photosLiked;
    }

    public int getPhotosCommented() {
        return photosCommented;
    }

    public void setPhotosCommented(int photosCommented) {
        this.photosCommented = photosCommented;
    }

    @Override
    public String toString() {
        return "Report{" + "currentLoop=" + currentLoop + ", photosLiked=" + photosLiked + ", photosCommented="
                        + photosCommented + '}';
    }
}
