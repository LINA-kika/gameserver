package ru.edu.game;

import java.sql.*;
import java.text.SimpleDateFormat;

public class DatabaseUtils {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/db_for_j?characterEncoding=UTF8&serverTimeZone=UTC",
                "root",
                "Kashka_dra_dra");
    }

    public static void create_table() throws SQLException {
        StringBuilder s1 = new StringBuilder("CREATE TABLE IF NOT EXISTS entites (e_id int(10) NOT NULL AUTO_INCREMENT, " +
                "title VARCHAR(100) NOT NULL, date_of_creation DATETIME NOT NULL, date_of_death DATETIME,PRIMARY KEY(e_id))");
        StringBuilder s2 = new StringBuilder("CREATE TABLE IF NOT EXISTS players (p_id int(10) NOT NULL, " +
                "nickname VARCHAR(100) NOT NULL, exp FLOAT(5,2) NOT NULL, FOREIGN KEY(p_id) REFERENCES entites(e_id))");
        StringBuilder s3 = new StringBuilder("CREATE TABLE IF NOT EXISTS battle_logs (b_id int(10) NOT NULL AUTO_INCREMENT, " +
                "win_id int(10) NOT NULL, looser_id int(10) NOT NULL, date_of_death DATETIME NOT NULL,PRIMARY KEY(b_id))");
        Connection c = getConnection();
        Statement s = c.createStatement();
        s.executeUpdate(s1.toString());
        s.executeUpdate(s2.toString());
        s.executeUpdate(s3.toString());
    }

    public  static void insert_entity(Entity e) throws SQLException {
        StringBuilder sb = new StringBuilder("INSERT INTO entites (e_id, title, date_of_creation) VALUES ( null, ");
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sb.append("\""+e.getTitle()+"\", "+"\'"+formatter.format(System.currentTimeMillis())+"\'"+")");
        Connection c = getConnection();
        System.out.println(sb);
        PreparedStatement ps = c.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
        ps.executeUpdate();
        ResultSet keys = ps.getGeneratedKeys();
        if(keys.next())
        {
            e.setId(keys.getInt(1));
        }
    }

    public  static void insert_entity_death_time(Entity e) throws SQLException {
        StringBuilder sb = new StringBuilder("UPDATE entites SET date_of_death = ");
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sb.append("\'"+formatter.format(System.currentTimeMillis())+"\'"+"WHERE e_id = "+e.getId());
        Connection c = getConnection();
        System.out.println(sb);
        Statement s = c.createStatement();
        s.executeUpdate(sb.toString());
    }

    public  static void insert_player(EntityPlayer p) throws SQLException {
        StringBuilder sb = new StringBuilder("INSERT INTO players VALUES (  ");
        sb.append(p.getId()+", "+"\""+p.getNickname()+"\""+", "+p.getExp()+")");
        Connection c = getConnection();
        System.out.println(sb);
        PreparedStatement ps = c.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
        ps.executeUpdate();
        ResultSet keys = ps.getGeneratedKeys();
        if(keys.next())
        {
            p.setId(keys.getInt(1));
        }
    }

    public  static void insert_battle(long winner_id, long looser_id) throws SQLException {
        StringBuilder sb = new StringBuilder("INSERT INTO battle_logs VALUES ( null,");
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sb.append("\""+winner_id+"\""+", "+looser_id+", "+"\'"+formatter.format(System.currentTimeMillis())+"\'"+")");
        System.out.println(sb);
        Connection c = getConnection();
        Statement s = c.createStatement();
        s.executeUpdate(sb.toString());
    }

    public static void set_exp(Entity e, EntityPlayer p) throws SQLException {
        StringBuilder sb = new StringBuilder("UPDATE players SET exp = exp + ");
        sb.append(GameServer.getInstance().getDifficulty()*e.getMaxHealth()+" where p_id ="+p.getId());
        Connection c = getConnection();
        System.out.println(sb);
        Statement s = c.createStatement();
        s.executeUpdate(sb.toString());
    }
    public static void update_exp(EntityPlayer p) throws SQLException {
        StringBuilder sb = new StringBuilder("UPDATE players SET exp = exp + ");
        sb.append(10*GameServer.getInstance().getDifficulty()+" where p_id ="+p.getId());
        Connection c = getConnection();
        System.out.println(sb);
        Statement s = c.createStatement();
        s.executeUpdate(sb.toString());
    }

    public static void get_exp(EntityPlayer p) throws SQLException {
        StringBuilder sb = new StringBuilder("SELECT exp FROM players WHERE p_id =  ");
        sb.append(p.getId());
        Connection c = getConnection();
        Statement s = c.createStatement();
        System.out.println(sb);
        ResultSet resultSet = s.executeQuery(sb.toString());
        if(resultSet.next())
        {
            p.setExp(resultSet.getFloat("exp"));
        }
    }

}
