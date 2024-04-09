package infrastructure.utilities;

import infrastructure.threadlocals.ThreadLocalEmail;

public class InvestingUserUtilities {

    /**
     * Check if user is signed in
     */
    public static boolean isUserSignedIn() {
        return ThreadLocalEmail.getEmailForSignedInUser() != null;
    }

}
