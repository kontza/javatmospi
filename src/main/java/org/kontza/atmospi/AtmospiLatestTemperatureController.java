package org.kontza.atmospi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author juharuotsalainen
 */
@RestController
public class AtmospiLatestTemperatureController {

    private Logger logger = LoggerFactory.getLogger(AtmospiLatestTemperatureController.class.getName());

    @Autowired
    private AtmospiSettings settings;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private TemperatureRepository temperatureRepository;

    @RequestMapping("/data/latest/temperature")
    public String latest() throws Exception {
        JSONObject retVal = new JSONObject();
        Iterable<Device> devices = deviceRepository.findAll();
        for (Device d : devices) {
            logger.info("Device: {}, {}", d.getLabel(), d.getDeviceid());
            Temperature temperature = temperatureRepository.findTop1ByDeviceIdOrderByTimestampDesc(d.getDeviceid());
            logger.info("Temperature: {}", temperature);
            ArrayList<String> values = new ArrayList<>();
            values.add(String.format("%d", 1000 * temperature.getTimestamp()));
            if (settings.getTemperatureUnit().equals(AtmospiSettings.TEMP_C)) {
                values.add(String.format("%f", temperature.getC()));
            } else {
                values.add(String.format("%f", temperature.getF()));
            }
            retVal.put(d.getLabel(), values);
        }
        return retVal.toString();
    }
}
