package com.hoangcn.n7.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hoangcn.n7.R;
import com.hoangcn.n7.models.Room;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private Context context;
    private List<Room> roomList;
    private OnRoomActionListener listener;

    public interface OnRoomActionListener {
        void onEdit(Room room, int position);
        void onDelete(Room room, int position);
    }

    public RoomAdapter(Context context, List<Room> roomList, OnRoomActionListener listener) {
        this.context = context;
        this.roomList = roomList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = roomList.get(position);

        holder.tvRoomName.setText(room.getName());
        
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.tvPrice.setText("Giá: " + currencyFormat.format(room.getPrice()));

        if (room.isRented()) {
            holder.tvStatus.setText("Đã thuê");
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_rented);
            holder.tvTenant.setText("Người thuê: " + room.getTenantName() + " (" + room.getTenantPhone() + ")");
            holder.tvTenant.setVisibility(View.VISIBLE);
        } else {
            holder.tvStatus.setText("Còn trống");
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_available);
            holder.tvTenant.setVisibility(View.GONE);
        }

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(room, position));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(room, position));
    }

    @Override
    public int getItemCount() {
        return roomList != null ? roomList.size() : 0;
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomName, tvPrice, tvStatus, tvTenant;
        ImageButton btnEdit, btnDelete;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTenant = itemView.findViewById(R.id.tvTenant);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
