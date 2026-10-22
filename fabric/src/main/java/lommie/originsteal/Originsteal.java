package lommie.originsteal;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;

public class Originsteal implements ModInitializer {

    @Override
    public void onInitialize() {

        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();
        ServerPlayerEvents.AFTER_RESPAWN.register(((oldPlayer, newPlayer, alive) ->
                Events.onPlayerDeath(oldPlayer,newPlayer)));
    }
}
