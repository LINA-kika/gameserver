package ru.edu.game;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EntityPlayer.class, name = "entityplayer"),
})
public class Entity  implements Comparable<Entity> {
    protected long id;
    protected String title;
    protected double posX;
    protected double posZ;
    protected boolean agressive;
    protected   int maxHealth;
    protected int health;
    protected   int attackDamage;
    protected Entity target;
    protected Boolean isDead=false;
    protected double range_to;
    @JsonIgnore
    protected World world;

    public Entity(String title, double posX, double posZ, boolean agressive, int maxHealth, int health, int attackDamage, World world) throws SQLException {
        this.title = title;
        this.posX = posX;
        this.posZ = posZ;
        this.agressive = agressive;
        this.maxHealth = maxHealth;
        this.health = health;
        this.world = world;
        this.attackDamage = attackDamage;
        DatabaseUtils.insert_entity(this);
    }
    public Entity(){}

    @Override
    public String toString() {
        return "\n"+"Entity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", posX=" + posX +
                ", posZ=" + posZ +
                ", agressive=" + agressive +
                ", maxHealth=" + maxHealth +
                ", health=" + health +
                ", attackDamage=" + attackDamage +
                ", isDead="+isDead+
                ", range to"+range_to+
                '}';
    }
    public int compareTo(Entity e){

        return Double.compare(this.range_to, e.range_to);
    }
    public void go(Entity target) throws SQLException {
            double x = target.getPosX() - this.getPosX();
            double z = target.getPosZ() - this.getPosZ();
            double path_length = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
            if (0 < path_length && path_length < 2) {
                this.attack(target);
            } else if (path_length < 20) {
                if (this.getPosX() > target.getPosX()) {
                    this.setPosX(this.getPosX() - 1);
                } else this.setPosX(this.getPosX() + 1);
                if (this.getPosZ() > target.getPosZ()) {
                    this.setPosZ(this.getPosZ() - 1);
                } else this.setPosZ(this.getPosZ() + 1);
            }
        //System.out.println(this.title + " goes to "+target.title);
    }


    public void update() throws SQLException {
        if (this.agressive == true) {

            {
                if (target == null) {
                    target = this.searchTarget();
                    if (target == null) {
                        //System.out.println("There is no enemy for " + this.title);
                    } else {
                        this.go(target);
                    }
                } else {
                    this.go(target);
                }
            }
        }
    }



        public Entity searchTarget()
    {
        List<Entity> targets =  new ArrayList<>();
        targets = GameServer.getInstance().getServerworld().getEntitiesNearEntity(this, 20);
        //System.out.println(targets.toString());
        for (Entity e:targets)
        {
            if(e.isAgressive()==false)
            {
                //System.out.println(this.title+" has enemy "+e.title);
                return e;
            }
        }
        return null;
    }

    public void delete_Entity(Entity entity) {

        entity.setDead(true);
        GameServer.getInstance().getServerworld().removeEntity(entity);
        for (Entity e: GameServer.getInstance().getServerworld().getEntites())
        {
            e.target=null;
        }
        //System.out.println(world.getEntites().toString());
    }

    public void attack(Entity entity) throws SQLException {
        double attack_value = this.attackDamage + 0.5 * GameServer.getInstance().getDifficulty();
        if (entity.getHealth()>0) {entity.setHealth(entity.getHealth()-(int)attack_value);}
        if (entity.getHealth()<0)
        {
            //System.out.println(this.title + "(" + this.getHealth() + " HP) "+" killed "+entity.title);
            DatabaseUtils.insert_entity_death_time(entity);
            DatabaseUtils.insert_battle(this.getId(),entity.getId());
            delete_Entity(entity);
        }
        if (entity instanceof EntityPlayer)
        {
            if (entity.getHealth()>0)
            {
                double new_health = this.getHealth()-(entity.attackDamage+0.5*GameServer.getInstance().getDifficulty());
                //System.out.println(entity.title+" attacked back");
                this.setHealth((int)new_health);
                if(this.getHealth()<0)
                {
                    DatabaseUtils.insert_entity_death_time(this);
                    DatabaseUtils.set_exp(this, (EntityPlayer) entity);
                    DatabaseUtils.insert_battle(entity.getId(),this.getId());
                    ((EntityPlayer) entity).setExp(((EntityPlayer) entity).getExp()+GameServer.getInstance().getDifficulty()*this.getMaxHealth());
                    //System.out.println(entity.title + " (" + entity.getHealth() + " HP) "+" killed "+this.title);
                    delete_Entity(this);
                }
            }
        }
        //System.out.println("attack ended"+this.title);

    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosZ() {
        return posZ;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }

    public boolean isAgressive() {
        return agressive;
    }

    public void setAgressive(boolean agressive) {
        this.agressive = agressive;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }
    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public Boolean getDead() {
        return isDead;
    }

    public void setDead(Boolean dead) {
        isDead = dead;
    }
}


