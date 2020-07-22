package com.ustctuixue.arcaneart.api.spell.interpreter.argument.entitylist;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;

import java.util.function.Predicate;

public interface IEntityPredicate extends Predicate<Entity>
{

    IEntityPredicate ENTITY = entity -> true;
    IEntityPredicate PLAYER = entity -> entity instanceof PlayerEntity;
    IEntityPredicate PROJECTILE = entity -> entity instanceof ProjectileItemEntity;
    IEntityPredicate ENEMY = entity -> entity instanceof IMob;
    IEntityPredicate ANIMAL = entity -> entity instanceof AnimalEntity;
    IEntityPredicate ALLY = entity -> entity instanceof TameableEntity;
    IEntityPredicate ITEM = entity -> entity instanceof ItemEntity;
    IEntityPredicate SELF = entity -> true;
}
