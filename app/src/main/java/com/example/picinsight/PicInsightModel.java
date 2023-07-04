package com.example.picinsight;

public class PicInsightModel {
    private int id;
    private String answer, question;

    public PicInsightModel(int id, String answer, String question) {
        this.id = id;
        this.answer = answer;
        this.question = question;
    }

    public PicInsightModel(String answer, String question) {
        this.answer = answer;
        this.question = question;
    }

    public PicInsightModel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
