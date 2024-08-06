package com.monetize360.employeedetails.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monetize360.employeedetails.domain.Currency;
import com.monetize360.employeedetails.domain.Employee;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class SalaryFormatter {

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Employee> employees = readJsonFromFile(objectMapper, "emp.json", new TypeReference<List<Employee>>() {});
        List<Currency> currencies = readJsonFromFile(objectMapper, "currency.json", new TypeReference<List<Currency>>() {});
        employees.forEach(employee -> {
            Optional<Currency> currencyOpt = currencies.stream()
                    .filter(c -> c.getCurrency().equals(employee.getCurrency()))
                    .findFirst();

            currencyOpt.ifPresent(currency -> {
                String formattedSalary = formatSalary(employee.getSalary(), currency);
                employee.setSalaryString(formattedSalary);
            });
        });
        writeJsonToFile(objectMapper, employees, "emp-details.json");
    }

    private static <T> T readJsonFromFile(ObjectMapper objectMapper, String fileName, TypeReference<T> typeReference) throws IOException {
        try (InputStream inputStream = SalaryFormatter.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + fileName);
            }
            return objectMapper.readValue(inputStream, typeReference);
        }
    }

    private static String formatSalary(double salary, Currency currency) {
        return String.format("%." + currency.getDecimals() + "f", salary) + " " + currency.getSymbol();
    }

    private static void writeJsonToFile(ObjectMapper objectMapper, List<Employee> employees, String fileName) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), employees);
    }
}
