package org.kontza.atmospi;

import java.util.ArrayList;
import java.util.List;
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
public class AtmospiTemperatureDevicesController {

    private Logger logger = LoggerFactory.getLogger(AtmospiTemperatureDevicesController.class.getName());

    @Autowired
    private AtmospiSettings settings;

    @Autowired
    private DeviceRepository deviceRepository;

    @RequestMapping("/data/devices/temperature")
    public String temperatureDevices() throws Exception {
        JSONObject retVal = new JSONObject();
        ArrayList<String> temperatureDevices = new ArrayList<>();
        temperatureDevices.add("dht22");
        temperatureDevices.add("dht11");
        temperatureDevices.add("am2302");
        temperatureDevices.add("ds18b20");
        List<Device> devices = deviceRepository.findByTypeInOrderByDeviceidAsc(temperatureDevices);
        for (Device d : devices) {
            logger.info("Device: {}", d.getLabel());
            retVal.put(String.format("%d", d.getDeviceid()), d.getLabel());
        }
        return retVal.toString();
    }
}
