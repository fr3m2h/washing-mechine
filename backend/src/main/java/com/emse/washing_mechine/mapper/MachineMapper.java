package com.emse.washing_mechine.mapper;

import com.emse.washing_mechine.dto.MachineDto;
import com.emse.washing_mechine.model.MachineEntity;

public class MachineMapper {
    public static MachineDto of (MachineEntity machine){
        return new MachineDto(
                machine.getId(),
                machine.getName(),
                machine.getIsUsed(),
                machine.getIsHs(),
                machine.getTimeLeft()
        );
    }
}