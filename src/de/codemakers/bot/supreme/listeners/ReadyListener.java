package de.panzercraft.bot.supreme.listeners;

import de.panzercraft.bot.supreme.permission.PermissionRole;
import de.panzercraft.bot.supreme.util.Standard;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * ReadyListener
 *
 * @author Panzer1119
 */
public class ReadyListener extends ListenerAdapter {

    @Override
    public final void onReady(ReadyEvent event) {
        final JDA jda = event.getJDA();
        for (Guild guild : jda.getGuilds()) {
            guild.getTextChannels().get(0).sendMessage("Hello!").queue();
            Standard.getAdvancedGuild(guild);
        }
        if (true) {
            return;
        }
        String out = "\nThis Bot is running on following Servers: \n";
        for (Guild guild : jda.getGuilds()) {
            out += guild.getName() + " (" + guild.getId() + ") \n";
            for (Role role : guild.getRoles()) {
                out += String.format("%nROLE: \"%s\" (ID: %s) %s%nLoaded ROLE: %s", role.getName(), role.getId(), role.getAsMention(), PermissionRole.getPermissionRoleByGuildIdAndRoleId(guild.getId(), role.getId()));
            }
            out += "\n";
        }
        System.out.println(out);
    }

    @Override
    public final void onReconnect(ReconnectedEvent event) {
        //onReady(event);
    }

}