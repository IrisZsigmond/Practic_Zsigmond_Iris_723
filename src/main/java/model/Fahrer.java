package model;

public class Fahrer {
    Integer id;
    String name;
    String team;
    FahrerStatus status;
    Integer skillLevel;

    public Fahrer() {};

    public Fahrer(Integer id, String name, String team, FahrerStatus status, Integer skillLevel) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.status = status;
        this.skillLevel = skillLevel;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getTeam() {
        return team;
    }
    public void setTeam(String team) {
        this.team = team;
    }

    public FahrerStatus getStatus() {
        return status;
    }
    public void setStatus(FahrerStatus status) {
        this.status = status;
    }

    public Integer getSkillLevel() {
        return skillLevel;
    }
    public void setSkillLevel(Integer skillLevel) {
        this.skillLevel = skillLevel;
    }
}
