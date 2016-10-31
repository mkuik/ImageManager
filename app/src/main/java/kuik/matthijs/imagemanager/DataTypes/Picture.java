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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by Matthijs on 05/10/2016.
 */

public class Picture extends Size implements Serializable {

    private transient final static int THUMBSIZE = 100;
    private String source;
    private String details = "";
    private int id = -1;
    private HueHistogramData colors = null;

    public Picture(Uri uri, int id, int width, int height) {
        super(width, height);
        source = uri.toString();
        this.id = id;
    }

    private String getMetaFilename() {
        return id + ".meta";
    }

    public String getFilename() {
        return getSource().getLastPathSegment();
    }

    private void loadColorsFromMemory(Context context) throws IOException, ClassNotFoundException {
        FileInputStream fis = context.openFileInput(getMetaFilename());
        ObjectInputStream in = new ObjectInputStream(fis);
        colors = (HueHistogramData) in.readObject();
        in.close();
        fis.close();
    }

    private void loadColorsFromBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            colors = new HueHistogramData();
            float[] hue_histogram = new float[360];
            for (int y = 0; y != bitmap.getHeight(); ++y) {
                for (int x = 0; x != bitmap.getWidth(); ++x) {
                    final int pixel = bitmap.getPixel(x, y);
                    final float[] hsv = new float[3];
                    Color.colorToHSV(pixel, hsv);
                    final float score = hsv[1] * hsv[2];
                    hue_histogram[(int) hsv[0]] += score;
                }
            }
            //hue_histogram = groupHistogramPeaks(hue_histogram);
            for (short hue = 0; hue != hue_histogram.length; ++hue) {
                float count = (float) Math.log(hue_histogram[hue]);
                if (count > 0) {
                    colors.add(new Hue(hue, count));
                }
            }
            Collections.sort(colors, new Hue.SortByCount());
        }
    }

    private void saveColorsToMemory(Context context) throws IOException {
        FileOutputStream fos = context.openFileOutput(getMetaFilename(), Context.MODE_PRIVATE);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(colors);
        out.close();
        fos.close();
    }

    public int getId() {
        return id;
    }

    public float getMaxCount() {
        return colors.getMax().getCount();
    }

    public double getHueDistance(short hue) {
//        Log.i("Picture", "getHueDistance( " + hue + " )");
        double maxScore = 0;
        for (int i = 0; i != getColors().size(); ++i) {
            Hue item = getColors().get(i);
            double distance = getHueDelta(item.getHue(), hue);
            double significance = item.getCount() / getMaxCount();
            double score = colorDeltaFormula(distance) * significance;
            //if (score > maxScore) maxScore = score;
            maxScore += score;
//            Log.i("Picture", String.format(Locale.ENGLISH, "%3d  %.3f x %.3f = %.3f",
//                    item.getHue(), colorDeltaFormula(distance), significance, score));
        }
//        Log.i("Picture", toString() + " score:" + maxScore);
        return maxScore;
    }

    private static double colorDeltaFormula(double x) {
        return Math.pow(1 - x, 10);
    }

    private static double getHueDelta(short A, short B) {
        if (A == B) {
            return 0;
        } else if (A < B) {
            return Math.min(B - A, 360 + A - B) / 360D;
        } else {
            return Math.min(A - B, 360 + B - A) / 360D;
        }
    }

    public Bitmap getThumbnail(Context context) throws IOException {
        final BitmapFactory.Options options = new BitmapFactory.Options();

        InputStream inputStream = context.getContentResolver().openInputStream(getSource());
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);
        if (inputStream != null) inputStream.close();

        inputStream = context.getContentResolver().openInputStream(getSource());
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(options, THUMBSIZE, THUMBSIZE);
        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        if (inputStream != null) inputStream.close();

        return bitmap;
    }

    public Bitmap getBitmap(Context context) throws IOException {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        InputStream inputStream = context.getContentResolver().openInputStream(getSource());
        options.inJustDecodeBounds = false;
        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        if (inputStream != null) inputStream.close();

        Log.d("Bitmap size", "" + options.outWidth + "x" + options.outHeight);
        return bitmap;
    }

    public HueHistogramData getColors() {
        return colors;
    }

    public Uri getSource() {
        return Uri.parse(source);
    }

    public void init(final Context context) throws IOException, NullPointerException {
        Log.d("init", source);
        try {
            loadColorsFromMemory(context);
        } catch (IOException | ClassNotFoundException e){
            initDimensions(context);
            loadColorsFromBitmap(getThumbnail(context));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        saveColorsToMemory(context);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private static float[] groupHistogramPeaks(float[] histogram) {
        final float[] groupedHistogram = new float[histogram.length];
        int xGroupStart = 0;
        int sumGroupCount = (int) histogram[0];
        boolean peakLocated = false;
        for (short x = 1; x != histogram.length; ++x) {
            final int y0 = (int)histogram[x-1];
            final int y1 = (int)histogram[x];

            Log.i("grouping", String.format(Locale.ENGLISH, "x:%d y:(%d -> %d) peak:%b", x, y0, y1, peakLocated));
            if (y0 > y1) {
                // decrease
                peakLocated = true;
                sumGroupCount += y1;
            } else if (peakLocated) {
                // equal or increase after group peak

                // analyse
                final int[] group = new int[sumGroupCount];
                for (int groupI = 0, groupX = xGroupStart; groupX != x; ++groupX) {
                    final int xRepeatCount = (int) histogram[groupX];
                    for (int groupXI = 0; groupXI != xRepeatCount; ++groupXI) {
                        Log.i("grouping", String.format(Locale.ENGLISH, "I:%d X:%d XI:%d", groupI, groupX, groupXI));
                        group[groupI] = groupX;
                        ++groupI;
                    }
                }
                final int groupX = getMedian(group);
                groupedHistogram[groupX] = sumGroupCount;

                xGroupStart = x;
                sumGroupCount = y1;
                peakLocated = false;
            } else {
                // equal or increase to group peak
                sumGroupCount += y1;
            }
        }
        return groupedHistogram;
    }

    private static int getMedian(int[] values) {
        int median;
        if (values.length % 2 == 0)
            median = (values[values.length/2] + values[values.length/2 - 1])/2;
        else
            median = values[values.length/2];
        return median;
    }

    private void initDimensions(Context context) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(getSource());
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);
        setWidth(options.outWidth);
        setHeight(options.outHeight);
    }

    private static int calculateInSampleSize(
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
        return String.format(Locale.ENGLISH, "Picture{%d, %s, '%s', %s}", id, source, details, colors);
    }
}
