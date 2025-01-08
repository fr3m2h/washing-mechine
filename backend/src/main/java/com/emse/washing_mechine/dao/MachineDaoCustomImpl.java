package com.emse.washing_mechine.dao;

import com.emse.washing_mechine.model.MachineEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class MachineDaoCustomImpl implements MachineDaoCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<MachineEntity> findUnusedMachine() {
        String jpql = "select m from MachineEntity m where m.isUsed = false order by m.id";
        return em.createQuery(jpql, MachineEntity.class)
                .getResultList();
    }
}

