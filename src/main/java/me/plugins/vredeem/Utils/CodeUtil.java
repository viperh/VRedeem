package me.plugins.vredeem.Utils;

import java.util.ArrayList;
import java.util.List;

public class CodeUtil {

    private int index;
    private int max_usages;

    private String code;
    private List<String> commands = new ArrayList<>();
    private List<String> messages = new ArrayList<>();
    private List<String> players = new ArrayList<>();

    int actual_usages;

    public CodeUtil(int index, int max_usages, String code, List<String> commands, List<String> messages, List<String> players){
        this.actual_usages = 0;
        this.index = index;
        this.max_usages = max_usages;
        this.code = code;
        this.commands = commands;
        this.messages = messages;
        this.players = players;
    }

    public void addUser(String username){
        players.add(username);

    }

    public void setUsages(int new_max_usages){
        actual_usages = new_max_usages;
    }

    public int getActualUsages(){
        return actual_usages;
    }

    public int getIndex(){
        return index;
    }

    public int getMaxUsages(){
        return max_usages;
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
