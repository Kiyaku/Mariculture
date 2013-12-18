package mariculture.magic.jewelry.parts;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import mariculture.core.Core;
import mariculture.core.lib.PrefixColor;
import mariculture.core.lib.Jewelry;
import mariculture.core.lib.PearlColor;

public class PartDiamond extends JewelryPart {
	@Override
	public boolean isValid(int type) {
		return (type == Jewelry.RING)? true: false;
	}
	
	@Override
	public boolean isVisible(int type) {
		return true;
	}
	
	@Override
	public String getPartName() {
		return "diamond";
	}
	
	@Override
	public String getPartLang() {
		return "part.jewel." + getPartName();
	}

	@Override
	public String getPartType(int type) {
		return (type == Jewelry.RING)? "jewel": "blank";
	}
	
	@Override
	public String getColor() {
		return PrefixColor.AQUA;
	}
	
	@Override
	public int getEnchantability() {
		return 8;
	}
	
	@Override
	public ItemStack getItemStack() {
		return new ItemStack(Item.diamond);
	}
}
