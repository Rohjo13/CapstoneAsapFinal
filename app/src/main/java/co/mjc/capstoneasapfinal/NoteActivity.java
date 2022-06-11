package co.mjc.capstoneasapfinal;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;

import co.mjc.capstoneasapfinal.adapter.NoteAdapter;
import co.mjc.capstoneasapfinal.database.DBHelper;
import co.mjc.capstoneasapfinal.note.WriteNote;
import co.mjc.capstoneasapfinal.pojo.NoteData;

public class NoteActivity extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase asapDb;

    ImageView returnNoteToNoteFolder;
    ImageView eraser;
    ImageView redPencil;
    ImageView blackPencil;

    TextView note_name_view_noteActivity;

    NoteData noteData;
    WriteNote writeNote;

    public void init() {
        returnNoteToNoteFolder = findViewById(R.id.returnNoteToNoteFolder);
        eraser = findViewById(R.id.eraser);
        redPencil = findViewById(R.id.redPencil);
        redPencil.setColorFilter(Color.parseColor("#ff0000"));
        blackPencil = findViewById(R.id.blackPencil);
        note_name_view_noteActivity = findViewById(R.id.note_name_view_noteActivity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_activity);

        dbHelper = new DBHelper(this);
        asapDb = dbHelper.getWritableDatabase();

        init();

        noteData = (NoteData) getIntent().getSerializableExtra("noteData");
        writeNote = new WriteNote(this, noteData.getNoteBitmapImage());

        note_name_view_noteActivity.setText(noteData.getNoteName());

        returnNoteToNoteFolder.setOnClickListener(view -> {
            Bitmap bitmap = writeNote.getmBitmap();
            String bitmapString = NoteFolderActivity.bitmapToString(bitmap);
            noteData.setNoteBitmapImage(bitmapString);
            asapDb.execSQL("UPDATE noteData SET noteImage = '" + bitmapString + "' WHERE noteName = '" + noteData.getNoteName() + "';");
            startActivity(new Intent(getApplicationContext(), NoteFolderActivity.class).putExtra("noteData",noteData));
        });

        ((LinearLayout) findViewById(R.id.noteActivity_drawingSpace)).addView(writeNote);


        redPencil.setOnTouchListener((view, motionEvent) -> {
            NoteActivity.this.writeNote.getmPaint().setColor(Color.RED);
            // 굵기
            NoteActivity.this.writeNote.getmPaint().setStrokeWidth(5);
            return false;
        });

        // 페인트 색상을 검정으로 변경
        blackPencil.setOnTouchListener((view, motionEvent) -> {
            NoteActivity.this.writeNote.getmPaint().setColor(Color.BLACK);
            // 굵기
            NoteActivity.this.writeNote.getmPaint().setStrokeWidth(5);
            return false;
        });

        // 지우개
        eraser.setOnTouchListener((view, motionEvent) -> {
            // 배경색으로 필기를 지운다.
            NoteActivity.this.writeNote.getmPaint().setColor(Color.parseColor("#FFFFFFFF"));
            // 굵기
            NoteActivity.this.writeNote.getmPaint().setStrokeWidth(25);
            return false;
        });
    }




}