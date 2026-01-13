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
}
