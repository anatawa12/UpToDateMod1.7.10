package yuma140902.uptodatemod;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.init.Items;
import yuma140902.uptodatemod.blocks.BlockStone;
import yuma140902.uptodatemod.config.ModConfigCore;
import yuma140902.uptodatemod.network.ArmorStandInteractHandler;
import yuma140902.uptodatemod.network.ArmorStandInteractMessage;
import yuma140902.uptodatemod.proxy.CommonProxy;
import yuma140902.uptodatemod.util.Stat;
import yuma140902.uptodatemod.util.UpdateChecker;
import yuma140902.uptodatemod.world.generation.MyMinableGenerator;

@Mod(modid = ModUpToDateMod.MOD_ID, name = ModUpToDateMod.MOD_NAME, version = ModUpToDateMod.MOD_VERSION, useMetadata = true, guiFactory = Stat.MOD_CONFIG_GUI_FACTORY)
public class ModUpToDateMod {
	@Mod.Metadata
	public static ModMetadata modMetadata;
	
	@Mod.Instance
	public static ModUpToDateMod INSTANCE;
	
	@SidedProxy(clientSide = Stat.PROXY_CLIENT, serverSide = Stat.PROXY_SERVER)
	public static CommonProxy proxy;
	
	public static SimpleNetworkWrapper networkWrapper;
	
	public static final String MOD_ID = "uptodate";
	public static final String MOD_NAME = "UpToDateMod";
	public static final String MOD_TEXTURE_DOMAIN = "uptodate";
	public static final String MOD_UNLOCALIZED_ENTRY_DOMAIN = "uptodate";
	public static final String MINECRAFT_VERSION = "1.7.10";
	public static final String MOD_VERSION = "1.4.1";
	public static final String MOD_VERSIONS_TSV_URL = "https://raw.githubusercontent.com/yuma140902/UpdateJSON_Forge/master/UpToDateModVersions.tsv";
	
	public static int glazedTerracottaRenderId;
	
	private void loadModMetadata(ModMetadata modMetadata) {
		modMetadata.modId = MOD_ID;
		modMetadata.name = MOD_NAME;
		modMetadata.version = MOD_VERSION;
		modMetadata.authorList.add("yuma140902");
		modMetadata.description = "Adds new Minecraft 1.8.0+ features";
		modMetadata.url = "https://minecraft.curseforge.com/projects/uptodatemod";
		modMetadata.autogenerated = false;
	}
	
	private void tweakVanilla() {
		Items.wooden_door.setMaxStackSize(64);
		Items.iron_door.setMaxStackSize(64);
		BlockTrapDoor.disableValidation = true;
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		loadModMetadata(modMetadata);
		ModConfigCore.loadConfig(event);
		try {
			UpdateChecker.INSTANCE.checkForUpdates();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(UpdateChecker.INSTANCE.hasNewVersionAvailable() ? "UpToDateMod: There is a new version available. - v" + UpdateChecker.INSTANCE.availableNewVersion + ". Visit " + UpdateChecker.INSTANCE.getNewVersionUrl() : "UpToDateMod is now up-to-date.");
		
		tweakVanilla();
		MyBlocks.register();
		MyItems.register();
		
		networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
		networkWrapper.registerMessage(ArmorStandInteractHandler.class, ArmorStandInteractMessage.class, 0, Side.SERVER);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		Recipes.removeVanillaRecipes();
		Recipes.register();
		
		proxy.registerEntities();
		glazedTerracottaRenderId = proxy.getNewRenderId();
		proxy.registerRenderers();
		
		
		MyMinableGenerator.Config stoneConfig = new MyMinableGenerator.Config(ModConfigCore.worldGen_genStones, 33, 10, 0, 80, ModConfigCore.worldGen_genStones_blackList);
		
		WorldGenerators.myMinableGenerator.addOreGenerator((Block) MyBlocks.stone, BlockStone.META_GRANITE, stoneConfig);
		WorldGenerators.myMinableGenerator.addOreGenerator((Block) MyBlocks.stone, BlockStone.META_DIORITE, stoneConfig);
		WorldGenerators.myMinableGenerator.addOreGenerator((Block) MyBlocks.stone, BlockStone.META_ANDESITE, stoneConfig);
		WorldGenerators.register();
		
		proxy.registerEventHandlers();
	}
}
