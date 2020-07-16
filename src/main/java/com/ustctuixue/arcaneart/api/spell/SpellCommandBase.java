package com.ustctuixue.arcaneart.api.spell;

import com.ustctuixue.arcaneart.api.util.II18nName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

@RequiredArgsConstructor
public class SpellCommandBase implements
        IForgeRegistryEntry<SpellCommandBase>,
        II18nName
{
    private ResourceLocation registryName;

    @Getter
    private String translationKey;


    /******************************
     * Forge Registry Starts Here
     ******************************/

    @Override
    public SpellCommandBase setRegistryName(ResourceLocation name)
    {
        this.registryName = name;
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName()
    {
        return registryName;
    }


    @Override
    public Class<SpellCommandBase> getRegistryType()
    {
        return SpellCommandBase.class;
    }

}
