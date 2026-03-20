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

    public RoomAdapter(Context context, List<Room> rooms, OnRoomActionListener listener) {
        this.context = context;
        this.rooms = rooms;
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

    // make ViewHolder static to avoid implicit reference to adapter
    public static class RoomViewHolder extends RecyclerView.ViewHolder {
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
            Context ctx = itemView.getContext();
            NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
            tvName.setText(room.getName());

            // Use string resource for price formatting
            String priceText = String.format(ctx.getString(R.string.price_format), nf.format(room.getPrice()));
            tvPrice.setText(priceText);

            // Null-safe status check using equals
            if (ctx.getString(R.string.status_rented).equals(room.getStatus())) {
                tvStatus.setText(R.string.status_rented);
                tvStatus.setTextColor(ctx.getColor(R.color.status_rented));
                String tenantText = String.format(ctx.getString(R.string.tenant_with_phone),
                        room.getTenantName() == null ? "" : room.getTenantName(),
                        room.getTenantPhone() == null ? "" : room.getTenantPhone());
                tvTenant.setText(tenantText);
            } else {
                tvStatus.setText(R.string.status_available);
                tvStatus.setTextColor(ctx.getColor(R.color.status_available));
                tvTenant.setText(ctx.getString(R.string.tenant_empty));
            }
        }
    }
}
