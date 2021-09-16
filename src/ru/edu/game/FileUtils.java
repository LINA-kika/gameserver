package ru.edu.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;


public class FileUtils {

    public static void saveConfig(File path, GameConfig config) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(path, config);
        System.out.println("I saved the config: "+config.toString());

    }
    public static GameConfig loadConfig(File path) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        GameConfig g = mapper.readValue(path, GameConfig.class);
        System.out.println("I load the config: "+g.toString());
        return g;

    }
    public static void saveWorld(File path, World world) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(path, world);
        System.out.println("I saved the world: "+world.toString());

    }
    public  static World loadWorld(File path)  throws IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        World w = mapper.readValue(path, World.class);
        System.out.println("I load the world: "+w.toString());
        return w;
    }


}
