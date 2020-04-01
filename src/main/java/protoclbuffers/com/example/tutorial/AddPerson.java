package protoclbuffers.com.example.tutorial;

import com.example.tutorial.AddressBookProtos.Person;
import com.example.tutorial.AddressBookProtos.AddressBook;

import java.io.*;

public class AddPerson {

    static Person PromptForAddress(BufferedReader stdin,PrintStream stdout) throws IOException {
        Person.Builder person = Person.newBuilder();
        stdout.println("Enter person ID : ");
        person.setId(Integer.valueOf(stdin.readLine()));
        stdout.print("Enter name: ");
        person.setName(stdin.readLine());
        stdout.print("Enter email address (blank for none): ");
        String email = stdin.readLine();
        if (email.length() > 0) {
            person.setEmail(email);
        }
        while (true) {
            stdout.print("Enter a phone number (or leave blank to finish): ");
            String number = stdin.readLine();
            if (number.length() == 0) {
                break;
            }
            Person.PhoneNumber.Builder phoneNumber =
                    Person.PhoneNumber.newBuilder().setNumber(number);
            stdout.print("Is this a mobile, home, or work phone? ");
            String type = stdin.readLine();
            if (type.equals("mobile")) {
                phoneNumber.setType(Person.PhoneType.MOBILE);
            } else if (type.equals("home")) {
                phoneNumber.setType(Person.PhoneType.HOME);
            } else if (type.equals("work")) {
                phoneNumber.setType(Person.PhoneType.WORK);
            } else {
                stdout.println("Unknown phone type.  Using default.");
            }
            person.addPhone(phoneNumber);
        }
        return person.build();
     }

    public static void main(String[] args) throws Exception {
//        if (args.length != 1) {
//            System.err.println("Usage:  AddPerson ADDRESS_BOOK_FILE");
//            System.exit(-1);
//        }
        AddressBook.Builder addressBook = AddressBook.newBuilder();
        // Read the existing address book.
        try {
            addressBook.mergeFrom(new FileInputStream("/Users/guixiaowen/IdeaProjects/learn_hadoop_coding/src/main/output/output.txt"));
        } catch (FileNotFoundException e) {
            System.out.println(args[0] + ": File not found.  Creating a new file.");
        }
        // Add an address.
        addressBook.addPerson(
                PromptForAddress(new BufferedReader(new InputStreamReader(System.in)),
                        System.out));
        // Write the new address book back to disk.
        FileOutputStream output = new FileOutputStream("/Users/guixiaowen/IdeaProjects/learn_hadoop_coding/src/main/output/output.txt");
        addressBook.build().writeTo(output);
        output.close();
    }


}
