package me.github.azurehanchen;

import java.io.*;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
//import java.nio.charset.Charset;
import java.util.*;
import java.util.List;
//import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
//import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
//import org.bukkit.plugin.PluginAwareness;
//import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.yaml.snakeyaml.DumperOptions;

//import com.google.common.base.Charsets;
//import com.google.common.io.ByteStreams;

import me.clip.placeholderapi.PlaceholderAPI;

public class EasyLobby extends JavaPlugin implements Listener {

	public static List<String> worlds;
	
	static {
		EasyLobby.worlds = new ArrayList<String>();
	}
	
    final String version = "1.0.0";
    FileConfiguration config;
	
	@Override
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage("§f");
		Bukkit.getConsoleSender().sendMessage("§f");
		Bukkit.getConsoleSender().sendMessage("§f[§eEasyLobby] §f[§eINFO§f] §aEasyLobby §f加载成功 ");
		Bukkit.getConsoleSender().sendMessage("§f[§eEasyLobby] §f[§eINFO§f] §f本插件在MCBBS免费发布 作者AzureHanChen  ");
		Bukkit.getConsoleSender().sendMessage("§f");
		Bukkit.getConsoleSender().sendMessage("§f");
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			Bukkit.getConsoleSender().sendMessage("§f[§eEasyLobby] §f[§eINFO§f] §f成功Hook to §aPlaceholderAPI");
        } else {
    		Bukkit.getConsoleSender().sendMessage("§f[§eEasyLobby] §f[§cWARN] §c错误:未找到PlaceholderAPI,您可能不能在信息中使用PAPI变量");
        }
		
		//同类插件排斥
		//
		
		
		
		this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
		if(!getDataFolder().exists()) {
            getDataFolder().mkdir();
    }
		this.reload();
    
    if (this.config.getString("Version") != version) {
    	 saveDefaultConfig();
    }
    
   // EasyLobby.worlds = this.config.getStringList("EnableWorld");
    
    if (this.config.getBoolean("Update")) {
    	updateCheck();
    }
	}
	
    private String Fix(final Player p,String string) {
        String text = ChatColor.translateAlternateColorCodes('&', string);
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
        	text = PlaceholderAPI.setPlaceholders(p , text);
        }
        else {
        	
        }
        return text;
    }
    
    
    
	public String getLatestVersion(){
		String ver=null;
		try
		{
			URL url=new URL("https://raw.githubusercontent.com/AzureHanChen/EasyLobby-CheckUpdate/master/version.txt");
			InputStream is=url.openStream();
			BufferedReader br=new BufferedReader(new InputStreamReader(is,"UTF-8"));
			ver=br.readLine();
		}
		catch (Exception e)
		{
			
			e.printStackTrace();
			ver= "0.0.0";
			
			Bukkit.getConsoleSender().sendMessage("§f[§eEasyLobby] §f[§cWARN] §c错误:无法检查更新");
		}
		return ver;
   }
	public boolean isLatestVersion() 
	{
		boolean isLatest=false;
		String latest=getLatestVersion();
		String current=(version);
		if(latest.equalsIgnoreCase(current))
				{
			isLatest=true;
				}
		return isLatest;
	}
	public void updateCheck()
	{
		new BukkitRunnable()
		{
			public void run()
			{
				if(isLatestVersion())
				{
					Bukkit.getConsoleSender().sendMessage("§f");
					Bukkit.getConsoleSender().sendMessage("§f[§eEasyLobby§f] §f[§eINFO§f] §f您运行的是最新版本的插件 V" + (version));
					Bukkit.getConsoleSender().sendMessage("§f");
				}
				else
				{
					Bukkit.getConsoleSender().sendMessage("§f");
					Bukkit.getConsoleSender().sendMessage("§f[§eEasyLobby§f] §f[§cWARN§f] §e您运行的不是最新版本的插件!");
					Bukkit.getConsoleSender().sendMessage("§f[§eEasyLobby§f] §f[§cWARN§f] §f您的当前版本 V" + (version));
					Bukkit.getConsoleSender().sendMessage("§f[§eEasyLobby§f] §f[§cWARN§f] §f当前最新版本 V" + getLatestVersion());
					Bukkit.getConsoleSender().sendMessage("§f");
				}
			}
		}.runTaskTimerAsynchronously((this), 20L, 36000L);
	}
	
	public boolean onCommand(CommandSender sender,Command cmd,String label,String[] args)
	{
		if(label.equalsIgnoreCase("easylobby"))
		{
			if(args.length==0)

			{
	            	if(sender.hasPermission("easylobby.admin.info")){
	                	sender.sendMessage("§f");
	                	sender.sendMessage("§eＥａｓｙＬｏｂｂｙ");
	                	sender.sendMessage("§f");
	                	sender.sendMessage("§f插件版本: §e"+(version));
	                	sender.sendMessage("§f插件作者: §eAzureHanChen");
	                	sender.sendMessage("§e");
	                	sender.sendMessage("§f使用 §e§n/easylobby help§r §f获取帮助");         
	                    return true;
	                    
	            	}
	            	else {
						sender.sendMessage("§7[§eEasyLobby§7] §c您没有使用该命令的权限");
	                return true;
	            	}
				
			}
			
			else if(args[0].equalsIgnoreCase("reload"))
			{
					if (sender.hasPermission("easylobby.admin.reload")) {
						
						

					    	reload();
					    	//EasyLobby.worlds = this.config.getStringList("DisableWorld");

						
						sender.sendMessage("§7[§eEasyLobby§7] §a尝试重载插件配置中");
					return true;
					}
					else {
						sender.sendMessage("§7[§eEasyLobby§7] §c您没有使用该命令的权限");
					
				}
				return true;
			}

			
			else if(args[0].equalsIgnoreCase("help"))
			{
			
				if (sender.hasPermission("easylobby.admin.help")) {
					sender.sendMessage("§f");
					sender.sendMessage("§eＥａｓｙＬｏｂｂｙ Ｈｅｌｐ ( 1 / 1 )");
					sender.sendMessage("§f");
					sender.sendMessage("§f/easylobby reload §a重载插件配置");
					sender.sendMessage("§f/easylobby setlobby §a设置大厅");
					sender.sendMessage("§f");
					return true;
			}
				else {
					sender.sendMessage("§7[§eEasyLobby§7] §c您没有使用该命令的权限");
					return true;
				}
			}
			
			else if(args[0].equalsIgnoreCase("setlobby"))
			{
				Player p = (Player)sender;
				if (sender instanceof Player) {
				
			
				if (sender.hasPermission("easylobby.admin.setlobby")) {
					this.saveLocation((p).getLocation());
					sender.sendMessage("§7[§eEasyLobby§7] §a尝试设置大厅位置中");
					return true;
			}
				else {
					sender.sendMessage("§7[§eEasyLobby§7] §c您没有使用该命令的权限");
					return true;
				}
			}
				
					sender.sendMessage("§f[§eINFO§f] §a该命令只能由玩家进行");
					return true;
				
			}
			

		
			}
		return true;
		
	}
	
	/**
	 * 设置部分
	 */
	
    public void saveLocation(final Location loc) {
    	this.config.set("location.World", (Object)loc.getWorld().getName());
    	this.config.set("location.X", (Object)loc.getX());
    	this.config.set("location.Y", (Object)loc.getY());
    	this.config.set("location.Z", (Object)loc.getZ());
    	this.config.set("location.Pitch", (Object)loc.getPitch());
    	this.config.set("location.Yaw", (Object)loc.getYaw());
        this.saveConfig();
        this.reload();
    }
    
	
	//设置lobby 地址
    public Location getLocation(Player p) {
        final Location teleportLocation = new Location((World)null, 0.0, 0.0, 0.0);
        try {
            teleportLocation.setWorld(Bukkit.getWorld(this.config.getString("location.World")));
            teleportLocation.setX(this.getConfig().getDouble("location.X"));
            teleportLocation.setY(this.getConfig().getDouble("location.Y"));
            teleportLocation.setZ(this.getConfig().getDouble("location.Z"));
            teleportLocation.setYaw((float)this.getConfig().getLong("location.Yaw"));
            teleportLocation.setPitch((float)this.getConfig().getLong("location.Pitch"));
            return teleportLocation;
        }
        catch (Exception ex) {
            return p.getWorld().getSpawnLocation();
        }
    }
    
	
	/**
	 * 大厅部分
	 */
    
    @EventHandler
    
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent e) {
        if (this.config.getBoolean("EToEDamage")) {
            return;
        }
        if (e.getDamager() instanceof Player) {
            final Player p = (Player)e.getDamager();
            if (e.getEntity() instanceof ItemFrame && p.hasPermission("easylobby.admin")) {
                return;
            }
                if (e.getEntity() instanceof ArmorStand && p.hasPermission("easylobby.admin")) {
                	 if (p.getGameMode() == GameMode.CREATIVE) {
                		 return;
                	 }
                	 else {
                		 e.setCancelled(true);
                		 return;
                	 }
                    
                }
            
            e.setCancelled(true);
        }
    }
	
	//禁止破坏方块
	@EventHandler(priority = EventPriority.HIGHEST)
	public void PlayerBreakBlock(final BlockBreakEvent e) {
		 
				 
		
		if (this.config.getBoolean("Event.PlayerBreakBlock.Enable") ) {}
		
		else if (this.config.getStringList("EnableWorld").contains(e.getPlayer().getWorld().getName())) {
			Player p = e.getPlayer();
			if (p.hasPermission("easylobby.event.breakblock")) {
				if (p.getGameMode() == GameMode.CREATIVE) {
					return;
				}
				else {
					e.setCancelled(true);
					return;
					}
			}
			
			else if(this.config.getBoolean("Event.PlayerBreakBlock.ShowTips")) {
				p.sendMessage(Fix(e.getPlayer(),this.config.getString("Event.PlayerBreakBlock.Tips")));
					e.setCancelled(true);
					
					return;
		}
			else  {
				e.setCancelled(true);
				return;
		}
	
		 

			 }
		 }
	 
	 
	    
		@EventHandler
	    public void onFallDamage(final EntityDamageEvent e) {

	        if (e.getEntity() instanceof Player) {
	        	final Player p = (Player)e.getEntity();
	            
	        if (this.config.getBoolean("CanDamageEvent")) {
	            return;
	        }
	        else if (this.config.getStringList("EnableWorld").contains(p.getWorld().getName())) {
	        	 e.setCancelled(true);
	        }
	        	
	           
	        }
	    }
	 
	 
	 
	 
	@EventHandler(priority = EventPriority.HIGHEST)
	 public void fp1(final HangingBreakByEntityEvent e) {
	        if (e.isCancelled()) {
	            return;
	        }
	        if (this.config.getBoolean("Event.FrameProtect") && e.getRemover() instanceof Player) {
	        	final Player p = (Player)e.getRemover();
	        	if (p.hasPermission("easylobby.admin") && this.config.getStringList("EnableWorld").contains(p.getWorld().getName())) {
	        		if (p.getGameMode() == GameMode.CREATIVE) {
	        			return;
	        		}
	        		e.setCancelled(true);
	        		return;
	        	}
	        	else if (this.config.getStringList("EnableWorld").contains(p.getWorld().getName())) {
	        	e.setCancelled(true);
	        	return;
	        }
	        }
	 }
	 
	 
	 
	@EventHandler(priority = EventPriority.HIGHEST)
	 public void fp2(final HangingPlaceEvent e) {
	        if (e.isCancelled()) {
	            return;
	        }
	        if (this.config.getBoolean("Event.FrameProtect")){
	        	final Player p = e.getPlayer();
	        	if (p.hasPermission("easylobby.admin") && this.config.getStringList("EnableWorld").contains(p.getWorld().getName())) {
	        		if (p.getGameMode() == GameMode.CREATIVE) {
	        			return;
	        		}
	        		else {
	        		e.setCancelled(true);
	        		return;
	        	}
	        	
	        }
	        	e.setCancelled(true);
        		return;
	        }
	 }
	 
		//禁止放置方块
	 
	@EventHandler(priority = EventPriority.HIGHEST)
	public void PlayerPlaceBlock(final BlockPlaceEvent e) {
		 
		 
		 if(e.isCancelled() == true) {
			 return;
		 }
			 
			 else {
		
		if (this.config.getBoolean("Event.PlayerPlaceBlock.Enable") ) {
			return;
		}
		else {
			
			Player p = e.getPlayer();
			 if (p.hasPermission("easylobby.event.placeblock") && this.config.getStringList("EnableWorld").contains(p.getWorld().getName())) {
				if (p.getGameMode() == GameMode.CREATIVE) {
					return;
					
				}
				else {
					e.setCancelled(true);
					return;
				}
			}
			
			else if(this.config.getBoolean("Event.PlayerPlaceBlock.ShowTips") && this.config.getStringList("EnableWorld").contains(p.getWorld().getName())) {
				p.sendMessage(Fix(e.getPlayer(),this.config.getString("Event.PlayerPlaceBlock.Tips")));
					e.setCancelled(true);
					
					return;
				
				
			}
			else if (this.config.getStringList("EnableWorld").contains(p.getWorld().getName())) {
				e.setCancelled(true);
				return;
			}
		}
	}
		 }
	 
		//禁止下降饱食度 ok
	 
	@EventHandler(priority = EventPriority.HIGHEST)
	public void flc(final FoodLevelChangeEvent e) {
		
		 if(e.isCancelled() == true) {
			 return;
		 }
		 
		 else if (this.config.getBoolean("Event.CanPlayerHungry") ) {}
		else {
			if ( this.config.getStringList("EnableWorld").contains(e.getEntity().getWorld().getName())) {
				e.setCancelled(true);
				
			}
		}
		 
	}
	 
	 //
	 
	 
	
	 
	 
	 
		//禁止破坏悬浮物 
	 
	 
	
		//禁止丢弃物品 ok
		 @EventHandler(priority = EventPriority.HIGHEST)
		public void HBE(final PlayerDropItemEvent e) {
			 
			 
			 if(e.isCancelled() == false) {
			
			if (this.config.getBoolean("Event.CanPlayerDrop")) {
				
			}
			else {
				if (this.config.getStringList("EnableWorld").contains(e.getPlayer().getWorld().getName())) {

					e.setCancelled(true);
				}
			}
		}
		 }
		 
		 
			//禁止丢弃物品 ok
		 
		@EventHandler(priority = EventPriority.HIGHEST)
		public void HBE1(final EntityPickupItemEvent e) {
			 
			 if(e.isCancelled() == false) {
			
			if (this.config.getBoolean("Event.CanPlayerPickup")) {}
			else {
				if (this.config.getStringList("EnableWorld").contains(e.getEntity().getWorld().getName())) {
					e.setCancelled(true);
				}
			}
			
		}
		 }
	 
	//掉落虚空拉回
		 
	    @EventHandler(priority = EventPriority.HIGHEST)
	    public void onMove(final PlayerMoveEvent e) {
	    	final Player p = e.getPlayer();
	    	if (this.config.getBoolean("VoidSpawn.Enable")) {
       
	    		if (e.getTo().getY() <= -50.0 && this.config.getStringList("EnableWorld").contains(p.getWorld().getName())) {
	            
	    		if (this.config.getBoolean("VoidSpawn.UseDefaultLocation")) {
	            p.teleport(p.getWorld().getSpawnLocation());
	            if (this.config.getBoolean("VoidSpawn.ShowTips")) {
					p.sendMessage(Fix(e.getPlayer(),this.config.getString("VoidSpawn.Tips")));
	            }
	    		}
	    		else {
	    			p.setFallDistance(0.0f);
	    			final Location location = this.getLocation(e.getPlayer());
	    			p.teleport(location);
		            if (this.config.getBoolean("VoidSpawn.ShowTips")) {
						p.sendMessage(Fix(e.getPlayer(),this.config.getString("VoidSpawn.Tips")));
			            }
	    		}
	    		}
	    		
	    }
	    }
	    
	    
	    
	    @EventHandler
	    
	    public void onItemDamage(final PlayerItemDamageEvent e) {
	    	
	        if (this.config.getBoolean("Event.EnablePlayerPVP")) {
	            return;
	        }
	        if (this.config.getStringList("EnableWorld").contains(e.getPlayer().getWorld().getName())) {
	        
	        e.setCancelled(true);
	    }
	    }
	    
	    //永远晴天
	    
	    @EventHandler(priority = EventPriority.HIGHEST)
	    public void WeatherChangeEvent(final org.bukkit.event.weather.WeatherChangeEvent e) {
	        if (e.toWeatherState()) {
	        	if(e.isCancelled() == false && this.config.getStringList("EnableWorld").contains(e.getWorld().getName())) {
	        	if (this.config.getBoolean("Event.CanChangeWeather")) {
	        		
	        	}
	        	else {
	            e.setCancelled(true);
	        	}
	        }
	    }
	    }
	    
	    //除玩家外不允许生成实体

	    
	    //是否阻止爆炸
	    
	    @EventHandler(priority = EventPriority.HIGHEST)
	    public void EntityExplodeEvent(final EntityExplodeEvent e) {
	    	if(e.isCancelled() == false) {
	        if (this.config.getBoolean("Event.CanExplodeEvent") && this.config.getStringList("EnableWorld").contains(e.getEntity().getWorld().getName())) {}
	        else {
	            e.setCancelled(true);
	        }
	    }
	    }
	    
	    //tp
	    @EventHandler
	    public void onJoin(final PlayerJoinEvent e) {
	    	
	    	Player p = e.getPlayer();
	    	final String mode = this.config.getString("GameModeInJoin.Gamemode");
	        if (this.config.getBoolean("GameModeInJoin.Enable")) {
	            p.setGameMode(GameMode.valueOf(mode));
	        }
	    	
	    	if (this.config.getBoolean("JoinSpawn.Enable")) {
	    		
	    	
    		if (this.config.getBoolean("JoinSpawn.UseDefaultLocation")) {
	            p.teleport(p.getWorld().getSpawnLocation());
	    		}
	    		else {
	    			final Location location = this.getLocation(e.getPlayer());
	    			p.teleport(location);
	    		}
	    	}
	    }

	    
	    /**
	     * 配置部分
	     */
	    
	    public void reload() {
	        final File file = new File(this.getDataFolder(), "config.yml");
	        if (!file.exists()) {
	            this.saveDefaultConfig();
	        }
	        
	        this.reloadConfig();
	        DumperOptions yamlOptions = null;
	        try {
	            final Field f = YamlConfiguration.class.getDeclaredField("yamlOptions");
	            f.setAccessible(true);
	            yamlOptions = new DumperOptions() {
	                public void setAllowUnicode(final boolean allowUnicode) {
	                    super.setAllowUnicode(false);
	                }
	                
	                public void setLineBreak(final DumperOptions.LineBreak lineBreak) {
	                    super.setLineBreak(DumperOptions.LineBreak.getPlatformLineBreak());
	                }
	            };
	            yamlOptions.setLineBreak(DumperOptions.LineBreak.getPlatformLineBreak());
	            f.set(this.getConfig(), yamlOptions);
	        }
	        catch (ReflectiveOperationException ex) {}
	        this.config = this.getConfig();
	    }
	}
