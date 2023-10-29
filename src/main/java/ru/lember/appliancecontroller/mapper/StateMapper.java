package ru.lember.appliancecontroller.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import ru.lember.appliancecontroller.dao.State;

@Mapper
public interface StateMapper {

    @Insert("INSERT INTO states (name, device_type, program_id, details) " +
            "VALUES (#{name}, #{deviceType}, #{programId}, CAST(#{details} AS json))")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void addState(State state);

}
