package org.reactive.protocol;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface ReactiveCustomPayload extends CustomPacketPayload {

    @Override
    Type<? extends ReactiveCustomPayload> type();
}
