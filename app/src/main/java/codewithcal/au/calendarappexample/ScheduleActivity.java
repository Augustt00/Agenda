package codewithcal.au.calendarappexample;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    private LocalDate selectedDate;
    private LocalTime selectedTime;
    private EditText meetingNameEditText;
    private Spinner timeSpinner;
    private ListView eventsListView;
    private TextView noEventsTextView;
    private EventRepository eventRepository;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        TextView selectedDateTextView = findViewById(R.id.selectedTimeTextView);
        meetingNameEditText = findViewById(R.id.meetingNameEditText);
        timeSpinner = findViewById(R.id.timeSpinner);
        eventsListView = findViewById(R.id.eventsListView);
        noEventsTextView = findViewById(R.id.noEventsTextView);
        eventRepository = new EventRepository(this);

        // Receber a data selecionada
        String receivedDate = getIntent().getStringExtra("selectedDate");
        if (receivedDate != null) {
            selectedDate = LocalDate.parse(receivedDate);
            // Formatar a data para o formato dia/mês/ano
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            selectedDateTextView.setText("Data Selecionada: " + selectedDate.format(dateFormatter));
        }

        setupTimeSpinner();
        updateEventList();
    }

    private void setupTimeSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.times_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(adapter);

        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTimeString = parent.getItemAtPosition(position).toString();
                selectedTime = LocalTime.parse(selectedTimeString, DateTimeFormatter.ofPattern("HH:mm")); // Formatando para HH:mm
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedTime = null;
            }
        });
    }

    private void updateEventList() {
        new Thread(() -> {
            List<Event> events = eventRepository.getEventsByDate(selectedDate.toString());

            runOnUiThread(() -> {
                if (events.isEmpty()) {
                    noEventsTextView.setVisibility(View.VISIBLE);
                    eventsListView.setVisibility(View.GONE);
                } else {
                    noEventsTextView.setVisibility(View.GONE);
                    eventsListView.setVisibility(View.VISIBLE);

                    EventAdapter eventAdapter = new EventAdapter(this, events);
                    eventsListView.setAdapter(eventAdapter);

                    // Adicionar o OnItemLongClickListener para exibir a opção de excluir
                    eventsListView.setOnItemLongClickListener((parent, view, position, id) -> {
                        Event event = (Event) parent.getItemAtPosition(position);
                        showDeleteDialog(event);
                        return true;
                    });
                }
            });
        }).start();
    }

    private void showDeleteDialog(Event event) {
        new AlertDialog.Builder(this)
                .setTitle("Excluir Agenda")
                .setMessage("Você tem certeza que deseja excluir esta agenda?")
                .setPositiveButton("Sim", (dialog, which) -> new Thread(() -> {
                    eventRepository.deleteEvent(event);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Agenda excluída com sucesso!", Toast.LENGTH_SHORT).show();
                        updateEventList();
                    });
                }).start())
                .setNegativeButton("Não", null)
                .show();
    }

    public void saveMeeting(View view) {
        String meetingName = meetingNameEditText.getText().toString();
        if (meetingName.isEmpty() || selectedTime == null) {
            Toast.makeText(this, "Por favor, insira o nome e selecione um horário.", Toast.LENGTH_SHORT).show();
            return;
        }

        Event newEvent = new Event(meetingName, selectedDate, selectedTime, selectedTime.format(DateTimeFormatter.ofPattern("HH:mm")));

        new Thread(() -> {
            eventRepository.insertEvent(newEvent);
            runOnUiThread(() -> {
                Toast.makeText(this, "Agenda Concluida", Toast.LENGTH_SHORT).show();
                updateEventList();
                meetingNameEditText.setText(""); // Limpa o campo de entrada
            });
        }).start();
    }
}
