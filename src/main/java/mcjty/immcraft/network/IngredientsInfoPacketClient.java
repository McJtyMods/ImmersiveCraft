package mcjty.immcraft.network;

import io.netty.buffer.ByteBuf;
import mcjty.immcraft.varia.BlockPosTools;
import mcjty.immcraft.varia.BlockTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public class IngredientsInfoPacketClient implements InfoPacketClient {

    private BlockPos pos;
    private List<String> ingredients;
    private List<String> missingIngredients;

    public IngredientsInfoPacketClient() {
    }

    public IngredientsInfoPacketClient(BlockPos pos, List<String> ingredients, List<String> missingIngredients) {
        this.pos = pos;
        this.ingredients = ingredients;
        this.missingIngredients = missingIngredients;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        ingredients = NetworkTools.readStringList(buf);
        missingIngredients = NetworkTools.readStringList(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        BlockPosTools.toBytes(pos, buf);
        NetworkTools.writeStringList(buf, ingredients);
        NetworkTools.writeStringList(buf, missingIngredients);
    }

    @Override
    public void onMessageClient(EntityPlayerSP player) {
        BlockTools.getTE(null, Minecraft.getMinecraft().theWorld, pos)
                .ifPresent(p -> p.setIngredients(ingredients, missingIngredients));
    }
}
