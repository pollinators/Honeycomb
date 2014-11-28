package io.github.pollinators.honeycomb.data.models;

import java.util.List;

import io.github.pollinators.honeycomb.data.models.AbstractModel;

/**
 * This model represents all possible surveys that could be done. It holds some meta data about the
 * survey, and the responses associated with it.
 */
public class SurveyResponseModel extends AbstractModel {

    //**********************************************************************************************
    // NON-STATIC DATA MEMBERS
    //**********************************************************************************************

    /**
     * Time that the survey was started (measured in milliseconds since epoch<br>
     * (System.currentTimeMillis()))
     */
    private long startTime;

    /**
     * Duration, in milliseconds, that the survey was in progress.
     */
    private int duration;

    /**
     * Unique username of the user who completed the survey. Useful for storing other users' data
     * in the same database.<br>
     * Some use cases include: querying regional data and multiple users on a single device.
     */
    private long userId;

    /**
     * The broader of the two survey types. This can include: Pollinators, Migration Patterns, etc.
     */
    private int surveyType;

    /**
     * The more narrow type of the survey, that usually must be associated with it's parent type,
     * but doesn't always have to be.<br>
     * Examples include: Bees, Birds, Bats, Ants
     */
    private int surveySubtype;

    /**
     * The latitude coordinate obtained where the survey took place
     */
    private Float latitude;

    /**
     * The longitude coordinate obtained where the survey took place
     */
    private Float longitude;

    /**
     * This is a numeric indicator of which set of questions to use so that we can easily
     * interchange them for any type of survey
     */
    private int questionSet;

    /**
     * NOT PERSISTED<br>
     */
    private long[] answerIds;

    /**
     * NOT PERSISTED<br>
     * Number of questions to be answered
     * TODO: Make non-magic number
     */
    private int questionCount = 10;

    //**********************************************************************************************
    // GETTERS
    //**********************************************************************************************

    public long getStartTime() {
        return startTime;
    }

    /**
     * Convenience method that calculates the ending time of the survey
     * @return the time that the survey ended
     */
    public long getEndTime() {
        return startTime + duration;
    }

    public int getDuration() {
        return duration;
    }

    public Float getLatitude() {
        return latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public long getUserId() {
        return userId;
    }

    /**
     * Convenience method that transforms the userId into the username
     * @return the username associated with the userId
     */
    public String getUsername() {
        // TODO: implement findUsername
        //return findUsername(userId);
        return null;
    }

    public int getSurveyType() {
        return surveyType;
    }

    public int getSurveySubtype() {
        return surveySubtype;
    }

    public int getQuestionSet() {
        return questionSet;
    }

    public long[] getAnswerIds() {
        return answerIds;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    //**********************************************************************************************
    // SETTERS
    //**********************************************************************************************

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * This makes it easy to store the duration. It is calculated here for convenience.
     */
    public void setSurveyEnded(boolean hasEnded) {
        if (hasEnded) {
            setDuration((int) (System.currentTimeMillis() - startTime));
        } else {
            setDuration(0);
        }
    }

    public void setEndTime(long endTime) {
        setDuration((int) (endTime - startTime));
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    /**
     * Convenience method that sets both of the coordinates
     * @param latitude
     * @param longitude
     */
    public void setLocation(float latitude, float longitude) {
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * Convenience method that looks up the username and puts the proper userId
     * @param username the user's string representation of their userId
     */
    public void setUsername(String username) {
        // TODO: implement findUserId(String)
        //this.userId = findUserId(username);
    }

    public void setSurveyType(int surveyType) {
        this.surveyType = surveyType;
    }

    public void setSurveySubtype(int surveySubtype) {
        this.surveySubtype = surveySubtype;
    }

    /**
     * Often, these two will be set together, so create a method that allows that.
     * @param surveyType the main type of the survey
     * @param surveySubtype the subtype of the main type
     */
    public void setSurveyType(int surveyType, int surveySubtype) {
        setSurveyType(surveyType);
        setSurveySubtype(surveySubtype);
    }

    public void setQuestionSet(int questionSet) {
        this.questionSet = questionSet;
    }

    public void setAnswerIds(long[] answerIds) {
        this.answerIds = answerIds;
    }
}
