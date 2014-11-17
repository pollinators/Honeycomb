package io.github.pollinators.honeycomb.data.models;

/**
 * Created by ted on 11/3/14.
 */
public class SurveyAnswer extends AbstractModel {

    private String textAnswer;
    private Integer intAnswer;
    private Double realAnswer;
    private byte[] blobAnswer;

    //**********************************************************************************************
    // GETTERS
    //**********************************************************************************************

    public boolean getBoolAnswer() {
        return (intAnswer != 0);
    }

    public String getTextAnswer() {
        return textAnswer;
    }

    public Integer getIntAnswer() {
        return intAnswer;
    }

    public Double getRealAnswer() {
        return realAnswer;
    }

    public byte[] getBlobAnswer() {
        return blobAnswer;
    }

    //**********************************************************************************************
    // SETTERS
    //**********************************************************************************************

    public void setBoolAnswer(Boolean boolAnswer) {
        setIntAnswer(boolAnswer ? 1 : 0);
    }

    public void setTextAnswer(String textAnswer) {
        this.textAnswer = textAnswer;
    }

    public void setIntAnswer(Integer intAnswer) {
        this.intAnswer = intAnswer;
    }

    public void setRealAnswer(Double realAnswer) {
        this.realAnswer = realAnswer;
    }

    public void setBlobAnswer(byte[] blob) {
        this.blobAnswer = blob;
    }
}
