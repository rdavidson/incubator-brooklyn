package brooklyn.entity.group;

import brooklyn.config.ConfigKey;
import brooklyn.entity.Entity;
import brooklyn.entity.Group;
import brooklyn.entity.basic.BasicGroup;
import brooklyn.entity.basic.ConfigKeys;
import brooklyn.entity.proxying.EntitySpec;
import brooklyn.entity.proxying.ImplementedBy;
import brooklyn.util.flags.SetFromFlag;

import com.google.common.base.Function;
import com.google.common.reflect.TypeToken;

@SuppressWarnings({ "serial" })
@ImplementedBy(DynamicMultiGroupImpl.class)
public interface DynamicMultiGroup extends Entity {

    /**
     * Identifies the entities that are to be considered for "bucketising".
     * @see DynamicMultiGroupImpl#iterableForChildren(Entity)
     * @see DynamicMultiGroupImpl#iterableForMembers(Entity)
     */
    @SetFromFlag("entityProvider")
    public static final ConfigKey<Iterable<Entity>> ENTITY_PROVIDER = ConfigKeys.newConfigKey(
            new TypeToken<Iterable<Entity>>(){},
            "brooklyn.multigroup.entityProvider",
            "Identifies which entities should be considered for 'bucketising'"
    );

    /**
     * Implements the mapping from entity to bucket (name).
     * @see DynamicMultiGroupImpl#bucketFromAttribute(brooklyn.event.AttributeSensor)
     * @see DynamicMultiGroupImpl#bucketFromAttribute(brooklyn.event.AttributeSensor, String)
     */
    @SetFromFlag("bucketFunction")
    public static final ConfigKey<Function<Entity, String>> BUCKET_FUNCTION = ConfigKeys.newConfigKey(
            new TypeToken<Function<Entity, String>>(){},
            "brooklyn.multigroup.bucketFunction",
            "Implements the mapping from entity to bucket (name)"
    );

    /**
     * Determines the entity type used for the "bucket" groups.
     */
    @SetFromFlag("groupSpec")
    public static final ConfigKey<EntitySpec<? extends Group>> GROUP_SPEC = ConfigKeys.newConfigKey(
            new TypeToken<EntitySpec<? extends Group>>(){},
            "brooklyn.multigroup.groupSpec",
            "Determines the entity type used for the 'bucket' groups",
            EntitySpec.create(BasicGroup.class)
    );


    /**
     * <p>Distribute entities provided by the {@link #ENTITY_PROVIDER} into uniquely-named "buckets"
     * according to the {@link #BUCKET_FUNCTION}.
     *
     * <p>A {@link Group} entity is created for each required bucket and added as a managed child of
     * this component. Entities for a given bucket are added as members of the corresponding group.
     * By default, {@link BasicGroup} instances will be created for the buckets, however any group
     * entity can be used instead (e.g. with custom effectors) by specifying the relevant entity
     * spec via the {@link #GROUP_SPEC} config key.
     *
     * <p>Entities for which the bucket function returns <tt>null</tt> are not allocated to any
     * bucket and are thus effectively excluded. Buckets that become empty following re-evaluation
     * are removed.
     *
     * @see ENTITY_PROVIDER
     * @see BUCKET_FUNCTION
     * @see GROUP_SPEC
     */
    public void distributeEntities();

}
