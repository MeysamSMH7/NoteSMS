package ir.helpdesk.notesms.Classes;


import android.app.FragmentManager;

import com.mohamadamin.persianmaterialdatetimepicker.date.DatePickerDialog;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import java.util.TimeZone;

public class getDate {

    private FragmentManager manager;

    public getDate(FragmentManager manager) {
        this.manager = manager;
    }

    public void getDate(final OnResponse OnResponse) {
        PersianCalendar persianCalendar = new PersianCalendar();
        persianCalendar.setTimeZone(TimeZone.getTimeZone("GMT+3:30"));
        DatePickerDialog datePickerDialog = DatePickerDialog
                .newInstance(new DatePickerDialog.OnDateSetListener() {
                                 @Override
                                 public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                     monthOfYear++;
                                     CalendarTool tool = new CalendarTool();
                                     tool.setIranianDate(year, monthOfYear, dayOfMonth);
                                     String monthMiladi = "";
                                     if (!(tool.getGregorianMonth() >= 10))
                                         monthMiladi = "0" + tool.getGregorianMonth();
                                     else
                                         monthMiladi = tool.getGregorianMonth() + "";

                                     String dayMiladi = "";
                                     if (!(tool.getGregorianDay() >= 10))
                                         dayMiladi = "0" + tool.getGregorianDay();
                                     else
                                         dayMiladi = tool.getGregorianDay() + "";

                                     String dateMiladi = tool.getGregorianYear() + "-" + monthMiladi + "-" + dayMiladi + "";
                                     String dateJalali = year + "/" + monthOfYear + "/" + dayOfMonth + "";
                                     OnResponse.OnResponse(dateJalali, dateMiladi);

                                 }
                             }, persianCalendar.getPersianYear(),
                        persianCalendar.getPersianMonth(),
                        persianCalendar.getPersianDay());
        datePickerDialog.show(manager, "tpd");

    }

    public interface OnResponse {
        void OnResponse(String persian, String miladi);
    }

}
