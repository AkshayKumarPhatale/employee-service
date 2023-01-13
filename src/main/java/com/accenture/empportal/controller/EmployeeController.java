package com.accenture.empportal.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.empportal.entity.Employee;
import com.accenture.empportal.exception.EmployeeCreationExpection;
import com.accenture.empportal.exception.EmployeeNotFoundException;
import com.accenture.empportal.exception.EmployeeUpdationFailed;
import com.accenture.empportal.exception.NoEmployeeFoundException;
import com.accenture.empportal.request.EmployeeRequest;
import com.accenture.empportal.response.EmployeeResponse;
import com.accenture.empportal.service.IEmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping(value = "/api/employee")
@RestController
@Tag(description = "Employee Controller", name = "Employee Service")
/*
 * @SwaggerDefinition(tags = {
 * 
 * @Tag(name = "empl", description = "employee portal") })
 */
public class EmployeeController {

	@Autowired
	private IEmployeeService iEmployeeService;
    
	
	
	@PostMapping(value = "/addEmployee")
	@Operation(summary = "save an employee", description = "This method is for saving employee object to the db")
	public ResponseEntity<EmployeeResponse> addEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {

		String successMessage = "New Employee Created ";
		String failedMessage = "Employee Creation Failed";
		
     
		Optional<Employee> savedEmployee = iEmployeeService.addEmployee(employeeRequest);

		if (savedEmployee.get().getEmpId() != null) {
			Employee employee=savedEmployee.get();
			EmployeeRequest empDto = new EmployeeRequest();
			empDto.setCreatedAt(employee.getCreatedAt());
			empDto.setEmpName(employee.getEmpName());
			empDto.setEmpId(employee.getEmpId());
			empDto.setEmpGender(employee.getEmpGender().toString());
			empDto.setEmpRole(employee.getEmpRole().toString());
			empDto.setEmpSalary(employee.getEmpSalary());
			empDto.setUpdatedAt(employee.getUpdatedAt());
			empDto.setEmpId(employee.getEmpId());
			empDto.setDeptId(employee.getDeptId());
			empDto.setJoinedDate(employee.getJoinedDate());
			empDto.setManager(employee.getManager());
			empDto.setEmail(employee.getEmail());
			EmployeeResponse employeeResponse = new EmployeeResponse(successMessage, true, empDto);

			return new ResponseEntity<EmployeeResponse>(employeeResponse, HttpStatus.CREATED);
		}

		else {
			 throw new EmployeeCreationExpection(failedMessage);
		}

	}

	@PutMapping(value = "/updateEmployee")
	@Operation(summary = "save an employee", description = "This method is for saving employee object to the db")
	public ResponseEntity<EmployeeResponse> updateEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {

		String successMessage = "Employee details updated ";
		String failedMessage = "Employee details update Failed";
		
		Employee savedEmployee = iEmployeeService.updateEmployee(employeeRequest).get();

		if (savedEmployee.getEmpId() != null) {
			
			EmployeeRequest empDto = new EmployeeRequest();
			empDto.setEmpName(savedEmployee.getEmpName());
			empDto.setEmpId(savedEmployee.getEmpId());
			empDto.setEmpGender(savedEmployee.getEmpGender().toString());
			empDto.setEmpRole(savedEmployee.getEmpRole().toString());
			empDto.setEmpSalary(savedEmployee.getEmpSalary());
			empDto.setUpdatedAt(savedEmployee.getUpdatedAt());
			empDto.setEmpId(savedEmployee.getEmpId());
			empDto.setDeptId(savedEmployee.getDeptId());
			empDto.setJoinedDate(savedEmployee.getJoinedDate());
			empDto.setManager(savedEmployee.getManager());
		
			EmployeeResponse employeeResponse = new EmployeeResponse(successMessage, true, empDto);

			return new ResponseEntity<EmployeeResponse>(employeeResponse, HttpStatus.CREATED);

		} else {
			throw new EmployeeUpdationFailed(failedMessage);
		}

	}

	@GetMapping("/{id}")
	@Operation(summary = "find employee by employee id", description = "This method is for finding employee by employee id from the db")
	public ResponseEntity<Employee> findByEmpId(@NotNull @PathVariable(value = "id") Long empId) {
      
		Optional<Employee> emp = iEmployeeService.findByEmpId(empId);

		if (emp.isPresent()) {

			return new ResponseEntity<Employee>(emp.get(), HttpStatus.OK);
		} else {
			throw new EmployeeNotFoundException("Employee not found for id:" + "" + empId);
		}

	}

	
	@GetMapping("/findall")
	
	@Operation(summary = "show all employee from the db", description = "This method is for showing all  the employees from the  db")
	public ResponseEntity<List<Employee>> findAll() {
		List<Employee> listEmployees = iEmployeeService.findAllEmployees();
		
		if (!listEmployees.isEmpty()) {

			return new ResponseEntity<List<Employee>>(listEmployees, HttpStatus.OK);
		} else {
			throw new NoEmployeeFoundException("No Employee Data Found");

		}

	}
	
	@GetMapping("/email/{email}")
	@Operation(summary = "find employee by the email", description = "This method is finding an employee by the email from the db")
	public ResponseEntity<Employee> findByEmpEmail(@NotNull @PathVariable(value = "email") String email) {
      
		Optional<Employee> emp = iEmployeeService.findByEmail(email);

		if (emp.isPresent()) {

			return new ResponseEntity<Employee>(emp.get(), HttpStatus.OK);
		} else {
			throw new EmployeeNotFoundException("Employee not found for id:" + "" + email);
		}

	}

}
