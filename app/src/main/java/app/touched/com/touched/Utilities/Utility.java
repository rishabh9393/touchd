package app.touched.com.touched.Utilities;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import app.touched.com.touched.Models.MessageModel;
import app.touched.com.touched.Models.User_Details;
import app.touched.com.touched.R;

/**
 * Created by Anshul on 2/26/2018.
 */

public class Utility {
    public static String tryGetValueFromString(String key, JSONObject fromObject) {
        String result = null;
        try {
            result = fromObject.getString(key);
        } catch (JSONException e) {
            result = null;
        }

        return result;


    }

    public static void showToastForContentNotAvailable(Context con) {
        Toast.makeText(con, "Sorry! this content is not available or you may deleted this file from phone folder", Toast.LENGTH_SHORT).show();
    }

    public static String getFullName(User_Details user) {
        return user.getFirst_name() + " " + user.getLast_name();
    }

    public static Bitmap decodeUri(Context context, Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);

        BitmapDrawable img = (BitmapDrawable) context.getResources().getDrawable(R.drawable.ic_launcher_foreground);
        // The new size we want to scale to
        final int REQUIRED_SIZE = img.getBitmap().getHeight();

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight + 32;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;

        int rotate = 0;
        try {

            Cursor cursor = context.getContentResolver().query(selectedImage,
                    new String[]{MediaStore.Images.Media.DATA},
                    null, null, null);

            try {
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        rotate = cursor.getInt(0);
                    } else {
                        rotate = -1;
                    }
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }

            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);
            Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);

    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}


