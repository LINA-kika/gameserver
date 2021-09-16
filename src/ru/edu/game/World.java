package ru.edu.game;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class World{
    private int id;
    private String worldName;
    private List<Entity> entites = new ArrayList<>();
    static int default_id=0;


    public World(){}
    public World(String worldName) {
        this.id = default_id;
        default_id++;
        this.worldName = worldName;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "World{" +
                "id=" + id +
                ", worldName='" + worldName + '\'' +
                ", entites=" + entites +
                '}';
    }

    public void update() throws SQLException {
        for (int i = entites.size()-1;i>=0;i--)
        {
          entites.get(i).update();
         }
    }

    public void addEntity (Entity new_entity)
    {
        entites.add(new_entity);
    }

    public List<Entity> getEntitiesInRegion (double x, double z, double range)
    {
        List<Entity> target_in_range = new ArrayList<>(entites);
        for (Entity e : target_in_range)
        {
            if (e.getId() == this.getId())
            {
                e.range_to = 20;
            }
            else
            {
                e.range_to = Math.sqrt(Math.pow(z-e.getPosZ(),2)+Math.pow(x-e.getPosX(),2));
            }
        }
        //System.out.println(target_in_range.toString());
        Collections.sort(target_in_range);
        //System.out.println(target_in_range.toString());
        return target_in_range;
    }


    public List<Entity> getEntitiesNearEntity (Entity entity, double range)
    {
        return this.getEntitiesInRegion(entity.getPosX(), entity.getPosZ(),20);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public List<Entity> getEntites() {
        return entites;
    }

    public void setEntites(List<Entity> entites) {
        this.entites = entites;
    }

    public void removeEntity(Entity entity) {
        entites.remove(entity);
    }
}
