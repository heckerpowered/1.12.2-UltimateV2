package ultimate.common.command;

import java.util.Collections;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import ultimate.common.util.UltimateUtil;

public final class CommandRemake extends CommandBase {

    @Override
    public String getName() {
        return "remake";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/remake";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            EntityPlayer player = getCommandSenderAsPlayer(sender);
            UltimateUtil.kill(player);
            notifyCommandListener(sender, this, "commands.kill.successful",
                    new Object[] { player.getDisplayName() });
        } else {
            Entity entity = getEntity(server, sender, args[0]);
            UltimateUtil.kill(entity);
            notifyCommandListener(sender, this, "commands.kill.successful", new Object[] { entity.getDisplayName() });
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
            BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames())
                : Collections.emptyList();
    }
}
