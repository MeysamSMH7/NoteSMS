package ir.helpdesk.notesms.Acticity.Main.Fragment;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import ir.helpdesk.notesms.Acticity.Main.Fragment.Adapter.AdRecyclFilterPhone;
import ir.helpdesk.notesms.Acticity.Setting.Adapter.onClickInterface;
import ir.helpdesk.notesms.Acticity.Main.Fragment.Adapter.AdRecycItems;
import ir.helpdesk.notesms.Acticity.Main.ModFilterPhone;
import ir.helpdesk.notesms.Classes.getDate;
import ir.helpdesk.notesms.DataBase.DataSource.tb_BillsDataSource;
import ir.helpdesk.notesms.DataBase.Structure.tb_BillsStructure;
import ir.helpdesk.notesms.DataBase.Tables.tb_Bills;
import ir.helpdesk.notesms.R;

import android.animation.Animator;
import android.text.TextUtils;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class frTab_search extends Fragment {

    private EditText edtFrSearch_FullName;
    private EditText edtFrSearch_Phone;
    private RadioGroup radioGroupFrSearch;
    private LinearLayout linearFrSearch_DateMulti;
    private TextView txtFrSearch_DateSMulti_Start;
    private TextView txtFrSearch_DateSMulti_Start2;
    private TextView txtFrSearch_DateSMulti_End;
    private TextView txtFrSearch_DateSMulti_End2;
    private LinearLayout linearFrSearch_DateSingle;
    private RelativeLayout relatFrSearch;
    private TextView txtFrSearch_DateSingle;
    private TextView txtFrSearch_Date2Single;
    private ScrollView scrollViewSearch;
    private FloatingActionButton btnSearch;
    private RecyclerView recyclFr_Search;
    private RelativeLayout layoutMain;
    private RelativeLayout relatFrDeleteSearch;
    private LinearLayout linearSearch;
    private TextView txtFrSearch_NoData;
    private ImageView imgDoSearch_v4_1;
    private ImageView imgDoSearch_v4_2;

    private String dateMiladi = "";
    private String dateMiladiStart = "";
    private String dateMiladiEnd = "";
    private String dateJalali = "";

    private List<tb_Bills> list;

    private AdRecycItems adapter;

    public static frTab_search newInstance() {

        Bundle args = new Bundle();
        frTab_search fragment = new frTab_search();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_tab_search, container, false);
        findViews(view);
        clicks();

        return view;
    }

    private void clicks() {
        clickDates();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scrollViewSearch.getVisibility() == View.VISIBLE)
                    doSearch();
                else {
                    scrollViewSearch.setVisibility(View.VISIBLE);
                    linearSearch.setVisibility(View.GONE);
                    circularRevealActivity(scrollViewSearch);
                }
            }
        });

        imgDoSearch_v4_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scrollViewSearch.getVisibility() == View.VISIBLE)
                    doSearch();
                else {
                    scrollViewSearch.setVisibility(View.VISIBLE);
                    linearSearch.setVisibility(View.GONE);
                    circularRevealActivity(scrollViewSearch);
                }
            }
        });
        imgDoSearch_v4_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scrollViewSearch.getVisibility() == View.VISIBLE)
                    doSearch();
                else {
                    scrollViewSearch.setVisibility(View.VISIBLE);
                    linearSearch.setVisibility(View.GONE);
                    circularRevealActivity(scrollViewSearch);
                }
            }
        });

        radioGroupFrSearch.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                switch (selectedId) {
                    case R.id.radioFrSearch_DateMulti:
                        linearFrSearch_DateMulti.setVisibility(View.VISIBLE);
                        linearFrSearch_DateSingle.setVisibility(View.GONE);
                        dateMiladi = "";
                        txtFrSearch_DateSingle.setText("انتخاب تاریخ");
                        txtFrSearch_Date2Single.setText("");

                        break;
                    case R.id.radioFrSearch_DateSingle:
                        linearFrSearch_DateMulti.setVisibility(View.GONE);
                        linearFrSearch_DateSingle.setVisibility(View.VISIBLE);
                        dateMiladiStart = "";
                        dateMiladiEnd = "";
                        txtFrSearch_DateSMulti_Start.setText("تاریخ شروع");
                        txtFrSearch_DateSMulti_Start2.setText("");
                        txtFrSearch_DateSMulti_End.setText("تاریخ پایان");
                        txtFrSearch_DateSMulti_End2.setText("");
                        break;
                }
            }
        });


    }

    private void doSearch() {

        if (
                TextUtils.isEmpty(edtFrSearch_FullName.getText()) &&
                        TextUtils.isEmpty(edtFrSearch_Phone.getText()) &&
                        TextUtils.isEmpty(txtFrSearch_Date2Single.getText()) &&
                        dateMiladi.equals("") &&
                        dateMiladiStart.equals("") &&
                        dateMiladiEnd.equals("") &&
                        dateMiladiStart.equals("") &&
                        txtFrSearch_DateSMulti_Start2.getText().toString().equals("") &&
                        !dateMiladiEnd.equals("")
        ) {
            Toast.makeText(getContext(), "لطفا یک فیلد را پر کنید" + "", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList arrayList = new ArrayList();

//        if (!(TextUtils.isEmpty(edtFrSearch_PK.getText()))) {
//            arrayList.add(tb_BillsStructure.PK_Bill + " = '" + edtFrSearch_PK.getText().toString() + "'");
//        }
        if (!(TextUtils.isEmpty(edtFrSearch_FullName.getText()))) {
            arrayList.add(tb_BillsStructure.txtSMS + " LIKE '%" + edtFrSearch_FullName.getText().toString() + "%'");
        }
        if (!(TextUtils.isEmpty(edtFrSearch_Phone.getText()))) {
            arrayList.add(tb_BillsStructure.senderSMS + " = '" + edtFrSearch_Phone.getText().toString() + "'");
        }
        if (!dateJalali.equals("")) {
            arrayList.add(tb_BillsStructure.dateSMSJalali + "='" +
                    txtFrSearch_Date2Single.getText().toString() + "'");
        }
        if (!dateMiladiStart.equals("") && !dateMiladiEnd.equals("")) {
            arrayList.add(tb_BillsStructure.dateSMSMiladi + " BETWEEN '" + dateMiladiStart + "' AND '" + dateMiladiEnd + "'");
        }

        if (arrayList.size() == 0) {
            Toast.makeText(getContext(), "لطفا یک فیلد را پر کنید" + "", Toast.LENGTH_SHORT).show();
            return;
        }

        list = new tb_BillsDataSource(getContext()).Search(arrayList);
        adapter = new AdRecycItems(getContext(), list, new onClickInterface() {
            @Override
            public void setClick(int position, View view, String id) {
                adapter.notifyDataSetChanged();

            }
        });
        recyclFr_Search.setAdapter(adapter);
        scrollViewSearch.setVisibility(View.GONE);
        linearSearch.setVisibility(View.VISIBLE);
        if (list.size() == 0) {
            txtFrSearch_NoData.setVisibility(View.VISIBLE);
            recyclFr_Search.setVisibility(View.GONE);
        } else {
            txtFrSearch_NoData.setVisibility(View.GONE);
            recyclFr_Search.setVisibility(View.VISIBLE);
        }
        circularRevealActivity(linearSearch);
    }

    private void clickDates() {
        txtFrSearch_DateSMulti_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getDate(getActivity().getFragmentManager()).getDate(new getDate.OnResponse() {
                    @Override
                    public void OnResponse(String persian, String miladi) {
                        dateMiladiStart = miladi;
                        txtFrSearch_DateSMulti_Start.setText("تاریخ انتخاب شده:");
                        txtFrSearch_DateSMulti_Start2.setText(persian);
                    }
                });
            }
        });

        txtFrSearch_DateSMulti_Start2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getDate(getActivity().getFragmentManager()).getDate(new getDate.OnResponse() {
                    @Override
                    public void OnResponse(String persian, String miladi) {
                        dateMiladiStart = miladi;
                        txtFrSearch_DateSMulti_Start.setText("تاریخ انتخاب شده:");
                        txtFrSearch_DateSMulti_Start2.setText(persian);
                    }
                });
            }
        });

        txtFrSearch_DateSMulti_End.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getDate(getActivity().getFragmentManager()).getDate(new getDate.OnResponse() {
                    @Override
                    public void OnResponse(String persian, String miladi) {
                        dateMiladiEnd = miladi;
                        txtFrSearch_DateSMulti_End.setText("تاریخ انتخاب شده:");
                        txtFrSearch_DateSMulti_End2.setText(persian);
                    }
                });
            }
        });
        txtFrSearch_DateSMulti_End2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getDate(getActivity().getFragmentManager()).getDate(new getDate.OnResponse() {
                    @Override
                    public void OnResponse(String persian, String miladi) {
                        dateMiladiEnd = miladi;
                        txtFrSearch_DateSMulti_End.setText("تاریخ انتخاب شده:");
                        txtFrSearch_DateSMulti_End2.setText(persian);
                    }
                });
            }
        });

        txtFrSearch_DateSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getDate(getActivity().getFragmentManager()).getDate(new getDate.OnResponse() {
                    @Override
                    public void OnResponse(String persian, String miladi) {
                        dateMiladi = miladi;
                        txtFrSearch_DateSingle.setText("تاریخ انتخاب شده:");
                        txtFrSearch_Date2Single.setText(persian);
                    }
                });
            }
        });
        txtFrSearch_Date2Single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getDate(getActivity().getFragmentManager()).getDate(new getDate.OnResponse() {
                    @Override
                    public void OnResponse(String persian, String miladi) {
                        dateJalali = persian;
                        txtFrSearch_DateSingle.setText("تاریخ انتخاب شده:");
                        txtFrSearch_Date2Single.setText(persian);
                    }
                });
            }
        });

        relatFrDeleteSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edtFrSearch_Phone.setText("");
                edtFrSearch_FullName.setText("");

                dateMiladiStart = "";
                dateMiladiEnd = "";
                txtFrSearch_DateSMulti_Start.setText("تاریخ شروع");
                txtFrSearch_DateSMulti_Start2.setText("");
                txtFrSearch_DateSMulti_End.setText("تاریخ پایان");
                txtFrSearch_DateSMulti_End2.setText("");

                dateMiladi = "";
                txtFrSearch_DateSingle.setText("انتخاب تاریخ");
                txtFrSearch_Date2Single.setText("");


            }
        });

    }

    private void findViews(View view) {

        edtFrSearch_FullName = view.findViewById(R.id.edtFrSearch_FullName);
        edtFrSearch_Phone = view.findViewById(R.id.edtFrSearch_Phone);
        radioGroupFrSearch = view.findViewById(R.id.radioGroupFrSearch);
        linearFrSearch_DateMulti = view.findViewById(R.id.linearFrSearch_DateMulti);
        txtFrSearch_DateSMulti_Start = view.findViewById(R.id.txtFrSearch_DateSMulti_Start);
        txtFrSearch_DateSMulti_Start2 = view.findViewById(R.id.txtFrSearch_DateSMulti_Start2);
        txtFrSearch_DateSMulti_End = view.findViewById(R.id.txtFrSearch_DateSMulti_End);
        txtFrSearch_DateSMulti_End2 = view.findViewById(R.id.txtFrSearch_DateSMulti_End2);
        linearFrSearch_DateSingle = view.findViewById(R.id.linearFrSearch_DateSingle);
        txtFrSearch_DateSingle = view.findViewById(R.id.txtFrSearch_DateSingle);
        txtFrSearch_Date2Single = view.findViewById(R.id.txtFrSearch_Date2Single);
        scrollViewSearch = view.findViewById(R.id.scrollViewSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        recyclFr_Search = view.findViewById(R.id.recyclFr_Search);
        layoutMain = view.findViewById(R.id.layoutMain);
        linearSearch = view.findViewById(R.id.linearSearch);
        txtFrSearch_NoData = view.findViewById(R.id.txtFrSearch_NoData);
        relatFrSearch = view.findViewById(R.id.relatFrSearch);
        relatFrDeleteSearch = view.findViewById(R.id.relatFrDeleteSearch);
        imgDoSearch_v4_1 = view.findViewById(R.id.imgDoSearch_v4_1);
        imgDoSearch_v4_2 = view.findViewById(R.id.imgDoSearch_v4_2);

        scrollViewSearch.setVisibility(View.VISIBLE);
        linearSearch.setVisibility(View.GONE);

        linearFrSearch_DateMulti.setVisibility(View.VISIBLE);
        linearFrSearch_DateSingle.setVisibility(View.GONE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            relatFrSearch.setVisibility(View.GONE);
            btnSearch.setVisibility(View.VISIBLE);
        } else {
            relatFrSearch.setVisibility(View.VISIBLE);
            btnSearch.setVisibility(View.GONE);
        }

        list = new ArrayList<>();

        ImageView imgListContacts = view.findViewById(R.id.imgListContacts);
        imgListContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogFilterPhone();
            }
        });


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void circularRevealActivity(View view) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int cx = view.getRight();
            int cy = view.getBottom();
            float finalRadius = Math.max(view.getWidth(), view.getHeight());
            Animator circularReveal = null;
            circularReveal = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
            circularReveal.setDuration(500);
            circularReveal.start();
        }

        view.setVisibility(View.VISIBLE);
    }

    // --------------- list ListContacts
    private AlertDialog alertDialogFilterPhone;

    private void alertDialogFilterPhone() {
        final AdRecyclFilterPhone adRecycPopUp;
        final androidx.appcompat.widget.SearchView editsearchSearchView;
        List<ModFilterPhone> arraylistSearchView = new ArrayList<ModFilterPhone>();
        ArrayList<tb_Bills> tb_billsList = new ArrayList<>(new tb_BillsDataSource(getContext()).GetList());

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.item_listview_chooser, null, false);

        Button btnFr = layout.findViewById(R.id.btnFr);
        btnFr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogFilterPhone.dismiss();
            }
        });

        if (arraylistSearchView.size() != 0)
            arraylistSearchView.clear();
        ArrayList arrayList = new ArrayList();

        for (int i = 0; i < tb_billsList.size(); i++)
            arrayList.add(tb_billsList.get(i).senderSMS);

        LinkedHashSet<String> lhs = new LinkedHashSet<String>();
        lhs.addAll(arrayList);
        arrayList.clear();
        arrayList.addAll(lhs);


        for (int i = 0; i < arrayList.size(); i++) {
            ModFilterPhone modAlerts = new ModFilterPhone(
                    arrayList.get(i) + "",
                    arrayList.get(i) + "");
            arraylistSearchView.add(modAlerts);
        }


        RecyclerView recycFitler = layout.findViewById(R.id.recycFitler);
        adRecycPopUp = new AdRecyclFilterPhone(getContext(), arraylistSearchView, new onClickInterface() {
            @Override
            public void setClick(int position, View view, String s) {

                TextView txttitle = ((LinearLayout) view).findViewById(R.id.txtTitle);
                TextView txtId = ((LinearLayout) view).findViewById(R.id.txtId);
                String titlePH = txttitle.getText().toString();
                String id = txtId.getText().toString();

                edtFrSearch_Phone.setText(id + "");
                alertDialogFilterPhone.dismiss();
            }
        });
        recycFitler.setAdapter(adRecycPopUp);

        editsearchSearchView = layout.findViewById(R.id.searchFr);
        editsearchSearchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText;
                adRecycPopUp.filter(text);
                return true;
            }
        });

        builder.setView(layout);
        alertDialogFilterPhone = builder.create();
        alertDialogFilterPhone.show();
        alertDialogFilterPhone.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

}