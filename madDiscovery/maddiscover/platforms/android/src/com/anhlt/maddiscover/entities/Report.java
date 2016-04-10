package com.anhlt.maddiscover.entities;

import java.util.Date;
/**
 * Created by anhlt on 2/19/16.
 */
public class Report {

    private Long id;
    private long eventId;
    private Date createDate;
    private String title;
    private String details;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return details;
    }

    public void setDetail(String detail) {
        this.details = detail;
    }
}
