package ru.lember.appliancecontroller.properties;

import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.Set;

public interface BusinessProperties {

    Set<Program> getPrograms();
    Set<Long> getProgramIdsByModel(String model);

    @Setter
    @ToString
    @EqualsAndHashCode
    class Bean implements BusinessProperties {

        private Map<String, Set<Long>> modelToProgramsMapping;
        private Set<Program> programs;

        @Override
        public Set<Program> getPrograms() {
            return Set.copyOf(programs);
        }

        @Override
        public Set<Long> getProgramIdsByModel(String model) {
            return Set.copyOf(modelToProgramsMapping.getOrDefault(model, Set.of()));
        }
    }
}
