package morerightclickfunctions.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;

import morerightclickfunctions.server.MoreRightClickFunctionsMaps;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(ShovelItem.class)
public class GrindablesAdderMixin extends DiggerItem {
	public GrindablesAdderMixin(Tier tier, float attackDamage, float speed, Properties properties) {
		super(attackDamage, speed, tier, BlockTags.MINEABLE_WITH_PICKAXE, properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext useOnContext) {
		Level level = useOnContext.getLevel();
		BlockPos pos = useOnContext.getClickedPos();
		Player player = useOnContext.getPlayer();
		BlockState blockState = level.getBlockState(pos);
		ItemStack itemStack = useOnContext.getItemInHand();
		Optional<BlockState> grindable = Optional.ofNullable(getGrinded(blockState));
		if (grindable.isPresent()) {
			level.playSound(player, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
			level.setBlockAndUpdate(pos, grindable.get());
			if (player != null) {
				itemStack.hurtAndBreak(1, player, (toBroadcastTo) -> {
					toBroadcastTo.broadcastBreakEvent(useOnContext.getHand());
				});
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		} else {
			return InteractionResult.PASS;
		}
	}

	private static BlockState getGrinded(BlockState state) {
		Block block = MoreRightClickFunctionsMaps.GRINDABLES.build().get(state.getBlock());
		return block != null ? block.defaultBlockState() : null;
	}
}
