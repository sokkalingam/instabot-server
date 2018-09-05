package instagram.model;

import instagram.model.enums.JobStatus;

import java.time.LocalDateTime;

public class Report {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int currentLoop;
    private int photosLiked;
    private int photosCommented;
    private String currentHashtag;
    private JobStatus jobStatus;

    public Report() {
        this.startTime = LocalDateTime.now();
        this.jobStatus = JobStatus.CREATED;
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

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setEndTimeAsNow() {
        this.endTime = LocalDateTime.now();
    }

    public void setJobAsCompleted() {
        this.jobStatus = JobStatus.COMPLETED;
        setEndTimeAsNow();
    }

    public void setJobAsAborted() {
        this.jobStatus = JobStatus.ABORTED;
        setEndTimeAsNow();
    }

    @Override
    public String toString() {
        return "Report{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", currentLoop=" + currentLoop +
                ", photosLiked=" + photosLiked +
                ", photosCommented=" + photosCommented +
                ", currentHashtag='" + currentHashtag + '\'' +
                ", jobStatus=" + jobStatus +
                '}';
    }
}
