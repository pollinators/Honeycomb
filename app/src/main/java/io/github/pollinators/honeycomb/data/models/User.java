package io.github.pollinators.honeycomb.data.models;

/**
 * Created by ted on 11/28/14.
 */
public class User extends AbstractModel {

    private String username;
    private String firstName;
    private String lastName;
    private int organizationId;

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getOrganizationId() {
        return organizationId;
    }
        public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }
}
