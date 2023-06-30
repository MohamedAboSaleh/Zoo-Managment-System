package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.PrimitiveIterator;

import Exceptions.MaximumCapcityException;
import Utils.Discount;
import Utils.Gender;
import Utils.MyFileLogWriter;
import Utils.TicketType;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class Visitor extends Person implements Serializable {
	private static int idCounter = 1;

	private TicketType ticket;
	private Discount discount;

	public Visitor(String firstName, String lastName, LocalDate date, Gender gender, TicketType ticket,
			Discount discount) {
		super(idCounter++, firstName, lastName, date, gender, null);
		this.ticket = ticket;
		this.discount = discount;
	}

	public Visitor(int id) {
		super(id);
	}

	public TicketType getTicket() {
		return ticket;
	}

	public void setTicket(TicketType ticket) {
		this.ticket = ticket;
	}

	public Discount getDiscount() {
		return discount;
	}

	public void setDiscount(Discount discount) {
		this.discount = discount;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " Name: " + getFirstName() + " " + getLastName() + ", TicketType: "
				+ ticket;
	}

	public void moveVisitorToSection(Section newSection) {
		try {

			if (newSection == null
					|| (this.getSection().getMaxCapacity() / 2) >= this.getSection().getVisitors().size() - 1) {
				// System.out.println(this.getSection().getMaxCapacity() +", " +
				// (this.getSection().getVisitors().size()-1));
				throw new MaximumCapcityException();
			}

			this.getSection().getVisitors().remove(this);
			Section s = this.getSection();
			this.setSection(newSection);
			newSection.getVisitors().add(this);
			Zoo.getInstance().saveToSerFile();
			// MyFileLogWriter.println(this+" moved from Section "+s.getSectionName()+" to"
			// + "Section "+newSection);
		} catch (MaximumCapcityException e) {
			Alert alertConfirm = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			alertConfirm.showAndWait();

		}

	}

	public double checkTicketPrice() {
		double perc = 100 - getDiscount().getPercentage();
		double price = perc * ticket.getValue() / 100;
		return price;
	}

	public boolean purchaseSnack(Snack s) {
		boolean isValid = true;
		if (s == null)
			isValid = false;

		if (s instanceof Drink) {

			Drink tmp = (Drink) s;

			if (isValid && tmp.isSprinkel() && this.getSection().getBar().getSnacks().contains(tmp)) {
				this.getSection().getBar().setProfit(this.getSection().getBar().getProfit() + s.getPrice() + 5);
				this.getSection().getBar().getSnacks().remove(tmp);
				// MyFileLogWriter.println("Visitor: "+this+" Purchased Drink: "+tmp);
				Zoo.getInstance().saveToSerFile();
				return true;

			}

			else if (isValid && !tmp.isSprinkel() && this.getSection().getBar().getSnacks().contains(tmp)) {
				this.getSection().getBar().setProfit(this.getSection().getBar().getProfit() + s.getPrice());
				this.getSection().getBar().getSnacks().remove(tmp);
				// MyFileLogWriter.println("Visitor: "+this+" Purchased Drink: "+tmp);
				Zoo.getInstance().saveToSerFile();
				return true;

			} else { // MyFileLogWriter.println("Visitor: "+this+" Did Not Purchased any DRINK");
				return false;
			}
		}

		else if (s instanceof Food) {

			Food tmp = (Food) s;

			if (isValid && tmp.isPlate() && this.getSection().getBar().getSnacks().contains(tmp)) {
				this.getSection().getBar().setProfit(this.getSection().getBar().getProfit() + s.getPrice() + 20);
				this.getSection().getBar().getSnacks().remove(tmp);
				// MyFileLogWriter.println("Visitor: "+this+" Purchased Food: "+tmp);
				Zoo.getInstance().saveToSerFile();
				return true;

			}

			else if (isValid && !tmp.isPlate() && this.getSection().getBar().getSnacks().contains(tmp)) {
				this.getSection().getBar().setProfit(this.getSection().getBar().getProfit() + s.getPrice());
				this.getSection().getBar().getSnacks().remove(tmp);
				// MyFileLogWriter.println("Visitor: "+this+" Purchased Food: "+tmp);
				Zoo.getInstance().saveToSerFile();
				return true;

			} else {
				// MyFileLogWriter.println("Visitor: "+this+" Did Not Purchased any FOOD");
				return false;
			}

		}

		else {
			// MyFileLogWriter.println("Visitor: "+this+" Did Not Purchased Snack");
			return false;
		}

	}

}
