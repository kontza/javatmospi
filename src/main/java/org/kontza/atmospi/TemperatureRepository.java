package org.kontza.atmospi;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface TemperatureRepository extends CrudRepository<Temperature, Long> {

    public List<Temperature> findAllByDeviceId(Long deviceId);

    public Temperature findTop1ByDeviceIdOrderByTimestampDesc(Long deviceid);

    public List<Temperature> findByDeviceIdAndTimestampBetween(Long deviceId, Long rangeMin, Long rangeMax);

}
