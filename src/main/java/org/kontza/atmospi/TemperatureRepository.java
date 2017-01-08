package org.kontza.atmospi;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface TemperatureRepository extends CrudRepository<Temperature, Long> {

    public List<Temperature> findAllByDeviceIdOrderByTimestampAsc(Long deviceId);

    public Temperature findTop1ByDeviceIdOrderByTimestampDesc(Long deviceid);

    public List<Temperature> findByDeviceIdAndTimestampBetweenOrderByTimestampAsc(Long deviceId, Long rangeMin, Long rangeMax);

    public Temperature findTop1ByDeviceIdOrderByTimestampAsc(Long deviceId);

    public Temperature findTop1ByOrderByTimestampDesc();

    public Temperature findTop1ByOrderByTimestampAsc();

}
