package net.metrosystems.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

public class Pontaj {
	
	private String zi;
	private String nume;
	private String start;
	private String stop;
	private int diferenta;

	public String getZi() {
		return zi;
	}

	public void setZi(String zi) {
		this.zi = zi;
	}

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getStop() {
		return stop;
	}

	public void setStop(String stop) {
		this.stop = stop;
	}

	public int getDiferenta() {
		return diferenta;
	}

	public void setDiferenta(int diferenta) {
		this.diferenta = diferenta;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Pontaj [zi=");
		builder.append(zi);
		builder.append(", nume=");
		builder.append(nume);
		builder.append(", start=");
		builder.append(start);
		builder.append(", stop=");
		builder.append(stop);
		builder.append(", diferenta=");
		builder.append(diferenta);
		builder.append("]");
		return builder.toString();
	}

}
