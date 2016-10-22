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
    private short maxCount = 0;
    private String details = "";

    public Picture(Context context, Uri uri) {
        source = uri;
        this.context = context;
    }

    public double getHueDistance(short hue) {
//        Log.i("Picture", "getHueDistance( " + hue + ")");
        double maxScore = 0;
        for (int i = 0; i != getColors().size(); ++i) {
            Hue item = getColors().get(i);
            double distance;
            if (item.getHue() == hue) {
                distance = 0;
            } else if (item.getHue() < hue) {
                distance = Math.min(hue - item.getHue(), 360 + item.getHue() - hue) / 360D;
            } else {
                distance = Math.min(item.getHue() - hue, 360 + hue - item.getHue()) / 360D;
            }
            double significance = item.getCount() / (double)maxCount;
            double score = colorDeltaFormula(distance) * significance;
//            Log.i("Picture", item.toString() + " distance:" + distance + " significance:" + significance + " score:" + score);
            if (score > maxScore) maxScore = score;
        }
        Log.i("Picture", toString() + " score:" + maxScore);
        return maxScore;
    }

    private double colorDeltaFormula(double x) {
        return (1 - x) * 4 - 3;
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
        if (colors.isEmpty()) {
            Bitmap bitmap = getThumbnail();
            int sumScore = 0;

            final float[] hue_histogram = new float[360];
            for (int y = 0; y != bitmap.getHeight(); ++y) {
                for (int x = 0; x != bitmap.getWidth(); ++x) {
                    final int pixel = bitmap.getPixel(x, y);
                    final float[] hsv = new float[3];
                    Color.colorToHSV(pixel, hsv);
                    final float score = hsv[1] * hsv[2];
                    hue_histogram[(int) hsv[0]] += score;
                    sumScore += score;
                }
            }
            for (short i = 0; i != hue_histogram.length; ++i) {
                short count = (short)hue_histogram[i];
                if (count > 0) {
                    colors.add(new Hue(i, count));
                }
                if (count > maxCount) maxCount = count;
            }
            Collections.sort(colors, new Hue.SortByCount());
        }
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

    public void setDetails(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "size=" + size +
                ", source=" + source +
                '}';
    }
}
