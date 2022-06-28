
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;


class Parser {
    String commandName;
    String[] args;

    //This method will divide the input into commandName and args
    //where "input" is the string command entered by the user
    public boolean parse(String input){
        String temp;
        int count=0;
        int index=0;
        int ind=0;
        for (int i=0;i<input.length();i++){
            if(input.charAt(i)==' ' && count==0){
                commandName=input.substring(0,i);
                temp=input.substring(i+1);
                count++;

                args = temp.split("\s+");
                break;
            }
            if(i==input.length()-1){
                args=null;
                commandName=input;
                break;
            }
        }
        if(input==" "){
            System.out.println("please enter the command name and arguments");
            return false;
        }
        else{
            return true;
        }
    }
    public String getCommandName(){
        return commandName;
    }
    public String[] getArgs() {

        return args;
    }
}
class Terminal {

    Parser parser=new Parser();
    static String currPath = System.getProperty("user.dir");

    public Terminal( Parser p) throws IOException {
        parser.commandName=p.getCommandName();
        parser.args=p.getArgs();
    }

    public static void echo(String  [] s){
        for (String value : s) {
            System.out.println(value+" ");
        }
    }

    public static String pwd() throws IOException {

        return currPath;
    }

    public static void ls() throws IOException {
        ArrayList<Path> array = new ArrayList<>();
        Files.list(new File(currPath).toPath()).forEach(path -> {array.add(path.getFileName());});

        for (Path path : array) {
            System.out.println(path);
        }

    }

    public static void ls_r() throws IOException {
        ArrayList<Path> array = new ArrayList<>();
        Files.list(new File(currPath).toPath()).forEach(path -> {array.add(path.getFileName());});

        for(int i= array.size()-1;i>=0;i--)
        {
            System.out.println(array.get(i));
        }

    }

    public static void cd( ) throws IOException {
        currPath = System.getenv("USERPROFILE");
    }

    public static String cd(String s ) throws IOException {

        if(s!=null){
            char chr = currPath.charAt(currPath.length()-1);
            char chr2 = s.charAt(0);
            if (s.equals("..")& chr=='\\'){
                return currPath;
            }
            else if(s.equals("..")){
                File file = new File(currPath);
                currPath=file.getParent();
                return currPath;
            }
            else if (!s.equals("..")&chr2=='.'){
                currPath= makeShortPath(s);
                return currPath;
            }
            else{
                currPath=s;
                return currPath;
            }
        }
        else
            return "Error,Please Enter one Argument ";
    }

