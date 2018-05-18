处理表单提交
==================

POM
------

.. code:: java

        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>

            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <scope>test</scope>
            </dependency>


                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-thymeleaf</artifactId>
                </dependency>

        </dependencies>


Create a web controller
------------------------------

src/main/java/hello/GreetingController.java

.. code:: java

    package hello;

    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.ModelAttribute;
    import org.springframework.web.bind.annotation.PostMapping;

    @Controller
    public class GreetingController {

        @GetMapping("/greeting")
        public String greetingForm(Model model) {
            model.addAttribute("greeting", new Greeting());
            return "greeting";
        }

        @PostMapping("/greeting")
        public String greetingSubmit(@ModelAttribute Greeting greeting) {
            return "result";
        }

    }

The greetingForm() method uses a Model object to expose a new Greeting to the view template. The Greeting object in the following code contains fields such as id and content that correspond to the form fields in the greeting view, and will be used to capture the information from the form.

Model
-----------

.. code:: java

    package hello;

    public class Greeting {

        private long id;
        private String content;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

    }

The implementation of the method body relies on a view technology to perform server-side rendering of the HTML by converting the view name "greeting" into a template to render. In this case we are using Thymeleaf, which parses the greeting.html template below and evaluates the various template expressions to render the form.


view
------------

src/main/resources/templates/greeting.html

.. code:: html

    <!DOCTYPE HTML>
    <html xmlns:th="http://www.thymeleaf.org">
    <head>
        <title>Getting Started: Handling Form Submission</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    </head>
    <body>
        <h1>Form</h1>
        <form action="#" th:action="@{/greeting}" th:object="${greeting}" method="post">
            <p>Id: <input type="text" th:field="*{id}" /></p>
            <p>Message: <input type="text" th:field="*{content}" /></p>
            <p><input type="submit" value="Submit" /> <input type="reset" value="Reset" /></p>
        </form>
    </body>
    </html>

The **th:action="@{/greeting}"** expression directs the form to POST to the **/greeting** endpoint, while the **th:object="${greeting}"** expression declares the model object to use for collecting the form data. The two form fields, expressed with **th:field="{id}**" and **th:field="{content}"**, correspond to the fields in the **Greeting** object above.

That covers the controller, model, and view for presenting the form. Now let’s review the process of submitting the form. As noted above, the form submits to the /greeting endpoint using a POST. The greetingSubmit() method receives the Greeting object that was populated by the form. The Greeting is a @ModelAttribute so it is bound to the incoming form content, and also the submitted data can be rendered in the result view by referring to it by name (the name of the method parameter by default, so "greeting" in this case). The id is rendered in the <p th:text="'id: ' + ${greeting.id}" /> expression. Likewise the content is rendered in the <p th:text="'content: ' + ${greeting.content}" /> expression.


src/main/resources/templates/result.html

.. code:: html

    <!DOCTYPE HTML>
    <html xmlns:th="http://www.thymeleaf.org">
    <head>
        <title>Getting Started: Handling Form Submission</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    </head>
    <body>
        <h1>Result</h1>
        <p th:text="'id: ' + ${greeting.id}" />
        <p th:text="'content: ' + ${greeting.content}" />
        <a href="/greeting">Submit another message</a>
    </body>
    </html>

For clarity, this example uses two separate view templates for rendering the form and displaying the submitted data; however, you can also use a single view for both purposes.










-------

参考

https://spring.io/guides/gs/validating-form-input/