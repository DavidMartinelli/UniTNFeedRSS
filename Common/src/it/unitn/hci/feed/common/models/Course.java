package it.unitn.hci.feed.common.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Course implements Model {

	@XmlElement(name = "id")
	private int mId;

	@XmlElement(name = "name")
	private CourseName mName;

	@XmlElement(name = "colour")
	private int mColour;

	@XmlRootElement
	public enum CourseName {
		GENERIC, FORMAL_LANGUAGES_AND_COMPILERS, INTRODUCTION_TO_CELL_BIOLOGY, PERCORSO_DI_ECCELLENZA_1, ANALISI_MATEMATICA_III, INFORMATICA_ESERCITAZIONE, MACHINE_LEARNING,
	}

	public Course(int id, CourseName name, int colour) {
		super();
		this.mId = id;
		this.mName = name;
		this.mColour = colour;
	}

	public int getId() {
		return mId;
	}

	public CourseName getName() {
		return mName;
	}

	public String getStringName() {
		return mName.toString().replace("_", " ").toLowerCase();
	}

	public int getColour() {
		return mColour;
	}

	@Override
	public boolean isPersistent() {
		return mId >= 0;
	}

	public static Course fromType(CourseName subject) {
		return new Course(-1, subject, generateRandomColor());
	}

	public static int generateRandomColor() {
		return 42;
	}
}
