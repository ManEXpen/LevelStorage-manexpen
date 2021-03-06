package manexpen.levelstorage.util;

import ic2.api.item.ElectricItem;
import ic2.api.util.Keys;
import manexpen.levelstorage.api.BootsFlyingEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;

@Deprecated
public class ArmorFunctions {
    public static HashMap<Integer, Integer> potionRemovalCost = new HashMap<>();
    public static HashMap<EntityPlayer, Boolean> onGroundMap = new HashMap<>();
    private static float jumpCharge;
    private static boolean waitJump = false;
    private static boolean repelFlag = false;

    public static HashMap<EntityPlayer, Integer> speedTickerMap = new HashMap<>();

    public static void extinguish(EntityPlayer ep, World w) {
        if (!w.isRemote) {
        }
        ep.extinguish();
    }

    public static void walkWater(World world, EntityPlayer player, ItemStack armor) {
//        if (LSKeyboard.getInstance().getIsKeyPressed(EnumKey.BEAMSHOOT) && player.inventory.getCurrentItem() == null) {
//            if (ElectricItem.manager.canUse(armor, ItemArmorAntimatterBase.EU_PER_TICK_WATERWALK)) {
//                if (!world.isRemote)
//                    ElectricItem.manager.use(armor, ItemArmorAntimatterBase.EU_PER_TICK_WATERWALK, player);
//                player.motionY = 0;
//            }
//        }
    }

    public static void jumpBooster(World world, EntityPlayer player, ItemStack itemStack) {
        boolean boostKey = Keys.instance.isBoostKeyDown(player);

        if (!onGroundMap.containsKey(player))
            onGroundMap.put(player, true);
        if (!world.isRemote) {
            boolean wasOnGround = onGroundMap.containsKey(player) ? ((Boolean) onGroundMap.get(player)).booleanValue()
                    : true;

            if ((wasOnGround) && (!player.onGround) && (Keys.instance.isJumpKeyDown(player))
                    && (Keys.instance.isBoostKeyDown(player))) {
                ElectricItem.manager.use(itemStack, 4000, null);
            }
            onGroundMap.remove(player);
            onGroundMap.put(player, Boolean.valueOf(player.onGround));
        } else {
            if ((ElectricItem.manager.canUse(itemStack, 4000)) && (player.onGround))
                jumpCharge = 2.0F;

            if ((player.motionY >= 0.0D) && (jumpCharge > 0.0F) && (!player.isInWater())) {
                if ((Keys.instance.isJumpKeyDown(player) && (Keys.instance.isBoostKeyDown(player)))) {
                    if (jumpCharge == 2.0F) {
                        player.motionX *= 5.0D;
                        player.motionZ *= 5.0D;
                    }

                    player.motionY += jumpCharge * 0.3F;
                    jumpCharge = ((float) (jumpCharge * 0.75D));
                } else if (jumpCharge < 1.0F) {
                    jumpCharge = 0.0F;
                }

            }
        }
    }

    public static void speedUp(EntityPlayer player, ItemStack itemStack) {
        if (!speedTickerMap.containsKey(player))
            speedTickerMap.put(player, 0);
        float speed = 0.66F;
        if ((ElectricItem.manager.canUse(itemStack, 1000)) && ((player.onGround) || (player.isInWater()))
                && (player.isSprinting())) {
            int speedTicker = speedTickerMap.containsKey(player) ? ((Integer) speedTickerMap.get(player)).intValue()
                    : 0;
            speedTicker++;

            if (speedTicker >= 10) {
                speedTicker = 0;
                ElectricItem.manager.use(itemStack, 1000, null);
            }
            speedTickerMap.remove(player);
            speedTickerMap.put(player, Integer.valueOf(speedTicker));

            if (player.isInWater()) {
                speed = 0.1F;
                if (Keys.instance.isJumpKeyDown(player))
                    player.motionY += 0.1000000014901161D;
            }

            if (speed > 0.0F)
                player.moveFlying(0.0F, 1.0F, speed);
        }
    }

