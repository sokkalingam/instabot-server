package instagram.model;

import java.time.LocalDateTime;

public class Report {

    private LocalDateTime startTime;
    private int currentLoop;
    private int photosLiked;
    private int photosCommented;
    private String currentHashtag;

    public Report() {
        this.startTime = LocalDateTime.now();
    }

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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getCurrentHashtag() {
        return currentHashtag;
    }

    public void setCurrentHashtag(String currentHashtag) {
        this.currentHashtag = currentHashtag;
    }

    @Override
    public String toString() {
        return "Report{" + "currentLoop=" + currentLoop + ", photosLiked=" + photosLiked + ", photosCommented="
                        + photosCommented + '}';
    }
}
