package org.kontza.atmospi;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AtmospiWebController {

    @Autowired
    TemperatureRepository temperatureRepository;

    @RequestMapping("/")
    public String index(Map<String, Object> model) {
        Temperature oldest = temperatureRepository.findTop1ByOrderByTimestampAsc();
        Temperature latest = temperatureRepository.findTop1ByOrderByTimestampDesc();
        model.put("subDomain", "");
        model.put("oldestTimestamp", 1000 * oldest.getTimestamp());
        model.put("latestTimestamp", 1000 * latest.getTimestamp());
        return "index";
    }
}
