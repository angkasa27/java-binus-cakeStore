public class Customer {
  private int idcustomer;
  private String name;

  public Customer() {
    // Empty constructor
  }

  public Customer(int idcustomer, String name) {
    this.idcustomer = idcustomer;
    this.name = name;
  }

  public int getIdcake() {
    return idcustomer;
  }

  public void setIdcake(int idcustomer) {
    this.idcustomer = idcustomer;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
