/*
Copyright (C) 2017  lluiscab 
WhereAreMySlimes - Simple plugin to find slime chunks near the player

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package net.lluiscab.slimes;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class WhereAreMySlimes extends JavaPlugin implements Listener{
	
	Integer mapRadius = 5; // 3 chunks on every side
	Integer diamater = mapRadius *2; // Two sides + the middle
	
	@Override
	public void onEnable() {
				
		getServer().getPluginManager().registerEvents(this, this);
		
	}
	
	@Override
	public void onDisable() {
		
		Bukkit.broadcastMessage("ADEU GENT");
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String Label, String [] args) {
		 
		if (cmd.getName().equalsIgnoreCase("slime-finder")) {
			if (sender instanceof Player) {
				
				Player player = (Player) sender;
				
				if(sender.isOp()) {
					
					// Get player location and center of the map
					Location location = player.getLocation();
					Chunk center = location.getChunk();
					World world = player.getWorld();
					Integer centerX = center.getX();
					Integer centerZ = center.getZ();
										
					// Get the border of the map
					Integer upBorderX = centerX - this.mapRadius;
					Integer upBorderZ = centerZ - this.mapRadius;
					
					// Values used for the loop
					Integer diameterX = this.diamater; 
					Integer diameterZ = this.diamater;
					
					// Final chat message
					String message = ChatColor.GOLD + this.getSpaces() + "____-[ " + ChatColor.GRAY + location.getBlockX() + " " + location.getBlockZ() + ChatColor.GOLD + " ]-____";
					
					// The loop
					int e = 0;
					while(e <= diameterX) {
						
						message = message + "\n" + this.getSpaces();
						
						int a = 0;
						while (a <= diameterZ) {
							
							Integer chunkX = upBorderX + e;
							Integer chunkZ = upBorderZ + a;
							
							Chunk theChunk = world.getChunkAt(chunkX, chunkZ);
							
							if(this.isSlimeChunk(theChunk)) {
								
								if(theChunk.equals(center)) {
									message = message + ChatColor.GOLD + "+ ";
								} else {
									message = message + ChatColor.GOLD + "- ";
								}
								
								
							} else {
								
								if(theChunk.equals(center)) {
									message = message + ChatColor.AQUA +  "+ ";
								} else {
									message = message + ChatColor.DARK_GRAY +  "- ";
								}
								
							}
							
							a++;
						}
						
						if (e == 4) message = message + this.getSpaces() + this.getSpaces() + ChatColor.RED + "     E";	
						if(e == 5) message = message + this.getSpaces() + this.getSpaces() + ChatColor.RED + "  N  " + ChatColor.AQUA + "+" + ChatColor.RED + "  S";
						if (e == 6) message = message + this.getSpaces() + this.getSpaces() + ChatColor.RED + "     W";	

						
						e++;
					}										
					
					player.sendMessage("\n" + message + "\n");
					player.playSound(player.getLocation(), Sound.ENTITY_SLIME_SQUISH, 1, 1);
				} else {
					player.sendMessage(getPrefix() + "You don't have permission to run this command");
					player.playSound(player.getLocation(), Sound.ENTITY_SLIME_SQUISH, 1, 1);
				}
				
				
			} else {
				sender.sendMessage(this.getPrefix() + "Only players");
			}
			return true;
		}
		return false;
	}
	
	private String getPrefix() {
		return ChatColor.GREEN + "[" + ChatColor.DARK_GREEN + "SlimeFinder" + ChatColor.GREEN +  "] " + ChatColor.GRAY;
	}
	
	private String getSpaces() {
		return "      ";
		
	}
	
	private boolean isSlimeChunk(Chunk chunk) {
		
		Long seed = chunk.getWorld().getSeed();
		Integer x = chunk.getX();
		Integer z = chunk.getZ();
		
		Random check = new Random(seed + x * x * 4987142 + x * 5947611 + z * z * 4392871L + z * 389711 ^ 0x3AD8025F);
		
		if (check.nextInt(10) == 0) {
			return true;
		} else {
			return false;
		}
		
	}
	
}
