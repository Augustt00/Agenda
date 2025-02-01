package codewithcal.au.calendarappexample;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Event {
    private int id; // Adicionado para suportar IDs no banco de dados
    private String name;
    private LocalDate date;
    private LocalTime time;
    private String formattedTime; // Adicionado para armazenar o tempo formatado

    // Construtor
    public Event(String name, LocalDate date, LocalTime time, String formattedTime) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.formattedTime = formattedTime;
    }

    // Construtor opcional para uso ao carregar do banco de dados (inclui o ID)
    public Event(int id, String name, LocalDate date, LocalTime time, String formattedTime) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.formattedTime = formattedTime;
    }

    // Métodos getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }
}
