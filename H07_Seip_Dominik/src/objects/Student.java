package objects;

import memomap.Identifiable;

public class Student implements Identifiable {

	
	public static Student[] students = new Student[100];
	
	private final int ID;
	
	public Student(int id) throws IndexOutOfBoundsException{
		for (int i=0; i<students.length; i++) {
			if (students[i] != null) {
				if (id == students[i].getID()) {
					throw new IndexOutOfBoundsException("ID bereits vergeben.");
				}
			}	
		}
		for (int i=0; i<students.length; i++) {
			if (students[i] == null) {
				students[i] = this;
			}
		}
		this.ID = id;
			
	}
	
	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return ID;
	}
	
	public boolean equals(Student s) {
		if (s.getID() == this.getID()) {
			return true;
		}
		return false;
	}

}
