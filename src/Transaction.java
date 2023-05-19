import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Transaction {
  private int idtransactionDetails = 0;
  private int idtransaction = 0;
  private String createdAt;
  private String cakeName;
  private int qty;
  private int price;
  private String customerName;

  public Transaction() {
    // Empty constructor
  }

  public Transaction(int idtransactionDetails, int idtransaction, String createdAt, String cakeName, int qty, int price,
      String customerName) {
    this.idtransactionDetails = idtransactionDetails;
    this.idtransaction = idtransaction;
    this.createdAt = createdAt;
    this.cakeName = cakeName;
    this.qty = qty;
    this.price = price;
    this.customerName = customerName;
  }

  public int getIdtransactionDetails() {
    return idtransactionDetails;
  }

  public void setIdtransactionDetails(int idtransactionDetails) {
    this.idtransactionDetails = idtransactionDetails;
  }

  public int getIdtransaction() {
    return idtransaction;
  }

  public void setIdtransaction(int idtransaction) {
    this.idtransaction = idtransaction;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getCakeName() {
    return cakeName;
  }

  public void setCakeName(String cakeName) {
    this.cakeName = cakeName;
  }

  public int getQty() {
    return qty;
  }

  public void setQty(int qty) {
    this.qty = qty;
  }

  public String getPriceText() {
    if (price == 0) {
      return "Rp0";
    } else {
      NumberFormat formatter = new DecimalFormat("#,###");
      return ("Rp" + formatter.format(price));
    }
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }
}
