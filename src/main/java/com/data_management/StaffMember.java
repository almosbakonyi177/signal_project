package com.data_management;

//Stores the crucial data of every staff member
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
     * Returns the role level of this staff member.
     * @return The role level of this staff member.
     */
    public int getRoleLevel() {
        return roleLevel;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return staffId;
    }
}
