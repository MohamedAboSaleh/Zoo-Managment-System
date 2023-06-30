package Model;

import java.io.Serializable;

import Exceptions.AnimalsVisitsException;

public interface AnimalsVisits extends Serializable{
	
	public void visitcount(Person p) throws AnimalsVisitsException ;
	public boolean hasVistedAnimal(Person p);

	
	

}
