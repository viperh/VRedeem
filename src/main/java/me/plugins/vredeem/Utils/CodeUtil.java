package me.plugins.vredeem.Utils;

import java.util.ArrayList;
import java.util.List;

public class CodeUtil {

    private int index;
    private int max_usages;
    private int usages_per_player;
    private String code;
    private List<String> commands = new ArrayList<>();
    private List<String> messages = new ArrayList<>();
    private List<String> players = new ArrayList<>();

    public CodeUtil(int index, int max_usages, int usages_per_player, String code, List<String> commands, List<String> messages, List<String> players){
        this.index = index;
        this.max_usages = max_usages;
        this.usages_per_player = usages_per_player;
        this.code = code;
        this.commands = commands;
        this.messages = messages;
        this.players = players;
    }


    public int getIndex(){
        return index;
    }

    public int getMaxUsages(){
        return max_usages;
    }

    public int getUsagesPerPlayer(){
        return usages_per_player;
    }
    public String getCode(){
        return code;
    }
    public List<String> getCommands(){
        return commands;
    }

    public List<String> getMessages(){
        return messages;
    }

    public List<String> getPlayers(){
        return players;
    }










}
