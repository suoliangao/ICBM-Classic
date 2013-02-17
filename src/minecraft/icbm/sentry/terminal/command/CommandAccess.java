package icbm.sentry.terminal.command;

import icbm.sentry.api.ISpecialAccess;
import icbm.sentry.api.ITerminal;
import icbm.sentry.platform.TileEntityTurretPlatform;
import icbm.sentry.terminal.AccessLevel;
import icbm.sentry.terminal.TerminalCommand;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Manipulates the access level of the turret platform.
 * 
 * @author Darkguardsman, Calclavia
 */
public class CommandAccess extends TerminalCommand
{

	@Override
	public String getCommandPrefix()
	{
		return "access";
	}

	@Override
	public boolean processCommand(EntityPlayer player, ITerminal terminal, String[] args)
	{
		if (args[0].equalsIgnoreCase("access") && args.length > 1 && args[1] != null && terminal instanceof TileEntityTurretPlatform)
		{
			TileEntityTurretPlatform turret = (TileEntityTurretPlatform) terminal;
			AccessLevel userAccess = terminal.getUserAccess(player.username);

			if (args[1].equalsIgnoreCase("?"))
			{
				terminal.addToConsole("Access Level: " + turret.getUserAccess(player.username).displayName);
				return true;
			}
			else if (args[1].equalsIgnoreCase("set") && args.length > 3 && userAccess.ordinal() >= AccessLevel.OPERATOR.ordinal())
			{
				String username = args[2];

				if (username.equalsIgnoreCase("~root"))
				{
					terminal.addToConsole("WIP");
				}
				else if (terminal.getUserAccess(username) != AccessLevel.NONE)
				{
					AccessLevel access = AccessLevel.get(args[3]);
					if (access != AccessLevel.NONE && terminal.addUserAccess(username, access, true))
					{
						terminal.addToConsole(username + " set to " + access.displayName);
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean canPlayerUse(EntityPlayer var1, ISpecialAccess mm)
	{
		return mm.getUserAccess(var1.username).ordinal() >= AccessLevel.BASIC.ordinal();
	}

	@Override
	public boolean showOnHelp(EntityPlayer player, ISpecialAccess mm)
	{
		return this.canPlayerUse(player, mm);
	}

	@Override
	public List<String> getCmdUses(EntityPlayer player, ISpecialAccess mm)
	{
		List<String> cmds = new ArrayList<String>();
		cmds.add("access set ~root [pass]");
		cmds.add("access set username level");
		cmds.add("access ?");
		return cmds;
	}

	@Override
	public boolean canMachineUse(ISpecialAccess mm)
	{
		return mm instanceof TileEntityTurretPlatform;
	}

}