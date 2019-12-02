package fr.istic.mob.star1cd.database.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import fr.istic.mob.star1cd.database.StarContract;

/**
 * Calendar Model
 * @version 1.0.1
 * @author Charly C, Pierre D
 */
@Entity
public class Calendar {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = StarContract.Calendar.CalendarColumns._ID)
    private int id;
    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.MONDAY)
    private int monday;
    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.TUESDAY)
    private int tuesday;
    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.WEDNESDAY)
    private int wednesday;
    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.THURSDAY)
    private int thursday;
    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.FRIDAY)
    private int friday;
    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.SATURDAY)
    private int saturday;
    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.SUNDAY)
    private int sunday;
    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.START_DATE)
    private int startDate;
    @ColumnInfo(name = StarContract.Calendar.CalendarColumns.END_DATE)
    private int endDate;

    public Calendar() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMonday() {
        return monday;
    }

    public void setMonday(int monday) {
        this.monday = monday;
    }

    public int getTuesday() {
        return tuesday;
    }

    public void setTuesday(int tuesday) {
        this.tuesday = tuesday;
    }

    public int getWednesday() {
        return wednesday;
    }

    public void setWednesday(int wednesday) {
        this.wednesday = wednesday;
    }

    public int getThursday() {
        return thursday;
    }

    public void setThursday(int thursday) {
        this.thursday = thursday;
    }

    public int getFriday() {
        return friday;
    }

    public void setFriday(int friday) {
        this.friday = friday;
    }

    public int getSaturday() {
        return saturday;
    }

    public void setSaturday(int saturday) {
        this.saturday = saturday;
    }

    public int getSunday() {
        return sunday;
    }

    public void setSunday(int sunday) {
        this.sunday = sunday;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }
}