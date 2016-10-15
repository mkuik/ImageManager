package kuik.matthijs.imagemanager.DataTypes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Matthijs on 05/10/2016.
 */

public class Picture {

    final static int THUMBSIZE = 60;

    private Size size;
    private Uri source;
    private Context context;
    private List<Hue> colors = new ArrayList<>();

    public Picture(Context context, Uri uri) {
        source = uri;
        this.context = context;
    }

    public Bitmap getThumbnail() throws IOException {
        final BitmapFactory.Options options = new BitmapFactory.Options();

        InputStream inputStream = context.getContentResolver().openInputStream(source);
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);
        inputStream.close();

        inputStream = context.getContentResolver().openInputStream(source);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(options, THUMBSIZE, THUMBSIZE);
        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        inputStream.close();

        Log.d("Thumbnail size", "" + options.outWidth + "x" + options.outHeight);
        return bitmap;
    }

    public Bitmap getBitmap() throws IOException {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        InputStream inputStream = context.getContentResolver().openInputStream(source);
        options.inJustDecodeBounds = false;
        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        inputStream.close();

        Log.d("Bitmap size", "" + options.outWidth + "x" + options.outHeight);
        return bitmap;
    }

    public Size getSize() {
        return size;
    }

    public List<Hue> getColors() {
        return colors;
    }

    public Uri getSource() {
        return source;
    }

    public void init() throws IOException {
        initDimensions();
        Bitmap bitmap = getThumbnail();

        final short[] hue_histogram = new short[360];
        for (int y = 0; y != bitmap.getHeight(); ++y) {
            for (int x = 0; x != bitmap.getWidth(); ++x) {
                final int pixel = bitmap.getPixel(x, y);
                final float[] hsv = new float[3];
                Color.colorToHSV(pixel, hsv);
                final short hue = (short) hsv[0];
                hue_histogram[hue]++;
            }
        }
        for (short i = 0; i != hue_histogram.length; ++i) {
            short count = hue_histogram[i];
            if (count > 0) {
                colors.add(new Hue(i, count));
            }
        }
        Collections.sort(colors, new Hue.SortByCount());
    }

    private void initDimensions() throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(source);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);
        size = new Size(options.outWidth, options.outHeight);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "size=" + size +
                ", source=" + source +
                '}';
    }
}
