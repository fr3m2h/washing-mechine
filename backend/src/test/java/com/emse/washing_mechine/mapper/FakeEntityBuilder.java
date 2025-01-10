package com.emse.washing_mechine.mapper;

import com.emse.washing_mechine.model.MachineEntity;

import java.util.ArrayList;
import java.util.List;

public class FakeEntityBuilder {

    public static MachineEntity createMachineEntity(Long id, String name, Boolean isUsed, Boolean isHs, Double timeLeft) {
        MachineEntity machineEntity = new MachineEntity(name, isUsed, isHs,timeLeft);
        machineEntity.setId(id);
        return machineEntity;
    }

    public static List<MachineEntity> createMultipleMachineEntities(int count) {
        List<MachineEntity> machines = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            machines.add(createMachineEntity(
                    (long) i,
                    "Machine" + i,
                    i % 2 == 0, // Alternates between used and unused,
                    false,
                    i * 10.0 // Arbitrary timeLeft value
            ));
        }
        return machines;
    }

    public static MachineEntity createUnusedMachineEntity(Long id, String name) {
        return createMachineEntity(id, name, false, false,0.0);
    }

    public static MachineEntity createUsedMachineEntity(Long id, String name, Double timeLeft) {
        return createMachineEntity(id, name, true, false,timeLeft);
    }
}

