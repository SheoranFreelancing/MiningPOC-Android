package com.drill.responsemodel;

import com.drill.utils.Constants;

/**
 * Created by GROUPON on 25/09/15.
 */
public class Metadata {
    private String email;
    private String lastName;
    private String firstName;
    private String channel;
    private String status;
    private String progress;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }


    public int getSalesStatus(){
        int index = 0;
        for (int i = 0; i < Constants.salesStatus.length; i++) {
            if (Constants.salesStatus[i].trim().equalsIgnoreCase(getStatus())) {
                index = i;
                break;
            }
        }

        return index;

    }


    public int getSpecialStatus() {
        int index = 0;
        for (int i = 0; i < Constants.specialStatus.length; i++) {
            if (Constants.specialStatus[i].trim().equalsIgnoreCase(getStatus())) {
                index = i;
                break;
            }
        }

        return index;
    }

    public int getSpecialProgress() {
        int index = 0;
            for (int i = 0; i < Constants.specialProgress.length; i++) {
                if (Constants.specialProgress[i].trim().equalsIgnoreCase(getProgress())) {
                    index = i;
                    break;
                }
            }
        return index;
    }
}
