package com.summit.summitproject.prebuilt.login;

/**
 * Used with the {@link LoginManager} to receive the result of a network call.
 */
public interface LoginListener {
    /**
     * If successful, this supplies the user's name, last 4 numbers of a single credit card,
     * and a list of recent transactions for that card.
     */
    void onLoginSuccess(String name, String cardNum);

    /**
     * If the login call fails, return the error information.
     */
    void onLoginError(Exception exception);
}
