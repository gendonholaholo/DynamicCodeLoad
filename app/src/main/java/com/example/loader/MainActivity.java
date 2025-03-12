package com.example.loader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.lang.reflect.Method;

public class MainActivity extends Activity {
    private static final String TAG = "DynamicLoader";
    private static final String DEX_PATH = "/sdcard/target_dex.jar";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        Button loadButton = findViewById(R.id.loadButton);

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDynamicClass();
            }
        });
    }

    private void loadDynamicClass() {
        try {
            File dexFile = new File(DEX_PATH);
            File optimizedDir = getDir("dex", MODE_PRIVATE);

            DexClassLoader dexClassLoader = new DexClassLoader(
                    dexFile.getAbsolutePath(),
                    optimizedDir.getAbsolutePath(),
                    null,
                    getClassLoader()
            );

            Class<?> loadedClass = dexClassLoader.loadClass("com.example.target.TargetClass");
            Object instance = loadedClass.newInstance();
            Method method = loadedClass.getMethod("getMessage");
            String message = (String) method.invoke(instance);

            textView.setText(message);
            Log.d(TAG, "Loaded message: " + message);
        } catch (Exception e) {
            textView.setText("Failed to load class");
            Log.e(TAG, "Error loading class", e);
        }
    }
}
