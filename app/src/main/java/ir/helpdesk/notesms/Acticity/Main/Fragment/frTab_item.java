package ir.helpdesk.notesms.Acticity.Main.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.helpdesk.notesms.Acticity.Setting.Adapter.onClickInterface;
import ir.helpdesk.notesms.Acticity.Main.Fragment.Adapter.AdRecycItems;
import ir.helpdesk.notesms.DataBase.Tables.tb_Bills;
import ir.helpdesk.notesms.R;

public class frTab_item extends Fragment {

  private   AdRecycItems adRecycItems;

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

        RecyclerView recycle = view.findViewById(R.id.recycle);
         adRecycItems = new AdRecycItems(getContext(), arrayList, new onClickInterface() {
            @Override
            public void setClick(int position, View view, final String id) {
                adRecycItems.notifyDataSetChanged();
            }
        });
        recycle.setAdapter(adRecycItems);

        return view;
    }

}