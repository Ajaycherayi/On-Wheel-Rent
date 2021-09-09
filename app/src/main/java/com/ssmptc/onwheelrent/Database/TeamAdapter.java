package com.ssmptc.onwheelrent.Database;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ssmptc.onwheelrent.R;

import java.net.URLEncoder;
import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ImageViewHolder> {

    private final Context mContext;
    private final List<TeamData> teamDataList;

    public TeamAdapter(Context context, List<TeamData> teamData){

        mContext = context;
        teamDataList = teamData;

    }

    @NonNull
    @Override
    public TeamAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_view_team_members,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamAdapter.ImageViewHolder holder, int position) {

        TeamData currentData = teamDataList.get(position);

       final String name;

        holder.tv_name.setText(currentData.getName());
        holder.tv_desTitle.setText(currentData.getDescTitle());
        holder.tv_desc.setText(currentData.getDesc());

        name = currentData.getName();

        holder.btn_email.setOnClickListener(v -> {

            String email = currentData.getEmail();

            Intent emailIntent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.parse("mailto:?subject=" +"On Wheel Rent"+ "&body=" + "Hai,\n" + "&to=" + email);
            emailIntent.setData(data);
            mContext.startActivity(emailIntent);
            Toast.makeText(mContext, "Contact "+name+" via E-mail", Toast.LENGTH_SHORT).show();
        });
        holder.btn_whatsapp.setOnClickListener(v -> {

            String phone = currentData.getPhoneNumber();

            Intent whatsapp = new Intent(Intent.ACTION_VIEW);

            try {
                String url = "https://api.whatsapp.com/send?phone="+ phone +"&text=" + URLEncoder.encode("Hai,\n", "UTF-8");
                whatsapp.setPackage("com.whatsapp");
                whatsapp.setData(Uri.parse(url));
                mContext.startActivity(whatsapp);
            } catch (Exception e){
                e.printStackTrace();
            }
            Toast.makeText(mContext, "Contact "+name+" via Whatsapp", Toast.LENGTH_SHORT).show();
        });
        holder.btn_insta.setOnClickListener(v -> {

            String instaId = currentData.getInsta();

            Uri uri = Uri.parse("https://instagram.com/"+instaId+"?utm_medium=copy_link");
            Intent instagram = new Intent(Intent.ACTION_VIEW, uri);
            instagram.setPackage("com.instagram.android");

            try {
                mContext.startActivity(instagram);
            } catch (ActivityNotFoundException e) {
                mContext.startActivity(instagram);
            }
            Toast.makeText(mContext, "Contact "+name+" via Instagram", Toast.LENGTH_SHORT).show();
        });

        Glide.with(mContext)
                .load(currentData.getImgUrl())
                .placeholder(R.drawable.bg_loading)
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return teamDataList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView,btn_whatsapp,btn_insta,btn_email;
        public TextView tv_desTitle,tv_name,tv_desc;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_desTitle = itemView.findViewById(R.id.tv_desTitle);
            tv_desc = itemView.findViewById(R.id.tv_desc);
            tv_name = itemView.findViewById(R.id.tv_name);
            imageView = itemView.findViewById(R.id.iv_member);
            btn_whatsapp = itemView.findViewById(R.id.btn_whatsapp);
            btn_insta = itemView.findViewById(R.id.btn_insta);
            btn_email = itemView.findViewById(R.id.btn_email);

        }

    }
}
