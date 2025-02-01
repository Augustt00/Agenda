package codewithcal.au.calendarappexample;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.time.YearMonth;
import java.util.List;

public class MonthPagerAdapter extends FragmentStateAdapter {
    private final List<YearMonth> months;

    public MonthPagerAdapter(FragmentActivity fa, List<YearMonth> months) {
        super(fa);
        this.months = months;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return MonthFragment.newInstance(months.get(position)); // Cada mês será um Fragment separado
    }

    @Override
    public int getItemCount() {
        return months.size();
    }
}
