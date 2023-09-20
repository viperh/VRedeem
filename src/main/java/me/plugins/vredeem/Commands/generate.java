package me.plugins.vredeem.Commands;

import me.plugins.vredeem.Utils.CodeUtil;
import me.plugins.vredeem.VRedeem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

@SuppressWarnings("all")
public class generate implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        String parentPermission = VRedeem.instance.permissionsMap.get("parent");
        String mainPermission = VRedeem.instance.permissionsMap.get("generate");


        if(!sender.hasPermission(parentPermission) && ! sender.hasPermission(mainPermission)){
            sender.sendMessage(VRedeem.instance.format("&cYou don't have the permission to do this!"));
            return true;
        }



        if(args.length < 1){
            sender.sendMessage(VRedeem.instance.format("&6Usage: /vgenerate [length]"));
            return true;
        }

        int length = -1;
        try{
            length = Integer.parseInt(args[0]);
        }
        catch(NumberFormatException e){
            sender.sendMessage(VRedeem.instance.format("&cEnter a valid number!"));
            return true;
        }

        if(length != -1){
            String generatedCode = VRedeem.instance.generateCode(length);


            int finalIndex = -1;

            for(Map.Entry<Integer, CodeUtil> entry : VRedeem.instance.utilMap.entrySet()){
                finalIndex = entry.getKey();
            }


            finalIndex += 1;

            sender.sendMessage(VRedeem.instance.format("&6-------------------------------------------------------"));
            sender.sendMessage(VRedeem.instance.format("&9&lThis plugin only generates the code. You will have to configure it in the config.yml file!!"));
            sender.sendMessage(VRedeem.instance.format("&6-------------------------------------------------------"));


            if(generatedCode.equals("")){
                sender.sendMessage(VRedeem.instance.format("&6&lCode: &9&lCode is empty! Contact developer!"));
            }
            else{
                sender.sendMessage(VRedeem.instance.format("&6&lCode: &9&l" + generatedCode));
            }

            StringSelection selection = new StringSelection(generatedCode);

            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

            clipboard.setContents(selection, selection);


            sender.sendMessage(VRedeem.instance.format("&9&lThe code has been copied to your clipboard"));

            sender.sendMessage(VRedeem.instance.format("&6-------------------------------------------------------"));



            CodeUtil codeUtility = new CodeUtil(finalIndex, 0, generatedCode, Arrays.asList("", ""), Arrays.asList("", ""), Arrays.asList("", ""));

            VRedeem.instance.utilMap.put(finalIndex, codeUtility);
            VRedeem.instance.updateYML();

        }
        else{
            VRedeem.instance.getServer().getLogger().severe("[VRedeem] - Debug: Length is -1!");
            return true;
        }






        return true;
    }



}
