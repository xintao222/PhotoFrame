package com.zwh.photoframe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public class ClassActivity extends Activity {

	public static String AVATAR_FILE_NAME = "class.png";

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
		intent.putExtra("aspectX", 2);  
		intent.putExtra("aspectY", 2);  
		// outputX outputY �ǲü�ͼƬ���  
		intent.putExtra("outputX", 300);  
		intent.putExtra("outputY", 300);
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
			Intent intent = new Intent(ClassAppWidgetProvider.ACTION_APPWIDGET_ONCLICK);
			sendBroadcast(intent);
		}
		this.finish();
	}
}
