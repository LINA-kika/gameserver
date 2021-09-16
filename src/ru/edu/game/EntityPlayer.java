package ru.edu.game;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.SQLException;

public class EntityPlayer extends Entity{
    private String nickname;
    private double exp;
    public EntityPlayer(Entity e,String nickname) throws SQLException {
        this.nickname = nickname;
        this.id = e.id;
        this.title = e.title;
        this.posX = e.posX;
        this.posZ = e.posZ;
        this.agressive = e.agressive;
        this.maxHealth = e.maxHealth;
        this.health = e.health;
        this.world = e.world;
        this.attackDamage = e.attackDamage;
        this.exp = 0;
        DatabaseUtils.insert_player(this);
    }
    public EntityPlayer() {}


    @Override
    public String toString() {
        return "EntityPlayer{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", posX=" + posX +
                ", posZ=" + posZ +
                ", agressive=" + agressive +
                ", maxHealth=" + maxHealth +
                ", health=" + health +
                ", attackDamage=" + attackDamage +
                ", target=" + target +
                ", isDead=" + isDead +
                ", range_to=" + range_to +
                ", nickname='" + nickname + '\'' +
                ", exp=" + exp +
                '}';
    }

    public void update() throws SQLException {
        super.update();
            if (GameServer.getServer_update() % 2 == 0) {
                if (this.health < this.maxHealth) {
                    this.setHealth(this.getHealth()+1);
                }
            }
        if (GameServer.getServer_update() % 5 == 0) {
            this.exp+=10*GameServer.getInstance().getDifficulty();
            DatabaseUtils.update_exp(this);
        }

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }
}
