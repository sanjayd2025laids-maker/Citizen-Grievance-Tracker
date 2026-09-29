package com.Citizen.demo.config;

import com.Citizen.demo.entity.Category;
import com.Citizen.demo.entity.Department;
import com.Citizen.demo.repository.CategoryRepository;
import com.Citizen.demo.repository.DepartmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadSampleData(DepartmentRepository departmentRepository,
                                    CategoryRepository categoryRepository) {
        return args -> {
            if (departmentRepository.count() == 0) {
                Department publicWorks = new Department();
                publicWorks.setName("Public Works Department");
                departmentRepository.save(publicWorks);

                Department water = new Department();
                water.setName("Water Department");
                departmentRepository.save(water);

                Department sanitation = new Department();
                sanitation.setName("Sanitation Department");
                departmentRepository.save(sanitation);
            }

            if (categoryRepository.count() == 0) {
                Department publicWorks = departmentRepository.findByName("Public Works Department")
                        .orElseThrow(() -> new IllegalStateException("Public Works Department not found."));
                Department water = departmentRepository.findByName("Water Department")
                        .orElseThrow(() -> new IllegalStateException("Water Department not found."));
                Department sanitation = departmentRepository.findByName("Sanitation Department")
                        .orElseThrow(() -> new IllegalStateException("Sanitation Department not found."));

                Category road = new Category();
                road.setName("Road");
                road.setSlaDays(3);
                road.setDepartment(publicWorks);
                categoryRepository.save(road);

                Category waterCategory = new Category();
                waterCategory.setName("Water");
                waterCategory.setSlaDays(2);
                waterCategory.setDepartment(water);
                categoryRepository.save(waterCategory);

                Category garbage = new Category();
                garbage.setName("Garbage");
                garbage.setSlaDays(1);
                garbage.setDepartment(sanitation);
                categoryRepository.save(garbage);
            }
        };
    }
}
