package com.anhlt.maddiscover.data.sqlStatement;

import com.anhlt.maddiscover.data.tables.EventImages;
import com.anhlt.maddiscover.data.tables.Events;
import com.anhlt.maddiscover.data.tables.Organizers;
import com.anhlt.maddiscover.data.tables.Venues;
import com.anhlt.maddiscover.entities.EventImage;
import com.anhlt.maddiscover.entities.Venue;

/**
 * Created by anhlt on 2/19/16.
 */
public class SQLStatement {

    private static final String GET_ALL = "SELECT * FROM ";

    public static String getAll(String tableName){
        return GET_ALL+ tableName;
    }

    public static String findById(String table){
        return getAll(table) + " WHERE id = ?";
    }

    public static String findByName(String table){
        return getAll(table) + " WHERE name = ?";
    }

    public static String find(String table, String columnCondition){
        return getAll(table) + " WHERE "+columnCondition+" = ?";
    }

    public static String getColumns(String table, String column,String condition){
        StringBuilder query = new StringBuilder();
        if(table!=null && column!=null && !table.isEmpty() && !column.isEmpty()){
            query.append("SELECT ");
            query.append(column);
            query.append(" FROM ");
            query.append(table);
            if (condition!=null && !condition.isEmpty()){
                query.append(" WHERE ");
                query.append(condition);
            }
        }
        return query.toString();
    }

    public static String validEvent(String eventName, String venueName, String organizerName, Long eventId){
        StringBuilder builder = new StringBuilder();

        builder.append("SELECT * FROM ");
        builder.append(Events.TABLE_NAME);
        builder.append(" as e inner join ");
        builder.append(Venues.TABLE_NAME);
        builder.append(" as v on e.venueId = v.id inner join ");
        builder.append(Organizers.TABLE_NAME);
        builder.append(" as o on o.id = e.organizer");
        builder.append(" where e.eventName = '");
        builder.append(eventName);
        builder.append("' ");

        if(venueName!=null){
            builder.append(" and v.name = '");
            builder.append(venueName);
            builder.append("' ");
        }

        if(organizerName!=null){
            builder.append(" and o.name = '");
            builder.append(venueName);
            builder.append("' ");
        }

        if(eventId != null){
            builder.append(" and e.id != ");
            builder.append(eventId);
            builder.append(" ");
        }

        return builder.toString();
    }

    public static String getReport(Long eventId){
        StringBuilder builder = new StringBuilder();

        if(eventId!=null){
            builder.append("Select * From reports Where eventId = ");
            builder.append(eventId);
            builder.append(" order by createDate Desc");
        }

        return builder.toString();
    }

    public static String getEventImages(Long eventId){
        StringBuilder builder = new StringBuilder();

        builder.append("SELECT ");
        builder.append(EventImages.fileName);
        builder.append(" FROM ");
        builder.append(EventImages.TABLE_NAME);
        builder.append(" WHERE ");
        builder.append(EventImages.eventId);
        builder.append(" = ");
        builder.append(eventId);

        return builder.toString();
    }

    public static String checkOrganizerInEvent(Long orgId){
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT count(*) From ");
        builder.append(Events.TABLE_NAME);
        builder.append(" WHERE organizer =");
        builder.append(orgId);
        return builder.toString();
    }
}
