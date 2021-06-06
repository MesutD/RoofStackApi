package taskPack;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojos.Book;
import utility.ConfigReader;
import utility.ObjectBox;
import utility.TestBase;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.baseURI;

public class BookTest extends TestBase {

    Book book = ObjectBox.bookCreator();
    Map<String,Object> bookChanged=new HashMap<>();


    /*
Verify that the API starts with an empty store
     */
    @Test(priority = 0)
    public void Task1() {
        given().accept(ContentType.JSON)
                .when()
                .get("/api/books")
                .then()
                .statusCode(404);
    }

    @Test(priority = 1)
    public void createBook() {

        Response response = (Response) given().accept(ContentType.JSON)
                .and().body(book)
                .when()
                .post("/api/books/")
                .then()
                .statusCode(201);
        int id =response.path("id");
        book.setId(id);
    }
    /*
Verify that title and author are required fields.
 */
    @Test(priority = 2)
    public void Task2(){

                given().accept(ContentType.JSON)
                        .and().pathParam("id",book.getId())
                        .and().body(bookChanged)
                        .when()
                        .put("/api/books/{id}")
                        .then().statusCode(410)//doesnt know the status code
                        .and()
                        .body("Error Message",is("Field is required"));
    }
    /*
Verify that title and author cannot be empty.
 */
    @Test(priority = 3)
    public void Task3(){

        bookChanged.put("author",null);
        bookChanged.put("title",null);
           given().accept(ContentType.JSON)
                .and().pathParam("id",book.getId())
                .and().body(bookChanged)
                .when()
                .put("/api/books/{id}")
                .then().statusCode(404)
                .and()
                .body("Error Message",is("Field can not be empty"));
    }
    /*
    Verify that the id field is read−only. • You shouldn't be able to send it in the PUT request to /api/books/.
     */
    @Test(priority = 4)
    public void Task4(){

        bookChanged.put("author","Murat");
        bookChanged.put("title","Yasam");
        bookChanged.put("id",7);
        given().accept(ContentType.JSON)
                .and().pathParam("id",book.getId())
                .and().body(bookChanged)
                .when()
                .put("/api/books/{id}")
                .then().statusCode(404)
                .and()
                .body("Error Message",is("Id field is read-only"));
    }
    /*
    Verify that you can create a new book via PUT
     */
    @Test(priority = 5)
    public void Task5(){

        bookChanged.remove("id",7);
        given().accept(ContentType.JSON)
                .and().pathParam("id",book.getId())
                .and().body(bookChanged)
                .when()
                .put("/api/books/{id}")
                .then().statusCode(201);

        given().accept(ContentType.JSON)
                .and().pathParam("id",book.getId())
                .and().body(bookChanged)
                .when()
                .get("/api/books/{id}")
                .then().statusCode(200)
                .and()
                .body("author",is(bookChanged.get("author")
                ), "title", is(bookChanged.get("title")));


    }
    /*
    Verify that you cannot create a duplicate book.
     */
    @Test(priority = 6)
    public void Task6(){

        given().accept(ContentType.JSON)
                .and().pathParam("id",book.getId())
                .and().body(bookChanged)
                .when()
                .put("/api/books/{id}")
                .then().statusCode(404)
                .and()
                .body("Error Message",is("you cannot create a duplicate book"));


    }
    @Test(priority = 7)
    public void Task7(){

        given().accept(ContentType.JSON)
                .and().pathParam("id",book.getId())
                .when()
                .delete("/api/books/{id}")
                .then().statusCode(201);

    }
}
