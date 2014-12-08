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

    private String textValue;
    private Integer intValue;
    private Double realValue;
    private byte[] blobValue;

    public void clearData() {
        textValue = null;
        intValue = null;
        realValue = null;
        blobValue = null;
    }

    public Boolean getBoolValue() {
        if (intValue == null) {
            return null;
        }

        return (intValue != 0);
    }

    public String getTextValue() {
        return textValue;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public Double getRealValue() {
        return realValue;
    }

    public byte[] getBlobValue() {
        return blobValue;
    }

    public void setBoolAnswer(Boolean boolAnswer) {
        if (boolAnswer == null) {
            setIntValue(null);
        } else {
            setIntValue(boolAnswer ? 1 : 0);
        }
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public void setRealValue(Double realValue) {
        this.realValue = realValue;
    }

    public void setBlobValue(byte[] blob) {
        this.blobValue = blob;
    }
}
