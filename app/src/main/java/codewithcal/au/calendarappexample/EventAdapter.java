package codewithcal.au.calendarappexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {

    public EventAdapter(Context context, List<Event> events) {
        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Verifica se a View reutilizável não está sendo usada, se não está inflar uma nova
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false); // Usando event_cell.xml
        }

        // Obter o evento da posição atual
        Event event = getItem(position);

        // Vincular dados aos TextViews no layout event_cell
        TextView eventNameTextView = convertView.findViewById(R.id.eventNameTextView);
        TextView eventTimeTextView = convertView.findViewById(R.id.eventTimeTextView);
        TextView eventDateTextView = convertView.findViewById(R.id.eventDateTextView); // Adicionando um TextView para a data

        if (event != null) {
            eventNameTextView.setText(event.getName());
            eventTimeTextView.setText("Horario: " + event.getFormattedTime());
            eventDateTextView.setText("Data: " + event.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))); // Formatando a data
        }

        return convertView;
    }
}
