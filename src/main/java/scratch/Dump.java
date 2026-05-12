package scratch;

import net.minecraft.world.level.block.SnifferEggBlock;

public class Dump {
    public static void main(String[] args) {
        for (java.lang.reflect.Method m : SnifferEggBlock.class.getDeclaredMethods()) {
            System.out.println(m.getName());
        }
    }
}
