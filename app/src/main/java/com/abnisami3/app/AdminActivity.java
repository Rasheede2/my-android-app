package com.abnisami3.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class AdminActivity extends Activity {
    private static final int REQ_IMAGES = 201;
    private final ArrayList<String> items = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(18, 24, 18, 12);

        TextView title = new TextView(this);
        title.setText("لوحة الأدوات");
        title.setTextSize(24);
        title.setGravity(Gravity.CENTER);
        root.addView(title, new LinearLayout.LayoutParams(-1, -2));

        Button images = new Button(this);
        images.setText("عرض الصور التي يسمح النظام بالوصول إليها");
        images.setOnClickListener(v -> requestImagesPermission());
        root.addView(images);

        Button camera = new Button(this);
        camera.setText("فتح الكاميرا");
        camera.setOnClickListener(v -> openCamera());
        root.addView(camera);

        TextView note = new TextView(this);
        note.setText("ملاحظة: لا يمكن للتطبيق قراءة رسائل واتساب الخاصة أو تجاوز حماية أندرويد. الصور المعروضة تعتمد على صلاحية النظام.");
        note.setPadding(0, 8, 0, 8);
        root.addView(note);

        ListView list = new ListView(this);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        list.setAdapter(adapter);
        root.addView(list, new LinearLayout.LayoutParams(-1, 0, 1));
        setContentView(root);
    }

    private void requestImagesPermission() {
        String permission;
        if (Build.VERSION.SDK_INT >= 33) permission = Manifest.permission.READ_MEDIA_IMAGES;
        else permission = Manifest.permission.READ_EXTERNAL_STORAGE;

        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{permission}, REQ_IMAGES);
        } else {
            loadImages();
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_IMAGES && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadImages();
        } else if (requestCode == REQ_IMAGES) {
            Toast.makeText(this, "لم يتم منح صلاحية الصور", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImages() {
        items.clear();
        Uri collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
        try (Cursor cursor = getContentResolver().query(collection, projection, null, null,
                MediaStore.Images.Media.DATE_ADDED + " DESC")) {
            if (cursor != null) {
                int nameIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                while (cursor.moveToNext()) items.add(cursor.getString(nameIndex));
            }
        } catch (Exception e) {
            Toast.makeText(this, "تعذر تحميل الصور", Toast.LENGTH_LONG).show();
        }
        adapter.notifyDataSetChanged();
        if (items.isEmpty()) items.add("لا توجد صور متاحة");
        adapter.notifyDataSetChanged();
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "لا يوجد تطبيق كاميرا متاح", Toast.LENGTH_SHORT).show();
        }
    }
}
