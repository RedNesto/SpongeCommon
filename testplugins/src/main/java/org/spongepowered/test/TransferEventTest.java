/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.test;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.Command;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.item.inventory.ChangeInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;

/**
 * Bedrock in hoppers prevents them from working
 */
@Plugin(id = "hoppereventtest", name = "Hopper Event Test", description = "A plugin to test hopper event")
public class TransferEventTest {

    private TransferListener listener = new TransferListener();
    private boolean registered = false;

    @Listener
    public void onInit(GameInitializationEvent event) {
        Sponge.getCommandManager().register(this,
            Command.builder().setExecutor((source, context) -> {
                if (registered)
                {
                    this.registered = false;
                    Sponge.getEventManager().unregisterListeners(listener);
                }
                else
                {
                    this.registered = true;
                    Sponge.getEventManager().registerListeners(this, listener);
                }
                return CommandResult.success();
            }).build(), "toggleBedrockTransferBlockage");
    }

    public static class TransferListener {
        @Listener
        public void onPreTransferEvent(ChangeInventoryEvent.Transfer.Pre event) {
            if (event.getSourceInventory().queryAny(ItemStack.of(ItemTypes.BEDROCK, 1)).capacity() != 0) {
                event.setCancelled(true);
            }
        }
    }
}
