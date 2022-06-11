package co.mjc.capstoneasapfinal;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import co.mjc.capstoneasapfinal.adapter.ChildHolder;
import co.mjc.capstoneasapfinal.adapter.ExpandableChildAdapter;
import co.mjc.capstoneasapfinal.adapter.ScheduleExpandableAdapter;
import co.mjc.capstoneasapfinal.database.DBHelper;
import co.mjc.capstoneasapfinal.pojo.DataEnum;
import co.mjc.capstoneasapfinal.pojo.NoteData;
import co.mjc.capstoneasapfinal.pojo.PdfData;
import co.mjc.capstoneasapfinal.pojo.Schedule;
import co.mjc.capstoneasapfinal.pojo.ScheduleEnum;


public class ScheduleActivity extends AppCompatActivity {

    private final String LogTAG = "ScheduleActivity";

    // 카메라 기능에 사용할 것
    static final int REQUEST_TAKE_PHOTO = 1;
    private String mCurrentPhotoPath;

    private Handler mHandler;
    // DB
    DBHelper dbHelper;
    SQLiteDatabase asapDb;

    // 오늘 날짜
    TextView dayOfWeek;
    // 실시간 시간
    TextView clockTextView;

    // 익스팬더블 리스트뷰 && 어댑터
    ExpandableListView expandableListView;
    ScheduleExpandableAdapter scheduleAdapter;
    ChildHolder childHolder;
    View childAdapterView;
    ExpandableChildAdapter childAdapter;

    // Lists
    List<Integer> childList;

    List<String> cameraPathList;
    List<Schedule> scheduleList;
    List<PdfData> pdfDataList;
    List<NoteData> noteDataList;


    public void init() {
        if (cameraPathList == null) {
            cameraPathList = (List<String>) getIntent().getSerializableExtra("cameraPathList");
        } else {

            cameraPathList = new ArrayList<>();
        }
        if (scheduleList == null) {
            scheduleList = new ArrayList<>();
        }
        if (pdfDataList == null) {
            pdfDataList = (List<PdfData>) getIntent().getSerializableExtra("pdfDataList");
        } else {
            pdfDataList = new ArrayList<>();
        }
        if (noteDataList == null) {
/*            noteDataList = (List<NoteData>) getIntent().getSerializableExtra("noteDataList");
        } else { */
            noteDataList = new ArrayList<>();
        }

        dayOfWeek = findViewById(R.id.dayofweek);

        dbHelper = new DBHelper(getApplicationContext());
        asapDb = dbHelper.getWritableDatabase();

        if (childList == null) {
            Log.d(LogTAG, "funcImageViewList is null");
            childList = new ArrayList<>();
            childList.add(R.drawable.cameraicon);
            childList.add(R.drawable.foldericon);
            childList.add(R.drawable.pdficon);
            childList.add(R.drawable.notesicon);
        }
    }

    // 삭제할 예정 디폴트값으로 넣음
    public void defaultData() {
        Schedule schedule = new Schedule();
        schedule.setLecName("hello");
        schedule.setDayOTW(ScheduleEnum.THURSDAY);
        scheduleList.add(schedule);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_activity);

        // List init
        init();
        // Now Clock
        nowClock();
        // childView
        staticChildView();
        staticParentView();
        // 오늘 날짜
        dayOfWeek.setText(DataEnum.dateCheck());

        // 강의 시간표 이름 정의해야하는가?


        defaultData();
        // update!!
        scheduleAdapter.notifyDataSetChanged();

