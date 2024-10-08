package com.haulmont.bookstore.actions;

import com.haulmont.bookstore.entity.Author;
import com.haulmont.bookstore.web.screens.author.AuthorBrowse;
import com.haulmont.cuba.core.entity.FileDescriptor;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.reports.app.service.ReportService;
import com.haulmont.reports.entity.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@ActionType("generateBooksByAuthorReport")
public class GenerateBooksByAuthorAction extends ItemTrackingAction {

    private static final String REPORT_CODE = "BOOKSBYAUTHOR";

    private static final Logger log = LoggerFactory.getLogger(GenerateBooksByAuthorAction.class);
    @Inject
    private DataManager dataManager;
    @Inject
    private ReportService reportService;
    @Inject
    private Messages messages;
    @Inject
    private ExportDisplay exportDisplay;

    public GenerateBooksByAuthorAction(String id) {
        super(id);
    }

    @Override
    public void actionPerform(Component component) {
        Frame frame = getTarget().getFrame();
        Author selectedAuthor = (Author) getTarget().getSingleSelected();

        if (selectedAuthor == null) {
            showNotificationForNoSelection(frame);
            return;
        }

        try {
            Report report = loadReportByCode(REPORT_CODE);
            if (report == null) {
                log.warn("Report with code {} not found", REPORT_CODE);
                showNotificationForReportCreationFailed(frame);
                return;
            }

            FileDescriptor fileDescriptor = generateReport(report, selectedAuthor);
            if (fileDescriptor != null) {
                exportDisplay.show(fileDescriptor);
                log.info("Report created successfully: {}", fileDescriptor);
            } else {
                log.error("Failed to create report for author: {}", selectedAuthor);
                showNotificationForReportCreationFailed(frame);
            }
        } catch (Exception e) {
            log.error("Error while generating report for author: {}", selectedAuthor, e);
            showNotificationForReportCreationFailed(frame);
        }
    }

    private Report loadReportByCode(String reportCode) {
        return dataManager.load(Report.class)
                .query("select r from report$Report r where r.code = :code")
                .parameter("code", reportCode)
                .optional()
                .orElse(null);
    }

    private FileDescriptor generateReport(Report report, Author author) {
        Map<String, Object> params = new HashMap<>();
        params.put("author", author);
        return reportService.createAndSaveReport(report, params, report.getLocName());
    }

    private void showNotificationForNoSelection(Frame frame) {
        frame.showNotification(messages.getMessage(getClass(), "selectOneAuthorMessage"),
                Frame.NotificationType.TRAY);
    }

    private void showNotificationForReportCreationFailed(Frame frame) {
        frame.showNotification(messages.getMessage(getClass(), "reportCreationFailedMessage"),
                Frame.NotificationType.ERROR);
    }

    @Override
    protected boolean isPermitted() {
        return security.isSpecificPermitted("allow-generateBooksByAuthorReport");
    }
}
