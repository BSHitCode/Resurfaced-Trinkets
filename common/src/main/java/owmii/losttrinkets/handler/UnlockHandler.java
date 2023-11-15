package owmii.losttrinkets.handler;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import owmii.losttrinkets.EnvHandler;
import owmii.losttrinkets.LostTrinkets;
import owmii.losttrinkets.api.trinket.ITrinket;
import owmii.losttrinkets.entity.Entities;
import owmii.losttrinkets.impl.LostTrinketsAPIImpl;
import owmii.losttrinkets.lib.util.Ticker;

import java.util.*;

public class UnlockHandler {
    private static final Map<UUID, Type> MAP = new HashMap<>();
    private static final Ticker DELAY = new Ticker(10);
    private static boolean flag;

    public static void tickPlayerOnServer(PlayerEntity player) {
        List<ITrinket> trinkets = LostTrinketsAPIImpl.UNLOCK_QUEUE.get(player.getUniqueID());
        if (trinkets != null) {
            trinkets.forEach(trinket -> UnlockManager.unlock(player, trinket, false));
        }
        LostTrinketsAPIImpl.UNLOCK_QUEUE.remove(player.getUniqueID());
        Iterator<UUID> itr = LostTrinketsAPIImpl.WEIGHTED_UNLOCK_QUEUE.iterator();
        while (itr.hasNext()) {
            if (itr.next().equals(player.getUniqueID())) {
                UnlockManager.unlock(player, false);
                itr.remove();
            }
        }
        checkUnlocks(player);
    }

    private static void checkUnlocks(PlayerEntity player) {
        if (LostTrinkets.config().unlockEnabled) {
            UUID id = player.getUniqueID();
            if (DELAY.isEmpty() && MAP.containsKey(id)) {
                if (player.world.rand.nextInt(MAP.get(id).getRandom()) == 0) {
                    UnlockManager.unlock(player, true);
                }
                flag = true;
            }
            if (flag) {
                DELAY.onward();
                MAP.remove(id);
                if (DELAY.ended()) {
                    DELAY.reset();
                    flag = false;
                }
            }
        }
    }

    private static void queueUnlock(PlayerEntity player, Type type) {
        if (!player.world.isRemote && !EnvHandler.INSTANCE.isFakePlayer(player)) {
            MAP.put(player.getUniqueID(), type);
        }
    }

    public static void trade(PlayerEntity player) {
        if (LostTrinkets.config().unlockEnabled && LostTrinkets.config().tradingUnlockEnabled) {
            if (!player.world.isRemote) {
                queueUnlock(player, Type.TRADING);
            }
        }
    }

    public static void kill(DamageSource source, LivingEntity target) {
        if (LostTrinkets.config().unlockEnabled) {
            Entity entity = source.getTrueSource();
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                if (!player.world.isRemote) {
                    if (Entities.isNonBossEntity(target)) {
                        if (LostTrinkets.config().killingUnlockEnabled) {
                            queueUnlock(player, Type.KILL);
                        }
                    } else if (LostTrinkets.config().bossKillingUnlockEnabled) {
                        queueUnlock(player, Type.BOSS_KILL);
                    }
                }
            }
        }
    }

    public static void checkBlockHarvest(PlayerEntity player, World world, BlockPos pos, BlockState state) {
        if (LostTrinkets.config().unlockEnabled) {
            if (!player.world.isRemote) {
                if (EnvHandler.INSTANCE.isOreBlock(state.getBlock())) {
                    if (LostTrinkets.config().oresMiningUnlockEnabled) {
                        queueUnlock(player, Type.ORE_MINE);
                    }
                } else if (BlockTags.CROPS.contains(state.getBlock())) {
                    if (LostTrinkets.config().farmingUnlockEnabled) {
                        queueUnlock(player, Type.FARM_HARVEST);
                    }
                } else if (BlockTags.LOGS.contains(state.getBlock())) {
                    if (LostTrinkets.config().woodCuttingUnlockEnabled) {
                        queueUnlock(player, Type.WOOD_CUTTING);
                    }
                }
            }
        }
    }

    public static void useHoe(PlayerEntity player) {
        if (LostTrinkets.config().unlockEnabled && LostTrinkets.config().farmingUnlockEnabled) {
            if (!player.world.isRemote) {
                queueUnlock(player, Type.FARM_HARVEST);
            }
        }
    }

    public static void bonemeal(PlayerEntity player) {
        if (LostTrinkets.config().unlockEnabled && LostTrinkets.config().farmingUnlockEnabled) {
            if (!player.world.isRemote) {
                queueUnlock(player, Type.FARM_HARVEST);
            }
        }
    }

    enum Type {
        KILL, BOSS_KILL, ORE_MINE, WOOD_CUTTING, FARM_HARVEST, TRADING;

        public int getRandom() {
            if (this == KILL) {
                return LostTrinkets.config().killing;
            } else if (this == BOSS_KILL) {
                return LostTrinkets.config().bossKilling;
            } else if (this == ORE_MINE) {
                return LostTrinkets.config().oresMining;
            } else if (this == TRADING) {
                return LostTrinkets.config().trading;
            } else if (this == FARM_HARVEST) {
                return LostTrinkets.config().farming;
            } else if (this == WOOD_CUTTING) {
                return LostTrinkets.config().woodCutting;
            }
            return Integer.MAX_VALUE;
        }
    }
}