        // 효과 줄거면 주고
        expandableListView.setOnGroupClickListener((expandableListView, view, gPos, l) -> {
            Toast.makeText(getApplicationContext(), scheduleList.get(gPos).getLecName() +
                    "를 선택하셨습니다.", Toast.LENGTH_SHORT).show();
            return false;
        });
        childAdapter.setOnItemClick((v, pos) -> {
            switch (pos) {
                case 0: // 0은 카메라
                    takeAPicture();
                    break;
                case 1: // 카메라 액티비티 전환
                    scheduleToCamera();
                    break;
                case 2: // pdf 액티비티 전환
                    scheduleToPdf();
                    break;
                case 3: // 노트 액티비티 전환
                    scheduleToNote();
                    break;
            }
        });
    }

    /**
     * 메뉴 바 생성
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 메뉴 아이템 리스너
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.createSchedule:
                setCreateSchedule();
                break;
            case R.id.deleteSchedule:
//                deleteSchedule();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 스케쥴 생성
     */
    public void setCreateSchedule() {
        // schedule 을 만드는 Dialog 생성
        Dialog dialog = new Dialog(ScheduleActivity.this);
        dialog.setContentView(R.layout.schedule_create_form);
        dialog.setTitle("시간표 추가");
        // Button 객체 생성
        Button addSchedule = dialog.findViewById(R.id.addSchedule);
        Button cancelAddSchedule = dialog.findViewById(R.id.cancelAddSchedule);
        // 강의 이름 설정
        EditText createNameSchedule = dialog.findViewById(R.id.createNameSchedule);
        TextView selectDate = dialog.findViewById(R.id.selectDate);
        // schedule 객체 생성
        Schedule schedule = new Schedule();
        // 요일을 설정하는 팝업 메뉴
        selectDate.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(ScheduleActivity.this, view);
            popup.getMenuInflater().inflate(R.menu.datepopup_schedule, popup.getMenu());
            popup.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.dateMon:
                        schedule.setDayOTW(ScheduleEnum.MONDAY);
                        selectDate.setText(ScheduleEnum.MONDAY.name());
                        break;
                    case R.id.dateTUE:
                        schedule.setDayOTW(ScheduleEnum.TUESDAY);
                        selectDate.setText(ScheduleEnum.TUESDAY.name());
                        break;
                    case R.id.dateWED:
                        schedule.setDayOTW(ScheduleEnum.WEDNESDAY);
                        selectDate.setText(ScheduleEnum.WEDNESDAY.name());
                        break;
                    case R.id.dateTHU:
                        schedule.setDayOTW(ScheduleEnum.THURSDAY);
                        selectDate.setText(ScheduleEnum.THURSDAY.name());
                        break;
                    case R.id.dateFRI:
                        schedule.setDayOTW(ScheduleEnum.FRIDAY);
                        selectDate.setText(ScheduleEnum.FRIDAY.name());
                        break;
                    default:
                        break;
                }
                return true;
            });
            popup.show();
        });
        // 확인 버튼인데, 데이터가 다 입력되어야만 추가 완료 if 문으로 검사
        addSchedule.setOnClickListener(view1 -> {
            schedule.setLecName(createNameSchedule.getText().toString());

            if (createNameSchedule.getText().toString().equals("") || schedule.getDayOTW() == null) {
                Toast.makeText(ScheduleActivity.this,
                        "전부 입력해주세요.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ScheduleActivity.this,
                        "강의가 추가 완료되었습니다.", Toast.LENGTH_LONG).show();
                scheduleList.add(schedule);
                // DB in
//                asapDb.execSQL("INSERT INTO schedule VALUES ('" + schedule.getLecName() + "'," +
//                        " '" + schedule.getDayOTW() + "');");
                scheduleAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        cancelAddSchedule.setOnClickListener(view2 -> dialog.dismiss());
        dialog.show();
    }

    /**
     * 부모 뷰(익스펜더블)
     */
    public void staticParentView() {
        expandableListView = findViewById(R.id.lsScheduleListExpandable);
        // 어뎁터 인스턴스 생성 여기다가 넘겨준게 loginMember 거라 요일별로 저장하려면 다른 것을 넣거나 해야 됌
        scheduleAdapter = new ScheduleExpandableAdapter(getApplicationContext(),
                childHolder, scheduleList, childList, childAdapterView);
        // 어뎁터 설정
        expandableListView.setAdapter(scheduleAdapter);
    }

    /**
     * 차일드 뷰(익스펜더블)
     */
    public void staticChildView() {
        childAdapterView = null;
        childHolder = null;
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        childAdapterView = inflater.inflate(R.layout.schedule_child_item_group, null, false);
        childHolder = new ChildHolder();
        childAdapterView.setTag(childHolder);
        // View 에 Tag 를 달아 식별한다.
        childHolder.horizontalListView = childAdapterView.findViewById(R.id.child_group);
        LinearLayoutManager linearLayoutManager = new
                LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        childHolder.horizontalListView.setLayoutManager(linearLayoutManager);

        childAdapter = new ExpandableChildAdapter(getApplicationContext(), childList);
        childHolder.horizontalListView.setAdapter(childAdapter);
    }


    // Camera 1 setting -> picture
    private void takeAPicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 권한 요청
            if (checkSelfPermission(Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(LogTAG, "권한 설정 완료 됌");
            } else {
                Log.d(LogTAG, "카메라 권한 요청");
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
        // 카메라 앱으로 넘어가는 인텐트
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
        }
        if (photoFile != null) {
            // 사진 파일 경로명 지정
            Uri photoURI = FileProvider.
                    getUriForFile(this,
                            "co.mjc.capstoneasapfinal.fileprovider", photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    // Camera 2 createTemp
    private File createImageFile() throws IOException {
        // 파일 저장 형식
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        // 외부 저장소 경로
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // 임시 파일을 만드는데, 형식을 특정 이름으로 저장하고, jpg 파일 형식으로, 내부 저장소 경로로 파일 생성하고,
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        // 이렇게 만들어진 저장경로를 String 에 저장함
        mCurrentPhotoPath = image.getAbsolutePath();
//        JPEG_20220508_175021_3479621252757571591.jpg
        return image;
    }

    // Camera 3 return result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        try {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO: {
                    if (resultCode == RESULT_OK) {
                        cameraPathList.add(mCurrentPhotoPath);
//                        asapDb.execSQL("INSERT INTO filePaths VALUES ('" + loginMember.getMemId()
//                                + "', '" + mCurrentPhotoPath + "');");
                    }
                    break;
                }
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    /**
     * 인텐트 변경
     */
    public void scheduleToCamera() {
        startActivity(new Intent(this, CameraFolderActivity.class)
                .putExtra("cameraPathList", (Serializable) cameraPathList));
    }

    public void scheduleToPdf() {
        startActivity(new Intent(getApplicationContext(),
                PdfFolderActivity.class).putExtra("pdfDataList", (Serializable) pdfDataList));
    }

    public void scheduleToNote() {
        startActivity(new Intent(getApplicationContext(),
                NoteFolderActivity.class).putExtra("noteDataList", (Serializable) noteDataList));
    }

    /**
     * 현재 시간
     */
    public void nowClock() {
        clockTextView = findViewById(R.id.clock);
        mHandler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String strTime = sdf.format(cal.getTime());
                clockTextView = findViewById(R.id.clock);
                clockTextView.setText(strTime);
            }
        };

        class NewRunnable implements Runnable {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessage(0);
                }
            }
        }
        NewRunnable nr = new NewRunnable();
        Thread t = new Thread(nr);
        t.start();
    }
}