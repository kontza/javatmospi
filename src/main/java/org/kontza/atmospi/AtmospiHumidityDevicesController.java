package org.kontza.atmospi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author juharuotsalainen
 */
@RestController
public class AtmospiHumidityDevicesController {

    private Logger logger = LoggerFactory.getLogger(AtmospiTemperatureDevicesController.class.getName());

    @RequestMapping("/data/devices/humidity")
    public String devices() throws Exception {
        logger.info("Returning an empty object for humidity devices.");
        return "{}";
    }
}
