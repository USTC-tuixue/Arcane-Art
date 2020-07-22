package com.ustctuixue.arcaneart.api.spell.compiler.argument;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;

public interface IEntitySelectorType
{
    boolean validate(Entity entity);

    IEntitySelectorType ENTITY = entity -> true;
    IEntitySelectorType PLAYER = entity -> entity instanceof PlayerEntity;
    IEntitySelectorType PROJECTILE = entity -> entity instanceof ProjectileItemEntity;
    IEntitySelectorType ENEMY = entity -> entity instanceof IMob;
    IEntitySelectorType ANIMAL = entity -> entity instanceof AnimalEntity;
    IEntitySelectorType ALLY = entity -> entity instanceof TameableEntity;
    IEntitySelectorType ITEM = entity -> entity instanceof ItemEntity;
}
