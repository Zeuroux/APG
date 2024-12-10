idk
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
