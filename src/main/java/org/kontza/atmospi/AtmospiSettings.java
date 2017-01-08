package org.kontza.atmospi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author juharuotsalainen
 */
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "atmospi.settings")
public class AtmospiSettings {

    @JsonIgnore
    public final static String TEMP_C = "C";

    @JsonIgnore
    public final static String TEMP_F = "F";

    // How far into the past should data be loaded (in seconds)? Default to 1 week.
    @JsonProperty("range_seconds")
    private int rangeSeconds;

    // The number of digits after the decimal place that will be stored.
    private int precision;

    // Temperature unit of measure (C or F).
    @JsonProperty("t_unit")
    private String temperatureUnit;

    public AtmospiSettings(int rangeSeconds, int precision, String temperatureUnit) {
        this.rangeSeconds = rangeSeconds;
        this.precision = precision;
        this.temperatureUnit = temperatureUnit;
    }

    public AtmospiSettings() {
        rangeSeconds = 7 * 24 * 3600;
        precision = 2;
        temperatureUnit = TEMP_C;
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

    public void setTemperatureUnit(String temperatureUnit) {
        if (temperatureUnit.toUpperCase() == TEMP_C) {
            this.temperatureUnit = TEMP_C;
        } else {
            this.temperatureUnit = TEMP_F;
        }
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

}
