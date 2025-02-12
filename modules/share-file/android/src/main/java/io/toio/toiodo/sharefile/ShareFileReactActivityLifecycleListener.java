package io.toio.toiodo.sharefile;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import expo.modules.core.interfaces.ReactActivityLifecycleListener;

public class ShareFileReactActivityLifecycleListener implements ReactActivityLifecycleListener {
    private Activity context;

    @Override
    public void onCreate(Activity activity, Bundle savedInstanceState) {
        ReactActivityLifecycleListener.super.onCreate(activity, savedInstanceState);
        context = activity;
        // Get import file
        getImportFile(activity.getIntent());
    }

    @Override
    public boolean onNewIntent(Intent intent) {
        // Get import file
        getImportFile(intent);
        return ReactActivityLifecycleListener.super.onNewIntent(intent);
    }

    private void getImportFile(Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            if (Intent.ACTION_SEND.equals(action)) {
                Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (uri != null) {
                    intent.removeExtra(Intent.EXTRA_STREAM);
                    getImportFile(uri);
                }
            } else if (Intent.ACTION_VIEW.equals(action)) {
                Uri uri = intent.getData();
                if (uri != null && ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
                    intent.setData(null);
                    getImportFile(uri);
                }
            }
        }
    }

    private void getImportFile(Uri uri) {
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            new Thread(() -> {
                JSONObject param = new JSONObject();
                try {
                    try (InputStream inputStream = context.getContentResolver().openInputStream(uri)) {
                        File outputFile = getOutputFile(uri);
                        assert inputStream != null;
                        inputStreamToFile(inputStream, outputFile);
                        param.put("path", outputFile.getPath());
                    } catch (Exception e) {
                        param.put("error", e.getMessage());
                    }
                } catch (Exception e) {
                    try {
                        param.put("error", e.getMessage());
                    } catch (JSONException ex) {
                        throw new RuntimeException(ex);
                    }
                } finally {
                    context.runOnUiThread(() -> ShareFileManager.instance.sendEvent(param));
                }
            }).start();
        }
    }

    private File getOutputFile(Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
        if (index >= 0 && !TextUtils.isEmpty(cursor.getString(index))) {
            String name = cursor.getString(index);
            File file = new File(context.getCacheDir(), name);
            if (!file.exists()) {
                cursor.close();
                return file;
            }

            int point = name.lastIndexOf(".");
            String base = (point > 0) ? name.substring(0, point) : name;
            String ext = (point > 0) ? name.substring(point) : "";
            for (int i = 1; i < Integer.MAX_VALUE; i++) {
                file = new File(context.getCacheDir(), (base + "-" + i + ext));
                if (!file.exists()) {
                    cursor.close();
                    return file;
                }
            }
        }

        cursor.close();
        return new File(context.getCacheDir(), String.valueOf(System.currentTimeMillis()));
    }

    private void inputStreamToFile(InputStream inputStream, File outputFile) throws IOException {
        try (OutputStream outputStream = new FileOutputStream(outputFile)) {
            int size = 0;
            byte[] buffer = new byte[8192];
            while ((size = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, size);
            }
        }
    }
}

