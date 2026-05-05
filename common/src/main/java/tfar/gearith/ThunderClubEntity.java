package tfar.gearith;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import tfar.gearith.platform.Services;


public class ThunderClubEntity extends Projectile {

    @Nullable
    private Entity hookedIn;
    private ClubState currentState = ClubState.FLYING;
    private final int pullingSpike;
    private final RandomSource syncronizedRandom = RandomSource.create();
    private int life;
    private final InterpolationHandler interpolationHandler = new InterpolationHandler(this);
    private boolean dealtDamage;

    private static final EntityDataAccessor<Integer> DATA_HOOKED_ENTITY = SynchedEntityData.defineId(ThunderClubEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<ItemStack> DATA_ITEM = SynchedEntityData.defineId(ThunderClubEntity.class, EntityDataSerializers.ITEM_STACK);


    public ThunderClubEntity(EntityType<? extends ThunderClubEntity> entityType, Level level, int pullingSpike,ItemStack stack) {
        super(entityType, level);
        this.pullingSpike = pullingSpike;
        setItem(stack);
    }

    protected ThunderClubEntity(EntityType<? extends ThunderClubEntity> entityType, Level level) {
        this(entityType, level,0,ItemStack.EMPTY);
    }
    public ThunderClubEntity(Player owner, Level level,int pullingSpike,ItemStack stack) {
        this(MEntityTypes.THUNDER_CLUB, level,pullingSpike,stack);
        this.setOwner(owner);
        float f = owner.getXRot();
        float f1 = owner.getYRot();
        float f2 = Mth.cos(-f1 * (float) (Math.PI / 180.0) - (float) Math.PI);
        float f3 = Mth.sin(-f1 * (float) (Math.PI / 180.0) - (float) Math.PI);
        float f4 = -Mth.cos(-f * (float) (Math.PI / 180.0));
        float f5 = Mth.sin(-f * (float) (Math.PI / 180.0));
        double d0 = owner.getX() - f3 * 0.3;
        double d1 = owner.getEyeY();
        double d2 = owner.getZ() - f2 * 0.3;
        this.snapTo(d0, d1, d2, f1, f);
        Vec3 vec3 = new Vec3(-f3, Mth.clamp(-(f5 / f4), -5.0F, 5.0F), -f2);
        double d3 = vec3.length();
        vec3 = vec3.multiply(
                0.6 / d3 + this.random.triangle(0.5, 0.0103365), 0.6 / d3 + this.random.triangle(0.5, 0.0103365), 0.6 / d3 + this.random.triangle(0.5, 0.0103365)
        );
        this.setDeltaMovement(vec3);
        this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * 180.0F / (float)Math.PI));
        this.setXRot((float)(Mth.atan2(vec3.y, vec3.horizontalDistance()) * 180.0F / (float)Math.PI));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    public int retrieve(ItemStack stack) {
        Player player = this.getPlayerOwner();
        if (!this.level().isClientSide() && player != null && !this.shouldRemoveClub(player)) {
            int i = 0;
            if (this.hookedIn != null) {
                this.pullEntity(this.hookedIn);
                //CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer)player, stack, this, Collections.emptyList());
                this.level().broadcastEntityEvent(this, EntityEvent.FISHING_ROD_REEL_IN);
                i = this.hookedIn instanceof ItemEntity ? 3 : 5;
            }

            if (this.onGround()) {
                i = 2;
            }

            this.discard();
            return i;
        } else {
            return 0;
        }
    }

    public void setItem(ItemStack stack) {
        entityData.set(DATA_ITEM, stack);
    }

    public ItemStack getItem() {
        return entityData.get(DATA_ITEM);
    }

    @Override
    public InterpolationHandler getInterpolation() {
        return this.interpolationHandler;
    }

    @Override
    public void tick() {
        this.syncronizedRandom.setSeed(this.getUUID().getLeastSignificantBits() ^ this.level().getGameTime());
        this.getInterpolation().interpolate();
        super.tick();
        Player player = this.getPlayerOwner();
        if (player == null) {
            this.discard();
        } else if (this.level().isClientSide() || !this.shouldRemoveClub(player)) {
            if (this.onGround()) {
                this.life++;
                if (this.life >= 1200) {
                    this.discard();
                    return;
                }
            } else {
                this.life = 0;
            }

            float f = 0.0F;
            BlockPos blockpos = this.blockPosition();
            FluidState fluidstate = this.level().getFluidState(blockpos);
            if (fluidstate.is(FluidTags.WATER)) {
                f = fluidstate.getHeight(this.level(), blockpos);
            }

            boolean isInWater = f > 0.0F;
            if (this.currentState == ClubState.FLYING) {
                if (this.hookedIn != null) {
                    this.setDeltaMovement(Vec3.ZERO);
                    this.currentState = ClubState.HOOKED_IN_ENTITY;
                    return;
                }

                if (isInWater) {
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.3, 0.2, 0.3));
                    this.currentState = ClubState.BOBBING;
                    return;
                }

                this.checkCollision();
            } else {
                if (this.currentState == ClubState.HOOKED_IN_ENTITY) {
                    if (this.hookedIn != null) {
                        if (!this.hookedIn.isRemoved() && this.hookedIn.canInteractWithLevel() && this.hookedIn.level().dimension() == this.level().dimension()
                        )
                        {
                            this.setPos(this.hookedIn.getX(), this.hookedIn.getY(0.8), this.hookedIn.getZ());
                        } else {
                            this.setHookedEntity(null);
                            this.currentState = ClubState.FLYING;
                        }
                    }

                    return;
                }

                if (this.currentState == ClubState.BOBBING) {
                    Vec3 vec3 = this.getDeltaMovement();
                    double d0 = this.getY() + vec3.y - blockpos.getY() - f;
                    if (Math.abs(d0) < 0.01) {
                        d0 += Math.signum(d0) * 0.1;
                    }

                    this.setDeltaMovement(vec3.x * 0.9, vec3.y - d0 * this.random.nextFloat() * 0.2, vec3.z * 0.9);


                    if (isInWater) {
                    } else {
                    }
                }
            }

