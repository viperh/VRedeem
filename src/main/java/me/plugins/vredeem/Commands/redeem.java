package me.plugins.vredeem.Commands;

import me.plugins.vredeem.Utils.CodeUtil;
import me.plugins.vredeem.VRedeem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class redeem implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage(VRedeem.instance.format("&cOnly players can run this command!"));
            return true;
        }

        Player player = (Player) sender;

        String mainPermission = VRedeem.permissionsMap.get("redeem");
        String parentPermission = VRedeem.permissionsMap.get("parent");


        if(!player.hasPermission(mainPermission) && !player.hasPermission(parentPermission)){
            player.sendMessage(VRedeem.instance.format("&cYou don't have permission to do this!"));
            return true;
        }

        if(args.length != 1){
            player.sendMessage(VRedeem.instance.format("&6Usage: /redeem [redeem_code]"));
        }

        String checkingCode = args[0];

        boolean codeIsValid = VRedeem.instance.codeValid(checkingCode);

        if(!codeIsValid){
            player.sendMessage(VRedeem.instance.format("&cThe code you entered is not valid!"));
            return true;
        }

        boolean playerExists = VRedeem.instance.existsPlayer(player.getName(), checkingCode);

        CodeUtil usingObject = VRedeem.instance.getCodeUtilByCode(checkingCode);
        if(!playerExists){
            usingObject.addUser(sender.getName());
        }

        int actual_max_usages = usingObject.getMaxUsages();
        int actual_usages = usingObject.getActualUsages();
        actual_usages += 1;

        usingObject.setUsages(actual_usages);

        int actual_index = usingObject.getIndex();

        if(actual_max_usages == actual_usages){
            VRedeem.instance.deleteSectionByIndex(actual_index);
            VRedeem.instance.getServer().getLogger().info("[VRedeem] - CodeSection deleted from config because code expired!");
        }
        return true;
    }
}
