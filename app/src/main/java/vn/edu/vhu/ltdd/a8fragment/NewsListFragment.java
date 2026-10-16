package vn.edu.vhu.ltdd.a8fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NewsListFragment extends Fragment {

    private static final String TAG = "A8_231A290089/List";
    public static final String REQUEST_KEY = "news_request";

    private static final String[] TIEU_DE = {
            "VHU khai giảng năm học mới",
            "Android Studio ra bản cập nhật",
            "Flutter 3.47 có gì mới?"
    };

    private static final String[] NOI_DUNG = {
            "Trường Đại học Văn Hiến tổ chức lễ khai giảng với sự tham dự của toàn thể sinh viên khóa mới.",
            "Bản cập nhật tập trung vào tốc độ build, trình soạn thảo layout và công cụ phân tích bộ nhớ.",
            "Phiên bản này bổ sung RadioGroup, cải thiện hiệu năng trên web và nhiều widget Material 3."
    };

    private TextView tvDaDoc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvDaDoc = view.findViewById(R.id.tvDaDoc);
        LinearLayout box = view.findViewById(R.id.boxTin);

        for (int i = 0; i < TIEU_DE.length; i++) {
            final int viTri = i;
            Button b = new Button(requireContext());
            b.setText(TIEU_DE[i]);
            b.setAllCaps(false);
            b.setOnClickListener(v -> moChiTiet(viTri));
            box.addView(b);
        }

        // Lắng nghe kết quả trả về từ NewsDetailFragment qua Fragment Result API
        getParentFragmentManager().setFragmentResultListener(
                REQUEST_KEY,
                getViewLifecycleOwner(),
                (requestKey, bundle) -> {
                    String tieuDe = bundle.getString(NewsDetailFragment.KEY_TIEU_DE, "");
                    Log.d(TAG, "Nhận kết quả đã đọc: " + tieuDe);
                    tvDaDoc.setText(getString(R.string.list_last_read, tieuDe));
                }
        );

        // Nạp bản tin đầu tiên ở khung bên phải nếu là màn hình >= 600dp
        View detailContainer = requireActivity().findViewById(R.id.detailContainer);
        if (detailContainer != null) {
            moChiTiet(0);
        }
    }

    private void moChiTiet(int viTri) {
        NewsDetailFragment f = NewsDetailFragment.newInstance(TIEU_DE[viTri], NOI_DUNG[viTri]);

        // NC1: Kiểm tra xem màn hình hiện tại có khung detailContainer (sw600dp) hay không
        View detailFrame = requireActivity().findViewById(R.id.detailContainer);

        if (detailFrame != null) {
            // Đang ở chế độ 2 cột (Master-Detail): Nạp thẳng vào khung bên phải, không cần BackStack
            getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.detailContainer, f)
                    .setReorderingAllowed(true)
                    .commit();
        } else {
            // Đang ở chế độ điện thoại 1 cột: Thay thế khung chính kèm hiệu ứng trượt NC4 và BackStack
            getParentFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,  // enter
                            R.anim.fade_out,  // exit
                            R.anim.fade_in,   // popEnter
                            R.anim.slide_out  // popExit
                    )
                    .replace(R.id.fragmentContainer, f)
                    .addToBackStack("chi_tiet")
                    .setReorderingAllowed(true)
                    .commit();
        }
    }
}