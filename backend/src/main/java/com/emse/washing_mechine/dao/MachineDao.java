package com.emse.washing_mechine.dao;

import com.emse.washing_mechine.model.MachineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MachineDao extends JpaRepository<MachineEntity, Long>, MachineDaoCustom {

    @Query("select m from MachineEntity m where m.name = :name")
    MachineEntity findByName(@Param("name") String name);

    @Modifying
    @Query("delete from MachineEntity c where c.name = :name")
    void deleteByName(@Param("name") String name);


}
