package codewithcal.au.calendarappexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.time.YearMonth;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EventRepository {
    private final EventDatabaseHelper dbHelper;

    public EventRepository(Context context) {
        dbHelper = new EventDatabaseHelper(context);
    }

    public void insertEvent(Event event) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", event.getName());
        values.put("date", event.getDate().toString()); // Certifique-se de que a data completa está sendo salva
        values.put("time", event.getTime().format(DateTimeFormatter.ofPattern("HH:mm"))); // Formatar para HH:mm
        db.insert("events", null, values);
        db.close();
    }

    public List<Event> getEventsByDate(String date) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<Event> events = new ArrayList<>();

        try {
            String[] columns = {"id", "name", "date", "time"};
            String selection = "date = ?";
            String[] selectionArgs = {date};

            cursor = db.query("events", columns, selection, selectionArgs, null, null, null);

            while (cursor.moveToNext()) {
                Event event = new Event(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("date"))),
                        LocalTime.parse(cursor.getString(cursor.getColumnIndexOrThrow("time")), DateTimeFormatter.ofPattern("HH:mm")),
                        cursor.getString(cursor.getColumnIndexOrThrow("time")) // Access the formatted time directly
                );
                events.add(event);
            }
        } finally {
            if (cursor != null) cursor.close(); // Fechar o cursor
            db.close(); // Fechar após a operação
        }

        return events;
    }

    // Método para buscar eventos por mês
    public List<Event> getEventsByMonth(YearMonth month) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        List<Event> events = new ArrayList<>();

        try {
            String[] columns = {"id", "name", "date", "time"};
            String selection = "strftime('%Y-%m', date) = ?";
            String[] selectionArgs = {month.toString()}; // Formatar para o formato "yyyy-MM"

            cursor = db.query("events", columns, selection, selectionArgs, null, null, "date ASC");

            while (cursor.moveToNext()) {
                Event event = new Event(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("date"))),
                        LocalTime.parse(cursor.getString(cursor.getColumnIndexOrThrow("time")), DateTimeFormatter.ofPattern("HH:mm")),
                        cursor.getString(cursor.getColumnIndexOrThrow("time")) // Access the formatted time directly
                );
                events.add(event);
            }
        } finally {
            if (cursor != null) cursor.close(); // Fechar o cursor
            db.close(); // Fechar após a operação
        }

        return events;
    }

    public void deleteEvent(Event event) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            db.delete("events", "id = ?", new String[]{String.valueOf(event.getId())});
        }
    }
}
