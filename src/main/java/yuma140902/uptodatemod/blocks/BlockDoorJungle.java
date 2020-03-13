package yuma140902.uptodatemod.blocks;

import net.minecraft.item.Item;
import yuma140902.mcmodlib.blocks.BlockGenericDoor;
import yuma140902.uptodatemod.MyItems;

public class BlockDoorJungle extends BlockGenericDoor {
	
	@Override
	protected String getNameForTexture() {
		return "jungle_door";
	}
	
	@Override
	protected String getName() {
		return "door_jungle";
	}
	
	@Override
	protected Item getItem() {
		return MyItems.itemDoorJungle;
	}
	
}
