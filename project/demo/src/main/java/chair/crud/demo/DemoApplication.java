package chair.crud.demo;

import chair.crud.demo.domain.Chair;
import chair.crud.demo.domain.Distributor;
import chair.crud.demo.domain.Manufacturer;
import chair.crud.demo.domain.Specification;
import chair.crud.demo.domain.extension.Address;
import chair.crud.demo.domain.extension.DestinationT;
import chair.crud.demo.domain.extension.MaterialT;
import chair.crud.demo.service.ChairManager;
import chair.crud.demo.service.DistributorManager;
import chair.crud.demo.service.ManufacturerManager;
import chair.crud.demo.service.SpecificationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner appSetup(@Autowired SpecificationManager specificationManager,
									  @Autowired @Qualifier("specifications") List<Specification> specifications,
									  @Autowired ChairManager chairManager,
									  @Autowired @Qualifier("chairs") List<Chair> chairs,
									  @Autowired ManufacturerManager manufacturerManager,
									  @Autowired @Qualifier("manufacturers") List<Manufacturer> manufacturers,
									  @Autowired DistributorManager distributorManager,
									  @Autowired @Qualifier("distributors") List<Distributor> distributors) {
		return args -> {

			for(Specification el : specifications) {
				specificationManager.addSpecification(el);
			}
			for(Manufacturer el : manufacturers) {
				manufacturerManager.addManufacturer(el);
			}
			for (Distributor el : distributors) {
				distributorManager.addDistributor(el);
			}
			for(int i=1; i<=chairs.size(); i++) {
				Chair chairToAdd = chairs.get(i-1);
				chairToAdd.setSpecification(specificationManager.getSpecification(i).get());
				chairToAdd.setManufacturer(manufacturerManager.getManufacturer(i%3 + 1).get());
				chairToAdd.setDistributors(new HashSet<>());
				chairToAdd.addDistributor(distributorManager.getDistributor(i%2 + 1).get());
				chairToAdd.addDistributor(distributorManager.getDistributor(i%2 + 2).get());
				chairManager.addChair(chairToAdd);
			}

		};
	}

	@Bean
	@Qualifier("specifications")
	public List<Specification> specificationList() {
		List<Specification> list = new ArrayList<>();
		list.add(new Specification(9, MaterialT.LEATHER));
		list.add(new Specification(8, MaterialT.WOOD));
		list.add(new Specification(2, MaterialT.PLASTIC));
		list.add(new Specification(12, MaterialT.LEATHER));
		list.add(new Specification(3, MaterialT.PLASTIC));
		list.add(new Specification(6, MaterialT.WOOD));
		list.add(new Specification(11, MaterialT.LEATHER));
		list.add(new Specification(4.5, MaterialT.PLASTIC));
		return list;
	}

	@Bean
	@Qualifier("chairs")
	public List<Chair> chairList() {
		List<Chair> list = new ArrayList<>();
		list.add(new Chair("Business comfort", DestinationT.OFFICE));
		list.add(new Chair("Dallas", DestinationT.KITCHEN));
		list.add(new Chair("Hoker", DestinationT.BAR));
		list.add(new Chair("Comfort casual", DestinationT.OFFICE));
		list.add(new Chair("Donner", DestinationT.KITCHEN));
		return list;
	}

	@Bean
	@Qualifier("manufacturers")
	public List<Manufacturer> manufacturerList() {
		List<Manufacturer> list = new ArrayList<>();
		list.add(new Manufacturer("Pomorskie-Meble", new Address("Grunwaldzka", "211", "Gdańsk", "80-226", "Polska")));
		list.add(new Manufacturer("Meblex", new Address("Niepodległości", "629", "Sopot", "81-553", "Polska")));
		list.add(new Manufacturer("Office Interiors", new Address("Simone Weil Avenue", "59", "Well", "LN13 0BE", "UK")));
		return list;
	}

	@Bean
	@Qualifier("distributors")
	public List<Distributor> distributorList() {
		List<Distributor> list = new ArrayList<>();
		list.add(new Distributor("Krusel Home", "Germany", "+48432343505", new Address("Złota Karczma", "26", "Gdańsk", "80-298", "Polska")));
		list.add(new Distributor("Agata Meble", "Polska", "+48513218556", new Address("Grunwaldzka", "121", "Rumia", "84-223", "Polska")));
		list.add(new Distributor("Jysk", "Polska", "+48512404130", new Address("Obrońców Wybrzeża", "1", "Gdańsk", "80-398", "Polska")));
		return list;
	}

}
