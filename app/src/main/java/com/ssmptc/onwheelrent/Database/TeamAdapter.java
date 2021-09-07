package com.ssmptc.onwheelrent.Database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ssmptc.onwheelrent.R;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ImageViewHolder> {

    private Context mContext;
    private List<TeamData> teamDataList;

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
        holder.tv_name.setText(currentData.getName());
        holder.tv_phone.setText(currentData.getPhoneNumber());
        Picasso.with(mContext)
                .load(currentData.getImgUrl())
                .placeholder(R.drawable.bg_loading)
                .into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        return teamDataList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView tv_phone,tv_name;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_phone = itemView.findViewById(R.id.tv_contact);
            tv_name = itemView.findViewById(R.id.tv_name);
            imageView = itemView.findViewById(R.id.iv_member);

        }

    }
}
