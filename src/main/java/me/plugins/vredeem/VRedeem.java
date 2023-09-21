package me.plugins.vredeem;

import me.plugins.vredeem.Commands.generate;
import me.plugins.vredeem.Commands.redeem;
import me.plugins.vredeem.Commands.reloadconfig;
import me.plugins.vredeem.Listeners.onJoin;
import me.plugins.vredeem.Utils.CodeUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@SuppressWarnings("all")
public final class VRedeem extends JavaPlugin {

    public static VRedeem instance;

    public static FileConfiguration config;

    public static String pattern;

    public static Map<String, String> permissionsMap = new HashMap<>();


    public static FileConfiguration database;

    public static char[] allChars = null;

    public Map<Integer, CodeUtil> utilMap;

    boolean disablePluginOnError;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        importDefaultConfig();
        importDatabase();
        getServer().getPluginManager().registerEvents(new onJoin(), this);
        registerCommands();
    }

    public void registerCommands(){
        getCommand("generate").setExecutor(new generate());
        getCommand("redeem").setExecutor(new redeem());
        getCommand("reloadconfig").setExecutor(new reloadconfig());
    }

    public CodeUtil getCodeUtilByCode(String code){
        CodeUtil returningCodeUtility = null;
        for(Map.Entry<Integer, CodeUtil> entry : utilMap.entrySet()){
            if(entry.getValue().getCode().equals(code)){
                returningCodeUtility = entry.getValue();
            }
        }
        return returningCodeUtility;
    }

    public void importDatabase(){
        File file = new File(getDataFolder().getPath(), "database.yml");

        if(!file.exists()){
            try{
                file.createNewFile();
            }
            catch(IOException e){
                e.printStackTrace();
                getServer().getLogger().severe("[VRedeem] - Database file couldn't be saved!");
                getServer().getLogger().severe("[VRedeem] - Disabling plugin! Contact developer or chec configs!");
                getServer().getPluginManager().disablePlugin(this);


            }
        }

        database = YamlConfiguration.loadConfiguration(file);

        if(database == null){
            getServer().getLogger().severe("[VRedeem] - Debug: 'database' variable is null!");
            getServer().getLogger().severe("[VRedeem] - Disabling plugin!");
            getServer().getPluginManager().disablePlugin(this);
        }

        utilMap = new HashMap<>();

        for(String indexString : database.getKeys(false)){
            int index = Integer.parseInt(indexString);
            ConfigurationSection configSec = database.getConfigurationSection(indexString);

            int max_usages = configSec.getInt("max-usages");
            int usages = configSec.getInt("actual-usages");
            String code = configSec.getString("code");
            List<String> commands = (List<String>) database.getList("commands");
            List<String> messages = (List<String>) database.getList("messages");
            List<String> players = (List<String>) database.getList("players");

            CodeUtil codeUtil = new CodeUtil(index, max_usages, code, commands, messages, players);

            utilMap.put(index, codeUtil);



        }



    }

    public void importConfig(){
        config = getConfig();
        pattern = config.getString("pattern");
        allChars = pattern.toCharArray();
        permissionsMap = new HashMap<>();

        String parentPermission = config.getString("parent");
        String generatePermission = config.getString("generate");
        String redeemPermission = config.getString("redeem");

        permissionsMap.put("parent", parentPermission);
        permissionsMap.put("generate", generatePermission);
        permissionsMap.put("redeem", redeemPermission);

    }

    public void updateYML(){

        File file = new File(getDataFolder().getPath(), "database.yml");

        for(String key : database.getKeys(false)){
            database.set(key, null);
        }


        try{
            if(file.exists()){
                file.delete();
                file.createNewFile();
                database = YamlConfiguration.loadConfiguration(file);
            }
            database.save(file);
        }
        catch(IOException e){
            getServer().getLogger().severe("[VRedeem] - Couldn't delete database! Contact developer or check console!");
            if(disablePluginOnError){
                getServer().getLogger().severe("[VRedeem] - Disabling plugin!");
                getServer().getPluginManager().disablePlugin(this);
            }
            e.printStackTrace();
        }

        for(Map.Entry<Integer, CodeUtil> entry : utilMap.entrySet()){

            Integer key = entry.getKey();
            CodeUtil value = entry.getValue();


            database.createSection(String.valueOf(key));

            ConfigurationSection configSec = database.getConfigurationSection(String.valueOf(key));

            configSec.set("max_usages", value.getMaxUsages());
            configSec.set("code", value.getCode());
            configSec.set("commands", value.getCommands());
            configSec.set("messages", value.getMessages());
            configSec.set("actual-usages", value.getActualUsages());
            configSec.set("players", value.getPlayers());

            try{
                database.save(file);
            }
            catch(IOException e){
                getServer().getLogger().severe("[VRedeem] - Couldn't save database! Contact developer or check console!");
                if(disablePluginOnError){
                    getServer().getLogger().severe("[VRedeem] - Disabling plugin!");
                    getServer().getPluginManager().disablePlugin(this);
                }
                e.printStackTrace();
            }

        }



    }

    public void deleteSectionByIndex(int index){
        utilMap.remove(index);
        updateYML();
    }

    public void updatePartYaml(int index, CodeUtil codeUtil){
        File file = new File(getDataFolder().getPath(), "database.yml");

        if(file.exists()){
            file.delete();
        }

        ConfigurationSection configSec = database.getConfigurationSection(String.valueOf(index));

        configSec.set("max_usages", codeUtil.getMaxUsages());
        configSec.set("code", codeUtil.getCode());
        configSec.set("commands", codeUtil.getCommands());
        configSec.set("messages", codeUtil.getMessages());
        configSec.set("players", codeUtil.getPlayers());

        try{
            database.save(file);
        }
        catch(IOException e){
            getServer().getLogger().severe("[VRedeem] - Couldn't save database! Check console or contact developer!");
            if(disablePluginOnError){
                getServer().getLogger().severe("[VRedeem] - Disabling plugin!");
                getServer().getPluginManager().disablePlugin(this);
            }
            e.printStackTrace();
        }



    }

    public boolean codeValid(String code){
        boolean valid = false;

        for(Map.Entry<Integer, CodeUtil> entry : utilMap.entrySet()){
            if(entry.getValue().getCode().equals(code)){
                valid = true;
                break;
            }
        }
        return valid;
    }
    public boolean existsPlayer(String playerName, String code){
        boolean found = false;

        for(Map.Entry<Integer, CodeUtil> entry : utilMap.entrySet()){
            if(entry.getValue().getCode().equals(code) && entry.getValue().getPlayers().contains(playerName)){
                found = true;
                break;
            }
        }

        return found;

    }

    public void importDefaultConfig(){
        File file = new File(getDataFolder().getPath(), "config.yml");

        if(!file.exists()){
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }

        config = getConfig();
        if(config == null){
            getServer().getLogger().severe("[VRedeem] - Config is null! Contact developer or add it manually to the folder!");
            getServer().getLogger().severe("[VRedeem] - Disabling plugin!");
            getServer().getPluginManager().disablePlugin(this);

        }
        else{
            pattern = config.getString("pattern");
            if(pattern == null){
                getServer().getLogger().severe("[VRedeem] - Pattern is null! Check config!");
                getServer().getLogger().severe("[VRedeem] - Disabling plugin!");
                getServer().getPluginManager().disablePlugin(this);
            }
            else{
                allChars = pattern.toCharArray();
            }

            ConfigurationSection configurationSection = config.getConfigurationSection("permissions");

            if(configurationSection == null){
                getServer().getLogger().severe("[VRedeem] - Permissions are null! Check config!");
                getServer().getLogger().severe("[VRedeem] - Disabling plugin!");
                getServer().getPluginManager().disablePlugin(this);
            }
            else{
                String generatePermission = configurationSection.getString("generate");
                String redeemPermission = configurationSection.getString("redeem");
                String parentPermission = configurationSection.getString("parent-permission");

                permissionsMap.put("parent", parentPermission);
                permissionsMap.put("generate", generatePermission);
                permissionsMap.put("redeem", redeemPermission);
            }

            disablePluginOnError = config.getBoolean("disable-plugin-on-error");
        }

    }

    public String generateCode(int length){

        Random random = new Random();

        String finalCode = "";

        for(int i = 0; i < length; i++){
            int actualIndex = random.nextInt(allChars.length);
            String actualLetter = String.valueOf(allChars[actualIndex]);
            finalCode = finalCode + actualLetter;
            // VRedeem.instance.getServer().getLogger().info("[VRedeem] - Debugging: - randomNumber: " + String.valueOf(actualIndex));
        }

        if(finalCode.toString().trim().equals("")){
            VRedeem.instance.getServer().getLogger().severe("[VRedeem] - Debugging: finalCode is empty!");
        }
        else{
            VRedeem.instance.getServer().getLogger().info("[VRedeem] - Debugging: - finalCode: " + finalCode);
        }

        return finalCode;
    }

    public String format(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
        updateYML();
    }
}
