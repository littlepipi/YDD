package app.example.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.yixuan.ProgressDialogHandle;

/**
 * ��ͼԤ�� ����������һ���������һ��Ӧ�ã����˷�����״̬�����Ŷ����кܶ���ͼ�� ���ǵ��ͼƬʱ���������ͼ�����Ŵ�ͼһ����ԷŴ󣬷Ŵ󵽳�����Ļ��
 * �����ƶ� ��Ҫ��activity��Intent���������� ���������url ��ͼ���ص�ַ smallPath ����ͼ���ڱ��صĵ�ַ
 * 
 * @author Administrator
 * 
 */
public class ImageActivity extends Activity {
	private ImageView zoomView;
	private Context ctx;
	private GestureDetector gestureDetector;
	String image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_picture_preview);
		ctx = this;
		zoomView = (ImageView) findViewById(R.id.zoom_view);

		Intent intent = getIntent();
		image = intent.getStringExtra("image");
		
//		System.out.println("ͼƬ��ַ--------"+image);
		/* ��ͼ�����ص�ַ */
		/* ����ͼ�洢�ڱ��صĵ�ַ */
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		final int widthPixels = metrics.widthPixels;
		final int heightPixels = metrics.heightPixels;
		File bigPicFile = new File(getLocalPath(image));
		if (bigPicFile.exists()) {
			/* ����Ѿ����ع���,ֱ�Ӵӱ����ļ��ж�ȡ */
			zoomView.setImageBitmap(zoomBitmap(
					BitmapFactory.decodeFile(getLocalPath(image)), widthPixels,
					heightPixels));
		} else if (!TextUtils.isEmpty(image)) {
			ProgressDialogHandle handle = new ProgressDialogHandle(this) {
				Bitmap bitmap = null;

				@Override
				public void handleData() throws JSONException, IOException,
						Exception {
					bitmap = getBitMapFromUrl(image);
					if (bitmap != null) {
						savePhotoToSDCard(
								zoomBitmap(bitmap, widthPixels, heightPixels),
								getLocalPath(image));
					}
				}

				@Override
				public String initialContent() {
					return null;
				}

				@Override
				public void updateUI() {
					if (bitmap != null) {
						// recycle();

						zoomView.setImageBitmap(zoomBitmap(bitmap, widthPixels,
								heightPixels));
						
					} else {
						Toast.makeText(ctx, "����ʧ�ܣ�", Toast.LENGTH_LONG).show();
					}
				}

			};
//			if (TextUtils.isEmpty(smallPath) && identify != -1) {
//				handle.setBackground(BitmapFactory.decodeResource(
//						getResources(), identify));
//			} else {
//				handle.setBackground(BitmapFactory.decodeFile(smallPath));
//			}
			handle.show();
		}
		gestureDetector = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						float x = e2.getX() - e1.getX();
						if (x > 0) {
							prePicture();
						} else if (x < 0) {

							nextPicture();
						}
						return true;
					}
				});
	}

	protected void nextPicture() {
		// TODO Auto-generated method stub

	}

	protected void prePicture() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		super.onResume();
		// recycle();
	}

	public void recycle() {
		if (zoomView != null && zoomView.getDrawable() != null) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) zoomView
					.getDrawable();
			if (bitmapDrawable != null && bitmapDrawable.getBitmap() != null
					&& !bitmapDrawable.getBitmap().isRecycled())

			{
				bitmapDrawable.getBitmap().recycle();
			}
		}
	}

	public Bitmap getBitMapFromUrl(String url) {
		Bitmap bitmap = null;
		URL u = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		try {
			u = new URL(url);
			conn = (HttpURLConnection) u.openConnection();
			is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			conn.disconnect();
		}
		return bitmap;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

	/**
	 * Resize the bitmap
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
		if (bitmap == null)
			return bitmap;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) width / w);
		float scaleHeight = ((float) height / h);
		if (scaleWidth < scaleHeight) {
			matrix.postScale(scaleWidth, scaleWidth);
		} else {
			matrix.postScale(scaleHeight, scaleHeight);
		}
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		return newbmp;
	}

	public static String getLocalPath(String url) {
		String fileName = "temp.png";
		if (url != null) {
			if (url.contains("/")) {
				fileName = url.replaceAll(".*?/(?=\\d+-\\d+-\\d+/)","").replace("/", "");
			}
			if (fileName != null && fileName.contains("&")) {
				fileName = fileName.replaceAll("&", "");
			}
			if (fileName != null && fileName.contains("%")) {
				fileName = fileName.replaceAll("%", "");
			}
			// if (fileName != null && fileName.contains("?")) {
			// fileName = fileName.replaceAll("?", "");
			// }
		}
		return Environment.getExternalStorageDirectory() + "/" + fileName;
	}

	/**
	 * Save image to the SD card
	 * 
	 * @param photoBitmap
	 * @param photoName
	 * @param path
	 */
	public static void savePhotoToSDCard(Bitmap photoBitmap, String fullPath) {
		if (checkSDCardAvailable()) {
			File file = new File(fullPath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}

			File photoFile = new File(fullPath);
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap != null) {
					if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100,
							fileOutputStream)) {
						fileOutputStream.flush();
					}
				}
			} catch (FileNotFoundException e) {
				photoFile.delete();
				e.printStackTrace();
			} catch (IOException e) {
				photoFile.delete();
				e.printStackTrace();
			} finally {
				try {
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					// if (photoBitmap != null && !photoBitmap.isRecycled()) {
					// photoBitmap.recycle();
					// }
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static boolean checkSDCardAvailable() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ImageActivity.this.finish();
		}

		return false;

	}

}
