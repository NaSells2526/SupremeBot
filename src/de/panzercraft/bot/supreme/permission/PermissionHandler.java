package de.panzercraft.bot.supreme.permission;

import de.panzercraft.bot.supreme.util.Standard;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * PermissionHandler
 *
 * @author Panzer1119
 */
public class PermissionHandler {

    public static boolean check(PermissionRole minimumPermission, MessageReceivedEvent event) {
        if (minimumPermission == null) {
            return true;
        }
        if (event == null) {
            return false;
        }
        for (Role role : event.getGuild().getMember(event.getAuthor()).getRoles()) {
            final PermissionRole temp = PermissionRole.getPermissionRoleByGuildAndRoleId(event.getGuild(), role.getId());
            if (temp != null && temp.isThisHigherOrEqual(minimumPermission)) {
                return true;
            }
        }
        event.getTextChannel().sendMessage(Standard.getNoPermissionMessage(event.getAuthor(), "command")).queue();
        return false;
    }

}
