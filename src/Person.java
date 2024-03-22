public class Person {
    private String name;
    private String sure_name;
    private String email;

    public Person(String name, String sure_name, String email) {

        this.name = name;
        this.sure_name = sure_name;
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setSureName(String sure_name) {
        this.sure_name = sure_name;

    }

    public String getSureName() {
        return sure_name;
    }

    public void setEmail(String email) {
        this.email = email;

    }

    public String getEmail() {
        return email;
    }

    public void displayPersonInfo() {

        System.out.println("\nName: " + name);
        System.out.println("Surname: " + sure_name);
        System.out.println("Email: " + email + "\n");
    }

}
