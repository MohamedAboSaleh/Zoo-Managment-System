package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.TreeSet;

import Exceptions.AnimalsVisitsException;
import Exceptions.DuplicatedValues;
import Exceptions.MaximumCapcityException;
import Utils.MyFileLogWriter;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Pair;

public class Zoo implements Serializable {

	private static Zoo zoo = null;

	private HashMap<Integer, ZooEmployee> employees;
	private HashMap<Integer, Visitor> visitors;
	private HashMap<Integer, Mammal> mammals;
	private HashMap<Integer, Reptile> reptiles;
	private HashMap<Integer, Bird> birds;
	private HashMap<Integer, Section> sections;
	private HashMap<Integer, Snack> snacks;
	private HashMap<Integer, SnackBar> bars;
	private HashMap<ZooEmployee, HashSet<Animal>> animalTreatedByZooEmployee;
	private HashMap<Visitor, HashSet<Animal>> AnimalVisitsByPeople;
	private static HashMap<Pair<String, String>, ZooEmployee> users;// the key is pair of (username, password) and the
																	// value is the employee of this account
	private static Pair<Pair<String, String>, ZooEmployee> loggedUser;// a pair that has a pair of (pair
																		// of(username,password),its employee) that
																		// stores the already logged in user

	public static Zoo getInstance() {
		if (zoo == null)
			zoo = new Zoo();
		return zoo;
	}

	private Zoo() {
		employees = new HashMap<>();
		visitors = new HashMap<>();
		mammals = new HashMap<>();
		reptiles = new HashMap<>();
		birds = new HashMap<>();
		sections = new HashMap<>();
		snacks = new HashMap<>();
		bars = new HashMap<>();
		animalTreatedByZooEmployee = new HashMap<>();
		AnimalVisitsByPeople = new HashMap<>();
		users = new HashMap<Pair<String, String>, ZooEmployee>();
		loggedUser = null;
	}

	public HashMap<Integer, ZooEmployee> getEmployees() {
		return employees;
	}

	public void setEmployees(HashMap<Integer, ZooEmployee> employees) {
		this.employees = employees;
	}

	public HashMap<Integer, Visitor> getVisitors() {
		return visitors;
	}

	public void setVisitorsById(HashMap<Integer, Visitor> visitors) {
		this.visitors = visitors;
	}

	public HashMap<Integer, Mammal> getMammals() {
		return mammals;
	}

	public void setMammalsById(HashMap<Integer, Mammal> mammals) {
		this.mammals = mammals;
	}

	public HashMap<Integer, Reptile> getReptiles() {
		return reptiles;
	}

	public void setReptilesById(HashMap<Integer, Reptile> reptiles) {
		this.reptiles = reptiles;
	}

	public HashMap<Integer, Bird> getBirds() {
		return birds;
	}

	public void setBirdsById(HashMap<Integer, Bird> birds) {
		this.birds = birds;
	}

	public HashMap<Integer, Section> getSections() {
		return sections;
	}

	public void setSections(HashMap<Integer, Section> sections) {
		this.sections = sections;
	}

	public HashMap<Integer, Snack> getSnacks() {
		return snacks;
	}

	public void setSnacksById(HashMap<Integer, Snack> snacks) {
		this.snacks = snacks;
	}

	public HashMap<Integer, SnackBar> getBars() {
		return bars;
	}

	public void setBars(HashMap<Integer, SnackBar> bars) {
		this.bars = bars;
	}

	/**
	 * @return the animalTreatedByZooEmployee
	 */
	public HashMap<ZooEmployee, HashSet<Animal>> getAnimalTreatedByZooEmployee() {
		return animalTreatedByZooEmployee;
	}

	/**
	 * @param animalTreatedByZooEmployee the animalTreatedByZooEmployee to set
	 */
	public void setAnimalTreatedByZooEmployee(HashMap<ZooEmployee, HashSet<Animal>> animalTreatedByZooEmployee) {
		this.animalTreatedByZooEmployee = animalTreatedByZooEmployee;
	}

	/**
	 * @return the animalVisitsByPeople
	 */
	public HashMap<Visitor, HashSet<Animal>> getAnimalVisitsByPeople() {
		return AnimalVisitsByPeople;
	}

