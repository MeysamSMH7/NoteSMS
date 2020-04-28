package ir.helpdesk.notesms.Acticity.Main.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ir.helpdesk.notesms.R;

public class frTab_search extends Fragment {


    public static frTab_search newInstance() {

        Bundle args = new Bundle();
        frTab_search fragment = new frTab_search();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_tab_search, container, false);


        return view;
    }

}