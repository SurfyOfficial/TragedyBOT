package surfy.utils;

import surfy.API.GuildMember;
import surfy.API.MojangAPI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

public class ConfigManager {

    private static HashMap<Long,ApplicationForm> queueApplications = new HashMap<>();

    private GuildMember[] users;

    public ConfigManager(GuildMember[] users) {
        this.users = users;
    }

    private void create() throws Exception {
        Properties properties = new Properties();
        Arrays.asList(users)
                .stream()
                .forEach(user -> properties.setProperty(user.getUUID(), MojangAPI.getUsername(user.getUUID(),false)));
        FileOutputStream fos = new FileOutputStream("users.config");
        properties.store(fos,"Proprieties");
        System.out.println("Loaded Config");
        fos.close();
    }

    public void reload() throws Exception {
        File file = new File("users.config");
        file.delete();
        create();
    }

    public void addUser(String UUID,String username) throws Exception {
        Properties properties = new Properties();
        FileInputStream fi =new FileInputStream("users.config");
        properties.load(fi);
        if(properties.containsKey(UUID)) return;
        properties.setProperty(UUID,username);
        FileOutputStream fos = new FileOutputStream("users.config");
        properties.store(fos,"Proprieties");
        fos.close();
    }

    public boolean exist() {
        return new File("users.config").exists();
    }

    public Properties load() throws Exception{
        File file = new File("users.config");
        if(!file.exists()) create();
        Properties properties = new Properties();
        FileInputStream fi =new FileInputStream("users.config");
        properties.load(fi);
        return properties;
    }

    public static HashMap<Long, ApplicationForm> getQueueApplications() {
        return queueApplications;
    }
}