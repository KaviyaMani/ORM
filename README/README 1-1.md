## One-to-One Mapping
    1 User have 1 Address mapped. Can be mapped by 3 ways
### **Using a Foreign Key**
        User - id, uname, address_id
        Address - id, street, city 
        address_id in User table act as a foreign key

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "address_id", referencedColumnName = "id")
        @JsonIgnore
        private Address address;
    @JoinColumn - Foreign key column - Owner of the relationship have this annotation
    @OneToOne - In bidirectional relationship we specify @OneToOne on both side but one entity is the owner
    **@JsonIgnore** - In bidirectional relationship It prevents Jackson from rendering a specified properties of associated objects else infinite loop will be created
                      Based on which side we are adding JsonIgnore it will only load its data, other entity will load its data + other data
                      EX: JsonIgnore is in Address entity  User Response{"id": 1,"uname": "Kaviya",                 "address": {"id": 1,"street": "Main","city": "Nkl"  } },
                            Address response : {  "id": 1,    "street": "Main",    "city": "Nkl"  },
        @OneToOne(mappedBy = "address")
        private User user;
    mappedBy - informing hibernate it does not own the relationship and to look for mapped entity with the field address
                mappedBy will be present in the table in which foreign key is not there. JoinColumn is in User entity hence mappedBy is in Address entity
    CascadeType.ALL - saving user will save Address, deleting user will delete address

### Using Shared Primary Key
