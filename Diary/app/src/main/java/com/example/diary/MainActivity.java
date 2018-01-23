package com.example.diary;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.media.MediaBrowserCompatUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ViewStub mEmptyStub;
    private ListView mDiaryList;
    private DiaryAdapter mDiaryAdapter;
    private FloatingActionButton menuFAB,topFab,centerFab,leftFab;
    private PopupWindow mPopupWindow;
    private boolean isMenuOpen = false;
    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCursor = getContentResolver().query(Fields.DiaryColumns.CONTENT_URI,null,null,null,null);
        mDiaryAdapter.notifyDataSetChanged();
    }

    private void initView(){
        mEmptyStub = (ViewStub)findViewById(R.id.emptyStub);
        mDiaryList = (ListView)findViewById(R.id.diaryList);
        mDiaryAdapter = new DiaryAdapter(MainActivity.this);
        mDiaryList.setAdapter(mDiaryAdapter);
        menuFAB = (FloatingActionButton)findViewById(R.id.mainFab);
        topFab = (FloatingActionButton)findViewById(R.id.topFab);
        centerFab = (FloatingActionButton)findViewById(R.id.centerFab);
        leftFab = (FloatingActionButton)findViewById(R.id.leftFab);
        leftFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,WriteDiaryActivity.class));
            }
        });
        topFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"设置密码",Toast.LENGTH_LONG).show();
            }
        });
        menuFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMenuOpen){
                    hideMenu();
                }else{
                    showMenu();
                }
//                if(mPopupWindow != null && mPopupWindow.isShowing()){
//                    TranslateAnimation animation = new TranslateAnimation(0,0,-60,0);
//                    animation.setDuration(500);
//                    animation.setFillAfter(true);
//                    menuFAB.startAnimation(animation);

//                    ObjectAnimator animator = ObjectAnimator.ofFloat(menuFAB,"translationY",-60,0);
//                    animator.setDuration(500);
//                    animator.start();
//                    mPopupWindow.dismiss();
//                }else{
                    //控件并没有真正的平移，控件的点击事件还在原地
//                    TranslateAnimation animation = new TranslateAnimation(0,0,0,-60);
//                    animation.setDuration(500);
//                    animation.setFillAfter(true);
//                    menuFAB.startAnimation(animation);
                    //控件位置真正改变
