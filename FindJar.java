public class FindJar {
    public static void main(String[] args) throws Exception {
        System.out.println(Class.forName("net.minecraft.world.entity.animal.sniffer.Sniffer").getProtectionDomain().getCodeSource().getLocation());
    }
}
