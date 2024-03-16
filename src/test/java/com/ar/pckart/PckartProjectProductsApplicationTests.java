package com.ar.pckart;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

//import com.ar.pckart.model.Person;
//import com.ar.pckart.model.PersonDTO;
//import com.ar.pckart.model.PersonRepo;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PckartProjectProductsApplicationTests {

	/*
	@Autowired
	private PersonRepo repo;

	@BeforeAll
	void beforeCreatePersons() {
		Map<String, String> map1 = new HashMap<>();
		map1.put("home", "9673");
		map1.put("office", "90484");
		Map<String, String> map2 = new HashMap<>();
		map2.put("home", "83348");
		map2.put("office", "1232");
		repo.save(
				Person.builder()
				.id(1L)
				.name("abdu")
//				.phones(new HashSet<>(List.of("9673","9083")))
				.phones(map1)
				.build());
		repo.save(
				Person.builder()
				.id(2L)
				.name("raaka")
//				.phones(new HashSet<>(List.of("8970","5272")))
				.phones(map1)
				.build());
	}
	
	@Test
	void findAllPersons() {
		List<PersonDTO> findAllPersons = repo.findAllPersons();
		Assertions.assertTrue(findAllPersons.size() > 0);
		findAllPersons.forEach(System.out::println);
	}
	
	@Test
	void allPhones() {
//		Set<String> findPhones = repo.findPhones();
		Map<String,String> findPhones = repo.findPhones();
		Assertions.assertTrue(!findPhones.isEmpty());
//		Set<Entry<String,String>> entrySet = findPhones.entrySet();
//		for(Entry<String,String> e: entrySet) {
//			System.err.println(e.getKey()+" : "+e.getValue());
//		}
	}
*/
}