	/**
	 * @param animalVisitsByPeople the animalVisitsByPeople to set
	 */
	public void setAnimalVisitsByPeople(HashMap<Visitor, HashSet<Animal>> animalVisitsByPeople) {
		AnimalVisitsByPeople = animalVisitsByPeople;
	}

	public boolean addEmployee(ZooEmployee employee) {
		if (employee == null)
			return false;

		if (!getEmployees().containsValue(employee)) {
			getEmployees().put(employee.getId(), employee);
			employee.getSection().getEmployees().add(employee);
		} else
			return false;

		return true;
	}

	public boolean addVisitor(Visitor v) {
		if (v == null)
			return false;

		if (!getVisitors().containsValue(v))
			getVisitors().put(v.getId(), v);
		else
			return false;
		return true;
	}

	public boolean addMammalById(Mammal m) {
		if (m == null)
			return false;

		if (!getMammals().containsValue(m)) {
			getMammals().put(m.getId(), m);
			m.getSection().getMammals().add(m);
		} else
			return false;
		return true;
	}

	public boolean addReptileById(Reptile r) {
		if (r == null)
			return false;

		if (!getReptiles().containsValue(r)) {
			getReptiles().put(r.getId(), r);
			r.getSection().getReptiles().add(r);
		} else
			return false;
		return true;
	}

	public boolean addBirdById(Bird b) {
		if (b == null)
			return false;

		if (!getBirds().containsValue(b)) {
			getBirds().put(b.getId(), b);

			b.getSection().getBirds().add(b);
		} else
			return false;
		return true;
	}

	public boolean addSection(Section s) {
		if (s == null)
			return false;

		if (!getSections().containsValue(s))
			getSections().put(s.getId(), s);
		else
			return false;

		return true;
	}

	public boolean addSnack(Snack s) {
		if (s == null)
			return false;

		if (!getSnacks().containsValue(s))
			getSnacks().put(s.getId(), s);
		else
			return false;
		s.getBar().getSnacks().add(s);
		return true;
	}

	public boolean addSnackBar(SnackBar sb, Section s) {
		if (sb == null || s == null)
			return false;

		if (!getBars().containsValue(sb))
			getBars().put(sb.getId(), sb);
		else
			return false;

		s.setBar(sb);
		return true;
	}

	public boolean removeEmployee(ZooEmployee employee) {
		if (employee == null)
			return false;

		getEmployees().remove(employee.getId());
		employee.getSection().getEmployees().remove(employee);
		return true;
	}

	public boolean removeVisitor(Visitor v) {
		if (v == null)
			return false;

		getVisitors().remove(v.getId());
		v.getSection().getVisitors().remove(v);

		return true;
	}

	public boolean removeMammal(Mammal m) {
		if (m == null)
			return false;

		getMammals().remove(m.getId());
		m.getSection().getMammals().remove(m);
		return true;
	}

	public boolean removeReptile(Reptile r) {
		if (r == null)
			return false;

		getReptiles().remove(r.getId());
		r.getSection().getReptiles().remove(r);
		return true;
	}

	public boolean removeBird(Bird b) {
		if (b == null)
			return false;

		getBirds().remove(b.getId());
		b.getSection().getBirds().remove(b);
		return true;
	}

	public boolean removeSection(Section oldSection, Section newSection) {
		if (oldSection == null || newSection == null)
			return false;

		newSection.getEmployees().addAll(oldSection.getEmployees());
		newSection.getVisitors().addAll(oldSection.getVisitors());
		newSection.getMammals().addAll(oldSection.getMammals());
		newSection.getReptiles().addAll(oldSection.getReptiles());
		newSection.getBirds().addAll(oldSection.getBirds());
		removeSnackBar(oldSection.getBar());

		getSections().remove(oldSection.getId());

		return true;
	}

	public boolean removeSnackBar(SnackBar sb) {
		if (sb == null)
			return false;

		sb.getSection().setBar(null);
		getBars().remove(sb.getId());
		return true;
	}

	public boolean removeSnack(Snack s) {
		if (s == null)
			return false;

		for (SnackBar sb : getBars().values()) {
			sb.getSnacks().remove(s);
		}

		getSnacks().remove(s.getId());
		return true;
	}

