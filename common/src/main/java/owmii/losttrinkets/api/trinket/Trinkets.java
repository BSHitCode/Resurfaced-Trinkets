package owmii.losttrinkets.api.trinket;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import owmii.losttrinkets.LostTrinkets;
import owmii.losttrinkets.api.LostTrinketsAPI;
import owmii.losttrinkets.api.player.PlayerData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Trinkets {
    private final List<ITrinket> available = new ArrayList<>();
    private final List<ITrinket> active = new ArrayList<>();
    private final List<ITickableTrinket> tickable = new ArrayList<>();
    private final List<ITargetingTrinket> targeting = new ArrayList<>();
    private final PlayerData data;
    private int slots = 1;
    private boolean slotsSet;

    public Trinkets(PlayerData data) {
        this.data = data;
    }

    public NbtCompound serializeNBT() {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("slots", this.slots);
        nbt.putBoolean("slots_set", this.slotsSet);
        NbtList availableTrinkets = new NbtList();
        this.available.forEach((trinket) -> {
            NbtCompound nbt1 = new NbtCompound();
            Identifier location = Registry.ITEM.getId(trinket.asItem());
            Objects.requireNonNull(location);
            nbt1.putString("trinket", location.toString());
            availableTrinkets.add(nbt1);
        });
        nbt.put("available_trinkets", availableTrinkets);
        NbtList activeTrinkets = new NbtList();
        this.active.forEach((trinket) -> {
            NbtCompound nbt1 = new NbtCompound();
            Identifier location = Registry.ITEM.getId(trinket.asItem());
            Objects.requireNonNull(location);
            nbt1.putString("trinket", location.toString());
            activeTrinkets.add(nbt1);
        });
        nbt.put("active_trinkets", activeTrinkets);
        return nbt;
    }

    public void deserializeNBT(NbtCompound nbt) {
        this.slots = nbt.getInt("slots");
        this.slotsSet = nbt.getBoolean("slots_set");
        NbtList availableTrinkets = nbt.getList("available_trinkets", (byte)10);
        this.available.clear();
        for (int i = 0; i < availableTrinkets.size(); i++) {
            NbtCompound nbt1 = availableTrinkets.getCompound(i);
            Item trinket = Registry.ITEM.get(new Identifier(nbt1.getString("trinket")));
            if (trinket instanceof ITrinket) {
                this.available.add((ITrinket) trinket);
            }
        }

        NbtList activeTrinkets = nbt.getList("active_trinkets", (byte)10);
        this.active.clear();
        this.tickable.clear();
        this.targeting.clear();
        for (int i = 0; i < activeTrinkets.size(); i++) {
            NbtCompound nbt1 = activeTrinkets.getCompound(i);
            Item item = Registry.ITEM.get(new Identifier(nbt1.getString("trinket")));
            if (item instanceof ITrinket) {
                ITrinket trinket = (ITrinket) item;
                if (this.active.size() < this.slots) {
                    this.active.add(trinket);
                    if (trinket instanceof ITickableTrinket) {
                        this.tickable.add((ITickableTrinket) trinket);
                    }
                    if (trinket instanceof ITargetingTrinket) {
                        this.targeting.add((ITargetingTrinket) trinket);
                    }
                }
            }
        }
    }

    public boolean unlockSlot() {
        if (this.slots < LostTrinkets.config().maxSlots) {
            this.slots++;
            this.data.setSync(true);
            return true;
        }
        return false;
    }

    public int getSlots() {
        return this.slots;
    }

    public void initSlots(int slots) {
        if (!this.slotsSet) {
            setSlots(slots);
            this.slotsSet = true;
        }
    }

    public void setSlots(int slots) {
        this.slots = slots;
        this.data.setSync(true);
    }

    public boolean clear() {
        if (!this.available.isEmpty() || !this.active.isEmpty()) {
            this.available.clear();
            this.active.clear();
            this.data.setSync(true);
            return true;
        }
        return false;
    }

    public boolean give(ITrinket trinket) {
        if (!has(trinket)) {
            this.available.add(trinket);
            this.data.setSync(true);
            return true;
        }
        return false;
    }

    public boolean setActive(ITrinket trinket, PlayerEntity player) {
        if (isAvailable(trinket)) {
            forceActive(trinket, player);
            this.available.remove(trinket);
            return true;
        }
        return false;
    }

    public boolean setInactive(ITrinket trinket, PlayerEntity player) {
        if (isActive(trinket)) {
            this.available.add(trinket);
            this.active.remove(trinket);
            if (trinket instanceof ITickableTrinket) {
                this.tickable.remove(trinket);
            }
            if (trinket instanceof ITargetingTrinket) {
                this.targeting.remove(trinket);
            }
            if (trinket instanceof Trinket) {
                ((Trinket<?>) trinket).removeAttributes(player);
            }
            trinket.onDeactivated(player.world, player.getBlockPos(), player);
            this.data.setSync(true);
            return true;
        }
        return false;
    }

    public boolean forceActive(ITrinket trinket, PlayerEntity player) {
        if (!isActive(trinket) && this.active.size() < this.slots) {
            this.active.add(trinket);
            if (trinket instanceof ITickableTrinket) {
                this.tickable.add((ITickableTrinket) trinket);
            }
            if (trinket instanceof ITargetingTrinket) {
                this.targeting.add((ITargetingTrinket) trinket);
            }
            if (trinket instanceof Trinket) {
                ((Trinket<?>) trinket).applyAttributes(player);
            }
            trinket.onActivated(player.world, player.getBlockPos(), player);
            this.data.setSync(true);
            return true;
        }
        return false;
    }

    public void removeDisabled(PlayerEntity player) {
        getActiveTrinkets().stream().filter(LostTrinketsAPI.get()::isDisabled).collect(Collectors.toList())
                .forEach(trinket -> setInactive(trinket, player));
        if (getAvailableTrinkets().removeIf(LostTrinketsAPI.get()::isDisabled)) {
            this.data.setSync(true);
        }
    }

    public boolean has(ITrinket trinket) {
        return isActive(trinket) || isAvailable(trinket);
    }

    public boolean isActive(ITrinket trinket) {
        return this.active.contains(trinket);
    }

    public boolean isActive(Supplier<? extends ITrinket> trinket) {
        return this.active.contains(trinket.get());
    }

    public boolean isAvailable(ITrinket trinket) {
        return this.available.contains(trinket);
    }

    public List<ITrinket> getActiveTrinkets() {
        return this.active;
    }

    public List<ITrinket> getAvailableTrinkets() {
        return this.available;
    }

    public List<ITickableTrinket> getTickable() {
        return this.tickable;
    }

    public List<ITargetingTrinket> getTargeting() {
        return this.targeting;
    }
}
