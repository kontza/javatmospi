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
public class AtmospiTemperatureDataController {

    private Logger logger = LoggerFactory.getLogger(AtmospiTemperatureDataController.class.getName());

    @Autowired
    private AtmospiSettings settings;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private TemperatureRepository temperatureRepository;

    @RequestMapping("/data/device/{deviceId}/temperature")
    public String temperatures(@PathVariable("deviceId") Long deviceId,
        @RequestParam(value = "range_min", required = false) Long rangeMin,
        @RequestParam(value = "range_max", required = false) Long rangeMax) throws Exception {
        logger.info("Data request for device ID {}, alpha {}, omega {}.", deviceId, rangeMin, rangeMax);
        if (rangeMin == null) {
            rangeMin = new Long(0);
        }
        if (rangeMax == null) {
            rangeMax = new Long(System.currentTimeMillis() / 1000);
        }

        List<Temperature> temperatures = temperatureRepository.findByDeviceIdAndTimestampBetween(deviceId, rangeMin, rangeMax);
        logger.info("Found {} temperature values.", temperatures.toArray().length);
        JSONArray retVal = new JSONArray();
        for (Temperature t : temperatures) {
            JSONArray pair = new JSONArray();
            pair.put(t.getTimestamp());
            if (settings.getTemperatureUnit().equals(AtmospiSettings.TEMP_C)) {
                pair.put(t.getC());
            } else {
                pair.put(t.getF());
            }
            retVal.put(pair);
        }
        return retVal.toString();
    }
}
