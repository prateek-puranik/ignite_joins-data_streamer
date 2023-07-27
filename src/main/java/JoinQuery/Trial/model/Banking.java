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
public class Banking implements Serializable {
    @QuerySqlField
    private String date;
    @QuerySqlField
    private String domain;
    @QuerySqlField
    private String location;
    @QuerySqlField
    private int value;
    @QuerySqlField
    private int transaction_count;
    @Id
    @QuerySqlField(index = true)
    private int id;


}
