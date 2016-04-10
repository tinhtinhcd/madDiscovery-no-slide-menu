package com.anhlt.maddiscover.entities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by anhlt on 2/19/16.
 */
public class Event {

    private Long id;
    private String eventName;
    private Long venueId;
    private Date createDate;
    private String dateOfEvent;
    private Date startDate;
    private Long organizer;
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(long venueId) {
        this.venueId = venueId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Long getOrganizer() {
        return organizer;
    }

    public void setOrganizer(long organizer) {
        this.organizer = organizer;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDateOfEvent() {
        return dateOfEvent;
    }

    public void setDateOfEvent(String dateOfEvent) {
        this.dateOfEvent = dateOfEvent;
    }
}
