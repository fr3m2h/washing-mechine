package com.emse.washing_mechine.mapper;


import com.emse.washing_mechine.dto.MachineDto;
import com.emse.washing_mechine.model.MachineEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MachineMapperTest {

    @Test
    void shouldMapMachine() {
        // Arrange
        MachineEntity machineEntity = FakeEntityBuilder.createMachineEntity(
                1L,
                "Machine1",
                true,
                15.0
        );

        // Act
        MachineDto machine = MachineMapper.of(machineEntity);

        // Assert
        MachineDto expectedMachine = new MachineDto(
                1L,
                "Machine1",
                true,
                15.0
        );
        Assertions.assertThat(machine).usingRecursiveComparison().isEqualTo(expectedMachine);
    }
}
