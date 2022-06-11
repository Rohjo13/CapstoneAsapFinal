package co.mjc.capstoneasapfinal;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.mjc.capstoneasapfinal.adapter.NoteAdapter;
import co.mjc.capstoneasapfinal.adapter.OnNoteSelectListener;
import co.mjc.capstoneasapfinal.pojo.NoteData;

public class NoteFolderActivity extends AppCompatActivity implements OnNoteSelectListener {

    ImageView returnNoteFolderToSchedule;
    ImageView createNote;

    private NoteAdapter noteAdapter;
    private RecyclerView noteRecyclerView;

    List<NoteData> noteDataList;

    Optional<NoteData> returnNoteData;


    // 주요 문제 생성은 Schedule 에서만 했지 중심이 되는 놈이 없음
    public void init() {
        returnNoteFolderToSchedule = findViewById(R.id.returnNoteFolderToSchedule);
        createNote = findViewById(R.id.createNote);
        noteRecyclerView = findViewById(R.id.noteRecyclerView);
        noteDataList = (ArrayList<NoteData>) getIntent().getSerializableExtra("noteDataList");
        returnNoteData = Optional.ofNullable((NoteData) getIntent().getSerializableExtra("noteData"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_folder_activity);

        init();

        returnNoteData.ifPresent(noteData ->
                noteDataList.add(noteData));

        noteActivate();

        // 노트 생성
        createNote.setOnClickListener(view -> {
            Dialog dialog = new Dialog(NoteFolderActivity.this);
            dialog.setContentView(R.layout.note_create_form);
            dialog.setTitle("노트 생성");

            EditText setNoteName = dialog.findViewById(R.id.setNoteName);

            Button createNoteBtn = dialog.findViewById(R.id.createNote_btn);
            Button cancelNoteBtn = dialog.findViewById(R.id.cancelNote_btn);

            createNoteBtn.setOnClickListener(create -> {
                NoteData noteData = new NoteData();
                noteData.setNoteName(setNoteName.getText().toString());
                noteData.setNoteBitmapImage(null);
                if (setNoteName.getText().equals("")) {
                    Toast.makeText(getApplicationContext(), "노트 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                noteDataList.add(noteData);
                Toast.makeText(getApplicationContext(), "생성되었습니다.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
            cancelNoteBtn.setOnClickListener(cancel -> {
                Toast.makeText(getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });

            noteAdapter.notifyDataSetChanged();
            dialog.show();
        });


        returnNoteFolderToSchedule.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),
                ScheduleActivity.class).putExtra("noteDataList",(Serializable) noteDataList)));


    }

    public void noteActivate() {
        noteRecyclerView.setHasFixedSize(true);
        noteRecyclerView.setLayoutManager(new GridLayoutManager(
                this, 2));
        // 어답터 인스턴스 생성
        noteAdapter = new NoteAdapter(getApplicationContext(), noteDataList,
                this);
        // 어답터 set
        noteRecyclerView.setAdapter(noteAdapter);
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNoteSelected(NoteData noteData) {
        startActivity(new Intent(getApplicationContext(),
                NoteActivity.class).putExtra("noteData",noteData));
    }

    /**
     * 비트맵 과 문자열 전환 메서드
     * @param bitmap
     * @return
     */
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, byteStream);
        byte[] bytes = byteStream.toByteArray();
        String temp = Base64.encodeToString(bytes, Base64.DEFAULT);
        return temp;
    }

    public static Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}