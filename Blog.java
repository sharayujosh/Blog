/* Author: Lina Li and Sharayu Josh
    String methods used:
    equalsIgnoreCase
    concat
    substring
    indexOf
    length
/*
The purpose of this program is to write blogs and store them into a text
    file with the ability to look up for blogs written on a specific date.
*/

import java.util.Scanner;
import java.io.File;  // Import the File class
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors

public class Blog {
    // fields
    private String date = ""; // required
    private String name = "Anonymous";  // optional
    private String title = ""; //auto or given
    private String text = "";  // required
    private boolean auto_title = true;
    private boolean fileCreated = false;
    Scanner scan = new Scanner(System.in);

    // constructor
    public Blog(String name, String date, String title) {
        this.date = date;
        this.name = name;
        this.title = title;
        String input = "";

        do {
            System.out.println("Auto title: ON/OFF");
            input = scan.nextLine();
        }
        while (!(input.equalsIgnoreCase("OFF") || input.equalsIgnoreCase("ON")));

        if (input.equalsIgnoreCase("OFF"))
            auto_title = false;

        writeBlog();
    }

    //constructor with date only
    public Blog(String date) {
        this.date = date;
        String input = "";
        do {
            System.out.println("Auto title: ON/OFF");
            input = scan.nextLine();
        }
        while (!(input.equalsIgnoreCase("OFF") || input.equalsIgnoreCase("ON")));

        if (input.equalsIgnoreCase("OFF"))
            auto_title = false;

        writeBlog();
    }

    //writing the text (is called in constructors)
    private void writeBlog() {
        boolean writing = true;
        String entry = "";
        while(writing){
            System.out.println("Enter blog entry:");
            entry = entry.concat(scan.nextLine());
            System.out.println("Would you like to continue writing?");
            if("no".equalsIgnoreCase(scan.nextLine()))
                writing = false;
        }
        text = text.concat(entry);
        checkBlanks();
        createFile();
    }

    //checks that the title and name fields are properly set
    private void checkBlanks() {
        if(auto_title)
            createTitle();
        else if(title.equals("")){
            System.out.println("You turned off auto titling. What is the title of your blog?");
            title = scan.nextLine();
        }
        System.out.println("This blog will be logged under " + name + ". Would you like to change this? YES/NO");
        if(scan.nextLine().equalsIgnoreCase("YES"))
            setName();
    }

    //creates an auto title using the first three words,
    // or if the blog is shorter, the whole of the blog text
    private void createTitle() {
        int nextSpaceIndex = 0;
        int words = 0;
        if(auto_title && text.indexOf(" ") != -1){
            for(words = 0; words < 3; words++){
                if(text.indexOf(" ", nextSpaceIndex) != -1)
                    nextSpaceIndex = text.indexOf(" ", nextSpaceIndex) + 1;
                else
                    break;
            }
        }
        System.out.println(nextSpaceIndex);
        if(words == 3)
            title = text.substring(0, nextSpaceIndex);
        else
            title = text;
    }

    //gives the user a chance to set a new author's name
    private void setName(){
        System.out.println("Enter new author name:");
        name = scan.nextLine();
    }

    // creates or pulls up the file to write the blog
    private void createFile() {
        try {
            File myObj = new File("Blogs.txt");
            // if file not created, write to file
            if (myObj.createNewFile()) {
                writeFile();
                fileCreated = true;
            } else {
                // if file created, add to file
                addToFile();
                fileCreated = true;
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //makes a new txt file if it doesn't exist
    private void writeFile() {
        try {
            FileWriter myWriter = new FileWriter("Blogs.txt");
            myWriter.write(date + "\n" + title + "\n" + name + "\n" + text + "\n\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //adds to txt file if it already exists
    private void addToFile() {
        try {
            FileWriter myWriter = new FileWriter("Blogs.txt", true);
            myWriter.write(date + "\n" + title + "\n" + name + "\n" + text + "\n\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //find all blogs based on date
    public void lookUpBlog() {
        if (fileCreated) {
            System.out.println("Enter the date of the blog you want to look up for in the format mm/dd/yy:");
            String temp = scan.nextLine();
            Scanner reader = new Scanner(System.in);
            try {
                reader = new Scanner(new File("Blogs.txt"));
            }
            catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            String sen = "";
            String blog = "";
            while (reader.hasNextLine()) {
                sen = reader.nextLine();
                if (sen.indexOf(temp) != -1) {
                    reader.nextLine();
                    reader.nextLine();
                    blog = reader.nextLine();
                    if (blog.equals(""))
                        System.out.println("No blog is found with the date");
                    else
                        System.out.println(formatBlog(blog));
                }
            }
        }
        else {
            System.out.println("You haven't written any blogs");
        }

    }

    //formats text for lookUpBlog
    private String formatBlog(String blogText){
        String formatted = "";
        int i = 0;
        String textLeft = blogText;

        if (blogText.length() / 50 != 0) {
            while( i < blogText.length() / 50 && textLeft.length() >= 50){
                formatted += blogText.substring(i * 50, (i+1) * 50) + "\n";
                textLeft = blogText.substring((i+1)* 50);
                i++;
            }
            formatted +=textLeft;
        }
        else
            formatted = blogText;

        return formatted;
    }

    // toString method
    public String toString() {
        return "This class has the following constructors and public methods: \n" +
                "Blog(String Date)\n" +
                "Blog(String author, String date, String title)\n" +
                "lookUpBlog() //This will return the blog of the specified date";
    }

}
