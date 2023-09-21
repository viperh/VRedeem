package me.plugins.vredeem.Commands;

import me.plugins.vredeem.VRedeem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class reloadconfig implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        String reloadPermission = VRedeem.instance.permissionsMap.get("reload");
        String parentPermission = VRedeem.instance.permissionsMap.get("parent");

        if(!sender.hasPermission(reloadPermission) && !sender.hasPermission(parentPermission)){
            sender.sendMessage(VRedeem.instance.format("&cYou don't have permission to do this!"));
        }

        if(args.length != 1){
            sender.sendMessage(VRedeem.instance.format("&6Usage: /reloadconfig default/database/all"));
            return true;
        }

        String option = args[0];

        if(option.equalsIgnoreCase("default")){
            VRedeem.instance.importConfig();
            sender.sendMessage(VRedeem.instance.format("&aThe default config was reloaded succesfully!"));
        }
        else if(option.equalsIgnoreCase("database")){
            VRedeem.instance.importDatabase();
            sender.sendMessage(VRedeem.instance.format("&aThe database was reloaded succesfully!"));
        }
        else if(option.equalsIgnoreCase("all")){
            VRedeem.instance.importConfig();
            VRedeem.instance.importDatabase();
            sender.sendMessage(VRedeem.instance.format("&aAll configs have been reloaded succesfully!"));
        }
        else{
            sender.sendMessage(VRedeem.instance.format("&6Usage: /reloadconfig default/database/all"));
        }


        return true;
    }
}
