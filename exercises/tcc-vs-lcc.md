# TCC *vs* LCC

Explain under which circumstances *Tight Class Cohesion* (TCC) and *Loose Class Cohesion* (LCC) metrics produce the same value for a given Java class. Build an example of such as class and include the code below or find one example in an open-source project from Github and include the link to the class below. Could LCC be lower than TCC for any given class? Explain.

A refresher on TCC and LCC is available in the [course notes](https://oscarlvp.github.io/vandv-classes/#cohesion-graph).

## Answer

TCC and LCC produce the same value for a given Java class when the class is maximally cohesive TCC = LCC = 1, or 
when it's non-cohesive (all methods are unconnected) TCC = LCC = 0.

The ranges for TCC and LCC are 0 <= TCC <= LCC <= 1.

The following class is maximally cohesive

```java
    public class ShoppingCart {
        private String customerName;
        private double totalAmount;
        private List<Item> items;
    
        public ShoppingCart(String customerName) {
            this.customerName = customerName;
            this.totalAmount = 0.0;
            this.items = new ArrayList<>();
        }
    
        public void addItem(Item item) {
            items.add(item);
            totalAmount += item.getPrice();
        }
    
        public void removeItem(Item item) {
            items.remove(item);
            totalAmount -= item.getPrice();
        }
    }
```

The following class is non-cohesive.

```java
class Client {
    int id;
    String firstName;
    String lastName;
    String email;

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}


```
