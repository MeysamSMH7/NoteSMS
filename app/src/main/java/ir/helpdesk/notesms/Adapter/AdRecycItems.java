package ir.helpdesk.notesms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.helpdesk.notesms.DataBase.Tables.tb_Bills;
import ir.helpdesk.notesms.R;

public class AdRecycItems extends RecyclerView.Adapter<AdRecycItems.ViewHolder> {

    private Context context;
    private List<tb_Bills> data;
    private int lastPosition = -1;
    private onClickInterface onClickInterface;
//    private ArrayList<tb_Bills> arraylist;

    public AdRecycItems(Context context, List<tb_Bills> data , onClickInterface onClickInterface) {
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

            tb_Bills tb_bills = data.get(position);

            String str = "پیامک از طرف: " + tb_bills.senderSMS
                    + "\n" + "تاریخ دریافت" + tb_bills.dateSMSJalali
                    + "\n" + "متن پیام:" + tb_bills.txtSMS
                    + "\n" + "کلید:" + tb_bills.PK_key
                    + "\n\n" + "متن نوت:" + tb_bills.txtNote
                    + "\n" + "تاریخ یادداشت:" + tb_bills.dateNoteMiladi
                    + "\n";

            holder.txtaasfd.setText(str + "");

            holder.txtaasfd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickInterface.setClick(position, holder.itemView);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutMain;
        TextView txtaasfd;

        ViewHolder(View view) {
            super(view);

            layoutMain = view.findViewById(R.id.layoutMain);
            txtaasfd = view.findViewById(R.id.txtaasfd);

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