package vdk.purchases.purchases;

public class product {
    private String name;

    public product(String id, String name) {
        this.name = name;
    }
    public product() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

