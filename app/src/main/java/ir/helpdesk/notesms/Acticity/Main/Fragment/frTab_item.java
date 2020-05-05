package ir.helpdesk.notesms.Acticity.Main.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

import ir.helpdesk.notesms.Acticity.Setting.Adapter.onClickInterface;
import ir.helpdesk.notesms.Acticity.Main.Fragment.Adapter.AdRecycItems;
import ir.helpdesk.notesms.Classes.CalendarTool;
import ir.helpdesk.notesms.DataBase.DataSource.tb_BillsDataSource;
import ir.helpdesk.notesms.DataBase.Structure.tb_BillsStructure;
import ir.helpdesk.notesms.DataBase.Tables.tb_Bills;
import ir.helpdesk.notesms.R;

public class frTab_item extends Fragment {

    private AdRecycItems adRecycItems;
    private String tag = "";
    private List<tb_Bills> data;

    public static frTab_item newInstance(String tag) {
        Bundle args = new Bundle();
        frTab_item fragment = new frTab_item();
        fragment.setArguments(args);
        args.putString("tag", tag);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_tab_main, container, false);
        tag = getArguments().getString("tag");

        if (tag.equals("امروز")) data = getDataWithRangeDate(tag);
        else if (tag.equals("7 روز گذشته")) data = getDataWithRangeDate(tag);
        else if (tag.equals("این هفته")) data = getDataWithRangeDate(tag);
        else if (tag.equals("این ماه")) data = getDataWithRangeDate(tag);
        else if (tag.equals("30 روز گذشته")) data = getDataWithRangeDate(tag);
        else
            data = getCustomData(tag);
        RecyclerView recycle = view.findViewById(R.id.recycle);
        setDataInRecycle(recycle);

        return view;
    }

    private List<tb_Bills> getDataWithRangeDate(String tag) {
        ArrayList arrayList = new ArrayList();
        CalendarTool tool = new CalendarTool();

        String todayMiladi = getMiladiDate(tool.getGregorianDate());

        if (tag.equals("امروز"))
            arrayList.add(tb_BillsStructure.dateSMSMiladi + "='" + todayMiladi + "'");
        else if (tag.equals("7 روز گذشته")) {
            tool.previousDay(7);
            arrayList.add(tb_BillsStructure.dateSMSMiladi + " BETWEEN '" +
                    getMiladiDate(tool.getGregorianDate()) + "' AND '" + todayMiladi + "'");
        }
        else if (tag.equals("30 روز گذشته")) {
            tool.previousDay(30);
            arrayList.add(tb_BillsStructure.dateSMSMiladi + " BETWEEN '" +
                    getMiladiDate(tool.getGregorianDate()) + "' AND '" + todayMiladi + "'");
        }
        else if (tag.equals("این هفته")) {
            int aa = tool.getDayOfWeekIran();
            tool.previousDay(aa);
            arrayList.add(tb_BillsStructure.dateSMSMiladi + " BETWEEN '" +
                    getMiladiDate(tool.getGregorianDate()) + "' AND '" + todayMiladi + "'");
        }
        else if (tag.equals("این ماه")) {
            tool.previousDay(tool.getIranianDay()-1);
            arrayList.add(tb_BillsStructure.dateSMSMiladi + " BETWEEN '" +
                    getMiladiDate(tool.getGregorianDate()) + "' AND '" + todayMiladi + "'");
        }

        return new tb_BillsDataSource(getContext()).SearchArrayList(arrayList);

    }

    private String getMiladiDate(String gregorianDate) {

        String[] date = gregorianDate.split("/");

        CalendarTool tool = new CalendarTool();

        tool.setGregorianDate(Integer.parseInt(date[0]), Integer.parseInt(date[1]),
                Integer.parseInt(date[2]));
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

        return tool.getGregorianYear() + "-" + monthMiladi + "-" + dayMiladi + "";


    }

    private void setDataInRecycle(final RecyclerView recycle) {
        adRecycItems = new AdRecycItems(getContext(), data,
                new onClickInterface() {
                    @Override
                    public void setClick(int position, View view, final String id) {
                        data = getCustomData(tag);
                        setDataInRecycle(recycle);
                    }
                });
        recycle.setAdapter(adRecycItems);
        adRecycItems.notifyDataSetChanged();
    }

    private ArrayList<tb_Bills> getCustomData(String phoneNum) {
        ArrayList<tb_Bills> tb_billsList = new ArrayList<>(new
                tb_BillsDataSource(getContext()).GetList());

        if (tag.equals("همه"))
            return tb_billsList;

        ArrayList<tb_Bills> bills = new ArrayList<>();

        for (int i = 0; i < tb_billsList.size(); i++) {
            tb_Bills tbBills = tb_billsList.get(i);
            if (tbBills.senderSMS.equals(phoneNum))
                bills.add(tbBills);
        }
        return bills;
    }

}