package JoinQuery.Trial.model;

import lombok.Data;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.cache.query.annotations.QueryTextField;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;


@Data
public class Person implements Serializable {

    private static final AtomicLong ID_GEN = new AtomicLong();
    public static final String ORG_SALARY_IDX = "ORG_SALARY_IDX";
    @QuerySqlField(index = true)
    public Long id;
    @QuerySqlField(index = true, orderedGroups = @QuerySqlField.Group(name = ORG_SALARY_IDX, order = 0))
    public Long orgId;
    @QuerySqlField
    public String firstName;
    @QuerySqlField
    public String lastName;
    @QuerySqlField
    public String resume;
    @QuerySqlField(index = true, orderedGroups = @QuerySqlField.Group(name = ORG_SALARY_IDX, order = 1))
    public double salary;
    private transient AffinityKey<Long> key;
    public Person(Organization org, String firstName, String lastName, double salary, String resume) {
        // Generate unique ID for this person.
        id = ID_GEN.incrementAndGet();
        orgId = org.getId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.resume = resume;
    }
    public AffinityKey<Long> key() {
        if (key == null)
            key = new AffinityKey<>(id, orgId);
        return key;
    }
}
