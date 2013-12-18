package mariculture.magic.jewelry.parts;

import net.minecraft.item.ItemStack;
import mariculture.core.Core;
import mariculture.core.lib.PrefixColor;
import mariculture.core.lib.Jewelry;
import mariculture.core.lib.PearlColor;

public class PartPearlYellow extends JewelryPart {
	@Override
	public boolean isVisible(int type) {
		return true;
	}
	
	@Override
	public String getPartName() {
		return "pearlYellow";
	}
	
	@Override
	public String getPartLang() {
		return "item.pearls." + getPartName() + ".name";
	}

	@Override
	public String getPartType(int type) {
		return (type == Jewelry.RING)? "jewel": "material";
	}
	
	@Override
	public String getColor() {
		return PrefixColor.YELLOW;
	}
	
	@Override
	public int getEnchantability() {
		return 4;
	}
	
	@Override
	public ItemStack getItemStack() {
		return new ItemStack(Core.pearls, 1, PearlColor.YELLOW);
	}
}
