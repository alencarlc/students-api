package com.schoology.app.students.unit;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.schoology.students.constraints.PaginationConstraints;
import com.schoology.students.domain.Student;
import com.schoology.students.dto.PaginatedEntityResults;
import com.schoology.students.service.StudentService;
import com.schoology.students.service.repository.StudentRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StudentServiceUnitTests.class)
@ActiveProfiles("test")
public class StudentServiceUnitTests {
	
	@Mock
	private PaginationConstraints paginationConstraints;
	
	@Mock
	private StudentRepository repository;
	
	@InjectMocks
	private StudentService service;
	
	@Value("${pagination.default.page}")
	private Integer defaultPaginationPage;
	
	@Value("${pagination.default.limit}")
	private Integer defaultPaginationLimit;
	
	@Test
	public void testFindByNameLike() {
		List<Student> studentMockList = Arrays.asList(new Student[]{Student.builder().name("student1").build(),Student.builder().name("student2").build()});
		when(repository.findByNameContainingOrderByIdDesc(anyString(), Mockito.any(Pageable.class))).thenReturn(studentMockList);
		mockRepositoryCount();
		when(paginationConstraints.getDefaultPage()).thenReturn(this.defaultPaginationPage);
		when(paginationConstraints.getDefaultLimit()).thenReturn(this.defaultPaginationLimit);
		PaginatedEntityResults results = this.service.findByNameLike("student", null, null);
		assertEquals(results.getResults().size(), 2);
		assertEquals(results.getTotal(), Long.valueOf(2L));
	}
	
	@Test
	public void testcountByNameContaining() {
		mockRepositoryCount();
		Long countResult = this.service.countByNameContaining("student");
		assertEquals(countResult, Long.valueOf(2L));
	}
	

	private void mockRepositoryCount() {
		Long returnNumber = Long.valueOf(2L);
		when(repository.countByNameContaining(anyString())).thenReturn(returnNumber);
	}
}

