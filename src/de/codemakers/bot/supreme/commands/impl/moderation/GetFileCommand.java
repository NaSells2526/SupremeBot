package de.codemakers.bot.supreme.commands.impl.moderation;

import de.codemakers.bot.supreme.commands.Command;
import de.codemakers.bot.supreme.commands.arguments.ArgumentList;
import de.codemakers.bot.supreme.commands.invoking.Invoker;
import de.codemakers.bot.supreme.entities.MessageEvent;
import de.codemakers.bot.supreme.permission.PermissionRole;
import de.codemakers.bot.supreme.permission.PermissionRoleFilter;
import de.codemakers.bot.supreme.util.Standard;
import java.io.File;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

/**
 * GetFileCommand
 *
 * @author Panzer1119
 */
public class GetFileCommand extends Command {

    @Override
    public final void initInvokers() {
        addInvokers(Invoker.createInvoker("getFile", this), Invoker.createInvoker("gFile", this));
    }

    @Override
    public final boolean called(Invoker invoker, ArgumentList arguments, MessageEvent event) {
        return arguments != null && arguments.isSize(1, 2);
    }

    @Override
    public final void action(Invoker invoker, ArgumentList arguments, MessageEvent event) {
        final File file = new File(arguments.get(0));
        if (file.exists() && file.isFile()) {
            final Message message = new MessageBuilder().appendFormat("%s here is your requested file:", event.getAuthor().getAsMention()).build();
            if (arguments.size() == 2) {
                event.sendFile(file, arguments.get(1), message);
            } else {
                event.sendFile(file, message);
            }
        } else {
            event.sendMessageFormat(":warning: Sorry, %s the file \"%s\" wasn't found!", event.getAuthor(), arguments.get(0));
        }
    }

    @Override
    public final void executed(boolean success, MessageEvent event) {
        System.out.println("[INFO] Command '" + getCommandID() + "' was executed!");
    }

    @Override
    public final EmbedBuilder getHelp(Invoker invoker, EmbedBuilder builder) {
        builder.addField(invoker + " <File Path> [Visible Filename]", "Uploads a file from the bot to the current channel with optionally custom filename.", false);
        return builder;
    }

    @Override
    public final PermissionRoleFilter getPermissionRoleFilter() {
        final PermissionRole owner = PermissionRole.getPermissionRoleByName("Admin");
        final PermissionRole bot_commander = PermissionRole.getPermissionRoleByName("Bot_Commander");
        return (role, member) -> {
            if (role.isThisHigherOrEqual(owner) || role.isThisEqual(bot_commander)) {
                return true;
            }
            return Standard.isSuperOwner(member);
        };
    }

    @Override
    public final String getCommandID() {
        return getClass().getName();
    }

}
