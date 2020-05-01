package ir.helpdesk.notesms.Acticity.Setting.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ir.helpdesk.notesms.Acticity.Main.ModFilterPhone;
import ir.helpdesk.notesms.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdRecyclFilterPhoneCheckBox extends RecyclerView.Adapter<AdRecyclFilterPhoneCheckBox.ViewHolder> {

    private Context context;
    private List<ModFilterPhone> data;
    private int lastPosition = -1;
    private onClickInterface onClickInterface;
    private ArrayList<ModFilterPhone> arraylist;
    private String tag;

    public AdRecyclFilterPhoneCheckBox(Context context, List<ModFilterPhone> data, onClickInterface onClickInterface) {
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

            holder.txtTitle.setVisibility(View.GONE);
            holder.checkboxTitle.setText(data.get(position).getTitle() + "");
            holder.txtId.setText(data.get(position).getId() + "");

            holder.checkboxTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickInterface.setClick(position, holder.itemView, null);
                }
            });
            final SharedPreferences preferences = context.getSharedPreferences("TuRn", 0);
            final String[] titles = preferences.getString("titles", "").split(",");

            boolean b = false;
            if (!titles[0].equals(""))
                for (int i = 0; i < titles.length; i++) {
                    if (data.get(position).getTitle().equals(titles[i])) {
                        b = true;
                        break;
                    }
                }

            if (b) holder.checkboxTitle.setChecked(true);
            else holder.checkboxTitle.setChecked(false);

            holder.checkboxTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    final String titlePH = holder.checkboxTitle.getText().toString();
                    String phoneNum = preferences.getString("phoneNum", "");
                    final String titles = preferences.getString("titles", "");
                    if (isChecked) {
                        if (!titles.contains(titlePH)) {
                            if (!titlePH.equals("") && !titles.contains(titlePH)) {
                                SharedPreferences.Editor editor = preferences.edit();
                                if (titles.equals("")) {
                                    editor.putString("titles", titlePH);
                                    editor.putString("phoneNum", titlePH);
                                } else {
                                    editor.putString("titles", titles + "," + titlePH);
                                    editor.putString("phoneNum", titles + "," + titlePH);
                                }
                                editor.apply();

                            } else
                                Toast.makeText(context, "این رو قبلا زدی", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(context, "چنین شماره ای وجود داره", Toast.LENGTH_SHORT).show();
                    } else {
                        final String[] titles2 = preferences.getString("titles", "").split(",");
                        String temp = "";
                        for (int i = 0; i < titles2.length; i++) {
                            String tempInPer = titles2[i];
                            if (!tempInPer.equals(titlePH)) {
                                if (i == 0)
                                    temp = tempInPer;
                                else if (temp.equals(""))
                                    temp = tempInPer;
                                else
                                    temp += "," + tempInPer;
                            }
                        }

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("titles", temp);
                        editor.putString("phoneNum", temp);
                        editor.apply();
                    }


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