package com.schoology.students.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.schoology.students.dto.PaginatedEntityResults;
import com.schoology.students.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin
@Tag(name = StudentRestController.STUDENTS_API_TAG, description = "Data access API")
public class StudentRestController {
	
	@Autowired
	private StudentService studentService;
	
	protected static final String STUDENTS_API_TAG = "Students";
	
	@Operation(summary = "Find students by name", description = "Receives a query parameter named 'name' to use as a filter to find students and supports pagination using 'limit' and 'offset' parameters."
			+ " Return an object containg the results and the total of entries found", 
				tags = { StudentRestController.STUDENTS_API_TAG }, security = @SecurityRequirement(name = "BearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Students found are returned", 
                content = @Content(schema = @Schema(implementation = PaginatedEntityResults.class))) })
	
	@RequestMapping(value = "/${api.version.v1}/students", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> findByName(@Parameter(description="Name to filter the students,", required=false) @RequestParam(required = false) String name, 
										  @Parameter(description="The (zero-based) page number", required=false) @RequestParam(required = false) Integer page,
										  @Parameter(description="The maximum number of entries to return", required=false) @RequestParam(required = false) Integer limit){
		final PaginatedEntityResults studentsFound = studentService.findByNameLike(name, limit, page);
		return ResponseEntity.ok(studentsFound);
	}
}