	public ZooEmployee getRealEmployee(int id) {
		return getEmployees().get(id);
	}

	public Visitor getRealVisitor(int id) {

		return getVisitors().get(id);
	}

	public Mammal getRealMammal(int id) {
		return getMammals().get(id);
	}

	public Reptile getRealReptile(int id) {

		return getReptiles().get(id);
	}

	public Bird getRealBird(int id) {

		return getBirds().get(id);
	}

	public Section getRealSection(int id) {
		return getSections().get(id);
	}

	public SnackBar getRealSnackBar(int id) {
		return getBars().get(id);
	}

	public Snack getRealSnack(int id) {

		return getSnacks().get(id);
	}

	public double checkTotalRevenue() {
		double revenue = 0;
		for (Section s : getSections().values()) {
			if (s != null && s.getBar() != null) {
				revenue += s.getTodayRevenue();
				revenue += s.getBar().getProfit() * SnackBar.getZooPercentage();
			}
		}
		// MyFileLogWriter.println("Zoo Revenue: "+revenue);
		return revenue;
	}

	public boolean newVisitorInZoo(Visitor v, Section s) {
		boolean isValid = true;
		if (v == null || s == null)
			isValid = false;

		if (!addVisitor(v))
			isValid = false;

		if (isValid && !s.getVisitors().contains(v)) {
			s.getVisitors().add(v);
			v.setSection(s);
			double price = v.checkTicketPrice();
			s.setTodayRevenue(s.getTodayRevenue() + price);
			// MyFileLogWriter.println("New "+v+" Paid "+price+" NIS");
			return true;
		}
		// MyFileLogWriter.println(v+" didn't enter the zoo");
		return false;
	}

	public ArrayList<Animal> getAllAnimalsBySectionMaxVisits(Section s) {
		if (s == null) {
			return null;
		}
		ArrayList<Animal> animals = new ArrayList<Animal>();

		for (Animal a : s.getBirds()) {

			animals.add(a);

		}

		for (Animal a : s.getReptiles()) {
			animals.add(a);

		}

		for (Animal a : s.getMammals()) {
			animals.add(a);

		}

		animals.sort(new Comparator<Animal>() {

			@Override
			public int compare(Animal o1, Animal o2) {
				String o1Class = o1.getClass().getSimpleName();
				String o2Class = o2.getClass().getSimpleName();
				if (o1Class.equals(o2Class)) {
					Integer count1 = o1.getVisitCounter();
					Integer count2 = o2.getVisitCounter();
					return count1.compareTo(count2);
				} else
					return o1Class.compareTo(o2Class);
			}

		});
		return animals;

	}

	@SuppressWarnings("null")
	public ArrayList<Animal> allAnimalsByWorker(ZooEmployee employee) {

		ArrayList<Animal> treatsBy = new ArrayList<Animal>();
		if (getEmployees().containsValue(employee)) {

			HashSet<Animal> animalsOfEmployee = getAnimalTreatedByZooEmployee().get(employee);

			if (animalsOfEmployee == null) {
				return null;
			}
			for (Bird b : getBirds().values()) {
				if (!animalsOfEmployee.contains(b) && (!b.isCanFly()))
					treatsBy.add(b);
			}
			for (Mammal m : getMammals().values()) {
				if (!animalsOfEmployee.contains(m) && (!m.isMeatEater()))
					treatsBy.add(m);
			}
			for (Reptile r : getReptiles().values()) {
				if (!animalsOfEmployee.contains(r) && (r.isDangerous()))
					treatsBy.add(r);
			}

			treatsBy.sort(null);

		}
		return treatsBy;
	}

	public ArrayList<Snack> findAllSnackByWorker(SnackBar sb) {
		ArrayList<Snack> snacks = new ArrayList<Snack>();

		for (Snack s : sb.getSnacks()) {
			if (s instanceof Drink) {
				Drink d = (Drink) s;
				if (!d.isSprinkel() && d.isIceCube()) {

					snacks.add(d);
				}
			} else if (s instanceof Food) {
				Food f = (Food) s;
				if (f.isSpaciy() && !f.isPlate()) {
					snacks.add(f);
				}
			}
		}

		snacks.sort(new Comparator<Snack>() {

			@Override
			public int compare(Snack o1, Snack o2) {

				return o1.getSnackName().compareTo(o2.getSnackName());
			}

		});

		return snacks;

	}

