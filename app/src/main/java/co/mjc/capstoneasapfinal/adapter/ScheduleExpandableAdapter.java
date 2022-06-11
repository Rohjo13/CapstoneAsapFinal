package co.mjc.capstoneasapfinal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import co.mjc.capstoneasapfinal.R;
import co.mjc.capstoneasapfinal.pojo.Schedule;

public class ScheduleExpandableAdapter extends BaseExpandableListAdapter {

    Context mContext;
    List<Schedule> scheduleArrayList;
    LayoutInflater mLayoutInflater = null;
    List<Integer> imageViews;
    View goToHereView;
    ChildHolder childHolder;
    CheckBox deleteSchedule;

    public ScheduleExpandableAdapter(Context mContext, ChildHolder childHolder,
                                     List<Schedule> scheduleArrayList, List<Integer> imageViews
            , View goToHereView) {
        this.mContext = mContext;
        this.scheduleArrayList = scheduleArrayList;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.imageViews = imageViews;
        this.childHolder = childHolder;
        this.goToHereView = goToHereView;
    }

    @Override
    public int getGroupCount() {
        return scheduleArrayList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        System.out.println("getChildrenCount");
        // 제발 1 바꾸지마 -> 숫자 올리면 중복해서 여러 개 생김
//        return imageViews.size();
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        System.out.println("getGroup");
        return scheduleArrayList.get(i);
    }

    @Override
    public Object getChild(int gPosition, int cPosition) {
        System.out.println("getChild");
        return imageViews.get(cPosition);
    }

    @Override
    public long getGroupId(int gPosition) {
        return gPosition;
    }

    @Override
    public long getChildId(int gPosition, int cPosition) {
        return cPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View notView, ViewGroup viewGroup) {
        View view = mLayoutInflater.inflate(R.layout.schedule_group_layout, null);
        TextView textView = view.findViewById(R.id.parentLectureName);
        TextView scheduleDate = view.findViewById(R.id.scheduleDate);
        deleteSchedule = view.findViewById(R.id.deleteSchedule);
        scheduleDate.setText(scheduleArrayList.get(i).getDayOTW().name());
        textView.setText(scheduleArrayList.get(i).getLecName());
        return view;
    }


    public CheckBox getDeleteSchedule() {
        return deleteSchedule;
    }

    @Override
    public View getChildView(int gPos, int cPos, boolean b, View notView, ViewGroup viewGroup) {
        return goToHereView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
