package co.mjc.capstoneasapfinal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.mjc.capstoneasapfinal.R;


public class ExpandableChildAdapter extends RecyclerView.Adapter<ExpandableChildAdapter.ChildViewHolder> {

    private Context context;
    private List<Integer> imageViews;

    public ExpandableChildAdapter(Context context, List<Integer> imageViews) {
        this.context = context;
        this.imageViews = imageViews;
    }

    public interface OnItemClick {
        void itemClick(View v, int pos);
    }

    OnItemClick onItemClick = null;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.schedule_child_layout, null, false);
        ChildViewHolder holder = new ChildViewHolder(view);
        holder.func = view.findViewById(R.id.func);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChildViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ImageView imageView = holder.func;
        Log.e("ExpandableChildAdapter", "호출됨");
        imageView.setImageResource(imageViews.get(position));
        holder.func.setOnClickListener(view -> onItemClick.itemClick(view, position));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return imageViews.size();
    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder {

        ImageView func;

        public ChildViewHolder(@NonNull View itemView) {
            super(itemView);
            func = itemView.findViewById(R.id.func);
        }
    }
}
