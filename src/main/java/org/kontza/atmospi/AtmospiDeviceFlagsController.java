package org.kontza.atmospi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AtmospiDeviceFlagsController {

    private Logger logger = LoggerFactory.getLogger(AtmospiDeviceFlagsController.class.getName());

    @RequestMapping("/data/device/{deviceId}/flags")
    public String flags() {
        logger.info("Returning an empty object for device flags.");
        return "{}";
    }
}
