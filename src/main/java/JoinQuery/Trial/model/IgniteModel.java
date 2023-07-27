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
public class IgniteModel implements Serializable {
    @Id
    @QuerySqlField(index = true)
    private int company_code;
    @QuerySqlField(index = true)
    private String comp_name;
    @QuerySqlField
    private int share_price;

}
