package JoinQuery.Trial.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IgnitionModel implements Serializable {
    @Id
    @QuerySqlField(index = true)
    private int manufacturer_id;
    @QuerySqlField(index = true)
    private String manufacturer_code;
    @QuerySqlField
    private int growth_rate;

}
