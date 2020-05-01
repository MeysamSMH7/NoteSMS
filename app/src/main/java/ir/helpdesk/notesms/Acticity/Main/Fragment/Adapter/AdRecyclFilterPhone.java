package ir.helpdesk.notesms.Acticity.Main.Fragment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ir.helpdesk.notesms.Acticity.Main.ModFilterPhone;
import ir.helpdesk.notesms.Acticity.Setting.Adapter.onClickInterface;
import ir.helpdesk.notesms.R;

public class AdRecyclFilterPhone extends RecyclerView.Adapter<AdRecyclFilterPhone.ViewHolder> {

    private Context context;
    private List<ModFilterPhone> data;
    private int lastPosition = -1;
    private onClickInterface onClickInterface;
    private ArrayList<ModFilterPhone> arraylist;
    private String tag;

    public AdRecyclFilterPhone(Context context, List<ModFilterPhone> data, onClickInterface onClickInterface) {
        this.context = context;
        this.data = data;
        this.onClickInterface = onClickInterface;
        this.tag = tag;
        this.arraylist = new ArrayList<ModFilterPhone>();
        this.arraylist.addAll(data);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_filter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {

            Animation animation = AnimationUtils.loadAnimation(context, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            holder.itemView.startAnimation(animation);
            lastPosition = position;

            holder.checkboxTitle.setVisibility(View.GONE);
            holder.txtTitle.setText(data.get(position).getTitle() + "");
            holder.txtId.setText(data.get(position).getId() + "");

            holder.txtTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickInterface.setClick(position, holder.itemView, null);
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
        TextView txtTitle;
        TextView txtId;
        CheckBox checkboxTitle;

        ViewHolder(View view) {
            super(view);

            layoutMain = view.findViewById(R.id.layoutMain);
            txtTitle = view.findViewById(R.id.txtTitle);
            txtId = view.findViewById(R.id.txtId);
            checkboxTitle = view.findViewById(R.id.checkboxTitle);

        }
    }


    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        data.clear();
        if (charText.length() == 0) {
            data.addAll(arraylist);
        } else {
            for (ModFilterPhone wp : arraylist) {
                if (wp.getTitle().toLowerCase(Locale.getDefault()).contains(charText)) {
                    data.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}