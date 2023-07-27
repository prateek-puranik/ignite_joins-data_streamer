package JoinQuery.Trial.controller;


import JoinQuery.Trial.config.IgniteConfig;
import JoinQuery.Trial.model.Organization;
import JoinQuery.Trial.model.Person;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class JoinConstructor {



    @Autowired
    IgniteConfig igniteConfig;


    @PostConstruct
    public void initialize()
    {
        IgniteCache<Long, Organization> orgCache = igniteConfig.igniteInstance().cache("Org_Cache");
        // Clear cache before running the example.
        orgCache.clear();
        // Organizations.insert
        Organization org1 = new Organization("Deutsche Bank");
        Organization org2 = new Organization("Tata Motors");
        orgCache.put(org1.getId(), org1);
        orgCache.put(org2.getId(), org2);

        IgniteCache<AffinityKey<Long>, Person> colPersonCache = igniteConfig.igniteInstance().cache("Col_Per_Cache");
        IgniteCache<Long, Person> personCache = Ignition.ignite().cache("Per_Cache");
        colPersonCache.clear();
        personCache.clear();

        // People.
        Person p1 = new Person(org1, "John", "Doe", 2000, "John Doe has Master Degree.");
        Person p2 = new Person(org1, "Jane", "Doe", 1000, "Jane Doe has Bachelor Degree.");
        Person p3 = new Person(org2, "John", "Smith", 1000, "John Smith has Bachelor Degree.");
        Person p4 = new Person(org2, "Jane", "Smith", 2000, "Jane Smith has Master Degree.");


        // Note that in this example we use custom affinity key for Person objects
        // to ensure that all persons are collocated with their organizations.
        colPersonCache.put(p1.key(), p1);
        colPersonCache.put(p2.key(), p2);
        colPersonCache.put(p3.key(), p3);
        colPersonCache.put(p4.key(), p4);

        // These Person objects are not collocated with their organizations.
        personCache.put(p1.id, p1);
        personCache.put(p2.id, p2);
        personCache.put(p3.id, p3);
        personCache.put(p4.id, p4);


    }

    @GetMapping("/sql")
    public void sqlQuery()
    {
        IgniteCache<Long, Person> cache = igniteConfig.igniteInstance().cache("Per_Cache");

        // SQL clause which selects salaries based on range.
        // Extract fields of the entry.
        String sql = "select * from Person where salary > ? and salary <= ?";

        // Execute queries for salary ranges.
        print("People with salaries between 0 and 1000 (queried with SQL query): ",
                cache.query(new SqlFieldsQuery(sql).setArgs(0, 1000)).getAll());

        print("People with salaries between 1000 and 2000 (queried with SQL query): ",
                cache.query(new SqlFieldsQuery(sql).setArgs(1000, 2000)).getAll());
    }



    @GetMapping("/sqlJoin")
    public void sqlQueryWithJoin() {
        IgniteCache<AffinityKey<Long>, Person> cache = igniteConfig.igniteInstance().cache("Col_Per_Cache");

        // SQL clause query which joins on 2 types to select people for a specific organization.
        String joinSql =
                "select pers.* from Person as pers, \"" + "Org_Cache" + "\".Organization as org " +
                        "where pers.orgId = org.id " +
                        "and lower(org.name) = lower(?)";

        // Execute queries for find employees for different organizations.
        print("Following people are 'Deutsche Bank' employees: ",
                cache.query(new SqlFieldsQuery(joinSql).setArgs("Deutsche Bank")).getAll());

        print("Following people are 'Tata Motors' employees: ",
                cache.query(new SqlFieldsQuery(joinSql).setArgs("Tata Motors")).getAll());
    }


    @GetMapping("/sqlDistributed")
    public void sqlQueryWithDistributedJoin() {
        IgniteCache<Long, Person> cache = igniteConfig.igniteInstance().cache("Per_Cache");

        // SQL clause query which joins on 2 types to select people for a specific organization.
        String joinSql =
                "select pers.* from Person as pers, \"" + "Org_Cache" + "\".Organization as org " +
                        "where pers.orgId = org.id " +
                        "and lower(org.name) = lower(?)";

        SqlFieldsQuery qry = new SqlFieldsQuery(joinSql).setArgs("Deutsche Bank");

        // Enable distributed joins for query.
        qry.setDistributedJoins(true);

        // Execute queries for find employees for different organizations.
        print("Following people are 'Deutsche Bank' employees (distributed join): ", cache.query(qry).getAll());

        qry.setArgs("Tata Motors");

        print("Following people are 'Tata Motors' employees (distributed join): ", cache.query(qry).getAll());
    }

    @GetMapping("sqlAggregation")
    public void sqlQueryWithAggregation() {
        IgniteCache<AffinityKey<Long>, Person> cache =igniteConfig.igniteInstance().cache("Col_Per_Cache");

        // Calculate average of salary of all persons in Deutsche Bank.
        // Note that we also join on Organization cache as well.
        String sql =
                "select avg(salary) " +
                        "from Person, \"" + "Org_Cache" + "\".Organization as org " +
                        "where Person.orgId = org.id " +
                        "and lower(org.name) = lower(?)";

        QueryCursor<List<?>> cursor = cache.query(new SqlFieldsQuery(sql).setArgs("Deutsche Bank"));

        // Calculate average salary for a specific organization.
        print("Average salary for 'Deutsche Bank' employees: ", cursor.getAll());
    }

    @GetMapping("sqlFields")
    public void sqlFieldsQuery() {
        IgniteCache<Long, Person> cache = igniteConfig.igniteInstance().cache("Per_Cache");

        // Execute query to get names of all employees.
        QueryCursor<List<?>> cursor = cache.query(new SqlFieldsQuery(
                "select concat(firstName, ' ', lastName) from Person"));

        // In this particular case each row will have one element with full name of an employees.
        List<List<?>> res = cursor.getAll();

        // Print names.
        print("Names of all employees:", res);
    }


    @GetMapping("/sqlFieldsJoin")
    public void sqlFieldsQueryWithJoin() {
        IgniteCache<AffinityKey<Long>, Person> cache = igniteConfig.igniteInstance().cache("Col_Per_Cache");

        // Execute query to get names of all employees.
        String sql =
                "select concat(firstName, ' ', lastName), org.name " +
                        "from Person, \"" + "Org_Cache" + "\".Organization as org " +
                        "where Person.orgId = org.id";

        QueryCursor<List<?>> cursor = cache.query(new SqlFieldsQuery(sql));

        // In this particular case each row will have one element with full name of an employees.
        List<List<?>> res = cursor.getAll();

        // Print persons' names and organizations' names.
        print("Names of all employees and organizations they belong to: ", res);
    }




    private static void print(String msg, Iterable<?> col) {
        print(msg);
        print(col);
    }
    private static void print(String msg) {
        System.out.println();
        System.out.println(">>> " + msg);
    }

    public static void print(Iterable<?> col) {
        for (Object next : col)
            System.out.println(">>>     " + next);
    }
}
