package co.mjc.capstoneasapfinal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.mjc.capstoneasapfinal.R;
import co.mjc.capstoneasapfinal.pojo.NoteData;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private Context context;
    private List<NoteData> noteDataList;
    private OnNoteSelectListener listener;

    public NoteAdapter(Context context, List<NoteData> noteDataList, OnNoteSelectListener listener) {
        this.context = context;
        this.noteDataList = noteDataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.
                note_items_folder_grid_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.txtName.setText(noteDataList.get(position).getNoteName());
//        holder. 사진도 넣어야 됌
        holder.txtName.setSelected(true);
        holder.cardView.setOnClickListener(view -> listener.onNoteSelected(noteDataList.get(position)));
    }

    @Override
    public int getItemCount() {
        return noteDataList.size();
    }
}
