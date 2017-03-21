new File(project.basedir, "src/test/resources/import.sql").withWriter { out ->
    // Devices.
    out.println "insert into devices (deviceid,type,serialid,label) values(1,'ds18b20','28-000003ea01f5','Kasvihuone');"
    out.println "insert into devices (deviceid,type,serialid,label) values(2,'ds18b20','28-000003ea02f5','Rumpuhuone');"

    // Temperatures.
    long steps = 512
    long interval = 300
    long now = System.currentTimeMillis() / 1000
    long then = now - (steps * interval)
    def deg2rad = { it * Math.PI / 180 }
    for(long id = 1; id < 2 * steps; id++) {
        def angle = id % 360
        def temp1 = 20 + 20 * Math.sin(deg2rad(angle))
        def temp2 = 5 * Math.cos(deg2rad(angle))
        def deviceId = 1
        def timestamp = then + id * interval
        out.println sprintf("insert into temperature (id,deviceid,timestamp,c,f) values(%d,%d,%d,%f,%f);",
                            id, deviceId, timestamp, temp1, 1.8 * temp1 + 32)
        deviceId = 2
        id = id + 1
        out.println sprintf("insert into temperature (id,deviceid,timestamp,c,f) values(%d,%d,%d,%f,%f);",
                            id, deviceId, timestamp, temp2, 1.8 * temp2 + 32)
    }
}
println ">>> import.sql generated"
