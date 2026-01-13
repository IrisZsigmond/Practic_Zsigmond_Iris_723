package model;

public class Strafe {
    Integer id;
    Integer fahrerId;
    StrafeGrund grund;
    Integer seconds;
    Integer lap;

    public Strafe() {}

    public Strafe(Integer id, Integer fahrerId, StrafeGrund grund, Integer seconds, Integer lap) {
        this.id = id;
        this.fahrerId = fahrerId;
        this.grund = grund;
        this.seconds = seconds;
        this.lap = lap;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFahrerId() {
        return fahrerId;
    }
    public void setFahrerId(Integer fahrerId) {
        this.fahrerId = fahrerId;
    }

    public StrafeGrund getGrund() {
        return grund;
    }
    public void setGrund(StrafeGrund grund) {
        this.grund = grund;
    }

    public Integer getSeconds() {
        return seconds;
    }
    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    public Integer getLap() {
        return lap;
    }
    public void setLap(Integer lap) {
        this.lap = lap;
    }
}
