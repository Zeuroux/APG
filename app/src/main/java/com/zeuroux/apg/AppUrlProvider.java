package com.zeuroux.apg;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aurora.gplayapi.Constants;
import com.aurora.gplayapi.data.models.File;
import com.aurora.gplayapi.helpers.AuthHelper;
import com.aurora.gplayapi.helpers.PurchaseHelper;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;

public class AppUrlProvider extends ContentProvider {
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        MatrixCursor cursor = new MatrixCursor(new String[]{"name", "url", "size"}), errorCursor = new MatrixCursor(new String[]{"error"});
        errorCursor.addRow(new Object[]{"Error while fetching apk url"});
        if (strings != null && strings.length == 5) {
            try {
                getThread(strings, errorCursor, cursor).join();
            } catch (InterruptedException e) {
                errorCursor.addRow(new Object[]{e.getMessage()});
            }
            return (cursor.getCount() > 0 ? cursor : errorCursor);
        }
        errorCursor.addRow(new Object[]{"No strings provided, Usage: email, token, device, packageName, versionCode"});
        return errorCursor;

    }

    @NonNull
    private Thread getThread(String[] data, MatrixCursor errorCursor, MatrixCursor cursor) {
        String email = data[0], token = data[1], device = data[2], packageName = data[3], versionCode = data[4];
        Thread async = new Thread(() -> {
            try {
                Properties device_properties = new Properties();
                device_properties.load(Objects.requireNonNull(getContext()).getResources().openRawResource(com.aurora.gplayapi.R.raw.class.getField("gplayapi_" + device).getInt(null)));
                if (device_properties.isEmpty()) {
                    errorCursor.addRow(new Object[]{"Device not found"});
                    return;
                }
                List<File> files = new PurchaseHelper(AuthHelper.INSTANCE.build(email, token, AuthHelper.Token.AAS, false, device_properties, Locale.getDefault())).purchase(packageName, Integer.parseInt(versionCode), 1, null, null, null, Constants.PatchFormat.UNKNOWN_5);
                if (files.isEmpty()) {
                    errorCursor.addRow(new Object[]{"Error while fetching apk url (no files)"});
                    return;
                }
                for (File file : files) {
                    cursor.addRow(new Object[]{file.getName(), file.getUrl(), file.getSize()});
                }
            } catch (Exception e) {
                errorCursor.addRow(new Object[]{e.getMessage()});
            }
        });
        async.start();
        return async;
    }

    @Override
    public boolean onCreate() { return false; }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) { return "text/plain"; }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) { return null; }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) { return 0; }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) { return 0; }
}