package ir.helpdesk.notesms.Acticity.Main.Fragment.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.helpdesk.notesms.Acticity.Main.Adapter.onClickInterface;
import ir.helpdesk.notesms.Classes.CalendarTool;
import ir.helpdesk.notesms.DataBase.DataSource.tb_BillsDataSource;
import ir.helpdesk.notesms.DataBase.Tables.tb_Bills;
import ir.helpdesk.notesms.R;

/**
 * TextView txtTitle = ((LinearLayout) view).findViewById(R.id.txtTitle);
 */

public class AdRecycItems extends RecyclerView.Adapter<AdRecycItems.ViewHolder> {

    private Context context;
    private List<tb_Bills> data;
    private int lastPosition = -1;
    private ir.helpdesk.notesms.Acticity.Main.Adapter.onClickInterface onClickInterface;
//    private ArrayList<tb_Bills> arraylist;

    private AlertDialog alertDialogAddNote;
    private String txtNote = "";

    public AdRecycItems(Context context, List<tb_Bills> data, onClickInterface onClickInterface) {
        this.context = context;
        this.data = data;
        this.onClickInterface = onClickInterface;
//        this.arraylist = new ArrayList<tb_Bills>();
//        this.arraylist.addAll(data);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listitems, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {

            Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            holder.itemView.startAnimation(animation);
            lastPosition = position;

            final tb_Bills tb_bills = data.get(position);

            String dateJalali = tb_bills.dateSMSJalali;
            String[] temp = dateJalali.split("/");
            CalendarTool tool = new CalendarTool();
            tool.setIranianDate(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));

            holder.txtNameWeek.setText(tool.getIranianWeekDayStr() + "");
            holder.txtSender.setText("فرستنده: " + tb_bills.senderSMS + "");
            holder.txtDate.setText("تاریخ: " + dateJalali);
            holder.txtTextSMS.setText("متن پیام:\n" + tb_bills.txtSMS);

            if (tb_bills.dateNoteJalali.equals("")) {
                holder.linearNoNote.setVisibility(View.VISIBLE);
                holder.linearNote.setVisibility(View.GONE);
            } else {
                holder.linearNoNote.setVisibility(View.GONE);
                holder.linearNote.setVisibility(View.VISIBLE);
                temp = tb_bills.dateNoteJalali.split("/");
                tool.setIranianDate(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));

                holder.txtDateNote.setText(tool.getIranianWeekDayStr() + " " + tool.getIranianDate());
                holder.txtTextNote.setText("متن پیام: " + tb_bills.txtNote + "");
            }


            holder.linearNoNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogAddNotee(tb_bills.PK_key,position,holder.itemView);
                }
            });

            holder.linearNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogAddNotee(tb_bills.PK_key,position,holder.itemView);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void alertDialogAddNotee(final String pk_key, final int position, final View itemView) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) li.inflate(R.layout.item_alert_show_add_note, null, false);
        Button btnSMessage_NO = layout.findViewById(R.id.btnSMessage_NO);
        Button btnSMessage_OK = layout.findViewById(R.id.btnSMessage_OK);
        final EditText edtSMessage_body = layout.findViewById(R.id.edtSMessage_body);
        btnSMessage_NO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogAddNote.dismiss();
            }
        });

        final tb_BillsDataSource source = new tb_BillsDataSource(context);
        final tb_Bills tb_billsEdit = source.GetARecord(pk_key + "");
        edtSMessage_body.setText(tb_billsEdit.txtNote + "");

        btnSMessage_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtSMessage_body.getText().toString().equals("")) {
                    txtNote = edtSMessage_body.getText().toString();
                    alertDialogAddNote.dismiss();

                    CalendarTool tool = new CalendarTool();
                    tb_billsEdit.dateNoteJalali = tool.getIranianDate();
                    tb_billsEdit.dateNoteMiladi = tool.getGregorianDate();
                    tb_billsEdit.txtNote = txtNote;
                    source.EditItems(tb_billsEdit);

                    onClickInterface.setClick(position, itemView, pk_key + "");

                } else
                    Toast.makeText(context, "متن نمیتواند خالی باشد", Toast.LENGTH_SHORT).show();
            }
        });


        builder.setView(layout);
        alertDialogAddNote = builder.create();
        alertDialogAddNote.show();
        alertDialogAddNote.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutMain;
        //-------------------------------------
        TextView txtNameWeek;
        TextView txtSender;
        TextView txtDate;
        TextView txtTextSMS;
        LinearLayout linearNoNote;
        ImageView imgAddNote;
        TextView txtNoNote;
        LinearLayout linearNote;
        TextView txtDateNote;
        TextView txtTextNote;

        ViewHolder(View view) {
            super(view);

            layoutMain = view.findViewById(R.id.layoutMain);

            txtNameWeek = view.findViewById(R.id.txtNameWeek);
            txtSender = view.findViewById(R.id.txtSender);
            txtDate = view.findViewById(R.id.txtDate);
            txtTextSMS = view.findViewById(R.id.txtTextSMS);
            linearNoNote = view.findViewById(R.id.linearNoNote);
            imgAddNote = view.findViewById(R.id.imgAddNote);
            txtNoNote = view.findViewById(R.id.txtNoNote);
            linearNote = view.findViewById(R.id.linearNote);
            txtDateNote = view.findViewById(R.id.txtDateNote);
            txtTextNote = view.findViewById(R.id.txtTextNote);

        }
    }

/*
    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        data.clear();
        if (charText.length() == 0) {
            data.addAll(arraylist);
        } else {
            for (tb_Bills wp : arraylist) {
                if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    */
}