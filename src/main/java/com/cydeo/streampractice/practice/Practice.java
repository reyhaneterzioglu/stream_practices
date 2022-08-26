package com.cydeo.streampractice.practice;

import com.cydeo.streampractice.model.*;
import com.cydeo.streampractice.service.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class Practice {

    public static CountryService countryService;
    public static DepartmentService departmentService;
    public static EmployeeService employeeService;
    public static JobHistoryService jobHistoryService;
    public static JobService jobService;
    public static LocationService locationService;
    public static RegionService regionService;

    public Practice(CountryService countryService, DepartmentService departmentService,
                    EmployeeService employeeService, JobHistoryService jobHistoryService,
                    JobService jobService, LocationService locationService,
                    RegionService regionService) {

        Practice.countryService = countryService;
        Practice.departmentService = departmentService;
        Practice.employeeService = employeeService;
        Practice.jobHistoryService = jobHistoryService;
        Practice.jobService = jobService;
        Practice.locationService = locationService;
        Practice.regionService = regionService;

    }

    // You can use the services above for all the CRUD (create, read, update, delete) operations.
    // Above services have all the required methods.
    // Also, you can check all the methods in the ServiceImpl classes inside the service.impl package, they all have explanations.

    // Display all the employees
    public static List<Employee> getAllEmployees() {
        return employeeService.readAll();
    }

    // Display all the countries
    public static List<Country> getAllCountries() {
        return countryService.readAll();
    }

    // Display all the departments
    public static List<Department> getAllDepartments() {
        return departmentService.readAll();
    }

    // Display all the jobs
    public static List<Job> getAllJobs() {
        return jobService.readAll();
    }

    // Display all the locations
    public static List<Location> getAllLocations() {
        return locationService.readAll();
    }

    // Display all the regions
    public static List<Region> getAllRegions() {
        return regionService.readAll();
    }

    // Display all the job histories
    public static List<JobHistory> getAllJobHistories() {
        return jobHistoryService.readAll();
    }

    // Display all the employees' first names
    public static List<String> getAllEmployeesFirstName() {
        // return employeeService.readAll() // this works too
        return getAllEmployees()
                .stream()
                .map(Employee::getFirstName)
                .collect(Collectors.toList());
    }

    // Display all the countries' names
    public static List<String> getAllCountryNames() {
        //  return countryService.readAll()
        return getAllCountries()
                .stream()
                .map(Country::getCountryName)
                .collect(Collectors.toList());
    }

    // Display all the departments' managers first names
    public static List<String> getAllDepartmentManagerFirstNames() {
        return departmentService.readAll()
                .stream()
                //  .map(department -> department.getManager().getFirstName())
                .map(Department::getManager)
                .map(Employee::getFirstName)
                .collect(Collectors.toList());
    }

    // Display all the departments where manager name of the department is 'Steven'
    public static List<Department> getAllDepartmentsWhichManagerFirstNameIsSteven() {
        //   return departmentService.readAll()
        return getAllDepartments()
                .stream()
                .filter(i -> i.getManager().getFirstName().equalsIgnoreCase("Steven"))
                .collect(Collectors.toList());
    }

    // Display all the departments where postal code of the location of the department is '98199'
    public static List<Department> getAllDepartmentsWhereLocationPostalCodeIs98199() {
        return getAllDepartments()
                .stream()
                .filter(i -> i.getLocation().getPostalCode().equals("98199"))
                .collect(Collectors.toList());
    }

    // Display the region of the IT department
    public static Region getRegionOfITDepartment() throws Exception {
     /* solution 1
        return departmentService.readAll()
                .stream()
                .filter(i -> i.getDepartmentName().equalsIgnoreCase("IT"))
                .map(department -> department.getLocation().getCountry().getRegion())
                .findAny().get();

      */
      /* solution 2
        return departmentService.readAll()
                .stream()
                .filter(i -> i.getDepartmentName().equalsIgnoreCase("IT"))
                .map(Department::getLocation)
                .map(Location::getCountry)
                .map(Country::getRegion)
                .findAny().get();

      */

// solution 3
        return getAllDepartments().stream()
                .filter(department -> department.getDepartmentName().equals("IT"))
                .findFirst().orElseThrow(() -> new RuntimeException("no department found"))
                .getLocation().getCountry().getRegion();
    }

    // 14. Display all the departments where the region of department is 'Europe'
    public static List<Department> getAllDepartmentsWhereRegionOfCountryIsEurope() {

        return getAllDepartments()
                .stream()
                .filter(i -> i.getLocation().getCountry().getRegion().getRegionName().equalsIgnoreCase("Europe"))
                .collect(Collectors.toList());
    }

    // 15. Display if there is any employee with salary less than 1000. If there is none, the method should return true
    public static boolean checkIfThereIsNoSalaryLessThan1000() {
        // return getAllEmployees().stream().allMatch(employee -> employee.getSalary() > 1000);
        //return getAllEmployees().stream().noneMatch(employee -> employee.getSalary() < 1000);
        // return employeeService.readAll().stream().map(Employee::getSalary).noneMatch(employee -> employee < 1000);
        return !getAllEmployees().stream().anyMatch(employee -> employee.getSalary() < 1000);
    }

    // 16. Check if the salaries of all the employees in IT department are greater than 2000 (departmentName: IT)
    public static boolean checkIfThereIsAnySalaryGreaterThan2000InITDepartment() {

     /*   return employeeService.readAll()
                .stream()
                .filter(i -> i.getDepartment().getDepartmentName().equalsIgnoreCase("IT"))
                .map(Employee::getSalary)
                .anyMatch(i -> i > 2000);

      */

        return getAllEmployees().stream()
                .filter(employee -> employee.getDepartment().getDepartmentName().equals("IT"))
                .noneMatch(employee -> employee.getSalary() < 2000);
    }

    // 17. Display all the employees whose salary is less than 5000
    public static List<Employee> getAllEmployeesWithLessSalaryThan5000() {
        return getAllEmployees()
                .stream()
                .filter(i -> i.getSalary() < 5000)
                .collect(Collectors.toList());
    }

    // 18. Display all the employees whose salary is between 6000 and 7000
    public static List<Employee> getAllEmployeesSalaryBetween() {
        return employeeService.readAll()
                .stream()
                .filter(i -> i.getSalary() > 6000 && i.getSalary() < 7000)
                .collect(Collectors.toList());
    }

    // 19. Display the salary of the employee Grant Douglas (lastName: Grant, firstName: Douglas)
    public static Long getGrantDouglasSalary() throws Exception {

      /*  return employeeService.readAll()
                .stream()
                .filter(i -> i.getFirstName().equalsIgnoreCase("Douglas") && i.getLastName().equalsIgnoreCase("Grant"))
                .map(Employee::getSalary)
                .findAny().get();

       */

        return getAllEmployees().stream().filter(i -> i.getFirstName().equalsIgnoreCase("Douglas"))
                .filter(i -> i.getLastName().equalsIgnoreCase("Grant"))
                .findFirst().orElseThrow(() -> new Exception("no employee found"))
                .getSalary();
    }

    // 20. Display the maximum salary an employee gets
    public static Long getMaxSalary() {


        return employeeService.readAll()
                .stream()
                .map(Employee::getSalary)
                // .reduce(Long::max).get();
                .reduce((salary1, salary2) -> salary1 > salary2 ? salary1 : salary2).get();


        /*
         return getAllEmployees().stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .limit(1).collect(Collectors.toList()).get(0).getSalary();
         */

        /*
       return getAllEmployees().stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .findFirst().get().getSalary();

         */

        /**   ?
         return getAllEmployees().stream().map(Employee::getSalary)
         .collect(Collectors.maxBy(Comparator.comparing(Long::longValue))).get();

         return getAllEmployees().stream().map(Employee::getSalary).max(Comparator.comparing(Long::longValue)).get();
         */
        /*
        return getAllEmployees().stream()
                .max(Comparator.comparing(Employee::getSalary))
                .get().getSalary();


        */

        /**  return getAllEmployees().stream()
         .map(Employee::getSalary)
         .mapToLong(i -> i)
         .max().getAsLong();
         */

    }

    // 21. Display the employee(s) who gets the maximum salary
    public static List<Employee> getMaxSalaryEmployee() {

        return getAllEmployees().stream()
                .filter(employee -> employee.getSalary().equals(getMaxSalary()))
                .collect(Collectors.toList());

      /*  if there were multiple people with the same max salary, this solution could cause some issues
       return employeeService.readAll()
                .stream()
                .max(Comparator.comparing(Employee::getSalary))
                .stream()
                .collect(Collectors.toList());

       */
    }

    // 22. Display the max salary employee's job
    public static Job getMaxSalaryEmployeeJob() throws Exception {

        return getMaxSalaryEmployee().get(0).getJob();

 /*       return jobService.readAll()
                .stream()
                .max(Comparator.comparing(Job::getMaxSalary))
                .stream().findAny().get();

  */
    }

    // 23. Display the max salary in Americas Region
    public static Long getMaxSalaryInAmericasRegion() throws Exception {

        return getAllEmployees()
                .stream()
                .filter(i -> i.getDepartment().getLocation().getCountry().getRegion().getRegionName().equalsIgnoreCase("Americas"))
                // .map(Employee::getSalary)
                //.reduce(Long::max)
                //.get();
                .max(Comparator.comparing(Employee::getSalary)).get().getSalary();


    }

    // 24. Display the second maximum salary an employee gets
    public static Long getSecondMaxSalary() throws Exception {

        return getAllEmployees()
                .stream()
                .filter(i -> i.getSalary().compareTo(getMaxSalary()) < 0)
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .findFirst().orElseThrow(() -> new RuntimeException("no employee found")).getSalary();



      /*  return employeeService.readAll()
                .stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .map(Employee::getSalary)
                .distinct()
                .skip(1)
                .findFirst().get();

       */
    }

    // 25. Display the employee(s) who gets the second maximum salary
    public static List<Employee> getSecondMaxSalaryEmployee() throws Exception {

        return getAllEmployees().stream()
                .filter(employee -> {
                    try {
                        return employee.getSalary().equals(getSecondMaxSalary());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());


    }

    // 26. Display the minimum salary an employee gets
    public static Long getMinSalary() {
        return getAllEmployees()
                .stream()
                .sorted(Comparator.comparing(Employee::getSalary))
                .findFirst().get().getSalary();
    /*    return employeeService.readAll()
                .stream()
                .min(Comparator.comparing(Employee::getSalary))
                .map(Employee::getSalary)
                .get();

     */

    }

    // 27. Display the employee(s) who gets the minimum salary
    public static List<Employee> getMinSalaryEmployee() {

        return getAllEmployees()
                .stream()
                .filter(employee -> employee.getSalary().equals(getMinSalary()))
                .collect(Collectors.toList());

      /*  return employeeService.readAll()
                .stream()
                .min(Comparator.comparing(Employee::getSalary))
                .stream().collect(Collectors.toList());

       */

    }

    // 28. Display the second minimum salary an employee gets
    public static Long getSecondMinSalary() throws Exception {

    /*    return getAllEmployees()
                .stream()
                .filter(employee -> employee.getSalary().compareTo(getMinSalary()) > 0)
                .sorted(Comparator.comparing(Employee::getSalary))
                .findFirst().orElseThrow(() -> new Exception("no employee found")).getSalary();

     */

        return employeeService.readAll()
                .stream()
                .map(Employee::getSalary)
                .distinct()
                .sorted()
                .skip(1)
                .findFirst().get();

    }

    // 29. Display the employee(s) who gets the second minimum salary
    public static List<Employee> getSecondMinSalaryEmployee() {
        //TODO Implement the method
        /**redo*/
     /*   long min = employeeService.readAll()
                .stream()
                .map(Employee::getSalary)
                .distinct()
                .sorted()
                .findFirst().get();

        return employeeService.readAll()
                .stream()
                .filter(i -> i.getSalary() != min)
                .min(Comparator.comparing(Employee::getSalary))
                .stream().collect(Collectors.toList());

      */

        return getAllEmployees().stream().filter(employee -> {
            try {
                return employee.getSalary().equals(getSecondMinSalary());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    // 30. Display the average salary of the employees
    public static Double getAverageSalary() {

 /*       long sum = employeeService.readAll()
                .stream()
                .map(Employee::getSalary)
                .reduce(Long::sum).get();

        return (double) sum / employeeService.readAll().size();


  */

        return getAllEmployees()
                .stream()
                .collect(Collectors.averagingDouble(Employee::getSalary));

     /*   return getAllEmployees().stream()
                .map(Employee::getSalary)
                .mapToDouble(salary->salary)
                .average().orElse(Double.NaN);


      */

    }

    // 31. Display all the employees who are making more than average salary
    public static List<Employee> getAllEmployeesAboveAverage() {

        return employeeService.readAll()
                .stream()
                .filter(i -> i.getSalary() > getAverageSalary())
                .collect(Collectors.toList());

    }

    // 32. Display all the employees who are making less than average salary
    public static List<Employee> getAllEmployeesBelowAverage() {

        return employeeService.readAll()
                .stream()
                .filter(i -> i.getSalary() < getAverageSalary())
                .collect(Collectors.toList());
    }

    // 33. Display all the employees separated based on their department id number
    public static Map<Long, List<Employee>> getAllEmployeesForEachDepartment() {

        return getAllEmployees()
                .stream()
                .collect(Collectors.groupingBy(employee -> employee.getDepartment().getId()));

    }

    // 34. Display the total number of the departments
    public static Long getTotalDepartmentsNumber() {

    /*    return departmentService.readAll()
                .stream()
                .map(Department::getId)
                .map(i -> i = 1L)
                .reduce(Long::sum).get();

     */
     /*   return getAllDepartments()
                .stream()
                .count();

      */

        return (long) getAllDepartments().size();
    }

    // 35. Display the employee whose first name is 'Alyssa' and manager's first name is 'Eleni' and department name is 'Sales'
    public static Employee getEmployeeWhoseFirstNameIsAlyssaAndManagersFirstNameIsEleniAndDepartmentNameIsSales() throws Exception {

        return employeeService.readAll()
                .stream()
                .filter(i -> i.getFirstName().equalsIgnoreCase("Alyssa") && i.getManager().getFirstName().equalsIgnoreCase("Eleni") &&
                        i.getDepartment().getDepartmentName().equalsIgnoreCase("Sales"))
                .findAny().get();

    }

    // 36. Display all the job histories in ascending order by start date
    public static List<JobHistory> getAllJobHistoriesInAscendingOrder() {

        return jobHistoryService.readAll()
                .stream()
                .sorted(Comparator.comparing(JobHistory::getStartDate))
                .collect(Collectors.toList());
    }

    // 37. Display all the job histories in descending order by start date
    public static List<JobHistory> getAllJobHistoriesInDescendingOrder() {

        return jobHistoryService.readAll()
                .stream()
                .sorted(Comparator.comparing(JobHistory::getStartDate).reversed())
                .collect(Collectors.toList());
    }

    // 38. Display all the job histories where the start date is after 01.01.2005
    public static List<JobHistory> getAllJobHistoriesStartDateAfterFirstDayOfJanuary2005() {

        return jobHistoryService.readAll()
                .stream()
                .filter(i -> i.getStartDate().isAfter(LocalDate.of(2005, 1, 1)))
                .collect(Collectors.toList());
    }

    // 39. Display all the job histories where the end date is 31.12.2007 and the job title of job is 'Programmer'
    public static List<JobHistory> getAllJobHistoriesEndDateIsLastDayOfDecember2007AndJobTitleIsProgrammer() {

        return jobHistoryService.readAll()
                .stream()
                .filter(i -> i.getEndDate().isEqual(LocalDate.of(2007, 12, 31)))
                .filter(i -> i.getJob().getJobTitle().equalsIgnoreCase("Programmer"))
                .collect(Collectors.toList());
    }

    // 40. Display the employee whose job history start date is 01.01.2007 and job history end date is 31.12.2007 and department's name is 'Shipping'
    public static Employee getEmployeeOfJobHistoryWhoseStartDateIsFirstDayOfJanuary2007AndEndDateIsLastDayOfDecember2007AndDepartmentNameIsShipping() throws Exception {

        return jobHistoryService.readAll()
                .stream()
                .filter(i -> i.getStartDate().isEqual(LocalDate.of(2007, 1, 1)))
                .filter(i -> i.getEndDate().isEqual(LocalDate.of(2007, 12, 31)))
                .filter(i -> i.getDepartment().getDepartmentName().equalsIgnoreCase("Shipping"))
                // .map(JobHistory::getEmployee)
                //.findAny().get();
                .findFirst().get().getEmployee();
    }

    // 41. Display all the employees whose first name starts with 'A'
    public static List<Employee> getAllEmployeesFirstNameStartsWithA() {

        return employeeService.readAll()
                .stream()
                .filter(i -> i.getFirstName().startsWith("A"))
                .collect(Collectors.toList());
    }

    // 42. Display all the employees whose job id contains 'IT'
    public static List<Employee> getAllEmployeesJobIdContainsIT() {

        return employeeService.readAll()
                .stream()
                .filter(i -> i.getJob().getId().contains("IT"))
                .collect(Collectors.toList());
    }

    // 43. Display the number of employees whose job title is programmer and department name is 'IT'
    public static Long getNumberOfEmployeesWhoseJobTitleIsProgrammerAndDepartmentNameIsIT() {
       /*
        return employeeService.readAll()
                .stream()
                .filter(i -> i.getJob().getJobTitle().equalsIgnoreCase("Programmer") && i.getDepartment().getDepartmentName().equalsIgnoreCase("IT"))
                .map(Employee::getSalary)
                .map(i -> i = 1L)
                .reduce(Long::sum)
                .get();

        */

        return getAllEmployees().stream()
                .filter(employee -> employee.getJob().getJobTitle().equals("Programmer"))
                .filter(employee -> employee.getDepartment().getDepartmentName().equals("IT"))
                .count();
    }

    // 44. Display all the employees whose department id is 50, 80, or 100
    public static List<Employee> getAllEmployeesDepartmentIdIs50or80or100() {

        return employeeService.readAll()
                .stream()
                .filter(i -> i.getDepartment().getId() == 50 || i.getDepartment().getId() == 80 || i.getDepartment().getId() == 100)
                .collect(Collectors.toList());
    }

    // 45. Display the initials of all the employees
    // Note: You can assume that there is no middle name
    public static List<String> getAllEmployeesInitials() {

        return employeeService.readAll()
                .stream()
                .map(i -> i.getFirstName().charAt(0) + "" + i.getLastName().charAt(0))
                .collect(Collectors.toList());
    }

    // 46. Display the full names of all the employees
    public static List<String> getAllEmployeesFullNames() {

        return employeeService.readAll()
                .stream()
                .map(employee -> employee.getFirstName() + " " + employee.getLastName())
                .collect(Collectors.toList());
    }

    // 47. Display the length of the longest full name(s)
    public static Integer getLongestNameLength() {

        return getAllEmployeesFullNames().stream()
                .max(Comparator.comparing(String::length))
                .get().length();

      /*  return getAllEmployeesFullNames()
                .stream()
                .map(String::length)
                .reduce(Integer::max)
                .stream().findFirst().get();

       */
    }

    // 48. Display the employee(s) with the longest full name(s)
    public static List<Employee> getLongestNamedEmployee() {

        return getAllEmployees()
                .stream()
                .filter(i -> i.getFirstName().length() + 1 + i.getLastName().length() == getLongestNameLength())
                .collect(Collectors.toList());


    }

    // 49. Display all the employees whose department id is 90, 60, 100, 120, or 130
    public static List<Employee> getAllEmployeesDepartmentIdIs90or60or100or120or130() {

        return employeeService.readAll()
                .stream()
                .filter(i -> i.getDepartment().getId() == 90 || i.getDepartment().getId() == 60 || i.getDepartment().getId() == 100 || i.getDepartment().getId() == 120 || i.getDepartment().getId() == 130)
                .collect(Collectors.toList());
    }

    // 50. Display all the employees whose department id is NOT 90, 60, 100, 120, or 130
    public static List<Employee> getAllEmployeesDepartmentIdIsNot90or60or100or120or130() {

        return employeeService.readAll()
                .stream()
                .filter(employee -> !getAllEmployeesDepartmentIdIs90or60or100or120or130().contains(employee))
                .collect(Collectors.toList());
    }
}
