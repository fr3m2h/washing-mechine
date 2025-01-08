package com.emse.washing_mechine.api;

import com.emse.washing_mechine.dao.MachineDao;
import com.emse.washing_mechine.dto.MachineDto;
import com.emse.washing_mechine.mapper.MachineMapper;
import com.emse.washing_mechine.model.MachineEntity;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController //
@RequestMapping("/api/machines") //
@Transactional //
public class MachineController {

    private final MachineDao machineDao;

    public MachineController(MachineDao machineDao){
        this.machineDao = machineDao;
    }

    @GetMapping
    public List<MachineDto> findAll(){
        return machineDao.findAll()
                .stream()
                .map(MachineMapper::of)
                .sorted(Comparator.comparing(MachineDto::name))
                .collect(Collectors.toList());  //
    }

    @GetMapping(path = "/{id}")
    public MachineDto findById(@PathVariable Long id) {
        return machineDao.findById(id).map(MachineMapper::of).orElse(null); //
    }

    @PostMapping //
    public ResponseEntity<MachineDto> create(@RequestBody MachineCommand machine) { //
        MachineEntity entity = new MachineEntity(machine.name(),machine.isUsed(), machine.timeLeft());
        entity.setIsUsed(machine.isUsed());
        MachineEntity saved = machineDao.save(entity);
        return ResponseEntity.ok(MachineMapper.of(saved));
    }

    @PutMapping(path = "/{id}") //
    public ResponseEntity<MachineDto> update(@PathVariable Long id, @RequestBody MachineCommand machine) {
        MachineEntity entity = machineDao.findById(id).orElse(null);
        if (entity == null) {
            return ResponseEntity.badRequest().build();
        }
        entity.setIsUsed(machine.isUsed());
        entity.setName(machine.name());
        entity.setTimeLeft(machine.timeLeft());
        // (11)
        return ResponseEntity.ok(MachineMapper.of(entity));
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable Long id) {
        machineDao.deleteById(id);
    }

}