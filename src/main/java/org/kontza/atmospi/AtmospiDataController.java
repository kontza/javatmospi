package org.kontza.atmospi;

import java.util.List;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author juharuotsalainen
 */
@RestController
public class AtmospiDataController {

    private Logger logger = LoggerFactory.getLogger(AtmospiDataController.class.getName());

    @Autowired
    private AtmospiSettings settings;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private TemperatureRepository temperatureRepository;

    @RequestMapping("/data/device/{deviceId}/{sensorType}")
    public String values(@PathVariable("deviceId") Long deviceId,
        @PathVariable("sensorType") String sensorType,
        @RequestParam(value = "range_min", required = false) Long rangeMin,
        @RequestParam(value = "range_max", required = false) Long rangeMax) throws Exception {

        if (sensorType.equalsIgnoreCase("temperature")) {
            return temperatures(deviceId, rangeMin, rangeMax);
        } else {
            return humidities(deviceId, rangeMin, rangeMax);
        }
    }

    private String temperatures(Long deviceId, Long rangeMin, Long rangeMax) {
        if (rangeMin == null) {
            Temperature t = temperatureRepository.findTop1ByDeviceIdOrderByTimestampAsc(deviceId);
            rangeMin = t.getTimestamp();
            logger.info("Tweaked rangeMin to {}.", rangeMin);
        }
        if (rangeMax == null) {
            Temperature t = temperatureRepository.findTop1ByDeviceIdOrderByTimestampDesc(deviceId);
            rangeMax = t.getTimestamp();
            logger.info("Tweaked rangeMax to {}.", rangeMax);
        }
        logger.info("Temperature request for device ID {}, alpha {}, omega {}.", deviceId, rangeMin, rangeMax);

        List<Temperature> temperatures = temperatureRepository.findByDeviceIdAndTimestampBetweenOrderByTimestampAsc(deviceId, rangeMin, rangeMax);
        logger.info("Found {} temperature values.", temperatures.toArray().length);
        JSONArray retVal = new JSONArray();
        for (Temperature t : temperatures) {
            JSONArray pair = new JSONArray();
            pair.put(1000 * t.getTimestamp());
            if (settings.getTemperatureUnit().equals(AtmospiSettings.TEMP_C)) {
                pair.put(t.getC());
            } else {
                pair.put(t.getF());
            }
            retVal.put(pair);
        }
        return retVal.toString();
    }

    private String humidities(Long deviceId, Long rangeMin, Long rangeMax) {
        logger.info("Humidity request for device ID {}, alpha {}, omega {}.", deviceId, rangeMin, rangeMax);
        logger.info("Returning an empty set for humidity values.");
        return "[]";
    }
}
