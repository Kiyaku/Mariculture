package mariculture.core.blocks;

import java.util.Random;

import mariculture.core.Core;
import mariculture.core.Mariculture;
import mariculture.core.handlers.SpongePacket;
import mariculture.core.helpers.InventoryHelper;
import mariculture.core.lib.GuiIds;
import mariculture.core.lib.Modules;
import mariculture.core.lib.OresMeta;
import mariculture.core.lib.UtilMeta;
import mariculture.factory.blocks.TileDictionary;
import mariculture.factory.blocks.TileSawmill;
import mariculture.factory.blocks.TileSluice;
import mariculture.factory.blocks.TileSponge;
import mariculture.fishery.blocks.TileAutofisher;
import mariculture.fishery.blocks.TileIncubator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockUtil extends BlockMachine {
	private Icon[] incubatorIcons;
	private Icon sluiceBack;
	private Icon[] liquifierIcons;

	public BlockUtil(int i) {
		super(i, Material.piston);
	}

	@Override
	public float getBlockHardness(World world, int x, int y, int z) {
		switch (world.getBlockMetadata(x, y, z)) {
		case UtilMeta.AUTOFISHER:
			return 1.5F;
		case UtilMeta.BOOKSHELF:
			return 1F;
		case UtilMeta.DICTIONARY:
			return 2F;
		case UtilMeta.INCUBATOR_BASE:
			return 10F;
		case UtilMeta.INCUBATOR_TOP:
			return 6F;
		case UtilMeta.LIQUIFIER:
			return 5F;
		case UtilMeta.SAWMILL:
			return 2F;
		case UtilMeta.SETTLER:
			return 6F;
		case UtilMeta.SLUICE:
			return 4F;
		case UtilMeta.SPONGE:
			return 3F;
		case UtilMeta.PURIFIER:
			return 5F;
		}

		return 3F;
	}

	@Override
	public boolean isBlockNormalCube(World world, int x, int y, int z) {
		return false;
	}

	@Override
	public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side) {
		return true;
	}

	@Override
	public Icon getIcon(int side, int meta) {
		if (meta < getMetaCount()) {
			switch (meta) {
			case UtilMeta.BOOKSHELF:
				return Block.bookShelf.getIcon(side, meta);
			case UtilMeta.LIQUIFIER:
				return side > 1 ? icons[meta] : Core.oreBlocks.getIcon(side, OresMeta.BASE_BRICK);
			case UtilMeta.SETTLER:
				return side > 1 ? icons[meta] : Core.oreBlocks.getIcon(side, OresMeta.BASE_IRON);
			case UtilMeta.SAWMILL:
				return side > 1 ? icons[meta] : Core.oreBlocks.getIcon(side, OresMeta.BASE_WOOD);
			case UtilMeta.AUTOFISHER:
				return side > 1 ? icons[meta] : Core.oreBlocks.getIcon(side, OresMeta.BASE_WOOD);
			case UtilMeta.DICTIONARY:
				return side > 1 ? icons[meta] : Core.oreBlocks.getIcon(side, OresMeta.BASE_WOOD);
			case UtilMeta.SLUICE:
				return side == 4 ? icons[meta] : Core.oreBlocks.getIcon(side, OresMeta.BASE_IRON);
			case UtilMeta.SPONGE:
				return side > 1 ? icons[meta] : Core.oreBlocks.getIcon(side, OresMeta.BASE_IRON);
			default:
				return icons[meta];
			}
		}

		return icons[0];
	}

	@Override
	public Icon getBlockTexture(IBlockAccess block, int x, int y, int z, int side) {
		if (side > 1) {
			if (block.getBlockTileEntity(x, y, z) instanceof TileSluice) {
				TileSluice tile = (TileSluice) block.getBlockTileEntity(x, y, z);
				int tileSide = tile.getFacing();
				int blockSide = side - 2;
				
				switch(tileSide) {
					case 0: return (blockSide == 0)? icons[UtilMeta.SLUICE]: (blockSide == 1)? sluiceBack: Core.oreBlocks.getIcon(side, OresMeta.BASE_IRON);
					case 1: return (blockSide == 1)? icons[UtilMeta.SLUICE]: (blockSide == 0)? sluiceBack: Core.oreBlocks.getIcon(side, OresMeta.BASE_IRON); 
					case 2: return (blockSide == 2)? icons[UtilMeta.SLUICE]: (blockSide == 3)? sluiceBack: Core.oreBlocks.getIcon(side, OresMeta.BASE_IRON); 
					case 3: return (blockSide == 3)? icons[UtilMeta.SLUICE]: (blockSide == 2)? sluiceBack: Core.oreBlocks.getIcon(side, OresMeta.BASE_IRON); 
					default: return Core.oreBlocks.getIcon(side, OresMeta.BASE_IRON);
				}
			}

			if (block.getBlockTileEntity(x, y, z) instanceof TileLiquifier) {
				TileLiquifier liquifier = (TileLiquifier) block.getBlockTileEntity(x, y, z);
				if (liquifier.master() == null) {
					return this.getIcon(side, block.getBlockMetadata(x, y, z));
				}

				if (liquifier == liquifier.master()) {
					return liquifierIcons[1];
				} else {
					return liquifierIcons[0];
				}
			}

			if (block.getBlockTileEntity(x, y, z) instanceof TileIncubator && side > 1) {
				TileIncubator incubator = (TileIncubator) block.getBlockTileEntity(x, y, z);
				if (incubator.isBase(x, y - 1, z)) {
					if (incubator.isTop(x, y, z) && incubator.isTop(x, y + 1, z)) {
						if (!incubator.isTop(x, y + 2, z)) {
							return incubatorIcons[0];
						}
					}
				}

				if (incubator.isBase(x, y - 2, z)) {
					if (incubator.isTop(x, y, z) && incubator.isTop(x, y - 1, z)) {
						if (!incubator.isTop(x, y + 1, z)) {
							return incubatorIcons[1];
						}
					}
				}
			}
		}

		return this.getIcon(side, block.getBlockMetadata(x, y, z));
	}

	@Override
	public int idDropped(final int i, final Random random, final int j) {
		return this.blockID;
	}

	@Override
	public int damageDropped(final int i) {
		return i;
	}

	@Override
	public void onBlockPlacedBy(final World world, final int x, final int y, final int z,
			final EntityLivingBase entity, final ItemStack stack) {
		if (stack.getItemDamage() == UtilMeta.SLUICE) {
			final int facing = MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
			world.setBlockMetadataWithNotify(x, y, z, UtilMeta.SLUICE, 2);
			final TileSluice tile = (TileSluice) world.getBlockTileEntity(x, y, z);
			switch (facing) {
			case 0:
				tile.setFacing(0);
				break;
			case 1:
				tile.setFacing(3);
				break;
			case 2:
				tile.setFacing(1);
				break;
			case 3:
				tile.setFacing(2);
				break;
			}
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i, float f, float g, float t) {
		TileEntity tile_entity = world.getBlockTileEntity(x, y, z);

		if (tile_entity == null && world.getBlockMetadata(x, y, z) != UtilMeta.INCUBATOR_TOP || player.isSneaking()) {
			return false;
		}

		if (tile_entity instanceof TileAutofisher) {
			player.openGui(Mariculture.instance, GuiIds.AUTOFISHER, world, x, y, z);
			return true;
		}

		if (tile_entity instanceof TileSettler) {
			player.openGui(Mariculture.instance, GuiIds.SETTLER, world, x, y, z);
			return true;
		}

		if (tile_entity instanceof TileLiquifier) {
			TileLiquifier liquifier = (TileLiquifier) ((TileLiquifier) tile_entity).master();
			if (liquifier != null) {
				player.openGui(Mariculture.instance, GuiIds.LIQUIFIER, world, liquifier.xCoord, liquifier.yCoord,
						liquifier.zCoord);
				return true;
			}

			return false;
		}

		if (tile_entity instanceof TileBookshelf) {
			player.openGui(Mariculture.instance, GuiIds.BOOKSHELF, world, x, y, z);
			return true;
		}

		if (tile_entity instanceof TileSawmill) {
			player.openGui(Mariculture.instance, GuiIds.SAWMILL, world, x, y, z);
			return true;
		}

		if (tile_entity instanceof TileDictionary) {
			player.openGui(Mariculture.instance, GuiIds.DICTIONARY, world, x, y, z);
			return true;
		}

		if (tile_entity instanceof TileSluice) {
			TileSluice sluice = (TileSluice) tile_entity;
			if(sluice.getHeight() > 1) {
				player.openGui(Mariculture.instance, GuiIds.SLUICE, world, x, y, z);
				return true;
			} else {
				return false;
			}
		}

		if (tile_entity instanceof TileIncubator || world.getBlockMetadata(x, y, z) == UtilMeta.INCUBATOR_TOP) {
			TileIncubator incubator = ((TileIncubator) tile_entity).master();
			if (incubator != null) {
				player.openGui(Mariculture.instance, GuiIds.INCUBATOR, world, incubator.xCoord, incubator.yCoord,
						incubator.zCoord);
				return true;
			}

			return false;
		}
		
		if (tile_entity instanceof TileSponge) {
			if(world.isRemote && player instanceof EntityClientPlayerMP) {
				SpongePacket.requestPowerUpdate((EntityClientPlayerMP)player, tile_entity.xCoord, tile_entity.yCoord, tile_entity.zCoord);
			} else if(player.getCurrentEquippedItem() != null && !world.isRemote) {
				Item currentItem = player.getCurrentEquippedItem().getItem();
				if (currentItem instanceof IEnergyContainerItem && !world.isRemote) {
					int powerAdd = ((IEnergyContainerItem)currentItem).extractEnergy(player.getCurrentEquippedItem(), 5000, true);
					int reduce = ((IEnergyHandler)tile_entity).receiveEnergy(ForgeDirection.UP, powerAdd, false);
					((IEnergyContainerItem)currentItem).extractEnergy(player.getCurrentEquippedItem(), reduce, false);
				} 
			}
			return true;
		}

		return true;
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
	}

	@Override
	public TileEntity createTileEntity(World world, int meta) {
		switch (meta) {
		case UtilMeta.INCUBATOR_BASE:
			return new TileIncubator();
		case UtilMeta.INCUBATOR_TOP:
			return new TileIncubator();
		case UtilMeta.AUTOFISHER:
			return new TileAutofisher();
		case UtilMeta.LIQUIFIER:
			return new TileLiquifier();
		case UtilMeta.SETTLER:
			return new TileSettler();
		case UtilMeta.BOOKSHELF:
			return new TileBookshelf();
		case UtilMeta.SAWMILL:
			return new TileSawmill();
		case UtilMeta.SLUICE:
			return new TileSluice();
		case UtilMeta.DICTIONARY:
			return new TileDictionary();
		case UtilMeta.PURIFIER:
			return new TileConverter();
        case UtilMeta.SPONGE:
            return new TileSponge();
		}

		return null;

	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int i, int meta) {
		InventoryHelper.dropItems(world, x, y, z);
		super.breakBlock(world, x, y, z, i, meta);

		if (meta == UtilMeta.SLUICE) {
			clearWater(world, x + 1, y, z);
			clearWater(world, x - 1, y, z);
			clearWater(world, x, y, z + 1);
			clearWater(world, x, y, z - 1);
		}
	}

	private void clearWater(World world, int x, int y, int z) {
		if (world.getBlockMaterial(x, y, z) == Material.water) {
			world.setBlockToAir(x, y, z);
		}
	}

	@Override
	public boolean isActive(int meta) {
		switch (meta) {
		case UtilMeta.AUTOFISHER:
			return (Modules.fishery.isActive());
		case UtilMeta.SAWMILL:
			return (Modules.factory.isActive());
		case UtilMeta.INCUBATOR_BASE:
			return (Modules.fishery.isActive());
		case UtilMeta.INCUBATOR_TOP:
			return (Modules.fishery.isActive());
		case UtilMeta.SLUICE:
			return (Modules.factory.isActive());
		case UtilMeta.SPONGE:
			return (Modules.factory.isActive());
		case UtilMeta.DICTIONARY:
			return (Modules.factory.isActive());
		case UtilMeta.PURIFIER:
			return !Loader.isModLoaded("ThermalExpansion");

		default:
			return true;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		incubatorIcons = new Icon[2];
		incubatorIcons[0] = iconRegister.registerIcon(Mariculture.modid + ":incubatorBottom");
		incubatorIcons[1] = iconRegister.registerIcon(Mariculture.modid + ":incubatorTopTop");

		liquifierIcons = new Icon[2];
		liquifierIcons[0] = iconRegister.registerIcon(Mariculture.modid + ":liquifierTop");
		liquifierIcons[1] = iconRegister.registerIcon(Mariculture.modid + ":liquifierBottom");
		
		sluiceBack = iconRegister.registerIcon(Mariculture.modid + ":sluiceBack");

		icons = new Icon[getMetaCount()];

		for (int i = 0; i < icons.length; i++) {
			if (i != UtilMeta.BOOKSHELF) {
				icons[i] = iconRegister.registerIcon(Mariculture.modid + ":"
						+ getName(new ItemStack(this.blockID, 1, i)));
			}
		}
	}

	@Override
	public int getMetaCount() {
		return UtilMeta.COUNT;
	}
}
