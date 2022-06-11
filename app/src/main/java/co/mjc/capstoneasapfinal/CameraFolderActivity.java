package co.mjc.capstoneasapfinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.mjc.capstoneasapfinal.adapter.ViewpagerAdapter;


public class CameraFolderActivity extends AppCompatActivity {

    final String Tag = "CameraFolderActivity";

    List<String> cameraPathList;
    List<Bitmap> stringToBitmapGalleries;

    ImageView returnCameraToSchedule;

    public void init() {
        stringToBitmapGalleries = new ArrayList<>();
        returnCameraToSchedule = findViewById(R.id.returnCameraToSchedule);
        cameraPathList = getIntent().getStringArrayListExtra("cameraPathList");
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_folder_activity);

        init();
        settingCameraImage();

        // 뷰페이저 '=. 리스트 뷰와 비슷하나 좌우로 스크롤 하여 볼 수 있다.
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        ViewpagerAdapter adapter = new ViewpagerAdapter(stringToBitmapGalleries);
        viewPager.setAdapter(adapter);    // 뷰페이저에 어댑터 등록

        // return ScheduleActivity
        returnCameraToSchedule.setOnClickListener(view ->
            startActivity(new Intent(getApplicationContext(), ScheduleActivity.class)));
    }

    // bitmap 으로 변환하는 메서드
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void settingCameraImage() {
        for (int i = 0; i < cameraPathList.size(); i++) {
            // 파일로 변환하여
            File file = new File(cameraPathList.get(i));
            Bitmap bitmap;
            // 이미지 해독
            ImageDecoder.Source source = ImageDecoder.createSource(
                    getContentResolver(), Uri.fromFile(file));
            // 해독된 비트맵이 null 값이 아니면 viewGallery 에 추가 ViewPager 로 볼 수 있다.
            try {
                bitmap = ImageDecoder.decodeBitmap(source);
                if (bitmap != null) {
                    stringToBitmapGalleries.add(bitmap);
                }
            } catch (IOException e) {
                Log.e(Tag, "Error!!!");
            }
        }
    }
}