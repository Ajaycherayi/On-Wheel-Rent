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

public class VehicleBookAdapter extends RecyclerView.Adapter<VehicleBookAdapter.ImageViewHolder>{

    private final Context mContext;
    private final List<VehicleData> vehicleDataList;
    private final List<VehicleData> copyList;
    private OnItemClickListener mListener;

    public VehicleBookAdapter(Context context, List<VehicleData> vehicleData) {

        mContext = context;
        vehicleDataList = vehicleData;
        this.copyList = new ArrayList<>();
        copyList.addAll(vehicleData);

    }

    @NonNull
    @Override
    public VehicleBookAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_view_all_vehicle,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleBookAdapter.ImageViewHolder holder, int position) {

        VehicleData currentData = vehicleDataList.get(position);
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

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imageView;
        public TextView tv_contact,tv_name;

        public ImageViewHolder(@NonNull View itemView) {
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


    public void Search(CharSequence txt) {
        List<VehicleData> searchList = new ArrayList<>();

        if (!TextUtils.isEmpty(txt)){
            for (VehicleData data : vehicleDataList){
                if (data.getPlace().toLowerCase().contains(txt) || data.getVehicleName().toLowerCase().contains(txt)){
                    searchList.add(data);
                }
            }
        }else {
            searchList.addAll(copyList);
        }
        vehicleDataList.clear();
        vehicleDataList.addAll(searchList);
        notifyDataSetChanged();
        searchList.clear();
    }
}
