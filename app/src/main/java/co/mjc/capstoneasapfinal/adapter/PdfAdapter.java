package co.mjc.capstoneasapfinal.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.mjc.capstoneasapfinal.R;
import co.mjc.capstoneasapfinal.pojo.PdfData;

public class PdfAdapter extends RecyclerView.Adapter<PdfViewHolder> {


    private Context context;
    private List<PdfData> pdfFiles;
    private OnPdfSelectListener listener;

    public PdfAdapter(Context context, List<PdfData> pdfFiles, OnPdfSelectListener listener) {
        this.context = context;
        this.pdfFiles = pdfFiles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new PdfViewHolder(LayoutInflater.from(context).inflate(R.layout.
                        pdf_items_folder_grid_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtName.setText(pdfFiles.get(position).getPdfName());
        holder.txtName.setSelected(true);
        holder.cardView.setOnClickListener(view ->
                // Uri 병렬화라서 직렬화 가능한 String 으로 바꾼다음에 다시 Uri 로 변환했음
                listener.onPdfSelected(Uri.parse(pdfFiles.get(position).getPdfUri())));
    }

    @Override
    public int getItemCount() {
        return pdfFiles.size();
    }

}
