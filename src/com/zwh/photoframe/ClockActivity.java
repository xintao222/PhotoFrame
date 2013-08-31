package com.zwh.photoframe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public class ClockActivity extends Activity {

	public static String AVATAR_FILE_NAME = "clock.png";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
				"image/*");
		startActivityForResult(intent, 1);
		super.onCreate(savedInstanceState);
	}

	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
		switch (requestCode) {  
		case 1:  
			// �����ֱ�Ӵ�����ȡ  
			if( data != null ){  
				startPhotoZoom(data.getData());
			}  
			break;  

		case 3:  
			// ȡ�òü����ͼƬ  
			if(data != null){
				setPicToView(data);  
			}  
			break;  
		default:  
			break;  

		}  
		super.onActivityResult(requestCode, resultCode, data);  
	}
	/**  
	 * �ü�ͼƬ����ʵ��  
	 * @param uri  
	 */
	public void startPhotoZoom(Uri uri) {  
		Intent intent = new Intent("com.android.camera.action.CROP");  
		intent.setDataAndType(uri, "image/*");  
		//�������crop=true�������ڿ�����Intent��������ʾ��VIEW�ɲü�  
		intent.putExtra("crop", "true");  
		// aspectX aspectY �ǿ�ߵı���  
		intent.putExtra("aspectX", 1);  
		intent.putExtra("aspectY", 1);  
		// outputX outputY �ǲü�ͼƬ���  
		intent.putExtra("outputX", 200);  
		intent.putExtra("outputY", 200);
		intent.putExtra("return-data", true);  
		startActivityForResult(intent, 3);
	}

	/**  
	 * ����ü�֮���ͼƬ����  
	 * @param picdata  
	 */
	private void setPicToView(Intent picdata) {  
		Bundle extras = picdata.getExtras(); 
		Bitmap avatarBitmap = null;
		if (extras != null) {  
			avatarBitmap = extras.getParcelable("data");
			FileUtils fileUtils = new FileUtils();
			//fileUtils.savePhotoBitmap(avatarBitmap, AVATAR_FILE_NAME);	
			fileUtils.saveFile(avatarBitmap, AVATAR_FILE_NAME);	
			Intent intent = new Intent(ClockAppWidgetProvider.ACTION_APPWIDGET_ONCLICK);
			sendBroadcast(intent);
		}
		this.finish();
	}
	
	/**
     * ת��ͼƬ��Բ��
     * @param bitmap ����Bitmap����
     * @return
     */
    public Bitmap toRoundBitmap(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float roundPx;
            float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
            if (width <= height) {
                    roundPx = width / 2;
                    top = 0;
                    bottom = width;
                    left = 0;
                    right = width;
                    height = width;
                    dst_left = 0;
                    dst_top = 0;
                    dst_right = width;
                    dst_bottom = width;
            } else {
                    roundPx = height / 2;
                    float clip = (width - height) / 2;
                    left = clip;
                    right = width - clip;
                    top = 0;
                    bottom = height;
                    width = height;
                    dst_left = 0;
                    dst_top = 0;
                    dst_right = height;
                    dst_bottom = height;
            }
             
            Bitmap output = Bitmap.createBitmap(width,
                            height, Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
             
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
            final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
            final RectF rectF = new RectF(dst);

            paint.setAntiAlias(true);
             
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, src, dst, paint);
            return output;
    }
}
