package com.example.mysignlanguege.model;

public class SignLanguageCourse {

    protected String id,name,type,desc;
    protected double time;
    int numLesson;
    int totalLesson;

    protected String videoRef;

    public SignLanguageCourse(String id, String name, String type, String desc, double time, int numLesson, int totalLesson, String videoRef) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.time = time;
        this.numLesson = numLesson;
        this.totalLesson = totalLesson;
        this.videoRef = videoRef;
    }

    public SignLanguageCourse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getNumLesson() {
        return numLesson;
    }

    public void setNumLesson(int numLesson) {
        this.numLesson = numLesson;
    }

    public int getTotalLesson() {
        return totalLesson;
    }

    public void setTotalLesson(int totalLesson) {
        this.totalLesson = totalLesson;
    }

    public String getVideoRef() {
        return videoRef;
    }

    public void setVideoRef(String videoRef) {
        this.videoRef = videoRef;
    }

    @Override
    public String toString() {
        return "SlCourse{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", desc='" + desc + '\'' +
                ", time=" + time +
                ", numLesson=" + numLesson +
                ", totalLesson=" + totalLesson +
                ", videoRef='" + videoRef + '\'' +
                '}';
    }
}
