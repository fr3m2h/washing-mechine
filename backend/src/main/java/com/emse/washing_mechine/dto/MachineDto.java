package com.emse.washing_mechine.dto;


import java.util.Set;

public record MachineDto(Long id, String name, Boolean isUsed, Boolean isHs,Double timeLeft) {}
