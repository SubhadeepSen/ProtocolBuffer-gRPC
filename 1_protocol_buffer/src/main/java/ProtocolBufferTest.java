import com.employee.Address;
import com.employee.Employee;
import com.google.protobuf.util.JsonFormat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ProtocolBufferTest {
    public static void main(String[] args) throws IOException {
        Address address = Address.newBuilder()
                .setCity("Hyderabad")
                .setFirstLine("Cybercity")
                .setSecondLine("Madhapur")
                .setState("Telangana")
                .setPinCode("500056")
                .setCountry("India")
                .build();

        Employee.Date dateOfBirth = Employee.Date.newBuilder()
                .setDay("22")
                .setMonth("04")
                .setYear("1985")
                .build();

        Employee.Date dateOfJoining = Employee.Date.newBuilder()
                .setDay("01")
                .setMonth("10")
                .setYear("2020")
                .build();

        Employee employee = Employee.newBuilder()
                .setAddress(address)
                .setDateOfBirth(dateOfBirth)
                .setDateOfJoining(dateOfJoining)
                .setFirstName("Rahul")
                .setLastName("Kumar")
                .build();

        // serializing employee as JSON and saving in employee.json
        String employeeAsJson = JsonFormat.printer().print(employee);
        System.out.println("\n Serialized employee as JSON:\n" + employeeAsJson);
        FileOutputStream fos = new FileOutputStream("employee.json");
        fos.write(employeeAsJson.getBytes(StandardCharsets.UTF_8));
        fos.close();

        // serializing employee as binary (protocol buffer) and saving in employee.bin
        fos = new FileOutputStream("employee.bin");
        fos.write(employee.toByteArray());
        fos.close();

        // deserializing employee from binary (protocol buffer)
        Employee deserializedEmployee = Employee.parseFrom(new FileInputStream("employee.bin"));
        employeeAsJson = JsonFormat.printer().print(deserializedEmployee);
        System.out.println("\n Deserialized employee from employee.bin as JSON:\n" + employeeAsJson);

    }
}
