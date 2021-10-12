package thulasi.hemanthkumar.foody.model;

public class Cart {
    private String id;
    private String name,qty,child, price,total,img;

    public Cart(String id, String name, String qty, String child, String price, String total, String img) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.child = child;
        this.price = price;
        this.total = total;
        this.img = img;
    }

    public Cart() {
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
