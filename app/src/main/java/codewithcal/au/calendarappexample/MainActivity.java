package codewithcal.au.calendarappexample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

import static codewithcal.au.calendarappexample.CalendarUtils.daysInMonthArray;

public class MainActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private GestureDetector gestureDetector;
    private Animation slideInLeft, slideOutRight, slideInRight, slideOutLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        loadAnimations();
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();
        setupGestureDetection();
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        monthYearText.setOnClickListener(this::openMonthlySchedule); // Definindo o clique para abrir a agenda mensal
    }

    private void loadAnimations() {
        slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        slideOutRight = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        slideInRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        slideOutLeft = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
    }

    private void setMonthView() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("pt", "BR"));
        String monthYear = capitalizeFirstLetter(CalendarUtils.selectedDate.format(formatter));
        monthYearText.setText(monthYear);
        ArrayList<LocalDate> daysInMonth = daysInMonthArray();

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupGestureDetection() {
        SwipeGestureListener.OnSwipeListener swipeListener = new SwipeGestureListener.OnSwipeListener() {
            @Override
            public void onSwipeLeft() {
                Log.d("MainActivity", "Swipe Left Detected");
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
                applyAnimation(slideOutLeft, slideInRight);
            }

            @Override
            public void onSwipeRight() {
                Log.d("MainActivity", "Swipe Right Detected");
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
                applyAnimation(slideOutRight, slideInLeft);
            }
        };

        gestureDetector = new GestureDetector(this, new SwipeGestureListener(swipeListener));
        calendarRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    private void applyAnimation(Animation outAnimation, Animation inAnimation) {
        calendarRecyclerView.startAnimation(outAnimation);
        outAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                setMonthView();
                calendarRecyclerView.startAnimation(inAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if (date != null) {
            // Abrir a tela de horários disponíveis
            Intent intent = new Intent(this, ScheduleActivity.class);
            intent.putExtra("selectedDate", date.toString());
            startActivity(intent);

            android.util.Log.d("MainActivity", "Abrindo ScheduleActivity para a data: " + date);
        }
    }

    public void openMonthlySchedule(View view) {
        String monthYear = monthYearText.getText().toString().trim().toLowerCase(); // Remove espaços e ajusta case

        android.util.Log.d("MainActivity", "Texto do mês/ano antes da conversão: " + monthYear);

        try {
            // Converte para YearMonth corretamente
            YearMonth yearMonth = YearMonth.of(
                    CalendarUtils.selectedDate.getYear(),
                    CalendarUtils.selectedDate.getMonthValue()
            );

            Intent intent = new Intent(this, MonthlyScheduleActivity.class);
            intent.putExtra("selectedMonthYear", yearMonth.toString()); // Enviar no formato "YYYY-MM"
            startActivity(intent);
        } catch (Exception e) {
            android.util.Log.e("MainActivity", "Erro ao processar mês/ano selecionado", e);
            Toast.makeText(this, "Formato inválido para o mês/ano.", Toast.LENGTH_SHORT).show();
        }
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
