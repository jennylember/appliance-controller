package ru.lember.appliancecontroller.properties;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ModelToProgramsMap {

    private Map<String, List<String>> modelToProgramsMap;

}
