package com.example.diary;

import android.app.Dialog;
import android.content.ContentValues;
import android.media.midi.MidiDeviceInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WriteDiaryActivity extends AppCompatActivity {
    private TextView editDiaryTitle;
    private TextView editDiaryContent;
    private Button saveBtn;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);
        initView();
    }
    private void initView(){
        editDiaryTitle = (TextView)findViewById(R.id.editDiaryTitle);
        editDiaryContent = (TextView)findViewById(R.id.editDiaryContent);
        saveBtn = (Button)findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              showDialog();
            }
        });
    }
    private void showDialog(){
        mDialog = new Dialog(WriteDiaryActivity.this);
        View view = LayoutInflater.from(WriteDiaryActivity.this).inflate(R.layout.save_diary_dialog,null);
        TextView cancelTv = (TextView)view.findViewById(R.id.cancelTv);
        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        TextView confirmTv = (TextView)view.findViewById(R.id.confirmTv);
        confirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(Fields.DiaryColumns.TITLE,editDiaryTitle.getText().toString());
                values.put(Fields.DiaryColumns.CONTENT,editDiaryContent.getText().toString());
               WriteDiaryActivity.this.getContentResolver().insert(Fields.DiaryColumns.CONTENT_URI,values);
                mDialog.dismiss();
                finish();
            }
        });
        mDialog.setContentView(view);
        mDialog.show();
    }
}
