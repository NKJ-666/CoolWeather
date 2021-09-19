package com.example.coolweather.model;

public class Suggestion {
    private String ComfortLevel;

    private String clearCarLevel;

    private String exerciseLevel;

    public Suggestion(String comfortLevel, String clearCarLevel, String exerciseLevel) {
        ComfortLevel = comfortLevel;
        this.clearCarLevel = clearCarLevel;
        this.exerciseLevel = exerciseLevel;
    }

    public String getComfortLevel() {
        return ComfortLevel;
    }

    public void setComfortLevel(String comfortLevel) {
        ComfortLevel = comfortLevel;
    }

    public String getClearCarLevel() {
        return clearCarLevel;
    }

    public void setClearCarLevel(String clearCarLevel) {
        this.clearCarLevel = clearCarLevel;
    }

    public String getExerciseLevel() {
        return exerciseLevel;
    }

    public void setExerciseLevel(String exerciseLevel) {
        this.exerciseLevel = exerciseLevel;
    }
}
