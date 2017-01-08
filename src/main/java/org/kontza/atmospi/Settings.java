/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kontza.atmospi;

/**
 *
 * @author juharuotsalainen
 */
public class Settings {

    // How far into the past should data be loaded (in seconds)? Default to 1 week.
    private int rangeSeconds;

    // The number of digits after the decimal place that will be stored.
    private int precision;

    // Temperature unit of measure (C or F).
    private String temperatureUnit;

    public Settings(int rangeSeconds, int precision, String temperatureUnit) {
        this.rangeSeconds = rangeSeconds;
        this.precision = precision;
        this.temperatureUnit = temperatureUnit;
    }

    public Settings() {
        rangeSeconds = 7 * 24 * 3600;
        precision = 2;
        temperatureUnit = AtmospiSettings.TEMP_C;
    }

    public int getRangeSeconds() {
        return rangeSeconds;
    }

    public void setRangeSeconds(int rangeSeconds) {
        this.rangeSeconds = rangeSeconds;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        if (temperatureUnit.toUpperCase() == AtmospiSettings.TEMP_C) {
            this.temperatureUnit = AtmospiSettings.TEMP_C;
        } else {
            this.temperatureUnit = AtmospiSettings.TEMP_F;
        }
    }
}
