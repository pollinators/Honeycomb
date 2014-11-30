package io.github.pollinators.honeycomb.data.models;

/**
 * Created by ted on 11/3/14.
 */
// Create these annotations to hadnle my bidding.

// Will connected the classes (POJO) data to the network. Searching  best matching nam with the class
// You may annotate as @DoesNotTransfer if
//@NetworkConnected
//@Saveable
public class Answer extends AbstractModel {

    //**********************************************************************************************
    // NON-STATIC DATA MEMBERS
    //**********************************************************************************************

    private String textAnswer;
    private Integer intAnswer;
    private Double realAnswer;
    private byte[] blobAnswer;

    public void clearData() {
        textAnswer = null;
        intAnswer = null;
        realAnswer = null;
        blobAnswer = null;
    }

    public Boolean getBoolAnswer() {
        if (intAnswer == null) {
            return null;
        }

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

    public void setBoolAnswer(Boolean boolAnswer) {
        if (boolAnswer == null) {
            setIntAnswer(null);
        } else {
            setIntAnswer(boolAnswer ? 1 : 0);
        }
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
