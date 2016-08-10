package com.drill.responsemodel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GROUPON on 28/09/15.
 */
public class MerchantTimings {
    private String message;
    private String closeAt;
    private String openAt;
    private List<Integer> daysOpen = new ArrayList<Integer>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCloseAt() {
        return closeAt;
    }

    public void setCloseAt(String closeAt) {
        this.closeAt = closeAt;
    }

    public String getOpenAt() {
        return openAt;
    }

    public void setOpenAt(String openAt) {
        this.openAt = openAt;
    }

    public List<Integer> getDaysOpen() {
        return daysOpen;
    }

    public void setDaysOpen(List<Integer> daysOpen) {
        this.daysOpen = daysOpen;
    }
}
