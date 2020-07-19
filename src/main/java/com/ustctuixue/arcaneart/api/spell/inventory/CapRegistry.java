package com.ustctuixue.arcaneart.api.spell.inventory;

import com.ustctuixue.arcaneart.ArcaneArt;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = ArcaneArt.MOD_ID)
public class CapRegistry {
	@SubscribeEvent
	public void setup(FMLCommonSetupEvent event) {
		CapabilityManager.INSTANCE.register(ISpellInventory.class, new Capability.IStorage<ISpellInventory>() {
			@Override
			public INBT writeNBT(Capability<ISpellInventory> capability, ISpellInventory instance,
					net.minecraft.util.Direction side) {
				return instance.serializeNBT();
			}

			@Override
			public void readNBT(Capability<ISpellInventory> capability, ISpellInventory instance,
					net.minecraft.util.Direction side, INBT nbt) {
				instance.deserializeNBT((CompoundNBT) nbt);
			}
		}, () -> null);
	}

	@SubscribeEvent
	public void attachCapabilityEntity(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof PlayerEntity) {
			event.addCapability(ArcaneArt.getResourceLocation("spellinventory"), new SpellInventoryProvider());
		}
	}

	@SubscribeEvent
	public static void onPlayerCloned(PlayerEvent.Clone event) {
		LazyOptional<ISpellInventory> oldSpeedCap = event.getOriginal()
				.getCapability(SpellInventoryCapability.SPELL_INVENTORY_CAPABILITY);
		LazyOptional<ISpellInventory> newSpeedCap = event.getPlayer()
				.getCapability(SpellInventoryCapability.SPELL_INVENTORY_CAPABILITY);

		if (oldSpeedCap.isPresent() && newSpeedCap.isPresent()) {
			newSpeedCap.ifPresent((newCap) -> {
				oldSpeedCap.ifPresent((oldCap) -> {
					newCap.deserializeNBT(oldCap.serializeNBT());
				});
			});
		}
	}
}
