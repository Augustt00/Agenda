package codewithcal.au.calendarappexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MonthGridAdapter extends BaseAdapter {
    private final ArrayList<LocalDate> days;
    private final Context context;

    public MonthGridAdapter(Context context, ArrayList<LocalDate> days) {
        this.context = context;
        this.days = days;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.calendar_day, parent, false);
        }

        TextView dayText = convertView.findViewById(R.id.dayTextView);
        LocalDate date = days.get(position);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d");
        dayText.setText(date.format(formatter));

        return convertView;
    }
}
