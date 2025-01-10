package com.emse.washing_mechine.api;


public record MachineCommand(String name, Boolean isUsed,Boolean isHs, Double timeLeft){
}


