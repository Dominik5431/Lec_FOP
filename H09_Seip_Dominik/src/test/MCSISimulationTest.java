package test;

import org.junit.jupiter.api.Test;

import collections.MyItem;
import collections.MySet;
import diseasespreadings.Contact;
import diseasespreadings.Person;
import diseasespreadings.simulation.MCSISimulation;

public class MCSISimulationTest {
	
	@Test
	public void testGetPersons_AtLeastNInfections() {
		//Realisierung des Kontaktnetzwerks
		Person p0 = new Person("0", 0);
		Person p1 = new Person("1", 1);
		Person p2 = new Person("2", 2);
		Person p3 = new Person("3", 3);
		Contact c01 = new Contact(p0, p1, 0.6, 11); //initial infizierte Person
		Contact c10 = new Contact(p1, p0, 0.6, 12);
		Contact c02 = new Contact(p0, p2, 0.6, 13);
		Contact c20 = new Contact(p2, p0, 0.6, 14);
		Contact c12 = new Contact(p1, p2, 0.6, 15);
		Contact c21 = new Contact(p2, p1, 0.6, 16);
		Contact c23 = new Contact(p2, p3, 0.6, 17);
		Contact c32 = new Contact(p3, p2, 0.6, 18);
		MySet<Contact> contacts = new MySet<>(new MyItem<Contact>(c01));
		MyItem<Contact> item = contacts.getHead();
		item.setNext(new MyItem<Contact>(c10));
		item = item.getNext();
		item.setNext(new MyItem<Contact>(c02));
		item = item.getNext();
		item.setNext(new MyItem<Contact>(c20));
		item = item.getNext();
		item.setNext(new MyItem<Contact>(c12));
		item = item.getNext();
		item.setNext(new MyItem<Contact>(c21));
		item = item.getNext();
		item.setNext(new MyItem<Contact>(c23));
		item = item.getNext();
		item.setNext(new MyItem<Contact>(c32));
		item = item.getNext();
		
		MCSISimulation sim = new MCSISimulation(p0, contacts, 1000);
		sim.run();
		MySet<Person> result = sim.getPersons_AtLeastNInfections(10);
	}

}
