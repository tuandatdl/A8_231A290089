package vn.edu.vhu.ltdd.a8fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "A8_231A290089";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.tab_home) {
                doiTab(new HomeFragment());
            } else if (id == R.id.tab_news) {
                doiTab(new NewsListFragment());
            } else if (id == R.id.tab_profile) {
                doiTab(ProfileFragment.newInstance("Đỗ Lê Tuấn Đạt", "231A290089"));
            }
            return true; // Bắt buộc return true để đánh dấu tab được tô sáng
        });

        // Chỉ nạp Fragment mặc định ở lần khởi tạo ĐẦU TIÊN
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.tab_home);
        }
    }

    private void doiTab(Fragment f) {
        Log.d(TAG, "Chuyển tab sang: " + f.getClass().getSimpleName());

        // Dọn dẹp khung detailContainer khi chuyển tab
        View detailContainer = findViewById(R.id.detailContainer);
        if (detailContainer != null) {
            Fragment detailFrag = getSupportFragmentManager().findFragmentById(R.id.detailContainer);
            if (detailFrag != null) {
                getSupportFragmentManager().beginTransaction().remove(detailFrag).commit();
            }
        }

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out) // NC4
                .replace(R.id.fragmentContainer, f)
                // KHÔNG addToBackStack cho các tab chính
                .setReorderingAllowed(true)
                .commit();
    }
}