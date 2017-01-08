package org.kontza.atmospi;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface DeviceRepository extends CrudRepository<Device, Long> {

    List<Device> findByType(String type);

    List<Device> findBySerialId(String serialId);

    List<Device> findByLabel(String label);

    public List<Device> findByTypeIn(ArrayList<String> temperatureDevices);
}
