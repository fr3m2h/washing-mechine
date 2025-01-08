package com.emse.washing_mechine.model;

import jakarta.persistence.*;

@Entity
@Table(name="SP_MACHINE")
public class MachineEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(name = "is_used", nullable = false)
    private Boolean isUsed;

    @Column(name = "time_left", nullable = false)
    private Double timeLeft;

    public MachineEntity(){
    }

    public MachineEntity (String name, Boolean isUsed, Double timeLeft)
    {
        this.name = name;
        this.isUsed = isUsed;
        this.timeLeft = timeLeft;
    }

    public MachineEntity (Long id, String name, Boolean isUsed, Double timeLeft)
    {
        this.name = name;
        this.isUsed = isUsed;
        this.timeLeft = timeLeft;
    }

    // getteur et setteur for id
    public Long getId()
    {
        return this.id;
    }
    public void setId(Long id)
    {
        this.id=id;
    }

    public String getName() {return this.name;}
    public void setName(String name)
    {
        this.name=name;
    }

    public Double getTimeLeft()
    {
        return this.timeLeft;
    }
    public void setTimeLeft(Double timeLeft) {this.timeLeft=timeLeft;}

    public Boolean getIsUsed()
    {
        return this.isUsed;
    }
    public void setIsUsed(Boolean isUsed)
    {
        this.isUsed=isUsed;
    }


}
