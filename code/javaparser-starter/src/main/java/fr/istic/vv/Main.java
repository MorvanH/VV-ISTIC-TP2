package fr.istic.vv;

import com.github.javaparser.Problem;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        File file;
        if(args.length == 0) {
            file = new File("/home/issameddine/Documents/DD/VV-ISTIC-TP2/code/javaparser-starter/src/main/java/Test");
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
