package codewithcal.au.calendarappexample;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView monthYearText;
    private ViewPager2 viewPager;
    private List<YearMonth> months;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        setupViewPager();
    }

    private void initWidgets() {
        viewPager = findViewById(R.id.viewPager);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setupViewPager() {
        months = new ArrayList<>();
        YearMonth currentMonth = YearMonth.now();
        months.add(currentMonth.minusMonths(1)); // Mês anterior
        months.add(currentMonth); // Mês atual
        months.add(currentMonth.plusMonths(1)); // Próximo mês

        MonthPagerAdapter adapter = new MonthPagerAdapter(this, months);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1, false); // Centraliza no mês atual

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                YearMonth month = months.get(position);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("pt", "BR"));
                monthYearText.setText(capitalizeFirstLetter(month.format(formatter)));
            }
        });
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
