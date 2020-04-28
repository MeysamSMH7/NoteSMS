package ir.helpdesk.notesms.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.helpdesk.notesms.Adapter.AdRecycItems;
import ir.helpdesk.notesms.Adapter.onClickInterface;
import ir.helpdesk.notesms.DataBase.Tables.tb_Bills;
import ir.helpdesk.notesms.R;

public class frTab_item extends Fragment {

    public static frTab_item newInstance(ArrayList<tb_Bills> tb_bills) {
        Bundle args = new Bundle();
        frTab_item fragment = new frTab_item();
        fragment.setArguments(args);
        args.putSerializable("list", tb_bills);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_tab_main, container, false);
        ArrayList<tb_Bills> arrayList = (ArrayList<tb_Bills>) getArguments().getSerializable("list");

        ListView lstItems = view.findViewById(R.id.lstItems);
        ArrayList<String> smsMessagesList = new ArrayList<String>();

        for (int i = 0; i < arrayList.size(); i++) {
            tb_Bills tb_bills = arrayList.get(i);
            String str = "پیامک از طرف: " + tb_bills.senderSMS
                    + "\n" + "تاریخ دریافت" + tb_bills.dateSMSJalali
                    + "\n" + "متن پیام:" + tb_bills.txtSMS
                    + "\n" + "کلید:" + tb_bills.PK_key
                    + "\n\n" + "متن نوت:" + tb_bills.txtNote
                    + "\n" + "تاریخ یادداشت:" + tb_bills.dateNoteMiladi
                    + "\n";
            smsMessagesList.add(str);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, smsMessagesList);
        lstItems.setAdapter(arrayAdapter);


        RecyclerView recycle = view.findViewById(R.id.recycle);
        AdRecycItems adRecycItems = new AdRecycItems(getContext(), arrayList, new onClickInterface() {
            @Override
            public void setClick(int position, View view) {

                Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
                
            }
        });
        recycle.setAdapter(adRecycItems);







        return view;
    }

}