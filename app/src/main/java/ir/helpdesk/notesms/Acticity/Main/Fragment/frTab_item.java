package ir.helpdesk.notesms.Acticity.Main.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.helpdesk.notesms.Acticity.Setting.Adapter.onClickInterface;
import ir.helpdesk.notesms.Acticity.Main.Fragment.Adapter.AdRecycItems;
import ir.helpdesk.notesms.DataBase.DataSource.tb_BillsDataSource;
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
        data = getCustomData(tag);
        RecyclerView recycle = view.findViewById(R.id.recycle);
        setDataInRecycle(recycle);

        return view;
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

        if (tag.equals("all"))
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