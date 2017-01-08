package org.kontza.atmospi;

import org.springframework.data.repository.CrudRepository;

public interface TemperatureRepository extends CrudRepository<Temperature, Long> {

    public Iterable<Temperature> findAllByDeviceId(Long deviceId);

    public Temperature findTop1ByDeviceIdOrderByTimestampDesc(Long deviceid);

}
