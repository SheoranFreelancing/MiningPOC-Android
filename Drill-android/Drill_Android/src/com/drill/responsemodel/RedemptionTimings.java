package com.drill.responsemodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GROUPON on 05/10/15.
 */
public class RedemptionTimings {
    private String openAt;
    private String message;
    private List<Integer> daysOpen = new ArrayList<Integer>();
    private String closeAt;

    public String getOpenAt() {
        return openAt;
    }

    public void setOpenAt(String openAt) {
        this.openAt = openAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Integer> getDaysOpen() {
        return daysOpen;
    }

    public void setDaysOpen(List<Integer> daysOpen) {
        this.daysOpen = daysOpen;
    }

    public String getCloseAt() {
        return closeAt;
    }

    public void setCloseAt(String closeAt) {
        this.closeAt = closeAt;
    }
}
