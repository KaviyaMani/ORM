# ORM Mappings
## One-to-One Associations
1 Customer have 1 Address
### Unidirectional One-to-One Associations
    Only have mapping in one entity, from Customer we want to get Address. From Address no need to get Customer
    Use @OneToOne Customer->Address, we can have like Address->Customer also if needed
    Internally hibernate uses name of associated entity and the name of primary key to make the foreign key
    Ex: By default address_id will be the fk column name. Can customize using @JoinColumn(name="id")
### Bidirectional One-to-One Associations
    It extends unidirectional so we can navigate in both direction Customer<->Address
    Have owning side(defines the mapping) and referencing side(links to that mapping)
    1. owning side will be same as unidirectional  @OneToOne @JoinColumn(name = “id”) private Address address
    2. reference side just links to the mapping, hibernate get the info from referenced mapping @OneToOne(mappedBy = “address”) Customer customer
## Many-to-One Associations
1 Order contains multiple Items, but multiple items belong to 1 order
Store order_id as foreign key in Items table
### Unidirectional Many-to-One Association
    Item M->1 Order
    In this Item have foreign key field of order @ManyToOne @JoinColumn(name = “id”) private Order order; 
    By default order_id(name of field + primary key field of Order entity) we can customize by @JoinColumn
### Unidirectional One-to-Many Association
    Order 1 -> M Item
    In this association mapping done on Order with collection of list. @OneToMany private List<Item> items = new ArrayList<>()
    To mention the foreign key(column to be mapped by association) in Item table @JoinColumn(name = “fk_order”)
### Bidirectional Many-to-One Associations
    Item M <-> 1 Order
    Uses the attribute of Order and Item table for assoaciation, can navigate on both direction
    2 parts - to-many side owns the mapping, to-one side use the mapping
    1. owning side - @ManyToOne  @JoinColumn(name = “fk_order”) private Order order; - this info is enough for hibernate to do the mapping
    2. reference side - by providing the name of association-mapping attribute to the mappedBy attribute is enough 
        @OneToMany(mappedBy = “order”) private List<Item> items = new ArrayList<>()
    Since this is bidirectional we have to set items to order before saving order.
    Utility can be used to set the value - public void addItem(OrderItem item) {   this.items.add(item);  item.setOrder(this); }
## Many-to-Many Associations
Multiple Stores sell multiple Products
It requires additional **association table** which contains primary key pairs of associated entities. No need to map this table to entity
You should use Set instead of List while associating
why set? If we did not mention set, hibernate will remove all records from association table and re-insert them which is very inefficient
### Unidirectional Many-to-Many Associations
    Store M -> M Product
    mapping done with Set @ManyToMany private Set<Product> products = new HashSet<>()
    association table - If we didn't mention anything hibernate create association table Store_Product with columns store_id and product_id
    To customize association table use @JoinTable, and its attributes joinColumns(fk of Store) and inverseJoinColumns(fk of Product) defines foreign key columns
    @ManyToMany
    @JoinTable(name = “store_product”,
           joinColumns = { @JoinColumn(name = “fk_store”) },
           inverseJoinColumns = { @JoinColumn(name = “fk_product”) })
    private Set<Product> products = new HashSet<>();
### Bidirectional Many-to-Many Associations
    Store M <-> M Product
    One of 2 entities own the association other references it
    Owning side - @ManyToMany     @JoinTable(name = “store_product”,
                                             joinColumns = { @JoinColumn(name = “fk_store”) },
                                             inverseJoinColumns = { @JoinColumn(name = “fk_product”) })
                                  private Set<Product> products = new HashSet<>();
    Reference side - @ManyToMany(mappedBy=”products”)     private Set<Store> stores = new HashSet<>();
    Have to update both ends of association while adding or removing
    Have a utility to add or remove avoid errors:
        public void addProduct(Product p) { this.products.add(p); p.getStores().add(this);  }
        public void removeProduct(Product p) { this.products.remove(p); p.getStores().remove(this);   }
# Fetch Types
Specifies when hibernate should fetch associate entities from the database
FetchType.EAGER – Fetch it, so you'll have it when you need it
FetchType.LAZY – Fetch it when you need it
### Default
    All to-one fetchType - EAGER
    All to-many fetchType - LAZY
    we can customize using @OneToMany(mappedBy = "order", fetch = FetchType.EAGER)
    we can use getter of LAZY field to load the data getOrderItems().
        Lets say we have 100 orders, hibernate has to perform 100 getOrderItems to get the details.
        This issue is called N+1 select issue. it will reduce the performance.
        Solution for N+1 problem - 
        1. Eager Loading if possible 
        2. Using Batch @OneToMany(fetch = FetchType.LAZY)    @BatchSize(size = 10) related data are fetched in batches of 10
        3. Using caching strategy
        4. Using Database Views
        5. Adding proper logging and monitoring for database to track and find
    optional = false means the association is not optional. So it's mandatory. So setting it to null throws an exception.
    @JsonIgnoreProperties and @JsonIgnore to avoid infinite loops

# Hibernate Lifecycle
    Lifecycle of mapped entity classes
    create new object->Store to db, fetch existing data of entity
    Each object of entity passes through various stages of lifecycle
    1. Transient State - When creating new instance of object 
    2. Persistent State - Once object connected with hibernate session by using (save, persist, saveOrUpdate, update)
    3. Detached State - by closing session, clear cache (detach, evict, clear, close). To reconnect detached object(merge, update, load, refresh, save, update)
    4. Removed State - entity object is deleted (session.delete)
# Cascade Types
Entity relationship depend on another entity. If we have Person and Address without Person Address entity does not have any value.
When we perform some action on the target entity, the same action will be applied to the associated entity.

    CascadeType.ALL - create, update, delete, and refresh should be cascaded
    CascadeType.PERSIST - create (or persist) operation should be cascaded
    CascadeType.MERGE - update (or merge) operation should be cascaded
    CascadeType.REMOVE - delete operation should be cascaded
    CascadeType.REFRESH - refresh operation should be cascaded
    CascadeType.DETACH - detach operation should be cascaded
    Cascade types provided by Hibernate:
        CascadeType.REPLICATE - replicate operation should be cascaded
        CascadeType.SAVE_UPDATE - save or update operation should be cascaded


# First level cache, second level cache