	public ArrayList<Reptile> reptilesSleepAtSeasson() {
		ArrayList<Reptile> reptiles = new ArrayList<Reptile>();
		if (getReptiles() != null) {

			for (Reptile r : getReptiles().values()) {
				if (r.isSeasonSleep() && (!r.isDangerous())) {
					reptiles.add(r);
				}
			}
		}

		reptiles.sort(new Comparator<Reptile>() {

			@Override
			public int compare(Reptile o1, Reptile o2) {
				if (o1.getName().equals(o2.getName())) {
					return o1.getGender().compareTo(o2.getGender());
				}
				return o1.getName().compareTo(o2.getName());
			}

		});
		return reptiles;

	}

	public TreeMap<Visitor, Double> geAllDiscountAmount() {

		TreeMap<Visitor, Double> Discount = new TreeMap<Visitor, Double>(new Comparator<Visitor>() {

			@Override
			public int compare(Visitor o1, Visitor o2) {
				Double o1Price = o1.getTicket().getValue() - o1.checkTicketPrice();
				Double o2Price = o2.getTicket().getValue() - o2.checkTicketPrice();
				return o1Price.compareTo(o2Price);
			}
		});

		for (Visitor v : getVisitors().values()) {

			Discount.put(v, v.getTicket().getValue() - v.checkTicketPrice());

		}

		return Discount;
	}

	public Section getMaxVisitorsVSMaxWorkers() {
		PriorityQueue<Section> pqSection = new PriorityQueue<Section>(new Comparator<Section>() {

			@Override
			public int compare(Section o1, Section o2) {
				int sub1 = o1.getVisitors().size() - o1.getEmployees().size();
				int sub2 = o2.getVisitors().size() - o2.getEmployees().size();

				return sub2 - sub1;
			}

		});

		for (Section s : getSections().values()) {

			pqSection.add(s);

		}
		return pqSection.poll();
	}

	public HashMap<Pair<String, String>, ZooEmployee> getUsers() {
		return users;
	}

	public void setUsers(HashMap<Pair<String, String>, ZooEmployee> users) {
		this.users = users;
	}

	public static void addUser(Pair<String, String> acc, ZooEmployee emp) {
		users.put(acc, emp);
	}

	public static Pair<Pair<String, String>, ZooEmployee> getLoggedUser() {
		return loggedUser;
	}

	public static void setLoggedUser(Pair<Pair<String, String>, ZooEmployee> loggedUser) {
		Zoo.loggedUser = loggedUser;
	}

	public void saveToSerFile() {
		ObjectOutputStream writeToSerFile = null;
		FileOutputStream writeDataToSerFile = null;
		try {
			// save all the data to the .ser file
			writeDataToSerFile = new FileOutputStream("Zoo.ser");
			writeToSerFile = new ObjectOutputStream(writeDataToSerFile);
			writeToSerFile.writeObject(users);
			writeToSerFile.writeObject(zoo.getEmployees());
			writeToSerFile.writeObject(zoo.getVisitors());
			writeToSerFile.writeObject(zoo.getMammals());
			writeToSerFile.writeObject(zoo.getReptiles());
			writeToSerFile.writeObject(zoo.getBirds());
			writeToSerFile.writeObject(zoo.getSections());
			writeToSerFile.writeObject(zoo.getSnacks());
			writeToSerFile.writeObject(zoo.getBars());
			writeToSerFile.writeObject(zoo.getAnimalTreatedByZooEmployee());
			writeToSerFile.writeObject(zoo.getAnimalVisitsByPeople());
			writeToSerFile.flush();
			writeToSerFile.close();

		} catch (IOException e) {
			Alert alertConfirm = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			alertConfirm.showAndWait();
		}
	}

