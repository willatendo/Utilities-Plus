package willatendo.extraitemuses.events;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import willatendo.extraitemuses.server.event.ModSetup;
import willatendo.extraitemuses.server.util.ExtraItemUsesUtils;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = ExtraItemUsesUtils.ID)
public class ForgeServerEvents {
    @SubscribeEvent
    public static void addRightClickFunctions(OnDatapackSyncEvent event) {
        RegistryAccess registryAccess = event.getPlayerList().getServer().registryAccess();

        ModSetup.addRightClickFunctions(registryAccess, true, true, true);
    }

    @SubscribeEvent
    public static void registerDataPackRegistries(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        InteractionHand interactionHand = event.getHand();
        InteractionResult interactionResult = ModSetup.interact(player, event.getLevel(), interactionHand, event.getHitVec());
        if (interactionResult == InteractionResult.SUCCESS) {
            player.swing(interactionHand);
        }
    }
}