    public static void cp(File f1, File f2) throws IOException {

        try {
            if (!f1.getAbsoluteFile().exists()) {
                System.out.println("Please Enter a Correct path for the First File");
            }
            if (!f2.getAbsoluteFile().exists()) {
                System.out.println("Please Enter a Correct path for the Second File");
            } else {
                FileReader ins = null;
                FileWriter outs = null;
                try {
                    ins = new FileReader(f1);
                    outs = new FileWriter(f2);
                    int ch;
                    while ((ch = ins.read()) != -1) {
                        outs.write(ch);
                    }
                } catch (IOException e) {
                    System.out.println("Error: can't Copy the files. ");
                } finally {
                    try {
                        ins.close();
                        outs.close();
                    } catch (IOException e) {
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void cp_r(File f1,File f2) throws IOException {
        try {
            f2.mkdir();
            if (f1.exists()&f2.exists()) {
                for (File file : f1.listFiles()) {
                    // hnshof lw howa file 3ady wla directory
                    if (file.isFile()){
                        FileReader ins = null;
                        FileWriter outs = null;
                        try {
                            ins = new FileReader(file);
                            if (f2.getAbsolutePath().contains(":")){
                                outs = new FileWriter(f2+"\\"+file.getName());
                            }
                            else
                                outs = new FileWriter(pwd()+"\\"+f2+"\\"+file.getName());
                            int ch;
                            while ((ch = ins.read()) != -1) {
                                outs.write(ch);
                            }
                        } catch (IOException e) {
                            System.out.println("Error: can't Copy the files. ");
                        } finally {
                            try {
                                ins.close();
                                outs.close();
                            } catch (IOException e) {
                            }
                        }
                    }
                    else if (file.isDirectory()){
                        File destDir=new File(f2+"\\"+file.getName());
                        cp_r(file, destDir);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error: can't Copy the files. ");
        }
    }

    public void rm(String f1) throws IOException {
        Path P1= Path.of(f1);
        if (!Files.isDirectory(P1)&Files.exists(P1)) {
            Files.delete(P1);
            System.out.printf("Delete : %s%n", f1);
        }
        else
            System.out.println("No Such Files!");

    }

    public static String makePath(String s ) throws IOException {
        String path=pwd()+"\\"+s;
        return path;
    }

    public void cat(File f1) throws IOException {
        if(f1.exists()) {
            BufferedReader in = new BufferedReader(new FileReader(f1));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        }
        else System.out.println("Error, File Not Exists");
    }

    public void cat(File f1,File f2) throws IOException {
        if(f1.exists()&f2.exists()) {
            BufferedReader in1 = new BufferedReader(new FileReader(f1));
            String line;
            while ((line = in1.readLine()) != null) {
                System.out.println(line);
            }
            BufferedReader in2 = new BufferedReader(new FileReader(f2));
            while ((line = in2.readLine()) != null) {
                System.out.println(line);
            }
        }
        else System.out.println("Error, Files Not Exists");
    }

    public void mkdir(String[] s) throws IOException {
        String path="";
        for(int i=0;i<s.length;i++)
        {
            if(s[i].contains("\\"))
            {

                File f1 = new File(makeShortPath(s[i]));
                boolean bool = f1.mkdir();
                if(bool){
                    System.out.println("Folder is created successfully");
                }else{
                    System.out.println("Error Found!");
                }

            }
            else if(s[i]!="")
            {
                path=currPath+"\\"+s[i];
                File f1 = new File(path);
                boolean bool = f1.mkdir();
                if(bool){
                    System.out.println("Folder is created successfully");
                }else{
                    System.out.println("Error Found!");
                }
            }
        }
    }

    public void rmdir(String s[]) throws IOException {
        if(s[0].contains("*"))
        {
            try {
                Files.list(new File(currPath).toPath()).forEach(path ->
                {
                    if(path.toFile().isDirectory() && path.toFile().list().length == 0) {
                        path.toFile().delete();
                        System.out.println("Directory is Deleted");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(s[0].contains(":"))
        {
            String pathD=s[0];
            File file=new File(pathD);
            if (file.list().length==0){
                file.delete();
            }
        }
        else if(s[0].contains("\\"))
        {
            File file=new File(makeShortPath(s[0]));
            if (file.list().length==0){
                file.delete();
            }
        }
    }

    public void touch (String s[]) throws IOException {
        File file;
        if (s[0].contains(":")){
            file = new File(s[0]);

        }
        else {
            file = new File(makeShortPath(s[0]));
        }
        if (!file.exists()){
            System.out.println("Error: Command not found or invalid parameters are entered");

        }
        else {
            boolean created;
            try
            {
                created = file.createNewFile();
                if(!created)
                {
                    System.out.println("File already exists at location");
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    public static String makeShortPath(String shortPath) throws IOException {
        File a = new File(currPath);
        File parentFolder = new File(a.getParent());
        File b = new File(parentFolder, shortPath);
        return b.getCanonicalPath();
    }

    public void chooseCommandAction() throws IOException {

        String []s1;
        s1=parser.getArgs();
        if (parser.getCommandName().equalsIgnoreCase("cp")){

            if(s1!=null) {
                if (s1.length == 3 & Objects.equals(s1[0], "-r")){
                    parser.commandName = "cp -r";
                }
                else if (s1.length==2){
                    parser.commandName="cp";
                }
            }
            else System.out.println("Error: Command not found or invalid parameters are entered");
        }

        if (parser.getCommandName().equalsIgnoreCase("exit")&s1==null){
            System.exit(0);
        }

        else if(parser.getCommandName().equalsIgnoreCase("echo")){
            echo(parser.getArgs());
        }

        else if(parser.getCommandName().equalsIgnoreCase("pwd")&s1==null){
            System.out.println(pwd());
        }

        else if(parser.getCommandName().equalsIgnoreCase("ls")&s1==null){
            ls();
        }

        else if(parser.getCommandName().equalsIgnoreCase("ls")&s1!=null){
            if (s1[0].equals("-r"))
                ls_r();
            else System.out.println("error");
        }

        else if(parser.getCommandName().equalsIgnoreCase("cd")&s1==null){
            cd();
        }

        else if(parser.getCommandName().equalsIgnoreCase("cd")&s1!=null){
            if (s1.length==1)
                cd(s1[0]);
            else System.out.println("Error,Please Enter one Argument ");
        }

        else if(parser.getCommandName().equalsIgnoreCase("cp")){
            if(s1.length==2){
                Path p = Paths.get(s1[0]);
                Path p2 = Paths.get(s1[1]);
                if (Files.exists(p) & Files.exists(p2)) {
                    File sourceDir = new File(s1[0]);
                    File destDir = new File(s1[1]);
                    cp(sourceDir, destDir);
                } else {
                    File sourceDir = new File(makePath(s1[0]));
                    File destDir = new File(makePath(s1[1]));
                    cp(sourceDir, destDir);
                }
            }
            else System.out.println("Error: Command not found or invalid parameters are entered");
        }

        else if(parser.getCommandName().equalsIgnoreCase("cp -r")){
            Path p=Paths.get(s1[1]) ;
            Path p1=Paths.get(s1[2]) ;
            if(Files.isDirectory(p.toAbsolutePath())&Files.isDirectory(p1.toAbsolutePath())){
                if (Files.exists(p)){
                    File sourceDir=new File(s1[1]);
                    File destDir=new File(s1[2]+"\\"+sourceDir.getName());
                    cp_r(sourceDir,destDir);
                }
                else{
                    File sourceDir=new File(makePath(s1[1]));
                    File destDir=new File(s1[2]+"\\"+sourceDir.getName());
                    cp_r(sourceDir,destDir);
                }
            }
            else System.out.println("Error: wrong Path or wrong Folder name");
        }

        else if(parser.getCommandName().equalsIgnoreCase("rm")) {
            if (s1.length != 1) {
                System.out.println("Wrong Arguments, Please Enter one Argument ");
            } else {
                rm(s1[0]);
            }
        }

        else if(parser.getCommandName().equalsIgnoreCase("cat")){
            if (s1.length!=0){
                File f1 =new File(makePath(s1[0]));
                if (s1.length==1){
                    cat(f1);
                }
                else{
                    File f2 =new File(makePath(s1[1]));
                    cat(f1,f2);
                }
            }
            else System.out.println("Error: Command not found or invalid parameters are entered");
        }

        else if(parser.getCommandName().equalsIgnoreCase("mkdir")){
            mkdir(s1);
        }

        else if(parser.getCommandName().equalsIgnoreCase("rmdir")){
            if(s1.length==1)
            {
                rmdir(s1);
            }
            else
            {
                System.out.println("Wrong Arguments, Please Enter one Argument ");
            }

        }

        else if(parser.getCommandName().equalsIgnoreCase("touch")){
            if(s1.length==1)
            {
                touch(s1);
            }
            else
            {
                System.out.println("Wrong Arguments, Please Enter one Argument ");
            }

        }

        else System.out.println("Error: Command not found or invalid parameters are entered");
    }

    public static void main(String[] args) throws IOException {

        Parser p = new Parser();
        Scanner sc = new Scanner(System.in);
        while (true){
            String a = sc.nextLine();
            p.parse(a);
            Terminal t=new Terminal(p);
            t.chooseCommandAction();
        }

    }
}
