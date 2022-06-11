package co.mjc.capstoneasapfinal.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import co.mjc.capstoneasapfinal.R;


public class PdfViewHolder extends RecyclerView.ViewHolder {

    public TextView txtName;
    public CardView cardView;

    public PdfViewHolder(@NonNull View itemView) {
        super(itemView);

        txtName = itemView.findViewById(R.id.pdf_txtName);
        cardView = itemView.findViewById(R.id.pdf_cardView);
    }
}
