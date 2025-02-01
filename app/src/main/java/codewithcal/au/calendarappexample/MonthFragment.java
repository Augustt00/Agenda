package codewithcal.au.calendarappexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

public class MonthFragment extends Fragment {
    private static final String ARG_MONTH = "month";
    private YearMonth yearMonth;
    private GridView monthGridView;

    public static MonthFragment newInstance(YearMonth month) {
        MonthFragment fragment = new MonthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MONTH, month.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            yearMonth = YearMonth.parse(getArguments().getString(ARG_MONTH));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.month_view, container, false);
        monthGridView = view.findViewById(R.id.monthGridView);

        ArrayList<LocalDate> days = daysInMonthArray(yearMonth);
        MonthGridAdapter gridAdapter = new MonthGridAdapter(requireContext(), days);
        monthGridView.setAdapter(gridAdapter);

        // ✅ Evento de clique nas datas
        monthGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LocalDate selectedDate = days.get(position);
                if (selectedDate != null) {
                    Intent intent = new Intent(requireContext(), ScheduleActivity.class);
                    intent.putExtra("selectedDate", selectedDate.toString());
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    private ArrayList<LocalDate> daysInMonthArray(YearMonth month) {
        ArrayList<LocalDate> days = new ArrayList<>();
        YearMonth lastMonth = month.minusMonths(1);
        int lastMonthDays = lastMonth.lengthOfMonth();

        LocalDate firstOfMonth = month.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i <= 42; i++) {
            if (i <= dayOfWeek) {
                days.add(LocalDate.of(lastMonth.getYear(), lastMonth.getMonth(), lastMonthDays - dayOfWeek + i));
            } else if (i <= dayOfWeek + month.lengthOfMonth()) {
                days.add(LocalDate.of(month.getYear(), month.getMonth(), i - dayOfWeek));
            } else {
                days.add(LocalDate.of(month.plusMonths(1).getYear(), month.plusMonths(1).getMonth(), i - dayOfWeek - month.lengthOfMonth()));
            }
        }
        return days;
    }
}
