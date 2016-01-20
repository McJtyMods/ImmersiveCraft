package mcjty.immcraft.network;

import io.netty.buffer.ByteBuf;
import mcjty.immcraft.varia.BlockPosTools;
import mcjty.immcraft.varia.BlockTools;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IngredientsInfoPacketServer implements InfoPacketServer {

    private BlockPos pos;

    public IngredientsInfoPacketServer() {
    }

    public IngredientsInfoPacketServer(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        BlockPosTools.toBytes(pos, buf);
    }

    @Override
    public Optional<InfoPacketClient> onMessageServer(EntityPlayerMP player) {
        List<String> ingredients = new ArrayList<>();
        List<String> missingIngredients = new ArrayList<>();
        BlockTools.getTE(null, player.worldObj, pos)
                .ifPresent(p -> p.calculateIngredients(ingredients, missingIngredients, player.inventory));
        return Optional.of(new IngredientsInfoPacketClient(pos, ingredients, missingIngredients));
    }
}
