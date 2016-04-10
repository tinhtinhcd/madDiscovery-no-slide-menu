package com.anhlt.maddiscover.data.repositories;

import android.content.Context;
import android.database.Cursor;

import com.anhlt.maddiscover.data.sqlStatement.SQLStatement;
import com.anhlt.maddiscover.data.tables.Reports;
import com.anhlt.maddiscover.entities.Report;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anhlt on 3/19/16.
 */
public class ReportRepository extends BasicRepository{

    public ReportRepository(Context context) {
        super(context);
    }

    public void create(Report report) {
        create(Reports.TABLE_NAME, report);
    }

    public List<Report> getReports(Long eventId) {
        Cursor cursor = databaseHelper.select(SQLStatement.getReport(eventId), null, null);
        List<Report> reports = new ArrayList<Report>();
        Report report;

        if (cursor.moveToFirst()){
            do{
                report = new Report();
                getObjectFromCursor(cursor, report);
                reports.add(report);
            }
            while (cursor.moveToNext());
        }

        return reports;
    }
}
