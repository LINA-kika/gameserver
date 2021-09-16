package ru.edu.game;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GameConfig {
    private String ip="127.0.0.1";
    private int port=25655;
    private int difficulty=2;
    private long updatePeriod=1000;
    private int savePeriod=5;
    @JsonIgnore
    private int savePeriodCounter=5;

    public GameConfig() {
    }

    public GameConfig(String ip, int port, int difficulty, long updatePeriod, int savePeriod) {
        this.ip = ip;
        this.port = port;
        this.difficulty = difficulty;
        this.updatePeriod = updatePeriod;
        this.savePeriod = savePeriod;
    }

    @Override
    public String toString() {
        return "GameConfig{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", difficulty=" + difficulty +
                ", updatePeriod=" + updatePeriod +
                ", savePeriod=" + savePeriod +
                '}';
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {

        if(difficulty<3&&difficulty>1) {
            this.difficulty = difficulty;
        }
        else System.out.println("Некорректный уровень сложности");
    }

    public long getUpdatePeriod() {
        return updatePeriod;
    }

    public void setUpdatePeriod(long updatePeriod) {
        this.updatePeriod = updatePeriod;
    }

    public int getSavePeriod() {
        return savePeriod;
    }

    public void setSavePeriod(int savePeriod) {
        this.savePeriod = savePeriod;
    }

    public int getSavePeriodCounter() {
        return savePeriodCounter;
    }

    public void setSavePeriodCounter(int savePeriodCounter) {
        this.savePeriodCounter = savePeriodCounter;
    }
}
