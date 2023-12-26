package fr.istic.vv;

import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        File file;
        if(args.length == 0) {
            //file = new File("/home/issameddine/Documents/DD/VV-ISTIC-TP2/code/javaparser-starter/src/main/java/Test");
            file = new File("C:/Users/issam/git/VV-ISTIC-TP2/code/javaparser-starter/src/main/java/Test");
        } else {
            file = new File(args[0]);
        }

        if(!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Provide a path to an existing readable directory");
            System.exit(2);
        }

        SourceRoot root = new SourceRoot(file.toPath());
        PublicElementsPrinter printer = new PublicElementsPrinter();
        CyclomaticComplexityCalculator calculator = new CyclomaticComplexityCalculator();
        TCCCalculator tccCalculator = new TCCCalculator();
        NoGetterRule myRule = new NoGetterRule();
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(printer, null));
            result.ifSuccessful(unit -> unit.accept(calculator, null));
            result.ifSuccessful(unit -> unit.accept(myRule, null));
            result.ifSuccessful(unit -> unit.accept(tccCalculator, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });
    }


}
