package codewithcal.au.calendarappexample;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.YearMonth;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class MonthlyScheduleActivity extends AppCompatActivity {

    private TextView monthYearTextView;
    private ListView eventsListView;
    private EventRepository eventRepository;
    private YearMonth selectedMonthYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_schedule);

        monthYearTextView = findViewById(R.id.monthYearTextView);
        eventsListView = findViewById(R.id.eventsListView);
        eventRepository = new EventRepository(this);

        // Receber o mês/ano selecionado
        String receivedMonthYear = getIntent().getStringExtra("selectedMonthYear");
        android.util.Log.d("MonthlyScheduleActivity", "Mês/ano recebido: " + receivedMonthYear); // Log para depuração

        if (receivedMonthYear != null) {
            try {
                selectedMonthYear = YearMonth.parse(receivedMonthYear, DateTimeFormatter.ofPattern("yyyy-MM"));
                android.util.Log.d("MonthlyScheduleActivity", "Data convertida para YearMonth: " + selectedMonthYear); // Log para depuração

                String monthYearText = capitalizeFirstLetter(selectedMonthYear.format(DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("pt", "BR"))));
                monthYearTextView.setText(monthYearText);
                updateEventList();
            } catch (Exception e) {
                android.util.Log.e("MonthlyScheduleActivity", "Erro ao converter data: " + e.getMessage()); // Log de erro
            }
        }
    }

    private void updateEventList() {
        new Thread(() -> {
            List<Event> events = eventRepository.getEventsByMonth(selectedMonthYear);
            android.util.Log.d("MonthlyScheduleActivity", "Eventos recuperados: " + events.size()); // Log de depuração

            runOnUiThread(() -> {
                if (events.isEmpty()) {
                    // Mensagem se não houver agendamentos
                    String monthYearText = capitalizeFirstLetter(selectedMonthYear.format(DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("pt", "BR"))));
                    monthYearTextView.setText("Nenhum agendamento encontrado para " + monthYearText);
                } else {
                    EventAdapter eventAdapter = new EventAdapter(this, events);
                    eventsListView.setAdapter(eventAdapter);
                }
            });
        }).start();
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
