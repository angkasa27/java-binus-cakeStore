import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Cake {
  private int idcake = 0;
  private String name;
  private String type;
  private int price;

  public Cake() {
    // Empty constructor
  }

  public Cake(int idcake, String name, String type, int price) {
    this.idcake = idcake;
    this.name = name;
    this.type = type;
    this.price = price;
  }

  public int getIdcake() {
    return idcake;
  }

  public void setIdcake(int idcake) {
    this.idcake = idcake;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getPrice() {
    return price;
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
}