//                    ObjectAnimator animator = ObjectAnimator.ofFloat(menuFAB,"translationY",0.0F,-60.0F);
//                    animator.setDuration(500);
//                    animator.start();
//                    showPopupWindow();
//                }
            }
        });
    }

    private void showMenu(){
        isMenuOpen = true;
        int x = (int) menuFAB.getX();
        int y = (int)menuFAB.getY();
        ValueAnimator animatorLeft = ValueAnimator.ofInt(x,x-200);
        animatorLeft.setDuration(300);
        animatorLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int l = (int) valueAnimator.getAnimatedValue();
                int t = (int) leftFab.getY();
                int r = leftFab.getWidth() + l;
                int b = leftFab.getHeight() + t;
                leftFab.layout(l,t,r,b);
            }
        });

        ValueAnimator animatorCenterX = ValueAnimator.ofInt(x,x-140);
        animatorCenterX.setDuration(300);
        ValueAnimator animatorCenterY = ValueAnimator.ofInt(y,y-140);
        animatorCenterY.setDuration(300);
        animatorCenterX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int l = (int)valueAnimator.getAnimatedValue();
                int t = (int) centerFab.getY();
                int r = centerFab.getWidth() + l;
                int b = centerFab.getHeight() + t;
                centerFab.layout(l,t,r,b);
            }
        });
        animatorCenterY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int l = (int) centerFab.getX();
                int t = (int)valueAnimator.getAnimatedValue();
                int r = centerFab.getWidth() + l;
                int b = centerFab.getHeight() + t;
                centerFab.layout(l,t,r,b);
            }
        });
        ValueAnimator animatorTop = ValueAnimator.ofInt(y,y-200);
        animatorTop.setDuration(300);
        animatorTop.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int l = (int) topFab.getX();
                int t = (int)valueAnimator.getAnimatedValue();
                int r = topFab.getWidth() + l;
                int b = topFab.getHeight() + t;
                topFab.layout(l,t,r,b);
            }
        });
        animatorLeft.start();
        animatorCenterX.start();
        animatorCenterY.start();
        animatorTop.start();
    }
    private void hideMenu(){
        isMenuOpen = false;
        int x = (int) leftFab.getX();
        ValueAnimator animatorLeft = ValueAnimator.ofInt(x,(int) menuFAB.getX());
        animatorLeft.setDuration(300);
        animatorLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int l = (int)valueAnimator.getAnimatedValue();
                int t = (int) leftFab.getY();
                int r = leftFab.getWidth() + l;
                int b = leftFab.getHeight() + t;
                leftFab.layout(l,t,r,b);
            }
        });
        x = (int) centerFab.getX();
        int y = (int)centerFab.getY();
        ValueAnimator animatorCenterX = ValueAnimator.ofInt(x,(int)menuFAB.getX());
        animatorCenterX.setDuration(300);
        animatorCenterX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int l = (int)valueAnimator.getAnimatedValue();
                int t = (int) centerFab.getY();
                int r = centerFab.getWidth() + l;
                int b = centerFab.getHeight() + t;
                centerFab.layout(l,t,r,b);
            }
        });
        ValueAnimator animatorCenterY = ValueAnimator.ofInt(y,(int) menuFAB.getY());
        animatorCenterY.setDuration(300);
        animatorCenterY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int l = (int)centerFab.getX();
                int t = (int)valueAnimator.getAnimatedValue();
                int r = centerFab.getWidth() + l;
                int b = centerFab.getHeight() + t;
                centerFab.layout(l,t,r,b);
            }
        });
        y = (int)topFab.getY();
        ValueAnimator animatorTop = ValueAnimator.ofInt(y,(int)menuFAB.getY());
        animatorTop.setDuration(300);
        animatorTop.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int t = (int)valueAnimator.getAnimatedValue();
                int l = (int) topFab.getX();
                int r = topFab.getWidth() + l;
                int b = topFab.getHeight() + t;
                topFab.layout(l,t,r,b);
            }
        });
        animatorLeft.start();
        animatorCenterX.start();
        animatorCenterY.start();
        animatorTop.start();
    }

    private void showPopupWindow(){
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.diary_menu_layout,null);
        TextView addNewDiary = (TextView)view.findViewById(R.id.addNewDiary);
        TextView setPassword = (TextView)view.findViewById(R.id.setPassword);
        mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.showAtLocation(view, Gravity.BOTTOM,0,0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mPopupWindow = null;
            }
        });
    }

    public class DiaryAdapter extends BaseAdapter{
        private Context mContext;
        public DiaryAdapter(Context context){
            this.mContext = context;
        }
        @Override
        public Object getItem(int i) {
            if(mCursor != null &&  mCursor.moveToPosition(i)){
                return mCursor;
            }else{
                return null;
            }

        }

        @Override
        public long getItemId(int i) {
            if(mCursor != null){
                return i;
            }else{
                return 0;
            }
        }

        @Override
        public int getCount() {
            if(mCursor != null){
                return mCursor.getCount();
            }else{
                return 0;
            }
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if(convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.diary_item,null,false);
                holder = new ViewHolder(convertView);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            if(holder != null){
                if(mCursor != null && mCursor.getCount() > 0){
                    mCursor.moveToPosition(i);
                    final String content = mCursor.getString(mCursor.getColumnIndexOrThrow(Fields.DiaryColumns.CONTENT));
                    String title = mCursor.getString(mCursor.getColumnIndexOrThrow(Fields.DiaryColumns.TITLE));
                    String time = mCursor.getString(mCursor.getColumnIndexOrThrow(Fields.DiaryColumns.TIME));
                    holder.diaryTitle.setText(title);
                    holder.diaryTime.setText(time);
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(MainActivity.this,content,Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            return convertView;
        }
        class ViewHolder{
            TextView diaryTitle;
            TextView diaryTime;
            public ViewHolder(View view){
                diaryTitle = (TextView)view.findViewById(R.id.diaryTitle);
                diaryTime = (TextView)view.findViewById(R.id.diaryTime);
                view.setTag(this);
            }
        }
    }
}
