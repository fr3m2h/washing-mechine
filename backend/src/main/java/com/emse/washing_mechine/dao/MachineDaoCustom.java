package com.emse.washing_mechine.dao;


import com.emse.washing_mechine.model.MachineEntity;

import java.util.List;

public interface MachineDaoCustom {
    List<MachineEntity> findUnusedMachine();
}
