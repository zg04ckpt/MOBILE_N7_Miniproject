package com.hoangcn.n7.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hoangcn.n7.databinding.ItemRoomBinding;
import com.hoangcn.n7.models.Room;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private List<Room> rooms;
    private Context context;
    private OnRoomClickListener listener;

    public interface OnRoomClickListener {
        void onEditClick(Room room, int position);
        void onDeleteClick(int position);
    }

    public RoomAdapter(List<Room> rooms, Context context, OnRoomClickListener listener) {
        this.rooms = rooms;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRoomBinding binding = ItemRoomBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new RoomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = rooms.get(position);
        holder.bind(room, position);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {
        private ItemRoomBinding binding;

        public RoomViewHolder(@NonNull ItemRoomBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Room room, int position) {
            binding.tvRoomName.setText(room.getName());
            binding.tvPrice.setText("Giá: " + room.getPrice() + " VND");
            binding.tvStatus.setText(room.getStatus());

            int statusColor = "Còn trống".equals(room.getStatus()) ?
                    context.getColor(android.R.color.holo_green_light) :
                    context.getColor(android.R.color.holo_red_light);
            binding.tvStatus.setTextColor(statusColor);

            binding.btnEdit.setOnClickListener(v -> listener.onEditClick(room, position));
            binding.btnDelete.setVisibility(android.view.View.GONE);
        }
    }

    public void updateRoom(int position, Room room) {
        rooms.set(position, room);
        notifyItemChanged(position);
    }
}