    public static void antimatterLeggingsFunctions(World world, EntityPlayer player, ItemStack armor) {
//        if (world.isRemote)
//            return;
//        if (LSKeyboard.getInstance().getIsKeyPressed(EnumKey.BEAMSHOOT) && player.isSneaking()) {
//            int x = 0, y = 0, z = 0;
//            MovingObjectPosition mop = getMovingObjectPositionFromPlayer(world, player, true);
//            if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
//                if (ElectricItem.manager.canUse(armor, ItemArmorAntimatterBase.EU_PER_TELEPORT))
//                    ElectricItem.manager.use(armor, ItemArmorAntimatterBase.EU_PER_TELEPORT, player);
//                x = mop.blockX;
//                y = mop.blockY;
//                z = mop.blockZ;
//                int sideHit = mop.sideHit;
//                BlockLocation bl = new BlockLocation(x, y, z);
//                bl = bl.move(ForgeDirection.getOrientation(sideHit), 1);
//                player.setPositionAndUpdate(bl.getX(), bl.getY(), bl.getZ());
//            }
//        }
    }

    public static MovingObjectPosition getMovingObjectPositionFromPlayer(World par1World, EntityPlayer par2EntityPlayer,
                                                                         boolean par3) {
        float var4 = 1.0F;
        float var5 = par2EntityPlayer.prevRotationPitch
                + (par2EntityPlayer.rotationPitch - par2EntityPlayer.prevRotationPitch) * var4;
        float var6 = par2EntityPlayer.prevRotationYaw
                + (par2EntityPlayer.rotationYaw - par2EntityPlayer.prevRotationYaw) * var4;
        double var7 = par2EntityPlayer.prevPosX + (par2EntityPlayer.posX - par2EntityPlayer.prevPosX) * var4;
        double var9 = par2EntityPlayer.prevPosY + (par2EntityPlayer.posY - par2EntityPlayer.prevPosY) * var4 + 1.62D
                - par2EntityPlayer.yOffset;
        double var11 = par2EntityPlayer.prevPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.prevPosZ) * var4;
        Vec3 var13 = Vec3.createVectorHelper(var7, var9, var11);
        float var14 = MathHelper.cos(-var6 * 0.017453292F - (float) Math.PI);
        float var15 = MathHelper.sin(-var6 * 0.017453292F - (float) Math.PI);
        float var16 = -MathHelper.cos(-var5 * 0.017453292F);
        float var17 = MathHelper.sin(-var5 * 0.017453292F);
        float var18 = var15 * var16;
        float var20 = var14 * var16;
        double var21 = 5000.0D;
        Vec3 var23 = var13.addVector(var18 * var21, var17 * var21, var20 * var21);
        return par1World.func_147447_a(var13, var23, par3, !par3, true);
    }

    public static void helmetFunctions(World world, EntityPlayer player, ItemStack itemStack, int RAY_COST, int ENTITY_HIT_COST, int FOOD_COST) {

//        if (LSKeyboard.getInstance().getIsKeyPressed(EnumKey.BEAMSHOOT) && !player.isSneaking()) {
//            if (ElectricItem.manager.canUse(itemStack, RAY_COST)) {
//                ElectricItem.manager.use(itemStack, RAY_COST, player);
//                player.playSound(LevelStorage.MODNAME.toLowerCase() + ":plasma_shotgun_shot", 1.0F, 1.2F / 0.9F);
//                CallMWRay.shotMW(world, player);
//            }
//        }
//
//
//        if (!world.isRemote) {
//            if (player.getFoodStats().getFoodLevel() < 18) {
//                if (ElectricItem.manager.canUse(itemStack, FOOD_COST)) {
//                    ElectricItem.manager.use(itemStack, FOOD_COST, player);
//                    player.getFoodStats().addStats(20, 20);
//                }
//            }
//            if (player.getAir() < 100) {
//                player.setAir(200);
//            }
//            LinkedList<PotionEffect> lk = new LinkedList(player.getActivePotionEffects());
//            for (PotionEffect effect : lk) {
//                int id = effect.getPotionID();
//
//                Integer cost = (Integer) potionRemovalCost.get(Integer.valueOf(id));
//
//                if (cost != null) {
//                    cost = Integer.valueOf(cost.intValue() * (effect.getAmplifier() + 1));
//
//                    if (ElectricItem.manager.canUse(itemStack, cost.intValue())) {
//                        ElectricItem.manager.use(itemStack, cost.intValue(), null);
//                        player.removePotionEffect(id);
//                    }
//                }
//            }
//        }
    }

    public static void fly(int energy, EntityPlayer player, ItemStack itemStack, World world) {
        if (ElectricItem.manager.canUse(itemStack, energy)) {
            player.capabilities.allowFlying = true;
            if (player.capabilities.isFlying) {
                if (Keys.instance.isBoostKeyDown(player)) {
                    float boost = 0.44f;
                    player.moveFlying(0.0F, 1.0F, boost);
                    if (!world.isRemote) {
                        ElectricItem.manager.use(itemStack, energy * 3, player);
                    }
                }
                if (!world.isRemote) {
                    if (!player.capabilities.isCreativeMode) {
                        ElectricItem.manager.use(itemStack, energy, player);

                        MinecraftForge.EVENT_BUS.post(new BootsFlyingEvent(player, itemStack));
                    }
                }
            }
        }
    }

    public static void MultipleJump(World world, EntityPlayer player, ItemStack itemstack) {
        if (Keys.instance.isJumpKeyDown(player) && Keys.instance.isBoostKeyDown(player)
                && (ElectricItem.manager.canUse(itemstack, 1000))) {
            if (waitJump) {
                player.motionY = 1;
                player.motionY += 0.5;
                player.motionY *= 2.5D;
                player.motionY = 3.0D;
            }
            ElectricItem.manager.use(itemstack, 250, player);
            waitJump = false;
        } else {
            waitJump = true;
        }
    }

    public static void HighSpeedDown(World world, EntityPlayer player, ItemStack itemstack) {
        if (world.isRemote) {
            if (player.isSneaking() && Keys.instance.isBoostKeyDown(player) && !player.onGround
                    && player.motionY <= 0) {
                player.motionY *= 2;
                EntityPlayerHelper.repelEntityInRange(world, player, 3.5F);
            }
        }
    }

    public static void repelEntity(World world, EntityPlayer player, ItemStack itemstack) {
//
//        ((ItemArmorAntimatterBoots) itemstack.getItem()).repelFlag = NBTHelper.NBTReadFromBootsIsRepelEnable(itemstack);
//
//        if (((ItemArmorAntimatterBoots) itemstack.getItem()).repelFlag) {
//            EntityPlayerHelper.repelEntityInRange(world, player, 10);
//        }
//
//        if (LSKeyboard.getInstance().getIsKeyPressed(EnumKey.CHANGE_REPEL_MODE)) {
//            ((ItemArmorAntimatterBoots) itemstack.getItem()).repelFlag = !((ItemArmorAntimatterBoots) itemstack.getItem()).repelFlag;
//            player.addChatComponentMessage(new ChatComponentText("現在のモード: " + ((ItemArmorAntimatterBoots) itemstack.getItem()).repelFlag));
//            NBTHelper.NBTWriteToBoots(itemstack, ((ItemArmorAntimatterBoots) itemstack.getItem()).repelFlag);
//        }
    }

    public static void ResetSomeList() {
        onGroundMap.clear();
        speedTickerMap.clear();
        potionRemovalCost.put(Integer.valueOf(Potion.poison.id), Integer.valueOf(10000));
        potionRemovalCost.put(Integer.valueOf(Potion.wither.id), Integer.valueOf(25000));
        potionRemovalCost.put(Integer.valueOf(Potion.blindness.id), Integer.valueOf(10000));
        potionRemovalCost.put(Integer.valueOf(Potion.hunger.id), Integer.valueOf(1000));
    }
}
