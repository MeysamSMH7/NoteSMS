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
                                     String dateMiladi = tool.getGregorianYear() + "-" + tool.getGregorianMonth() + "-" + tool.getGregorianDay() + "";
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
