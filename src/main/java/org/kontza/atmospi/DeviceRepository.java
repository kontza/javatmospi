package org.kontza.atmospi;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface DeviceRepository extends CrudRepository<Device, Long> {

    List<Device> findByTypeOrderByDeviceidAsc(String type);

    List<Device> findBySerialIdOrderByDeviceidAsc(String serialId);

    List<Device> findByLabelOrderByDeviceidAsc(String label);

    public List<Device> findByTypeInOrderByDeviceidAsc(ArrayList<String> temperatureDevices);
}
