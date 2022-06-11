package co.mjc.capstoneasapfinal.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import co.mjc.capstoneasapfinal.R;


public class NoteViewHolder extends RecyclerView.ViewHolder {

    public TextView txtName;
    public CardView cardView;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        txtName = itemView.findViewById(R.id.note_txtName);
        cardView = itemView.findViewById(R.id.note_cardView);
    }
}
