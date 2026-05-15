package com.data_management;

/**
 * Represents a staff member in the hospital.
 * Stores the crucial data of every staff member.
 */
public class StaffMember {
    private int staffId;
    private String firstName;
    private String lastName;
    private int roleLevel;

    public StaffMember(int staffId, String firstName, String lastName, int roleLevel) {
        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleLevel = roleLevel;
    }

    /**
     * Retrieves the role level of this staff member.
     * @return The role level of this staff member.
     */
    public int getRoleLevel() {
        return roleLevel;
    }

    /**
     * Retrieves the id of this staff member.
     * @return The id of this staff member.
     */
    public int getId() {
        return staffId;
    }
}
