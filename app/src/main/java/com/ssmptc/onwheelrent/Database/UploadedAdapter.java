package com.ssmptc.onwheelrent.Database;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ssmptc.onwheelrent.R;

import java.util.List;

public class UploadedAdapter extends RecyclerView.Adapter<UploadedAdapter.ImageViewHolder>{
    private Context mContext;
    private List<VehicleData> vehicleDataList;
    private OnItemClickListener mListener;

    public UploadedAdapter(Context context, List<VehicleData> vehicleData) {

        mContext = context;
        vehicleDataList = vehicleData;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card_view_uploaded_vehicle,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        VehicleData currentData = vehicleDataList.get(position);
        holder.tv_name.setText(currentData.getVehicleName());


        if (currentData.isBooked()){
            holder.tv_status.setText(" Booked");
            holder.tv_status.setBackgroundResource(R.drawable.bg_booking_active);
            holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.black));
        }else {
            holder.tv_status.setText(" Not Booked");
        }

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


    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public ImageView imageView;
        public TextView tv_status,tv_name;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_vehicle);
            tv_name = itemView.findViewById(R.id.tv_vName);
            tv_status = itemView.findViewById(R.id.tv_bookStatus);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

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

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Delete");
            MenuItem delete = menu.add(Menu.NONE, 1,1,"Delete Vehicle");

            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    item.getItemId();
                    mListener.onDeleteClick(position);
                    return true;
                }
            }

            return false;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

}