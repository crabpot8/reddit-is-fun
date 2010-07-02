package com.andrewshu.android.reddit.shareImage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andrewshu.android.reddit.R;

public class Test extends Activity {
	private PreviewAsyncTask previewAsyncTask;
	private UploadAsyncTask uploadAsyncTask;
	private ProgressBar uploadProgress_;

	@Override
	protected void onCreate(Bundle saved) {
		super.onCreate(saved);

		setContentView(R.layout.imgur_upload);

		previewAsyncTask = new PreviewAsyncTask(this);
		uploadAsyncTask = new UploadAsyncTask(this);

		uploadProgress_ = (ProgressBar) findViewById(R.id.imgur_upload_progress);
		uploadProgress_.setMax(100);
		uploadProgress_.setIndeterminate(false);

		Log.i("rf", "Started them both");
	}

	public void onPostUpload(String url) {
		Log.i("rf", "Upload returned");

		TextView tv = (TextView) findViewById(R.id.imgur_status);
		tv.setText(url);
		tv.invalidate();
	}

	public void onUploadStateProgress(String status) {
		TextView tv = (TextView) findViewById(R.id.imgur_status);
		tv.setText(status);
		tv.invalidate();
	}

	public void onPostPreview(Bitmap previewImage) {
		Log.i("rf", "Preview returned");

		ProgressBar pb = (ProgressBar) findViewById(R.id.imgur_preview_progress);
		pb.setEnabled(false);
		pb.setVisibility(View.INVISIBLE);
		pb.invalidate();

		ImageView iv = (ImageView) findViewById(R.id.imgur_preview);
		iv.setImageBitmap(previewImage);
		iv.invalidate();
	}

	@Override
	protected void onStart() {
		super.onStart();

		Uri uri = (Uri) getIntent().getExtras().get(Intent.EXTRA_STREAM);
		uploadAsyncTask.execute(uri);
		previewAsyncTask.execute(uri);
	}

	int i = 0;

	public void onUploadProgress(double progress) {
		// TODO - fix this
		uploadProgress_.setProgress((int) progress);
		//Log.i("rf", "At percent " + progress);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		uploadAsyncTask.cancel(true);
		previewAsyncTask.cancel(true);
	}

	public void onUploadError(String string) {
		Toast.makeText(this, string, Toast.LENGTH_LONG).show();
	}
}
