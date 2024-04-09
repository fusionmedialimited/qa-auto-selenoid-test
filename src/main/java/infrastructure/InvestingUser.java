package infrastructure;

import infrastructure.utilities.Utilities;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class InvestingUser {

    protected final String firstName;
    protected final String lastName;
    protected final String email;
    protected final String password;

    /**
     * Init {@link InvestingUser} instance with provided email and default name and password
     *
     * @param email test user's email
     */
    public InvestingUser(String email) {
        this.firstName = "name";
        this.lastName = "surname";
        this.email = email;
        this.password = "password";
    }

    /**
     *Init {@link InvestingUser} instance with random email and default name and password
     */
    public InvestingUser() {
        this(Utilities.generateEmail(4, "signup"));
    }
}
