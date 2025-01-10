package com.emse.washing_mechine.dao;

import com.emse.washing_mechine.model.MachineEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;


@DataJpaTest
class MachineDaoTest {

    @Autowired
    private MachineDao machineDao;

    @Test
    void shouldFindUnusedMachines() {
        MachineEntity machine1 = new MachineEntity("Machine 1", false, false,0.0);
        machineDao.save(machine1);

        MachineEntity machine2 = new MachineEntity("Machine 2", true, false,20.0);
        machineDao.save(machine2);

        List<MachineEntity> unusedMachines = machineDao.findUnusedMachine();

        Assertions.assertThat(unusedMachines).hasSize(1);
        Assertions.assertThat(unusedMachines.get(0).getName()).isEqualTo("Machine 1");
    }

    @Test
    public void shouldFindMachineByName() {
        // Insert test data
        MachineEntity machine = new MachineEntity("Test Machine", true, false,15.0);
        MachineEntity savedMachine = machineDao.save(machine);

        // Execute query
        MachineEntity foundMachine = machineDao.findByName(savedMachine.getName());

        // Assertions
        Assertions.assertThat(foundMachine).isNotNull();
        Assertions.assertThat(foundMachine.getId()).isEqualTo(savedMachine.getId());
        Assertions.assertThat(foundMachine.getName()).isEqualTo("Test Machine");
        Assertions.assertThat(foundMachine.getIsUsed()).isTrue();
        Assertions.assertThat(foundMachine.getIsHs()).isFalse();
        Assertions.assertThat(foundMachine.getTimeLeft()).isEqualTo(15.0);
    }

    @Test
    public void shouldDeleteMachineByName() {
        // Insert test data
        MachineEntity machine = new MachineEntity("Machine To Delete", false, false,10.0);
        machineDao.save(machine);

        MachineEntity anotherMachine = new MachineEntity("Another Machine", true, false,20.0);
        machineDao.save(anotherMachine);

        // Execute delete
        machineDao.deleteByName("Machine To Delete");

        // Verify deletion
        MachineEntity deletedMachine = machineDao.findByName("Machine To Delete");
        Assertions.assertThat(deletedMachine).isNull();

        // Verify other machine still exists
        MachineEntity remainingMachine = machineDao.findByName("Another Machine");
        Assertions.assertThat(remainingMachine).isNotNull();
        Assertions.assertThat(remainingMachine.getName()).isEqualTo("Another Machine");
    }


}