            if (!fluidstate.is(FluidTags.WATER) && !this.onGround() && this.hookedIn == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.03, 0.0));
            }

            this.move(MoverType.SELF, this.getDeltaMovement());
            this.applyEffectsFromBlocks();
            this.updateRotation();
            if (this.currentState == ClubState.FLYING && (this.onGround() || this.horizontalCollision)) {
                this.setDeltaMovement(Vec3.ZERO);
            }

            double d1 = 0.92;
            this.setDeltaMovement(this.getDeltaMovement().scale(d1));
            this.reapplyPosition();
        }
    }

    private boolean shouldRemoveClub(Player player) {
        if (player.canInteractWithLevel()) {
            ItemStack itemstack = player.getMainHandItem();
            ItemStack itemstack1 = player.getOffhandItem();
            boolean flag = itemstack.is(MItems.THUNDER_CLUB);
            boolean flag1 = itemstack1.is(MItems.THUNDER_CLUB);
            if ((flag || flag1) && this.distanceToSqr(player) <= 1024.0) {
                return false;
            }
        }

        this.discard();
        return true;
    }

    protected void pullEntity(Entity p_entity) {
        Entity owner = this.getOwner();
        if (owner != null) {
            Vec3 vec3 = new Vec3(owner.getX() - this.getX(), owner.getY() - this.getY(), owner.getZ() - this.getZ()).scale(0.1);
            p_entity.setDeltaMovement(p_entity.getDeltaMovement().add(vec3));
        }
    }


    private void checkCollision() {
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() == HitResult.Type.MISS ||
                !Services.PLATFORM.postOnProjectileImpact(this, hitresult)) this.onHit(hitresult);
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return super.canHitEntity(target) || target.isAlive() && target instanceof ItemEntity;
    }

    /**
     * Called when the arrow hits an entity
     */
    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (dealtDamage)return;
        Entity entity = result.getEntity();
        if (!this.level().isClientSide()) {
            hurtCollidedEntity(entity);
            if (pullingSpike > 0) {
                this.setHookedEntity(entity);
            }
            dealtDamage = true;
        }
    }

    void hurtCollidedEntity(Entity entity) {
        float f = 8.0F;
        Entity owner = this.getOwner();
        DamageSource damagesource = this.damageSources().trident(this, owner == null ? this : owner);
        if (this.level() instanceof ServerLevel serverlevel) {
            f = EnchantmentHelper.modifyDamage(serverlevel, this.getWeaponItem(), entity, damagesource, f);
        }

        //this.dealtDamage = true;
        if (entity.hurtOrSimulate(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (this.level() instanceof ServerLevel serverlevel1) {
                EnchantmentHelper.doPostAttackEffectsWithItemSourceOnBreak(
                        serverlevel1, entity, damagesource, this.getWeaponItem(), p_375964_ -> this.kill(serverlevel1)
                );
            }

            if (entity instanceof LivingEntity livingentity) {
                //this.doKnockback(livingentity, damagesource);
               // this.doPostHurtEffects(livingentity);
            }
        }

        this.deflect(ProjectileDeflection.REVERSE, entity, this.owner, false);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.setDeltaMovement(this.getDeltaMovement().normalize().scale(result.distanceTo(this)));
    }

    private void setHookedEntity(@Nullable Entity hookedEntity) {
        this.hookedIn = hookedEntity;
        this.getEntityData().set(DATA_HOOKED_ENTITY, hookedEntity == null ? 0 : hookedEntity.getId() + 1);
    }

    @Override
    public @org.jetbrains.annotations.Nullable ItemStack getWeaponItem() {
        return getItem();
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        this.updateOwnerInfo(null);
        super.remove(reason);
    }

    @Override
    public void onClientRemoval() {
        this.updateOwnerInfo(null);
    }

    @Override
    public void setOwner(@Nullable Entity owner) {
        super.setOwner(owner);
        this.updateOwnerInfo(this);
    }

    private void updateOwnerInfo(@Nullable ThunderClubEntity thunderClubEntity) {
        Player player = this.getPlayerOwner();
        if (player != null) {
            ((PlayerDuck)player).setThunderClubEntity(thunderClubEntity);
        }
    }

    @Nullable
    public Player getPlayerOwner() {
        return this.getOwner() instanceof Player player ? player : null;
    }

    @Nullable
    public Entity getHookedIn() {
        return this.hookedIn;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_HOOKED_ENTITY,0);
        builder.define(DATA_ITEM,ItemStack.EMPTY);
    }

    @Override
    protected boolean shouldBounceOnWorldBorder() {
        return true;
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (DATA_HOOKED_ENTITY.equals(key)) {
            int i = this.getEntityData().get(DATA_HOOKED_ENTITY);
            this.hookedIn = i > 0 ? this.level().getEntity(i - 1) : null;
        }

        super.onSyncedDataUpdated(key);
    }

    enum ClubState {
        FLYING,
        HOOKED_IN_ENTITY,
        BOBBING
    }
}
