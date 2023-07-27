package JoinQuery.Trial.model;

import lombok.Data;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;


@Data
public class Organization implements Serializable {
    private static final AtomicLong ID_GEN = new AtomicLong();
    @QuerySqlField(index = true)
    private Long id;
    @QuerySqlField(index = true)
    private String name;
    public Organization(String name) {
        id = ID_GEN.incrementAndGet();
        this.name = name;
    }
}
