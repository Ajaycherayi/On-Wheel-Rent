package com.ssmptc.onwheelrent.Database;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ssmptc.onwheelrent.R;

import java.util.ArrayList;
import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ProfileViewHolder>{

    private final Context mContext;
    private final List<TeamData> teamDataList;
    private VehicleBookAdapter.OnItemClickListener mListener;


    public TeamAdapter(Context context, List<TeamData> teamData) {

        mContext = context;
        teamDataList = teamData;

    }

    @NonNull
    @Override
    public TeamAdapter.ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_view_all_vehicle,parent,false);
        return new ProfileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamAdapter.ProfileViewHolder holder, int position) {

        TeamData data = teamDataList.get(position);
        holder.tv_name.setText(currentData.getVehicleName());
        holder.tv_contact.setText(currentData.getPhone());
        Glide.with(mContext)
                .load(currentData.getImgUrl())
                .placeholder(R.drawable.car_rent)
                .centerInside()
                .into(holder.imageView);



    }

    @Override
    public int getItemCount() {
        return vehicleDataList.size();
    }



    public class ProfileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imageView;
        public TextView tv_contact,tv_name;


        public ProfileViewHolder(View v) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_vehicle);
            tv_name = itemView.findViewById(R.id.tv_vName);
            tv_contact = itemView.findViewById(R.id.tv_contact);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.onItemClick(position);
                }
            }
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


}
