package utility;

import com.github.javafaker.Faker;
import pojos.Book;

public class ObjectBox {

    /*
    Bu methodla pojo classından obje oluşturup, constructor a Faker dan
    oluşturduğum random data yı parse ediyorum.
     */
    public static Book bookCreator(){
        Faker faker=new Faker();
        String author=faker.name().name();
        String title=faker.book().title();
        Book book1=new Book(author,title);

   return book1; }
}
