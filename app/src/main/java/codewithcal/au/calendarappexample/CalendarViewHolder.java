package codewithcal.au.calendarappexample;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarViewHolder extends RecyclerView.ViewHolder {
    public final View parentView;
    public final TextView dayOfMonth;

    public CalendarViewHolder(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener, ArrayList<LocalDate> days) {
        super(itemView);
        parentView = itemView.findViewById(R.id.parentView);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);

        // Clique único
        itemView.setOnClickListener(view -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                LocalDate date = days.get(position);
                onItemListener.onItemClick(position, date);
            }
        });
    }
}
