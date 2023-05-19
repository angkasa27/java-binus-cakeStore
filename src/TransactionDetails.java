import java.text.DecimalFormat;
import java.text.NumberFormat;

public class TransactionDetails {
  private int idtransactionDetails = 0;
  private int idcake = 0;
  private String name;
  private int qty;
  private int price;
  private int totalPrice;

  public TransactionDetails() {
    // Empty constructor
  }

  public TransactionDetails(int idtransactionDetails, int idcake, String name, int qty, int price) {
    this.idtransactionDetails = idtransactionDetails;
    this.idcake = idcake;
    this.name = name;
    this.qty = qty;
    this.price = price;
    this.totalPrice = qty * price;
  }

  public int getIdtransactionDetails() {
    return idtransactionDetails;
  }

  public void setIdtransactionDetails(int idtransactionDetails) {
    this.idtransactionDetails = idtransactionDetails;
  }

  public int getIdcake() {
    return idcake;
  }

  public void setIdckaegetIdcake(int idcake) {
    this.idcake = idcake;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getQty() {
    return qty;
  }

  public void setQty(int qty) {
    this.qty = qty;
    this.totalPrice = qty * price;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
    this.totalPrice = qty * price;
  }

  public String getPriceText() {
    if (price == 0) {
      return "Rp0";
    } else {
      NumberFormat formatter = new DecimalFormat("#,###");
      return ("Rp" + formatter.format(price));
    }
  }

  public int getTotalPrice() {
    return totalPrice;
  }

  public String getTotalPriceText() {
    if (totalPrice == 0) {
      return "Rp0";
    } else {
      NumberFormat formatter = new DecimalFormat("#,###");
      return ("Rp" + formatter.format(totalPrice));
    }
  }
}
