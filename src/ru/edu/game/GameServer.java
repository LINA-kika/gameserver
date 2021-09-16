package ru.edu.game;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;


public class GameServer {
    private static GameServer instance;

    public static void main(String[] args) throws IOException, SQLException {
        //System.out.println(instance.toString());
        File f_config = new File("config.json");
        if (f_config.createNewFile())
            System.out.println("File created");
        else
            System.out.println("File already exists");

        File f_world = new File("world.json");
        if (f_world.createNewFile())
            System.out.println("File created");
        else
            System.out.println("File already exists");
        new GameServer(f_config, f_world);
        for (int i = 0; i <= 30; i++) {
            instance.updateServer(f_world);
            try {
                Thread.sleep(getInstance().config.getUpdatePeriod());
              } catch (InterruptedException e) {
                 e.printStackTrace();
              }
        }
        //System.out.println(instance.toString());
        FileUtils.saveConfig(f_config, instance.getConfig());
    }

    public static GameServer getInstance() {
        return instance;
    }

    @Override
    public String toString() {
        return "GameServer{" +
                "ip='" + ip + '\'' +
                ", difficulty=" + difficulty +
                ", serverworld=" + serverworld +
                '}';
    }

    private static int server_update = 0;
    private String ip;
    private int difficulty;
    private World serverworld;
    private int serverTicks;
    private GameConfig config;

    public static int getServer_update() {
        return server_update;
    }

    private GameServer(File f_config, File f_world) {
        instance = this;
        this.ip = "192.168.0.19";
        this.difficulty = 2;
        try
        {
            instance.loadConfig(f_config);
            instance.loadWorld(f_world);
        }
        catch (IOException | SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void updateServer(File path) throws SQLException {
        serverworld.update();
        serverTicks++;
        server_update++;
        if(config.getSavePeriodCounter()==0)
        {
            try
            {
                FileUtils.saveWorld(path,serverworld);
                config.setSavePeriodCounter(5);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        config.setSavePeriodCounter(config.getSavePeriodCounter()-1);

    }



    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public World getServerworld() {
        return serverworld;
    }

    public void setServerworld(World serverworld) {
        this.serverworld = serverworld;
    }

    public GameConfig getConfig() {
        return config;
    }

    public void setConfig(GameConfig config) {
        this.config = config;
    }

    private void loadConfig(File path)  throws IOException
    {
        try
        {
            this.config = FileUtils.loadConfig(path);
        }
        catch (MismatchedInputException e)
        {
            e.printStackTrace();
            System.out.println("There is no config to load, so we would create a default config");
            this.config = new GameConfig();
        }
    }
    private void loadWorld(File path) throws IOException, SQLException {
        try
        {
            this.serverworld = FileUtils.loadWorld(path);
            for(Entity e: this.serverworld.getEntites())
            {
                if(e instanceof EntityPlayer)
                {
                    DatabaseUtils.get_exp((EntityPlayer) e);
                }
            }
        }
        catch (MismatchedInputException e)
        {
            e.printStackTrace();
            System.out.println("There is no world to load, so we would create a default world");
            World serverworld = new World("tutorial");
            this.serverworld = serverworld;
            Entity[] entites = new Entity[4];
            DatabaseUtils.create_table();
            entites[0] = new Entity("Tom", 0, 0, true, 50, 50, 2,serverworld);
            entites[1] = new Entity("Dog", 0,15,true,60,60,5,serverworld);
            entites[2] = new Entity("Bird", 0, 7, false, 150, 150, 6,serverworld);
            entites[3] = new EntityPlayer(entites[2],"Alina");
            this.serverworld.addEntity(entites[0]);
            this.serverworld.addEntity(entites[1]);
            this.serverworld.addEntity(entites[3]);


        }

    }
}
