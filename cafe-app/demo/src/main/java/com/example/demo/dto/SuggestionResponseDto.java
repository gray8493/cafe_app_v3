package com.example.demo.dto;

public class SuggestionResponseDto {
    private int age;
    private String emotion;
    private String suggestion;

    public SuggestionResponseDto() {}

    public SuggestionResponseDto(int age, String emotion, String suggestion) {
        this.age = age;
        this.emotion = emotion;
        this.suggestion = suggestion;
    }

    // Getters and Setters
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getEmotion() { return emotion; }
    public void setEmotion(String emotion) { this.emotion = emotion; }
    public String getSuggestion() { return suggestion; }
    public void setSuggestion(String suggestion) { this.suggestion = suggestion; }
}