package com.intuit.employee.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intuit.employee.entity.Employee;
import com.intuit.employee.exceptions.ServiceException;
import com.intuit.employee.model.EmployeeDTO;
import com.intuit.employee.repository.EmployeeRepository;
import com.intuit.employee.service.EmployeeService;
import com.intuit.employee.service.MetricsService;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings({"rawtypes","unchecked"})
@RestController
@RequestMapping("/api/employee")
@Slf4j
public class WorkerController {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	MetricsService metricService;

	
	/*
	 * Returns the list of employees details stored on the server based on pagination
	 * 
	 * @return the list of employee details the specified URL
	 * 
	 * @throws ServiceException
	 */
	@GetMapping("/fetchAllEmployees")
	public ResponseEntity getAllEmployees(@RequestParam(name = "pageNo",  defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize",  defaultValue = "10") Integer pageSize) throws ServiceException {

		try {
			Optional<List<Employee>> employeeList = Optional.ofNullable(employeeService.getAllEmployees(pageNo, pageSize));

			if (employeeList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			
			return new ResponseEntity(employeeList, HttpStatus.OK);

		} catch (Exception e) {
			log.error("Exception while processing getAllEmployees details : "+e.getMessage());
			throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	/*
	 * Returns employees by employeeId
	 * 
	 * @Param empId - Employee Id whose records need to be read
	 * 
	 * @return the specific employee as response at the specified URL
	 * 
	 * @throws ServiceException
	 */
	@GetMapping("fetchEmployee/{employeeId}")
	public ResponseEntity<Employee> retrieveEmployee(@PathVariable("employeeId") String empId) throws ServiceException {
		try {
			Optional<Employee> employeeList = employeeRepository.findById(Long.valueOf(empId));

			if (employeeList.isEmpty()) {
				return new ResponseEntity(employeeList, HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity(employeeList, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Exception while processing retrieveEmployee details {} "+e.getMessage());
			throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	/*
	 * Enroll new employee details and persist on the server
	 * 
	 * @Param employee - All details of the employee structure
	 * 
	 * @return the employee basic details as response at the specified URL
	 * 
	 * @throws ServiceException
	 */
	@PostMapping("/createEmployee")
	public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) throws ServiceException {
		try {
			Employee emp = employeeRepository.save(employee);

			EmployeeDTO employeeDto = new EmployeeDTO();
			employeeDto.setId(emp.getId());
			employeeDto.setName(emp.getName());

			return new ResponseEntity(employeeDto, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Exception while processing createEmployee :"+e.getMessage());
			throw new ServiceException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	/*
	 * Deletes the employee details on the server(Soft Delete)
	 * 
	 * @Param empId - Employee Id whose records need to be soft deleted
	 * 
	 * @return the status of the operation
	 * 
	 * @throws ServiceException
	 */
	@DeleteMapping("/deleteEmployee/{employeeId}")
	public ResponseEntity deleteEmpById(@PathVariable("employeeId") Long empId) throws ServiceException {
		try {
			Optional<Employee> employee = employeeRepository.findById(empId);

			if (employee.isEmpty()) {
				return new ResponseEntity("Employee Details not found", HttpStatus.NO_CONTENT);
			}

			employeeRepository.deleteById(empId);
			log.debug("Response deleteEmpById - {} ",empId); 
			return ResponseEntity.ok("Successfully deleted resource");
		} catch (Exception e) {
			log.error("Exception while processing createEmployee :"+e.getMessage());
			throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	/*
	 * Update the employee details on the server
	 * 
	 * @Param empId - Employee Id whose records need to be updated
	 * 
	 * @Param employeeDetails - Entity representation the employee
	 * 
	 * @return the status of the operation
	 * 
	 * @throws ServiceException
	 */
	@PutMapping("/updateEmployee/{employeeId}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "employeeId") Long empId,
			@RequestBody Employee employeeDetails) {
		Optional<Employee> employeeOptionalObj = employeeRepository.findById(empId);

		if (employeeOptionalObj.isEmpty()) {
			return new ResponseEntity(employeeOptionalObj, HttpStatus.NO_CONTENT);
		}

		Employee employee = employeeOptionalObj.get();
		employee.setAddressLine1(employeeDetails.getAddressLine1());
		employee.setAddressLine2(employeeDetails.getAddressLine2());
		employee.setCity(employeeDetails.getCity());
		employee.setCountry(employeeDetails.getCountry());
		employee.setDesigntion(employeeDetails.getDesigntion());
		employee.setIsActive(employeeDetails.getIsActive());
		employee.setPincode(employeeDetails.getPincode());
		employee.setState(employeeDetails.getState());

		final Employee updatedEmployee = employeeRepository.save(employee);
		log.debug("Response updateEmployee - {} ",updatedEmployee.getId());
		return ResponseEntity.ok(updatedEmployee);
	}

	/*
	 * Returns the metrics related to operations on the server
	 */
	@GetMapping("/viewMetrics")
	public ResponseEntity retrieveMetrics() {
		return ResponseEntity.ok(metricService.getStatusMetricMap());
	}

}
