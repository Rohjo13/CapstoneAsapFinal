package co.mjc.capstoneasapfinal;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import co.mjc.capstoneasapfinal.adapter.OnPdfSelectListener;
import co.mjc.capstoneasapfinal.adapter.PdfAdapter;
import co.mjc.capstoneasapfinal.pojo.PdfData;


public class PdfFolderActivity extends AppCompatActivity implements OnPdfSelectListener {

    // pdf 기능에 사용할 거
    private List<PdfData> pdfList;
    private PdfAdapter adapter;
    private RecyclerView recyclerView;
    static final int REQUEST_TAKE_FILE = 2;

    ImageView returnPdfFolderToSchedule;

    File externalFilesDir;

    ImageView importPdf;

    Uri pdfUri;
    String pdfName;

    public void init() {
        pdfList = new ArrayList<>();
        returnPdfFolderToSchedule = findViewById(R.id.returnPdfFolderToSchedule);
        importPdf = findViewById(R.id.importPdf);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_folder_activity);


        pdfActivate();

        returnPdfFolderToSchedule.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),
                ScheduleActivity.class)));

        // pdf 임포트한다.
        importPdf.setOnClickListener(view -> {
            // 외부 저장소 경로
            String path = Environment.getExternalStorageDirectory().getPath()
                    + "/Download/";
            // 앱에서 참조할 수 있도록 ACTION_GET_CONTENT 액션 사용
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            // 리소스를 접근할 수 있도록 경로 조정
            Uri uri = Uri.parse(path);
            // uri 가 접근 할 수 있는 타입 지정 현재는 pdf 타입
            intent.setDataAndType(uri, "application/pdf");
            // 액티비티를 실행하고 결과 값을 받는다.
            startActivityForResult(Intent.createChooser(intent, "Open"), REQUEST_TAKE_FILE);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            // 요청한 값이 REQUEST_TAKE_FILE 이면
            case REQUEST_TAKE_FILE:
                // 거기에 결과 값이 OK면
                if(resultCode == RESULT_OK) {
                    PdfData pdfData = new PdfData();
                    // Intent 에서 Uri Data 를 받아오고,
                    pdfUri = data.getData();
                    // 파일 이름을 알기 위해 DB를 Cursor 로 찾는다.
                    // Cursor -> 데이터베이스에 저장되어있는 테이블의 행을 참조하여 결과 값 가져옴
                    Cursor returnCursor =
                            getContentResolver().query(pdfUri, null,
                                    null, null, null);
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    // Cursor 를 첫번째 행으로 옮긴다.
                    returnCursor.moveToFirst();
                    // uri 에서 파일 이름을 가져옴
                    pdfName = returnCursor.getString(nameIndex);
                    // pdfData 객체에 set
                    pdfData.setPdfUri(pdfUri.toString());
                    pdfData.setPdfName(pdfName);
                    // List 에 추가
                    pdfList.add(pdfData);
                    // 추가 된 것을 알려야 함
                    adapter.notifyDataSetChanged();
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void pdfActivate() {
        // 다 수정해야 됌
        recyclerView = findViewById(R.id.pdfRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(
                this, 2));
        // 파일 경로
        // 어답터 인스턴스 생성
        adapter = new PdfAdapter(this, pdfList,
                this);
        // 어답터 set
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onPdfSelected(Uri uri) {
        startActivity(new Intent(getApplicationContext(),
                PdfActivity.class).putExtra("uri", uri));
    }
}