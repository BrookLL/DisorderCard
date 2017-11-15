package com.riverlet.disordercard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private String[] datas = new String[]{
            "火影忍者",
            "海贼王",
            "死神",
            "春天花花同学会",
            "七龙珠",
            "黑",
            "结界师",
            "春光灿烂猪八戒",
            "欢天喜地七仙女",
            "情癫大圣",
            "疾",
            "风",
            "传",
            "爱你一万年",
            "么么哒",
            "嘤嘤嘤",
            "瓦西里",
            "勇敢一点",
            "复仇者联盟",
            "源代码",
            "戴手铐的旅客",
            "驼铃",
            "风雷一剑",
            "先灭八荒，再杀叶开",
            "爱",
            "情",
            "公寓"
    };
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DisorderCardView disorderCardView = findViewById(R.id.disorder);
//        DisorderCardView.Adapter adapter = new DisorderCardView.Adapter(this, Arrays.asList(datas));
//        disorderCardView.setAdapter(adapter);
        disorderCardView.setData(Arrays.asList(datas));
        disorderCardView.setOnCarkClickListener(new DisorderCardView.OnCarkClickListener() {
            @Override
            public void onCardClick(String text) {
                Toast.makeText(MainActivity.this,text,Toast.LENGTH_SHORT).show();
            }
        });

        final Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (count % 3) {
                    case 0:
                        disorderCardView.setGravity(Gravity.LEFT);
                        button.setText("靠左");
                        break;
                    case 1:
                        disorderCardView.setGravity(Gravity.RIGHT);
                        button.setText("靠右");
                        break;
                    case 2:
                        disorderCardView.setGravity(Gravity.CENTER_HORIZONTAL);
                        button.setText("居中");
                        break;
                }
                count++;
            }
        });
    }
}
