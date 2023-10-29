package ru.lember.appliancecontroller.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import ru.lember.appliancecontroller.dao.DeviceState;

@Mapper
public interface ActiveStateMapper {

    @Select("SELECT name FROM active_state INNER JOIN states ON state_id = id WHERE device_id = #{deviceId}")
    DeviceState getActiveStateName(@Param("deviceId") Long deviceId);

    @Update("UPDATE active_state SET state_id = #{stateId}, modification_date = NOW() WHERE device_id = #{deviceId}")
    int updateActiveState(Long deviceId, Long stateId);
}
