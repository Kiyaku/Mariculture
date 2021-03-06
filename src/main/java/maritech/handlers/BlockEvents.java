package maritech.handlers;

import java.util.HashMap;

import mariculture.core.blocks.BlockMachine;
import mariculture.core.events.BlockEvent;
import mariculture.core.events.BlockEvent.BlockBroken;
import mariculture.core.events.BlockEvent.TilePlaced;
import maritech.util.IBlockExtension;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BlockEvents {
    public static HashMap<Class, IBlockExtension> blocks = new HashMap();
    
    public static void register(Class clazz, Class item, IBlockExtension extension) {
        blocks.put(clazz, extension);
        ItemEvents.register(item, extension);
    }
    
    @SubscribeEvent
    public void getIsActive(BlockEvent.GetIsActive event) {
        IBlockExtension extension = blocks.get(event.block.getClass());
        if (extension != null) {
            event.isActive = extension.isActive(event.meta, event.isActive);
        }
    }

    @SubscribeEvent
    public void isValidTab(BlockEvent.GetIsValidTab event) {
        IBlockExtension extension = blocks.get(event.block.getClass());
        if (extension != null) {
            event.isValid = extension.isValidTab(event.tab, event.meta, event.isValid);
        }
    }

    @SubscribeEvent
    public void getToolType(BlockEvent.GetToolType event) {
        IBlockExtension extension = blocks.get(event.block.getClass());
        if (extension != null) {
            event.tooltype = extension.getToolType(event.meta, event.tooltype);
        }
    }

    @SubscribeEvent
    public void getToolLevel(BlockEvent.GetToolLevel event) {
        IBlockExtension extension = blocks.get(event.block.getClass());
        if (extension != null) {
            event.level = extension.getToolLevel(event.meta, event.level);
        }
    }

    @SubscribeEvent
    public void getHardness(BlockEvent.GetHardness event) {
        IBlockExtension extension = blocks.get(event.block.getClass());
        if (extension != null) {
            event.hardness = extension.getHardness(event.meta, event.hardness);
        }
    }

    @SubscribeEvent
    public void getTileEntity(BlockEvent.GetTileEntity event) {
        IBlockExtension extension = blocks.get(event.block.getClass());
        if (extension != null) {
            event.tile = extension.getTileEntity(event.meta, event.tile);
        }
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent event) {
        if (event.action == Action.RIGHT_CLICK_BLOCK) {
            IBlockExtension extension = blocks.get(event.world.getBlock(event.x, event.y, event.z).getClass());
            if (extension != null) {
                extension.onRightClickBlock(event.world, event.x, event.y, event.z, event.entityPlayer);
            }
        }
    }

    @SubscribeEvent
    public void onBlockPlaced(TilePlaced event) {
        IBlockExtension extension = blocks.get(event.block.getClass());
        if (extension != null) {
            extension.onTilePlaced(event.stack, event.tile, event.entity, event.direction);
        }
    }

    @SubscribeEvent
    public void onBlockBroken(BlockBroken event) {
        IBlockExtension extension = blocks.get(event.block.getClass());
        if (extension != null) {
            event.setCanceled(extension.onBlockBroken(event.meta, event.world, event.x, event.y, event.z));
        }
    }
}
