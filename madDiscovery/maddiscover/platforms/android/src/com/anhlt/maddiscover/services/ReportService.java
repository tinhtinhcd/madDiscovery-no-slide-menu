package com.anhlt.maddiscover.services;

import android.content.Context;

import com.anhlt.maddiscover.entities.Report;
import com.anhlt.maddiscover.data.repositories.ReportRepository;

import java.util.List;

/**
 * Created by anhlt on 3/19/16.
 */
public class ReportService {

    ReportRepository reportRepository;
    Context context;

    public ReportService(Context context) {
        this.context = context;
        reportRepository = new ReportRepository(context);
    }

    public void create(Report report){
        reportRepository.create(report);
    }

    public List<Report> getReports(Long eventId){
        return reportRepository.getReports(eventId);
    }

}
