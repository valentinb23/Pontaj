package net.metrosystems.ui;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Notification.Type;

import net.metrosystems.domain.Pontaj;

@SpringUI(path = "pontaj")
public class HomePage extends UI {

	@Autowired
	JdbcTemplate jdbcTemplate;

	Grid<Pontaj> grid;
	TextField start;
	TextField stop;
	Label lunaAn;

	@Override
	protected void init(VaadinRequest request) {

		VerticalLayout mainLayout = new VerticalLayout();

		HorizontalLayout adaugaPontajLayout = new HorizontalLayout();	
		
		// Create a DateField with the default style
		DateField dataPontaj = new DateField();		
		
		// Set the date to present
		dataPontaj.setValue(LocalDate.now());
		// Display only year, month, and day
		dataPontaj.setDateFormat("dd.MM.yyyy");
		// Display the correct format as placeholder
		dataPontaj.setPlaceholder("dd.MM.yyyy");
		LocalDate dateStart = YearMonth.now().atDay(1);
		LocalDate dateEnd = YearMonth.now().atEndOfMonth();
		dataPontaj.setRangeStart(dateStart);
		dataPontaj.setRangeEnd(dateEnd);

		LocalDate ziLunaAnDate = dataPontaj.getValue();		
		String lunaAnString = ziLunaAnDate.format(DateTimeFormatter.ofPattern("MMMM YYYY")).toString();							
		lunaAn = new Label(lunaAnString);
		
		start = new TextField();
		start.setPlaceholder("HH:MI");
		start.setMaxLength(5);
		stop = new TextField();
		stop.setPlaceholder("HH:MI");
		stop.setMaxLength(5);

		Button adauga = new Button("Adauga pontaj");
		adauga.addClickListener(e -> {			

			if (!(validator(start.getValue()).equals("OK"))){
			Notification.show("Start hour " + validator(start.getValue()),Type.ERROR_MESSAGE);
			return;
			}
			if (!(validator(stop.getValue()).equals("OK"))){
			Notification.show("Stop hour " + validator(start.getValue()),Type.ERROR_MESSAGE);		
			return;
			}		
		
			LocalDate ziLunaAn = dataPontaj.getValue();
			
			String zi = ziLunaAn.format(DateTimeFormatter.ofPattern("dd"));						
			Locale RO = new Locale("ro","RO");
			String ziSapt = ziLunaAn.getDayOfWeek().getDisplayName(TextStyle.FULL, RO);
			//Notification.show("Aici"+ziSapt,Type.ERROR_MESSAGE);
			int dif = 0;//TODO
			
			String insert = "insert into pontaj (zi, nume, start, stop, diferenta) VALUES ('"+zi+"', '"+ziSapt+"', '"+
					start.getValue()+"', '"+stop.getValue()+"', "+dif+")";
			String update = "update pontaj set start='"+start.getValue()+"' , stop = '"+stop.getValue()+"', diferenta = "+dif+""
					+ " where zi = '"+zi+"'";		
			
			grid.removeAllColumns();
			mainLayout.removeComponent(grid);
			
			String sql = "SELECT * FROM pontaj order by zi";
			List<Pontaj> pontaje = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Pontaj.class));

			grid.setItems(pontaje);
			grid.addColumn(Pontaj::getZi).setCaption("Zi");
			grid.addColumn(Pontaj::getNume).setCaption("Nume");
			grid.addColumn(Pontaj::getStart).setCaption("Start");
			grid.addColumn(Pontaj::getStop).setCaption("Stop");
			grid.addColumn(Pontaj::getDiferenta).setCaption("Diferenta");
			
			mainLayout.addComponent(grid);
			setContent(mainLayout);
			
			try {
			jdbcTemplate.execute(insert);
			} catch (Exception ex) {
				jdbcTemplate.execute(update);
			}
			
		});

		Label istoric = new Label("Istoric");

		grid = new Grid();

		// GetPontajData getPontajData = new GetPontajData();
		// List<Pontaj> pontaje = getPontajData.read();

		String sql = "SELECT * FROM pontaj order by zi";
		List<Pontaj> pontaje = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Pontaj.class));

		grid.setItems(pontaje);
		grid.addColumn(Pontaj::getZi).setCaption("Zi");
		grid.addColumn(Pontaj::getNume).setCaption("Nume");
		grid.addColumn(Pontaj::getStart).setCaption("Start");
		grid.addColumn(Pontaj::getStop).setCaption("Stop");
		grid.addColumn(Pontaj::getDiferenta).setCaption("Diferenta");

		mainLayout.addComponent(lunaAn);
		adaugaPontajLayout.addComponent(dataPontaj);
		adaugaPontajLayout.addComponent(start);
		adaugaPontajLayout.addComponent(stop);
		mainLayout.addComponent(adaugaPontajLayout);

		mainLayout.addComponent(adauga);// buton Adauga pontaj
		
		mainLayout.addComponent(istoric);
		mainLayout.addComponent(grid);

		setContent(mainLayout);
	}

	public static String validator(String oraMinut) {
			
			// format HH24:MI
			if (oraMinut.length() != 5) {
				return " must have the HH24:MI format!";
			}
			
			int ora;
			int minut;
			
			// numeric check
			try {
				ora = Integer.parseInt(oraMinut.substring(0, 2));
			} catch (NumberFormatException e) {
				return " must the HH24:MI format!";
			}
			
			// numeric check
			try {
				minut = Integer.parseInt(oraMinut.substring(3, 5)+oraMinut.substring(5));
			} catch (NumberFormatException e) {
				return " must the HH24:MI format!";
			}			

			if (ora>23 || minut > 59)
			return " must the HH24:MI format!";

			return "OK";


	}
}
