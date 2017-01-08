package org.kontza.atmospi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AtmospiLatestHumidityController {

    private Logger logger = LoggerFactory.getLogger(AtmospiLatestHumidityController.class.getName());

    @RequestMapping("/data/latest/humidity")
    public String latest() throws Exception {
        logger.info("Returning an empty object for latest humidity.");
        return "{}";
    }
}
