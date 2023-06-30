package Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;

import Utils.Gender;
import Utils.Job;

public class ZooEmployee extends Person implements Serializable{
	private static int idCounter = 1;
	
	private Job job;
	
	
	public ZooEmployee(String firstName, String lastName, LocalDate date, Gender gender, Section section, Job job) {
		super(idCounter++,firstName, lastName, date, gender, section);
		this.job = job;
	}
	
	public ZooEmployee(int id) {
		super(id);
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName()+" Name: "+getFirstName()+" "+getLastName()+", Job: "+job;
	}

	

	

	
}