	public void getDataFromSerFile() {
		File serFile = new File("Zoo.ser");
		if (serFile.exists() && !serFile.isDirectory()) { // file exists
			try {
				// serFile.createNewFile();
				// read from the existing file
				FileInputStream readDataFromSerFile = new FileInputStream("Zoo.ser");
				ObjectInputStream readFromSerFile = new ObjectInputStream(readDataFromSerFile);

				// read all the data from the .ser file
				HashMap<Pair<String, String>, ZooEmployee> tempUsers = (HashMap<Pair<String, String>, ZooEmployee>) readFromSerFile
						.readObject();
				this.setUsers(tempUsers);
				HashMap<Integer, ZooEmployee> employees = (HashMap<Integer, ZooEmployee>) readFromSerFile.readObject();
				this.setEmployees(employees);
				HashMap<Integer, Visitor> visitors = (HashMap<Integer, Visitor>) readFromSerFile.readObject();
				this.setVisitorsById(visitors);
				HashMap<Integer, Mammal> mammals = (HashMap<Integer, Mammal>) readFromSerFile.readObject();
				this.setMammalsById(mammals);
				HashMap<Integer, Reptile> reptiles = (HashMap<Integer, Reptile>) readFromSerFile.readObject();
				this.setReptilesById(reptiles);
				HashMap<Integer, Bird> birds = (HashMap<Integer, Bird>) readFromSerFile.readObject();
				this.setBirdsById(birds);
				HashMap<Integer, Section> sections = (HashMap<Integer, Section>) readFromSerFile.readObject();
				this.setSections(sections);
				HashMap<Integer, Snack> snacks = (HashMap<Integer, Snack>) readFromSerFile.readObject();
				this.setSnacksById(snacks);
				HashMap<Integer, SnackBar> bars = (HashMap<Integer, SnackBar>) readFromSerFile.readObject();
				this.setBars(bars);
				HashMap<ZooEmployee, HashSet<Animal>> animalTreatedByZooEmployee = (HashMap<ZooEmployee, HashSet<Animal>>) readFromSerFile
						.readObject();
				this.setAnimalTreatedByZooEmployee(animalTreatedByZooEmployee);
				HashMap<Visitor, HashSet<Animal>> AnimalVisitsByPeople = (HashMap<Visitor, HashSet<Animal>>) readFromSerFile
						.readObject();
				this.setAnimalVisitsByPeople(AnimalVisitsByPeople);

				readDataFromSerFile.close();
				readFromSerFile.close();// close file

			} catch (IOException | ClassNotFoundException e) {
				Alert alertConfirm = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
				alertConfirm.showAndWait();
			}
		} else { // ser file doesn't exists
			try {
				// open new .ser file
				FileOutputStream writeDataToSerFile = new FileOutputStream("Zoo.ser");
				ObjectOutputStream writeToSerFile = new ObjectOutputStream(writeDataToSerFile);
				users.put(new Pair<>("admin", "admin"), null);// add the admin user
				writeToSerFile.writeObject(users);// save the users
				// save the data
				HashMap<Integer, ZooEmployee> employees = zoo.getEmployees();
				HashMap<Integer, Visitor> visitors = zoo.getVisitors();
				HashMap<Integer, Mammal> mammals = zoo.getMammals();
				HashMap<Integer, Reptile> reptiles = zoo.getReptiles();
				HashMap<Integer, Bird> birds = zoo.getBirds();
				HashMap<Integer, Section> sections = zoo.getSections();
				HashMap<Integer, Snack> snacks = zoo.getSnacks();
				HashMap<Integer, SnackBar> bars = zoo.getBars();
				HashMap<ZooEmployee, HashSet<Animal>> animalTreatedByZooEmployee = zoo.getAnimalTreatedByZooEmployee();
				HashMap<Visitor, HashSet<Animal>> AnimalVisitsByPeople = zoo.getAnimalVisitsByPeople();

				writeToSerFile.writeObject(employees);
				writeToSerFile.writeObject(visitors);
				writeToSerFile.writeObject(mammals);
				writeToSerFile.writeObject(reptiles);
				writeToSerFile.writeObject(birds);
				writeToSerFile.writeObject(sections);
				writeToSerFile.writeObject(snacks);
				writeToSerFile.writeObject(bars);
				writeToSerFile.writeObject(animalTreatedByZooEmployee);
				writeToSerFile.writeObject(AnimalVisitsByPeople);

				writeToSerFile.flush();
				writeDataToSerFile.close();// close file
				writeToSerFile.close();
			} catch (IOException e) {
				Alert alertConfirm = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
				alertConfirm.showAndWait();
			}
		}

	}
}
