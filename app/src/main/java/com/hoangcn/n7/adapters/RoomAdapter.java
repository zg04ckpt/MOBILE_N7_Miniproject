package com.hoangcn.n7.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hoangcn.n7.R;
import com.hoangcn.n7.models.Room;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    public interface OnRoomActionListener {
        void onEdit(Room room, int position);

        void onDelete(Room room, int position);
    }

    private final Context context;
    private final List<Room> rooms;
    private final OnRoomActionListener listener;
    private final NumberFormat currencyFormat;

    public RoomAdapter(Context context, List<Room> rooms, OnRoomActionListener listener) {
        this.context = context;
        this.rooms = rooms;
        this.listener = listener;
        this.currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.bind(room);
        holder.itemView.setOnClickListener(v -> listener.onEdit(room, position));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onDelete(room, position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    class RoomViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvPrice;
        private final TextView tvStatus;
        private final TextView tvTenant;

        RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvRoomName);
            tvPrice = itemView.findViewById(R.id.tvRoomPrice);
            tvStatus = itemView.findViewById(R.id.tvRoomStatus);
            tvTenant = itemView.findViewById(R.id.tvRoomTenant);
        }

        void bind(Room room) {
            tvName.setText(room.getName());
            tvPrice.setText(currencyFormat.format(room.getPrice()) + " VND");

            if (room.isRented()) {
                tvStatus.setText(R.string.status_rented);
                tvStatus.setTextColor(context.getColor(R.color.status_rented));
                tvTenant.setText(room.getTenantName() + " - " + room.getTenantPhone());
            } else {
                tvStatus.setText(R.string.status_available);
                tvStatus.setTextColor(context.getColor(R.color.status_available));
                tvTenant.setText(context.getString(R.string.tenant_empty));
            }
        }
    }
}
