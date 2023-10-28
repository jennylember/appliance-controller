package ru.lember.appliancecontroller.properties;

import lombok.*;


import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BusinessProperties {

    Set<Program> getPrograms();
    List<String> getProgramIdByModel(String model);

    @Setter
    @ToString
    @EqualsAndHashCode
    class Bean implements BusinessProperties {

        private Map<String, List<String>> modelToProgramsMapping;
        private Set<Program> programs;

        @Override
        public Set<Program> getPrograms() {
            return Set.copyOf(programs);
        }

        @Override
        public List<String> getProgramIdByModel(String model) {
            return List.copyOf(modelToProgramsMapping.getOrDefault(model, List.of()));
        }
    }
}
