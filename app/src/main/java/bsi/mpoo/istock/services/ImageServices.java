package bsi.mpoo.istock.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.media.ExifInterface;
import java.io.ByteArrayOutputStream;

public class ImageServices {

    public ImageServices(){}

    public byte[] imageToByte(Bitmap imageBitmap){
        byte[] imageByte = null;
        if (imageBitmap != null){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,
                    Constants.Image.QUALITY,out);
            imageByte = out.toByteArray();
        }
        return imageByte;
    }

    public Bitmap byteToImage(byte[] imageByte){
        Bitmap imageBitmap = null;
        if (imageByte != null){
            imageBitmap = BitmapFactory.decodeByteArray(imageByte,
                    Constants.Image.OFFSET, imageByte.length);
        }
        return imageBitmap;
    }

    public byte[] reduceBitmap(byte[] imageByte){
        byte[] resultImageByte = null;

        if (imageByte != null){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(imageByte, Constants.Image.OFFSET, imageByte.length, options);
            int scale = Constants.Image.SCALE_BASE;
            scale = reduceCalc(options, scale);
            BitmapFactory.Options options2 = new BitmapFactory.Options();
            options2.inSampleSize = scale;
            Bitmap resultBitmap = BitmapFactory.decodeByteArray(imageByte, Constants.Image.OFFSET, imageByte.length,options2);
            resultImageByte = imageToByte(resultBitmap);
        }
        return resultImageByte;
    }

    private int reduceCalc(BitmapFactory.Options options, int scale) {
        while (options.outWidth/scale/2 >= Constants.Image.REQUIRED_SIZE &&
                options.outHeight/scale/2 >= Constants.Image.REQUIRED_SIZE){
            scale *= 2;
        }
        return scale;
    }

    public Bitmap rotate(Bitmap bitmap, int codeOrientation) {
        int angle = Constants.Image.SAFE_ANGLE;
        switch (codeOrientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                angle = 0;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                angle = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                angle = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                angle = 270;
                break;
        }

        if (angle == Constants.Image.SAFE_ANGLE){
            return bitmap;
        }
        return rotateHelper(bitmap, angle);
    }
    private Bitmap rotateHelper(Bitmap bitmap, int angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap,
                Constants.Image.FIRST_PIXEL_CORDINATE_SOURCE_X,
                Constants.Image.FIRST_PIXEL_CORDINATE_SOURCE_Y,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


}
