package com.example.bookflix.author;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "author_by_id")
public class Author {
    
    @PrimaryKeyColumn(name = "author_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id;
    
    @Column("author_name")
    @CassandraType(type = Name.TEXT)
    private String name;
    
    @Column("personal_name")
    @CassandraType(type = Name.TEXT)
    private String personalName;

    public Author() {}
    
    /**
     * Constructor with parameters.
     *
     * @param id
     * @param name
     * @param personal
     */
    public Author(String id, String name, String personal) {
        this.id           = id;
        this.name         = name;
        this.personalName = personal;
    }
    
    /**
     * Getter accessor for attribute 'id'.
     *
     * @return
     *       current value of 'id'
     */
    public String getId() {
        return id;
    }

    /**
     * Setter accessor for attribute 'id'.
     * @param id
     * 		new value for 'id '
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter accessor for attribute 'name'.
     *
     * @return
     *       current value of 'name'
     */
    public String getName() {
        return name;
    }

    /**
     * Setter accessor for attribute 'name'.
     * @param name
     * 		new value for 'name '
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter accessor for attribute 'personalName'.
     *
     * @return
     *       current value of 'personalName'
     */
    public String getPersonalName() {
        return personalName;
    }

    /**
     * Setter accessor for attribute 'personalName'.
     * @param personalName
     * 		new value for 'personalName '
     */
    public void setPersonalName(String personalName) {
        this.personalName = personalName;
    }
}
