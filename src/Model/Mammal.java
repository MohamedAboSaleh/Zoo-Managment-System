package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;

import Exceptions.AnimalsVisitsException;
import Utils.AnimalFood;
import Utils.Gender;
import Utils.Job;

public class Mammal extends Animal implements AnimalsVisits, Serializable{
	private static int idCounter = 1;
	
	private boolean meatEater;
	private boolean canTakePic;

	
	public Mammal(String name, LocalDate date, AnimalFood food, Gender gender, Section section
			, boolean meatEater, boolean canTakePic) {
		super(idCounter++, name, date, food, gender, section);
		this.meatEater = meatEater;
		this.setCanTakePic(canTakePic);
	}
	
	public Mammal(int id) {
		super(id);
	}

	public boolean isMeatEater() {
		return meatEater;
	}

	public void setMeatEater(boolean meatEater) {
		this.meatEater = meatEater;
	}

	@Override
	public String toString() {
		return super.toString()+" Name: "+getName()+", Meat Eater: "+meatEater +",take a pic" +this.isCanTakePic();
	}

	public boolean isCanTakePic() {
		return canTakePic;
	}

	public void setCanTakePic(boolean canTakePic) {
		this.canTakePic = canTakePic;
	}

	@Override
	public void visitcount(Person p) throws AnimalsVisitsException {
		if (getVisitCounter()<30) {
			setVisitCounter(getVisitCounter()+1);
			hasVistedAnimal(p);
		}
		else {
			throw new AnimalsVisitsException();
		}
	}


	@Override
	public boolean hasVistedAnimal(Person p) {
			if(p instanceof ZooEmployee && getVisitCounter()>29) {
				ZooEmployee e= (ZooEmployee)p;
				if(!Zoo.getInstance().getAnimalTreatedByZooEmployee().containsKey(e)) {
					Zoo.getInstance().getAnimalTreatedByZooEmployee().put(e, new HashSet<Animal>());
					Zoo.getInstance().getAnimalTreatedByZooEmployee().get(e).add(this);
				}
			}
			else if(p instanceof Visitor && getVisitCounter()<30) {
				Visitor v= (Visitor)p;
				if(!Zoo.getInstance().getAnimalTreatedByZooEmployee().containsKey(v)) {
					Zoo.getInstance().getAnimalVisitsByPeople().put(v,new HashSet<Animal>());
					Zoo.getInstance().getAnimalVisitsByPeople().get(v).add(this);
				}
				
		}
				
		return false;
	}
}
