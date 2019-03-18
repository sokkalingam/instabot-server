package instagram.model;

import instagram.model.enums.JobStatus;
import instagram.utils.DateUtils;

import java.time.LocalDateTime;

public class Report {

    private String username;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int photosLiked;
    private int photosCommented;
    private String currentHashtag;
    private JobStatus jobStatus;
    private Data data;

    public Report(String username) {
        this.username = username;
        this.startTime = LocalDateTime.now();
        this.jobStatus = JobStatus.QUEUED;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void incrementPhotoLiked() {
        photosLiked++;
    }

    public void incrementPhotosCommented() {
        photosCommented++;
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "REPORT \n\n" +
                "Start Time: " + DateUtils.format(startTime) +
                "\nEnd Time: " + DateUtils.format(endTime) +
                "\nPhotos Liked: " + photosLiked +
                "\nPhotos Commented: " + photosCommented +
                "\nJob Status: " + jobStatus;
    }

}
