package com.angcyo.looprecyclerview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity implements ScalableCardHelper.OnPageChangeListener {

    List<String> datas = new ArrayList<>();

    private int dp2px(int dipValue) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;
        return (int) (dipValue * density + 0.5f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RLoopRecyclerView loopRecyclerView = (RLoopRecyclerView) findViewById(R.id.recycler_view);
        loopRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        for (int i = 0; i < 6; i++) {
            datas.add("" + i);
        }

        MyAdapter myAdapter = new MyAdapter();
        myAdapter.setDatas(datas);
        loopRecyclerView.setAdapter(myAdapter);
        ScalableCardHelper cardHelper = new ScalableCardHelper(this);
        cardHelper.attachToRecyclerView(loopRecyclerView);
    }

    private int getWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = getWindowManager();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    @Override
    public void onPageSelected(int position) {
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    class MyAdapter extends RLoopRecyclerView.LoopAdapter<MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.item_text_view, parent, false);
            return new MyViewHolder(inflate);
        }

        @Override
        public void onBindLoopViewHolder(MyViewHolder holder, final int position) {
            TextView txt = (TextView) holder.itemView.findViewById(R.id.txt);
            ImageView img = (ImageView) holder.itemView.findViewById(R.id.text_view);
            txt.setText("有品位" + position % 6);
            txt.setTextSize(13);
            RelativeLayout.LayoutParams txtLayoutParams = (RelativeLayout.LayoutParams) txt.getLayoutParams();
            txtLayoutParams.width = getWidth() / 5;
            txt.setLayoutParams(txtLayoutParams);
            RelativeLayout.LayoutParams imgLayoutParams = (RelativeLayout.LayoutParams) img.getLayoutParams();
            imgLayoutParams.width = getWidth() / 5;
            imgLayoutParams.height = getWidth() / 5;
            img.setLayoutParams(imgLayoutParams);

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.height = getWidth() / 5 + dp2px(20);
            holder.itemView.setLayoutParams(layoutParams);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("MainActivity", position + "");
                }
            });


            switch (position % 6) {
                case 1:
                    img.setImageResource(R.mipmap.a1);
                    break;
                case 2:
                    img.setImageResource(R.mipmap.a2);
                    break;
                case 3:
                    img.setImageResource(R.mipmap.a3);
                    break;
                case 4:
                    img.setImageResource(R.mipmap.a4);
                    break;
                case 5:
                    img.setImageResource(R.mipmap.a5);
                    break;
                case 6:
                    img.setImageResource(R.mipmap.a6);
                    break;
            }

            ImageView imgBg = (ImageView) holder.itemView.findViewById(R.id.item_bg);
            RelativeLayout.LayoutParams imgBgLayoutParams = (RelativeLayout.LayoutParams) imgBg.getLayoutParams();
            imgBgLayoutParams.height = getWidth() / 5 + dp2px(20);
            imgBgLayoutParams.width = getWidth() / 5;
        }
    }
}
