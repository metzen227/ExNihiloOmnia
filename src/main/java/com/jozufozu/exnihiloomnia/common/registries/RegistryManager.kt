package com.jozufozu.exnihiloomnia.common.registries

import com.jozufozu.exnihiloomnia.ExNihilo
import com.jozufozu.exnihiloomnia.common.lib.LibRegistries
import com.jozufozu.exnihiloomnia.common.registries.recipes.*
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import net.minecraft.world.World
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod.EventBusSubscriber(modid = ExNihilo.MODID)
object RegistryManager {
    val ORES = ReloadableRegistry.create(Ore::class.java, LibRegistries.ORE)

    val COMPOST = ReloadableRegistry.create(CompostRecipe::class.java, LibRegistries.COMPOST)
    val FLUID_CRAFTING = ReloadableRegistry.create(FluidCraftingRecipe::class.java, LibRegistries.FLUID_CRAFTING)
    val FLUID_MIXING = ReloadableRegistry.create(FluidMixingRecipe::class.java, LibRegistries.MIXING)
    val FERMENTING = ReloadableRegistry.create(FermentingRecipe::class.java, LibRegistries.FERMENTING)

    val SIFTING = ReloadableRegistry.create(SieveRecipe::class.java, LibRegistries.SIEVE)
    val HAMMERING = ReloadableRegistry.create(HammerRecipe::class.java, LibRegistries.HAMMER)

    val MELTING = ReloadableRegistry.create(MeltingRecipe::class.java, LibRegistries.MELTING)
    val HEAT = ReloadableRegistry.create(HeatSource::class.java, LibRegistries.HEAT_SOURCE)

    fun getHammerRewards(world: World, hammer: ItemStack, player: EntityPlayer, toHammer: IBlockState): NonNullList<ItemStack> {
        val drops = NonNullList.create<ItemStack>()

        if (toHammer.block === Blocks.AIR)
            return drops

        for (hammerRecipe in HAMMERING) {
            if (hammerRecipe.matches(toHammer)) {
                drops.addAll(hammerRecipe.rewards.roll(player, hammer, world.rand))
            }
        }

        return drops
    }

    /**
     * Whether or not the given input can generate rewards
     */
    fun siftable(input: ItemStack): Boolean {
        for (recipe in SIFTING) {
            if (recipe.matches(input))
                return true
        }

        return false
    }

    /**
     * Whether or not the given block can be hammered
     */
    fun hammerable(input: IBlockState): Boolean {
        for (recipe in HAMMERING) {
            if (recipe.matches(input))
                return true
        }

        return false
    }

    fun getCompost(input: ItemStack): CompostRecipe? {
        for (recipe in COMPOST) {
            if (recipe.matches(input))
                return recipe
        }

        return null
    }

    fun getMixing(fluidIn: FluidStack, fluidOn: FluidStack): FluidMixingRecipe? {
        for (recipe in FLUID_MIXING) {
            if (recipe.matches(fluidIn, fluidOn))
                return recipe
        }

        return null
    }

    fun getFluidCrafting(catalyst: ItemStack, fluidStack: FluidStack): FluidCraftingRecipe? {
        for (recipe in FLUID_CRAFTING) {
            if (recipe.matches(catalyst, fluidStack))
                return recipe
        }

        return null
    }

    fun getMelting(input: ItemStack): MeltingRecipe? {
        for (recipe in MELTING) {
            if (recipe.matches(input))
                return recipe
        }

        return null
    }

    fun getHeat(source: IBlockState): Int {
        for (heatSource in HEAT) {
            if (heatSource.matches(source))
                return heatSource.heatLevel
        }

        return 0
    }
}
