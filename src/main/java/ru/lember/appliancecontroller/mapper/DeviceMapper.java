package ru.lember.appliancecontroller.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import ru.lember.appliancecontroller.dao.Device;

import java.util.List;

@Mapper
public interface DeviceMapper {

    @Select("SELECT * FROM devices WHERE id = #{id}")
    public Device getDeviceById(long id);

    @Select("SELECT * FROM devices")
    public List<Device> getAllDevices();

}
