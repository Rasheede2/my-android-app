package com.abnisami3.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(28, 48, 28, 28);
        root.setGravity(Gravity.TOP);
        root.setBackgroundColor(Color.WHITE);

        TextView title = new TextView(this);
        title.setText("Abni Sami3 Novels");
        title.setTextSize(26);
        title.setTextColor(Color.rgb(45, 45, 45));
        title.setGravity(Gravity.CENTER);
        root.addView(title, new LinearLayout.LayoutParams(-1, -2));

        TextView subtitle = new TextView(this);
        subtitle.setText("أهلاً بك! مساحة مخصصة لتطوير تطبيق الروايات.");
        subtitle.setTextSize(18);
        subtitle.setPadding(0, 28, 0, 28);
        root.addView(subtitle);

        Button admin = new Button(this);
        admin.setText("فتح لوحة الأدوات");
        admin.setOnClickListener(v -> startActivity(new Intent(this, AdminActivity.class)));
        root.addView(admin, new LinearLayout.LayoutParams(-1, -2));

        setContentView(root);
    }
}
