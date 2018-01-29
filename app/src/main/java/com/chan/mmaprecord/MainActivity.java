package com.chan.mmaprecord;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chan.lib.MmapRecord;

import java.io.File;

public class MainActivity extends AppCompatActivity {

	private MmapRecord mMmapRecord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		File dir = getDir("sd", MODE_PRIVATE);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		final MmapRecord mmapRecord = mMmapRecord = new MmapRecord(new File(dir, "temp_log.log").getAbsolutePath(), new File(dir, "log.log").getAbsolutePath());
		final TextView textView = findViewById(R.id.sample_text);
		Button button = findViewById(R.id.button);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				byte[] array = mmapRecord.read();
				String json = array == null || array.length == 0 ? "" : new String(array);
				if (array != null) {
					mmapRecord.recycle(array);
				}
				String time = System.currentTimeMillis() + "";
				Log.d("chan_debug", time);
				mmapRecord.save((time + "|" + json).getBytes());
				if (!TextUtils.isEmpty(json)) {
					textView.setText(json);
				}
			}
		});

	}

	@Override
	protected void onDestroy() {
		mMmapRecord.release();
		super.onDestroy();
	}
}
