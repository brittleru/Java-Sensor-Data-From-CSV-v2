import org.w3c.dom.ls.LSOutput;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

public class Main {

    public static void main(String[] args) {

        String projectPath = System.getProperty("user.dir");                 // Ca sa nu hardcodez path-ul
        String inputFilePath = projectPath + "/Date _orig.csv";              // Fisierul csv de prelucrat
        String dateTemperaturaPath = projectPath + "/date_temperatura.csv";  // Fisierul csv generat pentru temperatura
        String dateUmiditatePath = projectPath + "/date_umiditate.csv";      // Fisierul csv generat pentru umiditate
        String vitezaPath = projectPath + "/viteza.csv";                     // Fisierul csv generat pentru viteze
        String prezentaPath = projectPath + "/prezenta.csv";                 // Fisierul csv generat pentru prezenta

        Solver solver = new Solver();                                        // Crearea obiectului solver
        solver.GenerateCSV(inputFilePath, dateTemperaturaPath, 0, 1, 2, 9); // Generarea csv-ului temperatura
        solver.GenerateCSV(inputFilePath, dateUmiditatePath, 3, 4, 5, 9);   // Generarea csv-ului umiditate
        solver.GenerateCSV(inputFilePath, vitezaPath, 6, 9);                // Generarea csv-ului viteza
        solver.GenerateCSV(inputFilePath, prezentaPath, 7, 8, 9);           // Generarea csv-ului prezenta

    }
}

class Solver {

    private String enrange(Double value, Double min, Double max) { // Metoda pentru verificarea datelor

        if (value < min) {

            value = min; // seteaza valoarea lui value la min

        } else if (value > max) {

            value = max; // seteaza valoarea lui value la max

        }

        return value.toString(); // returneaza reprezentarea string a lui value

    }

    private String getValue(String[] row, int index) {  // Metoda pentru modificarea datelor

        if (index >= 0 && index < 3) { // verificarea datelor pentru temperatura

            Double value = Double.parseDouble(row[index]);  // seteaza value cu transformarea reprezentarei string in
                                                            // double a unui element vectorului din csv temperatura
            return enrange(value, -5.0, 5.0);    // returneaza apelarea medodei enrange cu parametrii doriti

        }

        if (index >= 3 && index < 6) { // verificarea datelor pentru umiditate

            Double value = Double.parseDouble(row[index]); // seteaza value cu un element din vectorul din csv umiditate
            return enrange(value, 40.0, value);      //  returneaza apelarea metodei enrange cu parametrul dorit

        }

        if ( index == 6) { // modificarea datelor pentru viteza

            Double value = Double.parseDouble(row[index]); // seteaza value cu un element din vectorul din csv viteza
            value += 1;                                    // mareste valoarea lui value cu 1
            return value.toString();                       // returneaza reprezentarea string a lui value
        }

        return row[index]; // returneaza elementul din vector

    }

    public void GenerateCSV(String in, String out, int... columns) { // metoda pentru generarea csv-urilor
        BufferedReader reader = null; // seteaza bufferedreader null
        FileWriter writer = null;     // seteaza filewriter null

        try {

            reader = new BufferedReader(new FileReader(in)); // citeste din csv-ul original
            writer = new FileWriter(out);                    // scrie date in csv-ul generat

        } catch (Exception exception) { // exceptie in cazul in care nu se pot deschide fisierele

            System.out.println("Nu se poate deschide fisierul, eroarea: " + exception);

        }

        try {

            String line; //

            for (Integer i = 0; (line = reader.readLine()) != null; ++i) { // am folosit super clasa Integer pentru ca
                                                                           // altfel nu ar fi mers concatenarea
                String[] row = line.split(",");         // despare elementele din vector cu delimitatorul ","
                String insert = "";                           // un string gol

                if (i == 0) { //Header stuff

                    for (int column: columns) { // pentru coloana in coloane

                        insert += row[column] + ","; // stringul gol devine elementul vectorului de pe pozitia coloana

                    }
                }

                else {

                    for (int column: columns) {

                        insert += getValue(row, column) + ","; // altfel devine apelarea medodei getValue de rand si coloana

                    }
                }

                insert += i.toString();  // stringul se concateneaza cu elementul de pe linie
                writer.append(insert + "\n"); // pentru a trece la randul urmator

            }

        } catch (Exception exception) { // exceptie in cazul parsarii pentru input

            System.out.println("Eroare in timpul parsarii pentru input: " + exception);

        }

        try {

            reader.close(); // incearca sa inchida BufferedReader
            writer.close(); // incearca sa inchida FileWriter

        } catch (Exception exception) { // exceptie daca nu merge sa le inchida

            System.out.println("Eroare, incerc sa inchid filele: " + exception);

        }
    }
}
