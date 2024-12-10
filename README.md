The app must be installed to make the code below work

usage:
```
Uri uri = Uri.parse("content://com.zeuroux.apg.app");
String[] projection = new String[]{"email@example.com", "token", "deviceName", "packageName", "versionCode"};
try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
    if (cursor != null) {
        while (cursor.moveToNext()) {
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                System.out.println(cursor.getColumnName(i) + ": " + cursor.getString(i));
            }
        }
    } else {
        System.out.println("No data returned for URI: " + uri);
    }
} catch (Exception e) {
    e.printStackTrace();
}
```
![image](https://github.com/user-attachments/assets/ffacb9a6-8e95-4a67-8250-88ab1a3d95f1)

## Credits

[GPlayApi](https://gitlab.com/AuroraOSS/gplayapi)
