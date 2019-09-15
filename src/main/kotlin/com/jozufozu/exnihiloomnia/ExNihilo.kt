package com.jozufozu.exnihiloomnia

import com.jozufozu.exnihiloomnia.advancements.ExNihiloTriggers
import com.jozufozu.exnihiloomnia.client.ores.models.ExNihiloOreModels
import com.jozufozu.exnihiloomnia.common.registries.command.CommandRegistry
import com.jozufozu.exnihiloomnia.proxy.CommonProxy
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fluids.FluidRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.File

@Mod(modid = ExNihilo.MODID, name = ExNihilo.NAME, version = ExNihilo.VERSION, modLanguage = "kotlin", modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter")
object ExNihilo {
    const val MODID = "exnihiloomnia"
    const val NAME = "Ex Nihilo Omnia"
    const val VERSION = "1.0"

    val log: Logger = LogManager.getLogger(NAME)

    @SidedProxy(serverSide = "com.jozufozu.exnihiloomnia.proxy.CommonProxy", clientSide = "com.jozufozu.exnihiloomnia.proxy.ClientProxy")
    lateinit var proxy: CommonProxy

    var PATH: File? = null

    init {
        FluidRegistry.enableUniversalBucket()
        ExNihiloTriggers.USE_SIEVE_TRIGGER.toString() // Ensure that the trigger is properly loaded before it could be used
        ExNihiloOreModels
    }

    @EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(proxy)
        proxy.preInit(event)
    }

    @EventHandler
    fun init(event: FMLInitializationEvent) {
        proxy.init(event)
    }

    @EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        proxy.postInit(event)
    }

    @EventHandler
    fun onServerStart(event: FMLServerStartingEvent) {
        event.registerServerCommand(CommandRegistry())
    }
}
