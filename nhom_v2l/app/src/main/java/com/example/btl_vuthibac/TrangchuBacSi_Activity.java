package com.example.btl_vuthibac;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class TrangchuBacSi_Activity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private Handler handler;
    Database database;
    GridView grv;
    ArrayList<ThongTinBacSi> mangBSGridView; // Danh sách cho GridView
    ThongTinBacSiAdapter adapter;
    private Runnable runnable;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangchu_bac_si);
        viewPager = findViewById(R.id.sl1);

        ImageButton btncanhan = findViewById(R.id.btncanhan);
        ImageButton btndatlich = findViewById(R.id.btnlichdat);
        ImageButton btntinnhan = findViewById(R.id.btntinnhan);


        btntinnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //kiểm tra trạng thái đăng nhập của ng dùng
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

//                if (!isLoggedIn) {
//                    // Chưa đăng nhập, chuyển đến trang login
//                    Intent intent = new Intent(getApplicationContext(), Login.class);
//                    startActivity(intent);
//                } else {
//                    // Đã đăng nhập, chuyển đến trang 2
//                    Intent intent = new Intent(getApplicationContext(), TaiKhoan_Activity.class);
//                    startActivity(intent);
//                }
            }
        });

        btndatlich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                // Đã đăng nhập, chuyển đến trang 2
//                Intent intent = new Intent(getApplicationContext(), LichDatKhamTheoBacSi_Activity.class);
//                startActivity(intent);

            }
        });

        btncanhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //kiểm tra trạng thái đăng nhập của ng dùng
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

                if (!isLoggedIn) {
                    // Chưa đăng nhập, chuyển đến trang login
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                } else {
                    // Đã đăng nhập, chuyển đến trang 2
                    Intent intent = new Intent(getApplicationContext(), TrangCaNhan_Bacsi_Activity.class);
                    startActivity(intent);
                }
            }
        });


        // Tạo mảng chứa ID của hình ảnh
        int[] adImages = {
                R.drawable.sl6,
                R.drawable.sl1,
                R.drawable.sl2,
                R.drawable.sl3,
                R.drawable.sl4,
                R.drawable.sl5
        };

        // Thêm ảnh vào ViewPager2
        addImagesToViewPager(adImages);

        // Tạo Handler để tự động chuyển slide
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                currentPage++;
                if (currentPage >= adImages.length) {
                    currentPage = 0; // Reset về đầu
                }
                viewPager.setCurrentItem(currentPage, true);
                handler.postDelayed(this, 6000); // Chuyển sau 6 giây
            }
        };
        handler.postDelayed(runnable, 6000); // Bắt đầu chuyển slide
    }

    private void addImagesToViewPager(int[] adImages) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(adImages);
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.SlideViewHolder> {
        private int[] images;

        public ViewPagerAdapter(int[] images) {
            this.images = images;
        }

        @Override
        public SlideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide, parent, false);
            return new SlideViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SlideViewHolder holder, int position) {
            holder.adImage.setImageResource(images[position]);
        }

        @Override
        public int getItemCount() {
            return images.length;
        }

        class SlideViewHolder extends RecyclerView.ViewHolder {
            ImageView adImage;

            SlideViewHolder(View itemView) {
                super(itemView);
                adImage = itemView.findViewById(R.id.sl1); // Đảm bảo ID đúng
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); // Dừng chuyển slide khi Activity tạm dừng
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 6000); // Bắt đầu lại khi Activity tiếp tục
    }

    private byte[] convertBitmapToByteArray(int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

}