package com.zombie.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.zombie.app.utils.NumUtil;
import com.zombie.app.widget.praise_old.PraiseTextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private PraiseTextView ptvTest;
    private EditText etDesign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ptvTest = findViewById(R.id.ptv_test);
        etDesign = findViewById(R.id.et_design);
    }

    public void add(View v) {
        ptvTest.autoAdd();
    }

    public void reduce(View v) {
        ptvTest.autoReduce();
    }

    public void direct(View v) {
        ptvTest.setText(new Random().nextInt(100) + "");
    }

    public void design(View v) {
        ptvTest.setText(NumUtil.ofInteger(etDesign.getText().toString()) + "");
    }
}
