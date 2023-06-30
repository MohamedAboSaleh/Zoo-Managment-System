package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import Utils.AnimalFood;
import Utils.Gender;

public class Reptile extends Animal implements Serializable{
	private static int idCounter = 1;
	
	private boolean dangerous;
	private boolean seasonSleep;
	
	public Reptile(String name, LocalDate date, AnimalFood food, Gender gender, Section section
			, boolean isDangerous,boolean seasonSleep) {
		super(idCounter++, name, date, food, gender, section);
		this.dangerous = isDangerous;
		this.seasonSleep= seasonSleep;
	}
	
	public Reptile(int id) {
		super(id);
	}

	public boolean isDangerous() {
		return dangerous;
	}

	public void setDangerous(boolean isDangerous) {
		this.dangerous = isDangerous;
	}
	
	@Override
	public String toString() {
		return  super.toString()+ " Name: "+getName()+", Dangerous: "+dangerous+", sleep at the sesson: "+this.isSeasonSleep();
	}

	public boolean isSeasonSleep() {
		return seasonSleep;
	}

	public void setSeasonSleep(boolean seasonSleep) {
		this.seasonSleep = seasonSleep;
	}
}
